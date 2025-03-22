package pt.nb.dsi;

import java.util.Date;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        Date now = new Date();
        String isoNow = java.time.format.DateTimeFormatter.ISO_INSTANT
            .format(now.toInstant());
        return "Hello from Quarkus REST \r\nat " + isoNow;
    }
}
