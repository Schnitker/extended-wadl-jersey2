package com.github.schnitker.extwadl.sample;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Provides Person Service
 */
@Path ("person")
public class PersonService {

    /**
     * @return list of person
     */
    @GET
    @Produces ( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON } )
    public List< RsPerson > getAllPerson() {
        return new ArrayList< RsPerson >();
    }

    /**
     * Gets person of given name
     * 
     * @response.representation.200.qname {http://www.sample.com/sampleapp/2018/01}person 
     * @response.representation.200.mediaType application/xml, application/json
     * 
     * @param name the name/id of the person
     * 
     * @return the requested properties
     */
    @Path ("{id}")
    @GET
    @Produces ( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON } )
    public RsPerson getPerson ( @PathParam("id") String name ) {
        return new RsPerson (name, name + "@github.com");
    }
}
