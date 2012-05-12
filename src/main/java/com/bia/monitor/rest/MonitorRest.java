package com.bia.monitor.rest;

/**
 *
 * @author intesar
 */
import com.bia.monitor.service.MonitorService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

// The Java class will be hosted at the URI path "/rest/monitor"
@Path("/monitor")
public class MonitorRest {

    protected static Logger logger = Logger.getLogger(MonitorRest.class);
    private MonitorService service = MonitorService.getInstance();

    // curl -i -X POST -H 'Content-Type: application/json' -d 'url=http://www.google.com&email=intesar@ymail.com' http://localhost:8080/rest/monitor/
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_JSON})
    public String post(@FormParam("url") String url, @FormParam("email") String email) {
        if (logger.isTraceEnabled()) {
            logger.trace("url : " + url + ", email : " + email);
        }

        return service.add(url, email);
        //return "Please check your email!";
    }

    // @DELETE // users will be clicking on links from email
    // curl -i -X GET http://localhost:8080/rest/monitor/delete/34343
    //http://localhost:8080/rest/monitor/delete/123
    @GET
    @Path("/delete/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public String delete(@PathParam("id") String id) {
        if (logger.isTraceEnabled()) {
            logger.trace("delete id : " + id);
        }
        if (service.remove(id)) {
            return "Removed your site from monitoring!";
        } else {
            return "Cannot find your site in active list!";
        }
    }
}
