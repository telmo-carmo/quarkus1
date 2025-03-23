package pt.nb.dsi;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.jboss.logging.Logger;

import pt.nb.dsi.dal.AppUser;
import pt.nb.dsi.dal.AppUserRepository;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AppUserResource {
    private static final Logger logger = Logger.getLogger(AppUserResource.class);
    @Inject
    AppUserRepository repository;

    @GET
    public List<AppUser> getAllUsers() {
        return repository.listAll();
    }


    @GET
    @Path("/count")
    @Transactional
    public int getUsersCount() {
        // AppUser u1 = new AppUser("admin", "admin", "ADMIN", LocalDate.now());
        // repository.persist(u1);
        // logger.info(u1);

        return repository.listAll().size();
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") UUID id) {
        logger.info("getUserById: " + id);
        AppUser u = repository.findById(id);
        if (u == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(u).build();
    }

    @POST
    @Transactional
    public Response createUser(AppUser user) {
        //user.id = UUID.randomUUID(); // persit() generates the UUID
        repository.persist(user);
        logger.info("Created User with id: " + user.id);
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateUser(@PathParam("id") UUID id, AppUser user) {
        logger.info("updateUser: " + id);
        AppUser u = repository.findById(id);
        if (u == null) {
            //throw new WebApplicationException("User not found", 404);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        u.createdAt = user.createdAt;
        u.passHash = user.passHash;
        u.role = user.role;
        u.username = user.username;
        repository.persist(u);
        return Response.ok(u).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") UUID id) {
        boolean deleted = repository.deleteById(id);
        if (!deleted) {
            //throw new WebApplicationException("User not found", 404);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
