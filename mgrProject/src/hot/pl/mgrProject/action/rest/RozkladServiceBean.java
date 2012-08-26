package pl.mgrProject.action.rest;

import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import pl.mgrProject.model.Linia;
import pl.mgrProject.model.Przystanek;
import flexjson.JSONSerializer;

@Name("rozkladServiceBean")
@Scope(ScopeType.EVENT)
@Stateful
@Path("/ws/rozklad")
public class RozkladServiceBean implements RozkladService {

	@Logger
	private Log log;

	@In
	private EntityManager mgrDatabase;

	private JSONSerializer serializer = new JSONSerializer();

	@GET
	@Path("/all")
	public Response all() {
		// TODO Auto-generated method stub
		return null;
	}

	@GET
	@Path("/linie/")
	public String linie() {

		List<Linia> listaLinii = mgrDatabase.createNamedQuery("wszystkieLinie")
				.getResultList();

		String json = serializer.serialize(listaLinii);
		log.info("Pobrano linie");
		return json;
	}

	@GET
	@Path("/linie/{numer}")
	public String linia(@PathParam("numer") Integer numer) {
		List<Linia> listaLinii = mgrDatabase.createNamedQuery("liniePoNumerze")
				.setParameter("numer", numer).getResultList();
		String json = serializer.serialize(listaLinii);
		log.info("Pobrano linie");
		return json;


	}

	@GET
	@Path("/przystanki/")
	public Response przystanki() {
		List<Przystanek> listaPrzystankow = mgrDatabase.createNamedQuery(
				"wszystkiePrzystanki").getResultList();
		return null;
	}

	@GET
	@Path("/przystanki/{id}")
	public Response przystanek(@PathParam("id") Integer id) {

		return null;
	}

	@Destroy
	@Remove
	public void destory() {
	}

}
