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
import local.iks.maven_web_test.model.dto.DTO_User;


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
       
    @Path("/access")
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
          //return Response.ok(Response.Status.BAD_REQUEST).build();
        }
        catch (Exception e){
          return Response.status(Response.Status.UNAUTHORIZED).build();               
        }
        //Auth (End)
                      
        try {            
          List<DTO_Student> students;                
          Jsonb jsonb = JsonbBuilder.create();            
          
          //Preparing data (Begin)  
          try {              
           students = jsonb.fromJson(studentsJSON,new ArrayList<DTO_Student>(){}.getClass().getGenericSuperclass());                    
          }
          catch (JsonbException e) {
           return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();	             
          }
          //Preparing data (End)  
          
          //Calling model and return to client (Begin)
	  model.run(user,students);	  	  	  
          return Response.ok(jsonb.toJson(students)).build();           
          //Calling model and return to client (End)                    
        }
        catch (Exception e) {
          return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();	             
        }  
        
    }
    
    
    @Path("/refresh")
    @POST       
    @Consumes("application/json")
    @Produces("application/json")
    public Response post1(String studentsJSON) {                            
        
        //Auth (Begin)
        String header = httpHeaders.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED).build();	             
        }          
        
        String token = header.substring("Bearer".length()).trim();        
        
        String user;
        try {         
          user = model.extractUserFromTokenIfValid(token);
          return Response.ok(model.issueToken(user)+" "+model.refreshToken(user)).build();
        }
        catch (Exception e){
          return Response.status(Response.Status.UNAUTHORIZED).build();             
        }
        //Auth (End)     
    }
}