package pt.nb.dsi;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;

import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import java.util.Date;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import pt.nb.dsi.dal.AppUser;
import pt.nb.dsi.dal.AppUserRepository;
import io.quarkus.security.UnauthorizedException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;


import io.quarkus.logging.Log;


@Path("/auth")
public class AuthResource {

    @ConfigProperty(name = "app.jwt.duration", defaultValue = "3600") // 1 hour default
    long jwt_duration;
    @ConfigProperty(name = "app.jwt.secret", defaultValue = "my-secret-symmetric-key in 32 BY") // 256 bit key
    String jwt_Secret;

    @Inject
    AppUserRepository repository;

    @POST
    @Path("/login")
    // @PermitAll //import javax.annotation.security.PermitAll;
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> login(LoginReq req) {
        Log.infof("AuthResource.login username=%s", req.uid);
        String token = null;
        String u_role = authenticate(req.uid, req.pwd);
        if (u_role != null) {
            Instant expirationTime = Instant.now().plus(jwt_duration * 1000, ChronoUnit.MILLIS);
            Date expirationDate = Date.from(expirationTime);
            Log.info("JWT expires at " + expirationDate);
            // SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));
            Key key = Keys.hmacShaKeyFor(jwt_Secret.getBytes());
            String bp_audience = "https://wwwcert.bportugal.net/adfs/oauth2/token";
            //Collection<String> audienceList = new ArrayList<>();
            //audienceList.add(bp_audience);

            token = Jwts.builder()
                    .claim("id", 1)
                    .claim("sub", req.uid)
                    .claim("roles", u_role) // .setIssuedAt(Date.from(Instant.ofEpochSecond(1466796822L)))
                    .expiration(expirationDate)
                    .claim("aud", bp_audience)
                    .signWith(key)
                    .compact();
        } else {
            Log.error("Invalid login credentials");
            throw new UnauthorizedException("Invalid credentials");
        }
        Map<String, String> map1 = new HashMap<>();

        map1.put("username", "demo");
        if (token != null) {
            map1.put("token", "Bearer " + token);
            map1.put("error", null);
        } else {
            map1.put("token", null);
            map1.put("error", "Error generating JWT");
        }
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
    public double rnd(@HeaderParam("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
            Log.infof("Authorization Header: %s", authorizationHeader);
        }
        double rn = Math.random();
        Log.warnf("/auth Random = %f", rn);
        return rn;
    }

    public static class UserInfo {
        public String username;
        public String roles;
        public Integer id;

        public UserInfo(Integer id, String username, String roles) {
            this.id = id;
            this.username = username;
            this.roles = roles;
        }
        public String toString() {
            return String.format("UserInfo{ id=%d, username=%s, roles=%s}", id, username, roles);
        }
    }

    @GET
    @Path("/valid")   // GET /auth/valid?token=Bearer+XXXX
    public Map<String,String> valid(@QueryParam("token") String token) {
        Map<String,String> model = new HashMap<>();
        if (token == null || token.trim().isEmpty() ||  !token.startsWith("Bearer ")) {
            model.put("error", "bad token!");
            model.put("valid", "false");
            return model;
        }

        UserInfo up =  validJWT(token);
        Log.infof("Valid: token: %s - user: %s",token,up);
        model.put("token", token);
        if (up != null) {
            model.put("valid", "true");
            model.put("principal", up.toString());
        }
        else
            model.put("error", "bad token");
        return model;
    }

    private UserInfo validJWT(String token) {
        if (token.startsWith("Bearer "))
            token = token.replace("Bearer ", "");
        return parseToken(token);
    }
    public UserInfo parseToken(String token) {
        Jws<Claims> jwsClaims;

        try {
            SecretKey key = Keys.hmacShaKeyFor(jwt_Secret.getBytes());
            jwsClaims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
 
        } catch (io.jsonwebtoken.security.SignatureException | io.jsonwebtoken.MalformedJwtException ex) {
            Log.error("Invalid JWT",ex);
            return null;
        }
        Claims claims = jwsClaims.getPayload();
        String username = claims.getSubject();
        Integer userId = claims.get("id", Integer.class);
        String sroles = claims.get("roles", String.class);
        // List<String> roles = null;
        // if (sroles != null && sroles.length()>0)
        //     roles =  Arrays.asList(sroles.split(";"));
        //logger.info("JWT Issued at: {}" , claims.getIssuedAt());
        Log.info("parseToken JWT Expiration: " + claims.getExpiration());
        UserInfo u1 = new UserInfo(userId, username, sroles);
        Log.infof("parseToken User: %s", u1);
        return u1;
    }

    private String authenticate(String username, String password) {
        // Replace with your database or external authentication system

        AppUser u = repository.findByUsername(username);
        if (u != null) {
            String pw =  u.passHash;
            if (pw.startsWith("sha:")) {
                pw = pw.substring(4);
                password = Utils1.sha256Base64(password);
                Log.infof("AuthResource.authenticate: sha: %s == %s", pw,password);
            }
            if (pw.equals(password))
                return u.role;
        }
        return null;
    }

    public static class LoginReq {
        public String uid;
        public String pwd;
        public String aut;
    }
}