/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bia.monitor.rest;

/**
 *
 * @author intesar
 */
import com.bia.monitor.Job;
import com.bia.monitor.MonitorService;
import javax.ws.rs.*;
import org.apache.log4j.Logger;

// The Java class will be hosted at the URI path "/rest/monitor"
@Path("/monitor")
public class MonitorRest {

    protected static Logger logger = Logger.getLogger(MonitorRest.class);
    
    private MonitorService service = new MonitorService();

    // curl -i -X POST -H 'Content-Type: application/json' -d 'url=intesar@ymail.com' http://localhost:8080/rest/monitor/
    @POST
    @Consumes({"application/json","application/x-www-form-urlencoded"})
    @Produces({"application/json"})
    public String post(@FormParam("url") String url, @FormParam("email") String email) {
        if ( logger.isDebugEnabled() ) {
            logger.debug("url : " + url + ", email : " + email);
        }
        
        // TODO
        service.add(new Job ());
        return "Please check your email!";
    }
    
    // @DELETE // users will be clicking on links from email
    // curl -i -X GET http://localhost:8080/rest/monitor/delete/id=34343
    // http://localhost:8080/rest/monitor/delete/id=123
    @GET
    @Path("/delete/{id}")
    @Produces({"application/json"})
    public String deletePrinter(@PathParam("id") String id) {
        if ( logger.isDebugEnabled() ) {
            logger.debug("delete id : " + id );
        }
        // TODO
        service.remove(new Job());
        return "Please check your email!";
    }
    
    
}
