package pt.nb.dsi;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;


import java.util.HashMap;
import java.util.Map;
import java.util.Date;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import io.quarkus.security.UnauthorizedException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Collection;

import io.quarkus.logging.Log;

@Path("/auth")
public class AuthResource {

    @ConfigProperty(name = "app.jwt.duration", defaultValue = "3600") // 1 hour default
    long jwt_duration;
    @ConfigProperty(name = "app.jwt.secret", defaultValue = "my-secret-symmetric-key in 32 BY") // 256 bit key
    String jwt_Secret;


    @POST
    @Path("/login")
    // @PermitAll //import javax.annotation.security.PermitAll;
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> login(LoginReq req) {
        Log.infof("AuthResource.login %s %s", req.uid, req.pwd);
        String token = null;

        if (authenticate(req.uid, req.pwd)) {
            Instant expirationTime = Instant.now().plus(jwt_duration * 1000, ChronoUnit.MILLIS);
            Date expirationDate = Date.from(expirationTime);

            // SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));
            Key key = Keys.hmacShaKeyFor(jwt_Secret.getBytes());
            String bp_aud = "https://wwwcert.bportugal.net/adfs/oauth2/token";
            Collection<String> audienceList = new ArrayList<>();
            audienceList.add(bp_aud);

            token = Jwts.builder()
                    .claim("id", "1")
                    .claim("sub", req.uid)
                    .claim("roles", "USER") // .setIssuedAt(Date.from(Instant.ofEpochSecond(1466796822L)))
                    .expiration(expirationDate)
                    .claim("aud", bp_aud)
                    .signWith(key)
                    .compact();
        } else {
            Log.error("Invalid login credentials");
            throw new UnauthorizedException("Invalid credentials");
        }
        Map<String, String> map1 = new HashMap<>();

        map1.put("username", "demo");
        if (token != null)
            map1.put("token", "Bearer " + token);
        map1.put("error", null);
        Log.infof("AuthResource.login JWT token=%s", token);
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
        Log.warnf("/auth Random = %f", rn);
        return rn;
    }

    private boolean authenticate(String username, String password) {
        // Replace with your database or external authentication system
        // Example (very insecure):
        return "123".equals(password);
    }

    public static class LoginReq {
        public String uid;
        public String pwd;
        public String aut;
    }
}