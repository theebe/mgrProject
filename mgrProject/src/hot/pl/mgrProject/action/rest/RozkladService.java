package pl.mgrProject.action.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/ws/rozklad")
public interface RozkladService {

	@GET
	@Path("/all")
	public Response all();
	
	@GET
	@Path("/linie/")
	public Response linie(); 
	
	@GET
	@Path("/linie/{numer}")
	public Response linia(Integer numer);
	
	@GET
	@Path("/przystanki/")
	public Response przystanki();
	
	@GET
	@Path("/przystanki/{id}")
	public Response przystanek(Integer id); 
	
	
}
