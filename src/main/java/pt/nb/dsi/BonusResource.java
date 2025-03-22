
package pt.nb.dsi;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import pt.nb.dsi.dal.Bonus;
import pt.nb.dsi.dal.BonusRepository;

import java.util.List;

@Path("/api/bonus")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BonusResource {

    @Inject
    BonusRepository bonusRepository;

    @GET
    public List<Bonus> getAll() {
        return bonusRepository.findAll();
    }

    @GET
    @Path("/{id}")
    public Bonus getById(@PathParam("id") String id) {
        Bonus bonus = bonusRepository.findOne(id);
        if (bonus == null) {
            throw new WebApplicationException("Bonus not found", Response.Status.NOT_FOUND);
        }
        return bonus;}

    @POST
    public Response create(Bonus bonus) {
        bonusRepository.save(bonus);
        return Response.status(Response.Status.CREATED).entity(bonus).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") String id, Bonus bonus) {
        Bonus existingBonus = bonusRepository.findOne(id);
        if (existingBonus == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        existingBonus.setJob(bonus.getJob());
        existingBonus.setSal(bonus.getSal());
        existingBonus.setComm(bonus.getComm());
        bonusRepository.update(existingBonus);
        return Response.ok(existingBonus).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        boolean deleted = bonusRepository.delete(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
