package pt.nb.dsi;

/*
 * This is a simple REST resource that returns a greeting message.
 * It also returns the current date and time in ISO format.
 * It also returns the version of the database engine.  /hello/up
 
---
./mvnw compile quarkus:dev

./mvnw package -Dquarkus.package.jar.type=uber-jar
java -jar target/quarkus1-1.0.0-SNAPSHOT-runner.jar 
curl http://localhost:8080/hello/up

 */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import javax.sql.DataSource;

import io.quarkus.logging.Log;

@Path("/hello")
public class GreetingResource {

    @Inject
    DataSource dataSource;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        Date now = new Date();
        String isoNow = java.time.format.DateTimeFormatter.ISO_INSTANT.format(now.toInstant());
        Log.warn("Hello at " + isoNow);
        return "Hello from Quarkus REST \r\nat " + isoNow;
    }

    @GET
    @Path("/up")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> getStatus() {
        Map<String, String> res = new HashMap<>();
        res.put("status", "up");
        // String sql1 = "SELECT version()";
        String sql1 = "SELECT sqlite_version()";

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql1)) {

            if (resultSet.next()) {
                res.put("dbver", resultSet.getString(1));
            }
        } catch (Exception e) {
            res.put("dbver", "unknown err:" + e.getMessage());
            Log.error(e);
        }

        return res;
    }
}
