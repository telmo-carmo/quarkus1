package pt.nb.dsi;

/*
 smallrye.jwt.sign.key.location="my-secret-symmetric-key in 32 BY"
 smallrye.jwt.sign.key.location="privateKey.pem"
 */


import java.util.HashMap;
import java.util.Map;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;


import io.quarkus.security.UnauthorizedException;
import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.config.inject.ConfigProperty;


import io.quarkus.logging.Log;

@Path("/auth")
public class AuthResource {

    @ConfigProperty(name = "jwt.duration", defaultValue = "3600") // 1 hour default
    long jwt_duration;

    @POST
    @Path("/login")
    //@PermitAll    //import javax.annotation.security.PermitAll;
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String,String> login(LoginReq req) {
        Log.infof("AuthResource.login %s %s",req.uid,req.pwd);
        String token = null;

        if (authenticate(req.uid, req.pwd)) {
            
            token = Jwt.
                claims()
                    .subject(req.uid)
                    .claim("roles", "USER")
                    .claim("exp", System.currentTimeMillis() / 1000 + jwt_duration)
                    .claim("aud", "https://wwwcert.bportugal.pt/adfs/oauth2/token")
                .sign();


            /* 
            Set<String> roles = new HashSet<>();
            roles.add("User"); // or admin, etc.

            token = Jwt.issuer("quarkus-jwt")
                    .subject(req.uid)
                    .upn(req.uid)
                    .groups(roles)
                    .expiresIn(Duration.ofSeconds(jwt_duration))
                    .sign();
            */
        } else {
            Log.error("Invalid login credentials");
            throw new UnauthorizedException("Invalid credentials");
        }
        Map<String,String> map1 = new HashMap<>();

        map1.put("username", "demo");
        map1.put("token",  "Bearer " + token);
        map1.put("error", null);
        Log.infof("AuthResource.login JWT token=%s",token);
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

    private boolean authenticate(String username, String password) {
        // Replace with your database or external authentication system
        // Example (very insecure):
        return  "123".equals(password);
    }

    public static class LoginReq {
        public String uid;
        public String pwd;
        public String aut;
    }
}