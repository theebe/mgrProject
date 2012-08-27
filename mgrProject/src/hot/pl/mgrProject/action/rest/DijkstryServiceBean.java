package pl.mgrProject.action.rest;

import javax.ejb.Remove;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Name;

import flexjson.JSONSerializer;

@Name("dijkstryServiceBean")
@Path("/algorytm")
public class DijkstryServiceBean implements DijkstryService {

	private JSONSerializer serializer = new JSONSerializer();
	private static final String INFO = "Algorytm wyszukiwania trasy zaimplementowany na bazie algorytmu DIJKSTRY";

	@GET
	@Path("/info")
	public String info() {
		String ret = serializer.serialize(INFO);
		return ret;
	}

	@GET
	@Path("/run")
	public Response run() {
		// TODO Auto-generated method stub
		return Response.ok().build();
	}

	@Destroy
	@Remove
	public void destory() {
	}
}
