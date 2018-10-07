package com.github.schnitker.extwadl.tasks;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.internal.JerseyResourceContext;
import org.glassfish.jersey.server.wadl.WadlGenerator;
import org.glassfish.jersey.server.wadl.config.WadlGeneratorConfig;
import org.glassfish.jersey.server.wadl.config.WadlGeneratorDescription;
import org.glassfish.jersey.server.wadl.internal.ApplicationDescription;
import org.glassfish.jersey.server.wadl.internal.WadlBuilder;
import org.glassfish.jersey.server.wadl.internal.generators.WadlGeneratorApplicationDoc;
import org.glassfish.jersey.server.wadl.internal.generators.WadlGeneratorGrammarsSupport;
import org.glassfish.jersey.server.wadl.internal.generators.resourcedoc.WadlGeneratorResourceDocSupport;

public class GenerateWadlTask {

    static class WadlGeneratorConfigFile extends WadlGeneratorConfig {

        private final File wadlDir;

        public WadlGeneratorConfigFile( File wadlDir ) {
            this.wadlDir = wadlDir;
        }
        
        @Override
        public List< WadlGeneratorDescription > configure() {
            
            try {
                File applicationDocs = new File ( wadlDir, "application-doc.xml" );
                File grammars = new File ( wadlDir, "application-grammars.xml" );
                File resourceDoc = new File ( wadlDir, "resourcedoc.xml" );

                return generator( WadlGeneratorApplicationDoc.class ).prop( "applicationDocsStream", new FileInputStream( applicationDocs ) )
                                                                     .generator( WadlGeneratorGrammarsSupport.class )
                                                                     .prop( "grammarsStream", new FileInputStream( grammars ) )
                                                                     .generator( WadlGeneratorResourceDocSupport.class )
                                                                     .prop( "resourceDocStream", new FileInputStream( resourceDoc ) )
                                                                     .descriptions();
            } catch ( FileNotFoundException e ) {
                
                throw new RuntimeException ( e.getMessage() );
            }
        }
    }

    /**
     * @param args
     */
    public static void main( String[] args ) throws Exception {
        
        if ( args.length == 0 ) {
            System.out.println( "Usage: extwadl [packages]" );
            System.exit( 1 );
        }
        // set logger
        Logger rootLogger = LogManager.getLogManager().getLogger( "" );
        Level julLevel = java.util.logging.Level.WARNING;
        rootLogger.setLevel( julLevel );

        // create resource config
        ResourceConfig rc = new ResourceConfig();
        rc.packages( args );

        // run WADL generator
        File outputDir = new File (".");
        new GenerateWadlTask().generate( rc, outputDir );
    }

    /**
     * @param rc
     * @param wadlDir
     * @throws Exception
     */
    public void generate( ResourceConfig rc, File wadlDir ) throws Exception {

        Logger logger = Logger.getLogger( this.getClass().getName() ); 
        
        // local resources (in + out)
        WadlGeneratorConfigFile wgen = new WadlGeneratorConfigFile( wadlDir );
        File wadlFile = new File( wadlDir, "application.wadl" );
        
        // initialize
        ApplicationHandler ah = new ApplicationHandler( rc );
        WadlGenerator wadlGenerator = wgen.createWadlGenerator( ah.getInjectionManager() );

        // get resource context, create builder
        JerseyResourceContext context = ah.getInjectionManager().getInstance( JerseyResourceContext.class );
        ApplicationDescription ad = new WadlBuilder( wadlGenerator, false, null ).generate( context.getResourceModel().getResources() );

        // write WADL
        JAXBContext c = JAXBContext.newInstance( wadlGenerator.getRequiredJaxbContextPath(),
                                                 Thread.currentThread().getContextClassLoader() );
        Marshaller m = c.createMarshaller();
        m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );
        OutputStream out = new BufferedOutputStream( new FileOutputStream( wadlFile ) );

        m.marshal( ad.getApplication(), out );
        out.close();

        logger.info( "Wrote " + wadlFile );
    }
}
