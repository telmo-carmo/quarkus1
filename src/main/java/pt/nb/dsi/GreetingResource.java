package pt.nb.dsi;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
/*
 * This is a simple REST resource that returns a greeting message.
 * It also returns the current date and time in ISO format.
 * It also returns the version of the database engine.  /hello/up
 
see: https://docs.quarkiverse.io/quarkus-quinoa/dev/index.html  for React FE

./mvnw quarkus:add-extension -Dextensions="io.quarkus:quarkus-smallrye-openapi"

add default /q/openapi endpoint or change:
 quarkus.smallrye-openapi.path=/swagger
http://localhost:8080/q/openapi?format=JSON

By default, Swagger UI is accessible at /q/swagger-ui.

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
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

import javax.sql.DataSource;

import io.quarkus.logging.Log;

@Path("/hello")
public class GreetingResource {

    @Inject
    DataSource dataSource;

    @Inject
    ExcelService1 excelService;

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

    @GET
    @Path("/excel1")
    @Produces("application/vnd.ms-excel")
    public Response downloadExcel() {

        final String filename = "Sample1.xlsx";

        InputStream is = new ByteArrayInputStream(excelService.toByteArray());
        ResponseBuilder responseBuilder = Response.ok(is);
        responseBuilder.header("Content-Disposition", String.format("attachment; filename=\"%s\"", filename));
        return responseBuilder.build();
    }
}
