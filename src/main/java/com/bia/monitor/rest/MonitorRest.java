package com.bia.monitor.rest;

/**
 * REST endpoint for UI access
 * @author Intesar Mohammed
 */
import com.bia.monitor.service.MonitorService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// The Java class will be hosted at the URI path "/rest/monitor"
@Component
@Path("/monitor")
public class MonitorRest {
    
    protected static Logger logger = Logger.getLogger(MonitorRest.class);
    
    @Autowired
    protected MonitorService service;

    public MonitorRest() {
        
        logger.info("instantiated!");
        
    }
    
    // curl -i -X POST -H 'Content-Type: application/json' -d 'url=http://www.google.com&email=intesar@ymail.com' http://localhost:8080/rest/monitor/
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_JSON})
    public String post(@FormParam("url") String url, @FormParam("email") String email) {
        
    	logger.info("Monitor add request for url=" + url + " from email=" + email);

        String key = service.add(url, email);
        if (key.equals("Invalid data!") || key.equals("Check email!")) {
            return key;
        } else {
            return "Check email!";
        }
        //return "Please check your email!";
    }

    // @DELETE // users will be clicking on links from email
    // curl -i -X GET http://localhost:8080/rest/monitor/delete/34343
    //http://localhost:8080/rest/monitor/delete/123
    @GET
    @Path("/delete/{id}/{email}")
    @Produces({MediaType.APPLICATION_JSON})
    public String delete(@PathParam("id") String id, @PathParam("email") String email) {
    	
    	logger.info("Monitor delete request for id=" + id + " from email=" + email);
        
    	if (service.remove(id, email)) {
            return "Removed your site from monitoring!";
        } else {
            return "Cannot find your site in active list!";
        }
    }

    // curl -i -X GET http://localhost:8080/rest/monitor/status/34343
    //http://localhost:8080/rest/monitor/status/123
    @GET
    @Path("/status/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public String status(@PathParam("id") String id) {
        
    	logger.info("Monitor online status request for id=" + id);
    	
        return service.status(id);
    }
}
