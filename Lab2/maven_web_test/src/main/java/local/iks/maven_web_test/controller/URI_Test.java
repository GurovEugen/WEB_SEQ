package local.iks.maven_web_test.controller;


import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import local.iks.maven_web_test.model.dto.DTO_Student;
import local.iks.maven_web_test.model.IModel;


/**
 *
 * @author 
 */
@Path("/test")
public class URI_Test {
    
    @Context         
    private HttpHeaders httpHeaders;
    
    @Inject
    private IModel model;
       
    
    @POST       
    @Consumes("application/json")
    @Produces("application/json")
    public Response post(String studentsJSON) {                            
        
        //Auth (Begin)
        String header = httpHeaders.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED).build();	             
        }          
        
        String token = header.substring("Bearer".length()).trim();        
        
        String user;
        try {         
          user = model.extractUserFromTokenIfValid(token);
        }
        catch (Exception e){
          return Response.status(Response.Status.UNAUTHORIZED).build();	               
        }
        //Auth (End)
                        
        try {    
            	  	  
          return Response.ok(model.retrieveRowsCountByJPA(studentsJSON)).build();           
          //Calling model and return to client (End)                    
        }
        catch (Exception e) {
          return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();	             
        }            
    }
}