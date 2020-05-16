package local.iks.maven_web_test.controller;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.core.Response;

/**
 *
 * @author 
 */
@Path("/ping")
public class URI_Ping {
       
    @GET   
    public Response get(){        
        return Response.ok("ping...OK").build();
    }
}