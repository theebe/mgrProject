package pl.mgrProject.action.rest;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;


@Stateful
@Name("dijkstryServiceBean")
@Scope(ScopeType.EVENT)
@Path("/ws/algorytm")
public class DijkstryServiceBean implements DijkstryService {

	@Logger
	private Log log;

	@In
	private EntityManager mgrDatabase; 
	
	private static final String INFO = "Algorytm wyszukiwania trasy zaimplementowany na bazie algorytmu DIJKSTRY";
	
	@GET
	@Path("/info")
	public String info() {
		return INFO;
	}

	@GET
	@Path("/run")
	public Response run() {
		// TODO Auto-generated method stub
		return null;
	}

	@Destroy
	@Remove
	public void destory(){}
}
