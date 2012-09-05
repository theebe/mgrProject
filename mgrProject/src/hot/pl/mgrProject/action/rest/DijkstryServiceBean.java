package pl.mgrProject.action.rest;

import static pl.mgrProject.action.rest.message.MessageBuilderHelper.getMessageBuilder;
import static pl.mgrProject.action.rest.message.MessageBuilderHelper.sendOut;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.postgis.Point;

import pl.mgrProject.action.Odpowiedz;
import pl.mgrProject.action.algorithm.Algorithm;
import pl.mgrProject.action.rest.message.MessageBuilder;

@Name("dijkstryServiceBean")
@Path("/algorytm")
public class DijkstryServiceBean implements DijkstryService {

	@Logger
	private Log log;

	@In(create = true)
	private Algorithm algorithmBean;

	public static final String START = "start";
	public static final String STOP = "stop";
	public static final String DATA = "data";

	private static final String INFO = "Algorytm wyszukiwania trasy zaimplementowany na bazie algorytmu DIJKSTRY";

	
	@GET
	@Path("/info")
	@Produces(MediaType.TEXT_PLAIN)
	public Response info(@QueryParam("format") String format, @QueryParam("pretty") String pp) {
		log.info("Wybrano format: " + (format == null ? "json" : format));
		try {
			MessageBuilder messageBuilder = getMessageBuilder(format, pp);

			log.info("Budowanie wiadomosci info ");

			String out = messageBuilder.buildMessage(INFO);
			log.info("Wyslanie wiadomosci");

			return sendOut(out, format);
		} catch (Exception e) {

			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR)
			.entity(e.toString()).build();
		}
	}

	@GET
	@Path("/run")
	@Produces(MediaType.TEXT_PLAIN)
	public Response runEmpty(@QueryParam("format") String format, @QueryParam("pretty") String pp) {

		log.info("Wybrano format: " + (format == null ? "json" : format));
		String ret = "Brak parametrów \n\nINFO: \n" + INFO;
		try {

			MessageBuilder messageBuilder = getMessageBuilder(format, pp);

			log.info("Budowanie wiadomosci info ");

			String out = messageBuilder.buildMessage(ret);
			log.info("Wyslanie wiadomosci");

			return sendOut(out, format);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR)
			.entity(e.toString()).build();
		}
	}

	@GET
	@Path("/run/{path:.*}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response run(@QueryParam("format") String format,
			@PathParam("path") String path, @QueryParam("pretty") String pp) {
		try {
			log.info("Wybrano format: " + (format == null ? "json" : format));
			log.info("PATH : " + path);

			List<String> errs = new ArrayList<String>();
			Map<String, Object> vars = parse(path, errs);

			if (errs.size() != 0) {
				return sendOut(stringErrs(errs), format); 
			}

			Point start = (Point) vars.get(START);
			Point stop = (Point) vars.get(STOP);
			Date dataStartu = (Date) vars.get(DATA);

			log.info("Odczytane parametry: " + start.getX() + " "
					+ start.getY() + " " + stop.getX() + " " + stop.getY()
					+ " " + dataStartu.toString());

			Odpowiedz odp = findRoute(start, stop, dataStartu);

			log.info("Otrzymano odpowiedz: " + odp.toString());

			MessageBuilder messageBuilder = getMessageBuilder(format, pp);

			log.info("Budowanie odpowiedzi ");

			String out = messageBuilder.buildMessage(odp);
			log.info("Wyslanie wiadomosci");

			return sendOut(out, format);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR)
			.entity(e.toString()).build();
		}
	}

	/**
	 * Wyciaga wartosci ze sciezki
	 * 
	 * @param path
	 *            sciezka
	 * @param errs
	 *            tablica do wrzucania bledow
	 * @return mape z kluczami START, STOP, DATA, z obiektami odczytanymi ze
	 *         sciezki
	 */
	private Map<String, Object> parse(String path, List<String> errs) {
		Map<String, Object> vars = new HashMap<String, Object>();

		String[] pathVars = parsePath(path, errs);

		Double startLon = parseDouble(0 < pathVars.length ? pathVars[0] : null,
				errs);
		Double startLat = parseDouble(1 < pathVars.length ? pathVars[1] : null,
				errs);
		Double stopLon = parseDouble(2 < pathVars.length ? pathVars[2] : null,
				errs);
		Double stopLat = parseDouble(3 < pathVars.length ? pathVars[3] : null,
				errs);
		errs.addAll(checkInputs(startLon, startLat, stopLon, stopLat));
		if (errs.size() != 0) {
			return vars;
		}
		Date data = parseDate(4 < pathVars.length ? pathVars[4] : null);
		Point start = new Point(startLon, startLat);
		Point stop = new Point(stopLon, stopLat);

		vars.put(START, start);
		vars.put(STOP, stop);
		vars.put(DATA, data);
		return vars;
	}

	/**
	 * String to double
	 * 
	 * @param string
	 * @param errs
	 * @return
	 */
	private Double parseDouble(String string, List<String> errs) {
		if (string == null)
			return null;
		try {
			Double d = Double.valueOf(string);
			return d;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			errs.add("NumberFormatException: blad parsowania znakow : "
					+ string + " na wartosc double");
			return null;
		}
	}

	/**
	 * ze sciezki wyciaga tablice wartosci (oddziela po znaku / )
	 * 
	 * @param path
	 * @param errs
	 * @return
	 */
	private String[] parsePath(String path, List<String> errs) {
		if (path == null || path.length() < 3) {
			errs.add("Blad skladni w sciezce");
			log.info("Blad skladni w sciezce");
			return null;
		}
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		String[] pathParts = path.split("/");
		if (pathParts.length < 4) {
			errs.add("Brak ktoregos z parametrow");
		}
		return pathParts;
	}

	/**
	 * Erors to string
	 * 
	 * @param errs
	 * @return
	 */
	private String stringErrs(List<String> errs) {
		StringBuilder sb = new StringBuilder();
		sb.append("Wystapil blad: \n");
		for (String err : errs)
			sb.append(err).append(";\n");
		return sb.toString();
	}

	/**
	 * Parsuje date wedle schematu opisanego w mgr Date zwraca zawsze, jak nie
	 * uda sie odczytac ze stringu zwraca aktualny czas
	 * 
	 * @param d
	 * @return data
	 */
	private Date parseDate(String d) {
		Calendar calendar = new GregorianCalendar(), tmp1 = null, tmp2 = null;
		if (d == null) {
			return calendar.getTime();
		}
		String[] czesci = d.split("T");
		if (czesci.length == 2) {
			DateFormat dateFrm = new SimpleDateFormat("yyyy-MM-dd");
			try {
				tmp1 = new GregorianCalendar();
				tmp1.setTime(dateFrm.parse(czesci[0]));
			} catch (ParseException e) {
				tmp1 = null;
				e.printStackTrace();
			}

			dateFrm = new SimpleDateFormat("HH:mm");
			try {
				tmp2 = new GregorianCalendar();
				tmp2.setTime(dateFrm.parse(czesci[1]));
			} catch (ParseException e) {
				tmp2 = null;
				e.printStackTrace();
			}
		} else if (czesci.length == 1) {
			DateFormat dateFrm = new SimpleDateFormat("HH:mm");
			try {
				tmp2 = new GregorianCalendar();
				tmp2.setTime(dateFrm.parse(czesci[0]));
			} catch (ParseException e) {
				tmp2 = null;
				e.printStackTrace();
			}
		}

		if (tmp1 != null) {
			calendar.set(Calendar.YEAR, tmp1.get(Calendar.YEAR));
			calendar.set(Calendar.MONTH, tmp1.get(Calendar.MONTH));
			calendar.set(Calendar.DAY_OF_MONTH, tmp1.get(Calendar.DAY_OF_MONTH));
		}
		if (tmp2 != null) {
			calendar.set(Calendar.HOUR, tmp2.get(Calendar.HOUR));
			calendar.set(Calendar.MINUTE, tmp2.get(Calendar.MINUTE));
		}
		return calendar.getTime();
	}

	/**
	 * Sprawdza parametry wejsciowe
	 * 
	 * @param startLon
	 * @param startLat
	 * @param stopLon
	 * @param stopLat
	 * @param data
	 * @return lista bledow, null gdy brak bledow
	 */
	private List<String> checkInputs(Double startLon, Double startLat,
			Double stopLon, Double stopLat) {

		log.info("Parametry wejœciowe: startLon " + startLon + "; startLat "
				+ startLat + "; stopLon " + stopLon + "; stopLat " + stopLat);

		List<String> errs = new ArrayList<String>();
		if (startLat == null || startLon == null)
			errs.add("Brak punktu startowego");
		if (stopLat == null || stopLon == null)
			errs.add("Brak punktu koncowego");

		return errs;
	}

	private Odpowiedz findRoute(Point startPoint, Point stopPoint,
			Date startTime) {
		Odpowiedz odpowiedz;

		algorithmBean.setStartPoint(startPoint);
		algorithmBean.setStopPoint(stopPoint);
		// TODO: czasami startTime nie zawiera godziny ustawionej w formularzu
		// na stronie tylko ustawia aktualna godzine
		algorithmBean.setStartTime(startTime);
		Boolean result = algorithmBean.run();
		if (result)
			odpowiedz = new Odpowiedz(algorithmBean.getPath(),
					algorithmBean.getHours(), "OK");
		else
			odpowiedz = new Odpowiedz(null, null, "Nie znaleziono trasy.");

		return odpowiedz;
	}

}
