package com.github.schnitker.extwadl.tasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XsltTransform {

    public static void main ( String[] args ) throws Exception {
        
        if (args.length != 3)
            System.exit ( 1 );
                
        System.setProperty("javax.xml.transform.TransformerFactory",
                           "org.apache.xalan.processor.TransformerFactoryImpl");

        
        FileOutputStream htmlOs = new FileOutputStream(new File(args[0]));
        InputStream xslStream = new FileInputStream(new File(args[1]));
        InputStream xmlStream = new FileInputStream(new File(args[2]));
 
        StreamSource xslSource = new StreamSource(xslStream);

        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer(xslSource);
        
        StreamSource xmlSource = new StreamSource(xmlStream);
        transformer.transform(xmlSource, new StreamResult(htmlOs));        
        
        xslStream.close ();
        xmlStream.close ();
        htmlOs.close ();
    }
}
