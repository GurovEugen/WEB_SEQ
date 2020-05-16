package local.iks.maven_web_test.controller;

import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;

import javax.inject.Inject;

import local.iks.maven_web_test.model.dto.DTO_User;
import local.iks.maven_web_test.model.IModel;



/**
 *
 * @author 
 */
@Path("/login")
public class URI_Login {
        
    @Inject
    private IModel model;
    
    
    @POST       
    @Consumes("application/json")
    public Response post(String userJSON){
                       
        Jsonb jsonb = JsonbBuilder.create();          
        DTO_User user;              
        try {
            
          try { 
           user = jsonb.fromJson(userJSON,DTO_User.class);                    
          }
          catch (JsonbException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();	             
          }
          
	  boolean auth = model.authenticate(user);	  	  
          if (auth==false) {                  
            return Response.status(Response.Status.UNAUTHORIZED).build();	             
          }
          
          return Response.ok(model.issueToken(user.getName())+" "+model.refreshToken(user.getName())).build();
                               
        }        
        catch (Exception e) {
          return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();	             
        }                          
    }
}