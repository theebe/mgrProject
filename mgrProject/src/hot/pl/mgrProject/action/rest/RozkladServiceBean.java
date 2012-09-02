package pl.mgrProject.action.rest;

import static pl.mgrProject.action.rest.message.MessageBuilderHelper.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import pl.mgrProject.action.rest.message.MessageBuilder;
import pl.mgrProject.action.rest.message.MessageBuilderHelper;
import pl.mgrProject.model.Linia;
import pl.mgrProject.model.Przystanek;
import pl.mgrProject.model.PrzystanekTabliczka;

@Path("/rozklad")
@Name("rozkladServiceBean")
public class RozkladServiceBean implements RozkladService {

	@Logger
	private Log log;

	private static final String[] keys = { "linie", "przystanki" };

	@In
	private EntityManager mgrDatabase;

	@GET
	@Path("/all")
	public Response all(@QueryParam("format") String format,
			@QueryParam("pretty") String pp) {
		try {
			log.info("Wybrano format: " + (format == null ? "json" : format));

			List<Przystanek> listaPrzystankow = null;

			listaPrzystankow = mgrDatabase.createNamedQuery(
					"wszystkiePrzystanki").getResultList();

			log.info("Pobrano " + listaPrzystankow.size() + " przystankow");

			MessageBuilder messageBuilder = getMessageBuilder(format, pp);
			log.info("Budowanie wiadomosci z przystankow ");
			String out = messageBuilder.buildMessagePrzystanek(
					listaPrzystankow, TABLICZKI);
			log.info("Wyslanie wiadomosci");
			return sendOut(out, format);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(e.toString()).build();
		}
	}

	@GET
	@Path("/linie/")
	@Produces(MediaType.TEXT_PLAIN)
	public Response linie(@QueryParam("format") String format,
			@QueryParam("pretty") String pp) {
		try {
			log.info("Wybrano format: " + (format == null ? "json" : format));

			List<Linia> listaLinii = null;

			listaLinii = mgrDatabase.createNamedQuery("wszystkieLinie")
					.getResultList();

			log.info("Pobrano " + listaLinii.size() + " przystankow");

			MessageBuilder messageBuilder = getMessageBuilder(format, pp);
			log.info("Budowanie wiadomosci z przystankow ");
			String out = messageBuilder.buildMessageLinia(listaLinii,
					PRZYSTANEK);
			log.info("Wyslanie wiadomosci");
			return sendOut(out, format);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(e.toString()).build();
		}
	}

	@GET
	@Path("/linie/{numer}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response linia(@PathParam("numer") Integer numer,
			@QueryParam("format") String format,
			@QueryParam("pretty") String pp) {
		try {
			log.info("Wybrano format: " + (format == null ? "json" : format));

			List<Linia> listaLinii = mgrDatabase
					.createNamedQuery("liniePoNumerze")
					.setParameter("numer", numer).getResultList();

			log.info("Pobrano " + listaLinii.size() + " lini");

			MessageBuilder messageBuilder = getMessageBuilder(format, pp);

			log.info("Budowanie wiadomosci z linii ");

			String out = messageBuilder
					.buildMessageLinia(listaLinii, TABLICZKI);
			log.info("Wyslanie wiadomosci");
			return sendOut(out, format);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(e.toString()).build();
		}
	}

	@GET
	@Path("/przystanki/")
	@Produces(MediaType.TEXT_PLAIN)
	public Response przystanki(@QueryParam("format") String format,
			@QueryParam("pretty") String pp) {
		try {
			log.info("Wybrano format: " + (format == null ? "json" : format));

			List<Przystanek> listaPrzystankow = null;

			listaPrzystankow = mgrDatabase.createNamedQuery(
					"wszystkiePrzystanki").getResultList();

			log.info("Pobrano " + listaPrzystankow.size() + " przystankow");

			MessageBuilder messageBuilder = getMessageBuilder(format, pp);
			log.info("Budowanie wiadomosci z przystankow ");
			String out = messageBuilder
					.buildMessagePrzystanek(listaPrzystankow);
			log.info("Wyslanie wiadomosci");
			return sendOut(out, format);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(e.toString()).build();
		}

	}

	@GET
	@Path("/przystanki/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response przystanek(@PathParam("id") Long id,
			@QueryParam("format") String format,
			@QueryParam("pretty") String pp) {

		try {
			log.info("Wybrano format: " + (format == null ? "json" : format));

			log.info("Pobieranie przystanku z nr " + id);
			List<Przystanek> listaPrzystankow = null;

			listaPrzystankow = mgrDatabase
					.createQuery("SELECT p from Przystanek p where p.id = :id")
					.setParameter("id", id).getResultList();
			log.info("Pobrano " + listaPrzystankow.size() + " przystankow");

			// lazy loading :/
			int sum = 0;
			for (Przystanek przystanek : listaPrzystankow) {
				List<PrzystanekTabliczka> przystanekTabliczka = przystanek
						.getPrzystanekTabliczki();
				sum += przystanekTabliczka.size();

			}
			log.info("Pobrano " + sum + " tabliczek");

			MessageBuilder messageBuilder = getMessageBuilder(format, pp);
			log.info("Budowanie wiadomosci z przystankow ");
			String out = messageBuilder.buildMessagePrzystanek(
					listaPrzystankow, TABLICZKI);
			log.info("Wyslanie wiadomosci");
			return sendOut(out, format);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(e.toString()).build();
		}

	}

}
