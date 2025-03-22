package pt.nb.dsi;


import java.util.HashMap;
import java.util.Map;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;


import io.quarkus.logging.Log;

@Path("/auth")
public class AuthResource {



    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String,String> login() {
        Map<String,String> map1 = new HashMap<>();
        Log.info("AuthResource.login");
        map1.put("username", "demo");
        map1.put("token", 
        "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwic3ViIjoiZGVtbyIsInJvbGVzIjoiVVNFUiIsImV4cCI6MTc0MjY5NDMwMywiYXVkIjoiaHR0cHM6Ly93d3djZXJ0LmJwb3J0dWdhbC5uZXQvYWRmcy9vYXV0aDIvdG9rZW4ifQ.GT28Ai8n7u_UD7wTcdmXxK8kIDzjNXg-LcBXCkCQ-lg");
        map1.put("error", null);
        return map1;
    }
    
    @GET
    @Path("/logout")
    public String logout() {
        Log.info("AuthResource.logout");
        return "yes";
    }

    @GET
    @Path("/rnd")
    public double rnd() {
        double rn = Math.random();
        Log.warnf("/auth Random = %f",rn);
        return rn;
    }
}