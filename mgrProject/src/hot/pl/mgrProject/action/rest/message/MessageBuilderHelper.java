package pl.mgrProject.action.rest.message;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import pl.mgrProject.model.TypDnia;
import pl.mgrProject.model.TypKomunikacji;

public final class MessageBuilderHelper {
	public static final String NL = "\n";
	public static final String TAB = "\t";
	public static final String DTAB = TAB + TAB;
	public static final String TTAB = DTAB + TAB;

	public static final String XML_L = "<";
	public static final String XML_R = ">";
	public static final String XML_LK = "</";
	public static final String XML_RK = "/>";

	public static final String JSON_COLON = ":";
	public static final String JSON_LO = "{";
	public static final String JSON_RO = "}";
	public static final String JSON_LA = "[";
	public static final String JSON_RA = "]";
	public static final String JSON_COMMA = ",";
	public static final String JSON_QM = "\"";

	public static final String PRZYSTANKI = "przystanki";
	public static final String PRZYSTANEK = "przystanek";
	public static final String INFO = "info";

	public static final String TABLICZKI = "tabliczki";
	public static final String TABLICZKA = "tabliczka";
	public static final String ODJAZDY = "odjazdy";
	public static final String ODJAZD = "odjazd";
	public static final String ID = "id";
	public static final String NAZWA = "nazwa";
	public static final String TYP = "typ";
	public static final String TYPDNIA = "typdnia";

	public static final String LOCATION = "location";
	public static final String LON = "lon";
	public static final String LAT = "lat";
	public static final String LINIA = "linia";
	public static final String LINIE = "linie";
	public static final String CZAS_DO_NAST = "czasDoNastepnego";
	public static final String CZAS = "czas";
	public static final String TYP_DNIA = "typDnia";
	public static final String NUMER = "numer";
	public static final String ODPOWIEDZ = "odpowiedz";

	public static final String XML_VERSION = "<?xml version=\"1.0\"  encoding=\"utf-8\" ?>";

	public static String typKomToString(TypKomunikacji tk) {
		if (tk == TypKomunikacji.T) {
			return "TRAMWAJOWY";
		} else if (tk == TypKomunikacji.A) {
			return "AUTOBUSOWY";
		} else
			return "";
	}

	public static String typDniaToString(TypDnia td) {
		if (td == TypDnia.DZIEN_POWSZEDNI) {
			return "DZIEN POWSZEDNI";
		} else if (td == TypDnia.SWIETA) {
			return "SWIETA";
		} else
			return "";
	}

	public static String dateToString(Date date) {
		DateFormat formatter = new SimpleDateFormat("HH:mm");
		return formatter.format(date);
	}

	/**
	 * Fabryka
	 * 
	 * @param format
	 * @param ppString
	 * @return
	 */
	public static MessageBuilder getMessageBuilder(String format,
			String ppString) {

		boolean pp = parsePP(ppString);
		if (format != null && (format.equals("xml") || format.equals("XML"))) {
			return new MessageBuilderXml(pp);
		} else {
			return new MessageBuilderJson(pp);
		}
	}

	private static boolean parsePP(String pp) {
		if (pp == null)
			return true;
		if ("false".equals(pp) || "FALSE".equals(pp) || "0".equals(pp)
				|| "F".equals(pp) || "f".equals(pp))
			return false;

		return true;
	}

	public static Response sendOut(String in, String format) {
		byte[] out;
		try {
			// na problemy z polskimi znakami
			out = in.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(e.toString()).build();
		}
		if (format != null && (format.equals("xml") || format.equals("XML"))) {

			return Response
					.ok()
					.header("Content-Type",
							MediaType.APPLICATION_XML + "; charset=UTF-8")
					.entity(out).build();
		} else {
			return Response
					.ok()
					.header("Content-Type",
							MediaType.APPLICATION_JSON + "; charset=UTF-8")
					.entity(out).build();
		}
	}
}
