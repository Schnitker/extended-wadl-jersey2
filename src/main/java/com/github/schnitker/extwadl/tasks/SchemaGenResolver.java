package com.github.schnitker.extwadl.tasks;

import java.io.Writer;

import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

/**
 * Our schema gen resolver.
 *
 * @author dstrauss
 * @version 0.1
 */
public class SchemaGenResolver extends SchemaOutputResolver {
    private final Writer out;

    /**
     * @param out
     *            a Writer for the generated schema
     */
    public SchemaGenResolver(final Writer out) {
        super();
        this.out = out;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Result createOutput( String namespaceUri, String suggestedFileName ) {
        final StreamResult result = new StreamResult( this.out );
        result.setSystemId( "no-id" );
        return result;
    }
}