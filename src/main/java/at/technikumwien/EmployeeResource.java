package at.technikumwien;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Path("/employees")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EmployeeResource {

    @Inject
    private EmployeeService employeeService;

    @Context
    private UriInfo uriInfo;

    @POST
    public Response create(Employee employee){ // macht automatisch ein Employee aus dem JSON da wir bei consumes json angegeben haben
        employeeService.save(employee);
        URI location = uriInfo.getAbsolutePathBuilder().path(employee.getId().toString()).build();
        return Response.created(null).build(); // Gibt Responst mit location zurück
    }

    @GET
    @Path("/{id}") // Nicht nur get sondern auch wenn eine id mitgegeben wird  - Pathparam übergibt die id an den long
    public Employee retrieve(@PathParam("id") long id){
        return employeeService.findById(id).orElse(null);
    }

    @PUT
    @Path("/{id}") // Nicht nur get sondern auch wenn eine id mitgegeben wird  - Pathparam übergibt die id an den long
    public void update(@PathParam("id") long id, Employee employee){
        employee.setId(id); // könnte man weglassen
        employeeService.save(employee);
    }


    @GET
    public List<Employee> retrieveAll(){
        return employeeService.findAll();
    }

    @DELETE
    @Path("/{id}") // Nicht nur get sondern auch wenn eine id mitgegeben wird  - Pathparam übergibt die id an den long
    public void delete(@PathParam("id") long id){
        employeeService.deleteById(id);
    }}
