package pt.nb.dsi;

import jakarta.inject.Inject;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

import pt.nb.dsi.dal.AppUser;
import pt.nb.dsi.dal.AppUserRepository;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AppUserResource {
    @Inject
    AppUserRepository repository;

    @GET
    public List<AppUser> getAllUsers() {
        return repository.listAll();
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") UUID id) {
        AppUser u = repository.findById(id);
        if (u == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(u).build();
    }

    @POST
    public Response createUser(AppUser user) {
        user.id = UUID.randomUUID();
        repository.persist(user);
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") UUID id, AppUser user) {
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
    public Response deleteUser(@PathParam("id") UUID id) {
        boolean deleted = repository.deleteById(id);
        if (!deleted) {
            //throw new WebApplicationException("User not found", 404);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
