package pl.mgrProject.action.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/ws/algorytm")
public interface DijkstryService {

	@GET
	@Path("/info")
	public Response info(); 
	
	@POST
	@Path("/run")
	public Response run();
	
}
