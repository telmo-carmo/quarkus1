package pt.nb.dsi;

import pt.nb.dsi.dal.Emp;
import pt.nb.dsi.dal.IEmpRepository;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/emp")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmpResource {

    @Inject
    IEmpRepository empRepository;

    @GET
    public List<Emp> listAll() {
        return empRepository.listAll();
    }

    @GET
    @Path("/{id}")
    public Emp getById(@PathParam("id") Integer id) {
        return empRepository.findById(id);
    }

    @POST
    @Transactional
    public Response create(Emp emp) {
        empRepository.persist(emp);
        return Response.status(Response.Status.CREATED).entity(emp).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Integer id, Emp emp) {
        Emp entity = empRepository.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        entity.setEname(emp.getEname());
        entity.setJob(emp.getJob());
        entity.setMgr(emp.getMgr());
        entity.setHiredate(emp.getHiredate());
        entity.setSal(emp.getSal());
        entity.setComm(emp.getComm());
        entity.setDeptno(emp.getDeptno());
        entity.setTstamp(emp.getTstamp());
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Integer id) {
        boolean deleted = empRepository.deleteById(id);
        if (deleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
