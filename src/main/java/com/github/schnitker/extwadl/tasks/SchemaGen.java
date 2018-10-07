package com.github.schnitker.extwadl.tasks;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;

public class SchemaGen {

    public static void main( String[] args ) throws Exception {

        ArrayList<String> list = new ArrayList<>();
        for ( String arg : args ) {
            list.add( arg );
        }

        String output = null;
        Iterator<String> iter = list.iterator();
        while ( iter.hasNext() ) {
            String arg = iter.next();
            if ( arg.equals( "-o" )) {
                iter.remove();
                if ( iter.hasNext()) {
                    output = iter.next();
                    iter.remove();
                }
            }
        }
        if ( output == null ) {
            throw new IllegalArgumentException( "missing output filename" );
        }

        Class< ? > foundClasses[] = parseFileList( list );
        if ( foundClasses == null || foundClasses.length <= 0 ) {
            return;
        }
        JAXBContext ctx = JAXBContext.newInstance( foundClasses );
        
        try ( FileOutputStream fos = new FileOutputStream( output )) {            
            OutputStreamWriter osw = new OutputStreamWriter( fos, StandardCharsets.UTF_8 );
            SchemaGenResolver sor = new SchemaGenResolver( osw );
            ctx.generateSchema( sor );        
        }
    }

    private static Class< ? >[] parseFileList( List<String> classes ) {

        List< Class< ? > > rc = new ArrayList<>();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        for ( String cls : classes ) {
            try {
                Class< ? > foundClass = cl.loadClass( cls );
                rc.add( foundClass );
            } catch ( ClassNotFoundException e ) {
                System.out.println( "ClassNotFoundException:" + e.getMessage() );
            }
        }

        return rc.toArray( new Class< ? >[0] );
    }
}
