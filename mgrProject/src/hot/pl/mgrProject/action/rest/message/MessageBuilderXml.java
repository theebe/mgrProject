package pl.mgrProject.action.rest.message;

import static pl.mgrProject.action.rest.message.MessageBuilderHelper.*;

import java.util.Date;
import java.util.List;
import java.util.Stack;

import pl.mgrProject.action.Odpowiedz;
import pl.mgrProject.model.Linia;
import pl.mgrProject.model.Odjazd;
import pl.mgrProject.model.Przystanek;
import pl.mgrProject.model.PrzystanekTabliczka;

public class MessageBuilderXml extends AbstractMessageBuilder {

	public MessageBuilderXml(boolean pp) {
		super(pp);
	}

	@Override
	public String buildMessage(String s) {
		StringBuilder sb = new StringBuilder();
		sb.append(XML_VERSION);
		xmlOpen(INFO, sb);
		xmlAdd(s, sb);
		xmlCloseAll(sb);
		return sb.toString();
	}

	@Override
	public String buildMessage(Odpowiedz o) {
		StringBuilder sb = new StringBuilder();
		// brak odb
		if (o == null || o.getDateList().isEmpty() || o.getPtList().isEmpty()
				|| !(o.getInfo().equals("OK"))) {
			sb.append(buildMessage("Brak wyniku. \n" + o.getInfo()));
			return sb.toString();
		}
		sb.append(XML_VERSION);

		buildOdpowiedz(o, sb);

		xmlCloseAll(sb);
		return sb.toString();
	}

	@Override
	public String buildMessagePrzystanek(List<Przystanek> lp, String... include) {
		StringBuilder sb = new StringBuilder();
		// brak przystankow
		if (lp == null || lp.isEmpty()) {
			sb.append(buildMessage("Brak przystanku o wybranym id."));
			return sb.toString();
		}
		sb.append(XML_VERSION);
		xmlOpen(PRZYSTANKI, sb);
		for (Przystanek p : lp) {
			buildOnePrzystanek(p, sb, include);
		}
		xmlCloseAll(sb);
		return sb.toString();
	}

	@Override
	public String buildMessageLinia(List<Linia> ll, String... include) {
		StringBuilder sb = new StringBuilder();
		// brak linii
		if (ll == null || ll.isEmpty()) {
			sb.append(buildMessage("Brak linii o wybranym numerze."));
			return sb.toString();
		}

		sb.append(XML_VERSION);

		xmlOpen(LINIE, sb);
		for (Linia l : ll) {
			buildOneLinia(l, sb, include);
		}
		xmlCloseAll(sb);
		return sb.toString();
	}

	// //////////////////////////
	// real builder :)

	private void buildOnePrzystanek(Przystanek p, StringBuilder sb,
			String... include) {
		xmlOpen(PRZYSTANEK, sb);
		{
			xmlOpen(ID, sb);
			xmlAdd(String.valueOf(p.getId()), sb);
			xmlClose(sb);

			xmlOpen(NAZWA, sb);
			xmlAdd(p.getNazwa(), sb);
			xmlClose(sb);

			xmlOpen(LOCATION, sb);
			{
				xmlOpen(LON, sb);
				xmlAdd(String.valueOf(p.getLocation().getX()), sb);
				xmlClose(sb);

				xmlOpen(LAT, sb);
				xmlAdd(String.valueOf(p.getLocation().getY()), sb);
				xmlClose(sb);
			}
			xmlClose(sb);

			xmlOpen(TYP, sb);
			xmlAdd(typKomToString(p.getTyp()), sb);
			xmlClose(sb);

			if (include != null) {
				for (String inc : include) {
					if (TABLICZKI.equals(inc)) {
						List<PrzystanekTabliczka> lpt = p
								.getPrzystanekTabliczki();
						buildPrzystanekTabliczkaList(lpt, null, sb, LINIA,
								ODJAZDY);
					}
				}
			}

		}
		xmlClose(sb);

	}

	private void buildOneLinia(final Linia l, final StringBuilder sb,
			final String... include) {
		xmlOpen(LINIA, sb);

		xmlOpen(ID, sb);
		xmlAdd(String.valueOf(l.getId()), sb);
		xmlClose(sb);

		xmlOpen(NUMER, sb);
		xmlAdd(String.valueOf(l.getNumer()), sb);
		xmlClose(sb);

		xmlOpen(TYP, sb);
		xmlAdd(typKomToString(l.getTyp()), sb);
		xmlClose(sb);

		if (include != null) {
			for (String inc : include) {
				if (TABLICZKI.equals(inc)) {
					List<PrzystanekTabliczka> lpt = l.getPrzystanekTabliczka();
					buildPrzystanekTabliczkaList(lpt, null, sb, PRZYSTANEK,
							ODJAZDY);
				}
				if (PRZYSTANEK.equals(inc)) {
					List<PrzystanekTabliczka> lpt = l.getPrzystanekTabliczka();
					for (PrzystanekTabliczka pt : lpt) {
						Przystanek p = pt.getPrzystanek();
						buildOnePrzystanek(p, sb);
					}
				}
			}
		}

		xmlClose(sb);
	}

	private void buildOdpowiedz(Odpowiedz o, StringBuilder sb) {
		xmlOpen(ODPOWIEDZ, sb);

		xmlOpen(INFO, sb);
		xmlAdd(o.getInfo(), sb);
		xmlClose(sb);

		List<PrzystanekTabliczka> lpt = o.getPtList();
		List<Date> ldate = o.getDateList();

		buildPrzystanekTabliczkaList(lpt, ldate, sb, LINIA, PRZYSTANEK, CZAS);

		xmlClose(sb);
	}

	private void buildPrzystanekTabliczkaList(
			final List<PrzystanekTabliczka> lpt, List<Date> ldate,
			final StringBuilder sb, final String... include) {
		xmlOpen(TABLICZKI, sb);

		for (int i = 0; i < lpt.size(); ++i) {
			PrzystanekTabliczka pt = lpt.get(i);
			xmlOpen(TABLICZKA, sb);
			{
				xmlOpen(ID, sb);
				xmlAdd(String.valueOf(pt.getId()), sb);
				xmlClose(sb);

				if (include != null) {
					for (String inc : include) {
						if (PRZYSTANEK.equals(inc)) {
							Przystanek p = pt.getPrzystanek();
							buildOnePrzystanek(p, sb);
						}
						if (LINIA.equals(inc)) {
							Linia l = pt.getLinia();
							buildOneLinia(l, sb);
						}
						if (ODJAZDY.equals(inc)) {
							List<Odjazd> lo = pt.getOdjazdy();
							buildOdjazdy(lo, sb);
						}
						if (ldate != null && CZAS.equals(inc)) {
							Date date = ldate.get(i);
							xmlOpen(CZAS, sb);
							xmlAdd(dateToString(date), sb);
							xmlClose(sb);

						}
					}
				}

				xmlOpen(CZAS_DO_NAST, sb);
				xmlAdd(String.valueOf(pt.getCzasDoNastepnego()), sb);
				xmlClose(sb);

			}
			xmlClose(sb);
		}

		xmlClose(sb);

	}

	private void buildOdjazdy(final List<Odjazd> lo, final StringBuilder sb) {
		xmlOpen(ODJAZDY, sb);
		for (Odjazd o : lo) {
			xmlOpen(ODJAZD, sb);

			xmlOpen(CZAS, sb);
			xmlAdd(dateToString(o.getCzas()), sb);
			xmlClose(sb);

			xmlOpen(TYPDNIA, sb);
			xmlAdd(typDniaToString(o.getTypDnia()), sb);
			xmlClose(sb);

			xmlClose(sb);
		}
		xmlClose(sb);

	}

	/**
	 * otwierajacy tag, dodaje do stosu
	 * 
	 * @param prop
	 * @param sb
	 */
	private void xmlOpen(final String prop, final StringBuilder sb) {
		tabs(sb);
		sb.append(XML_L).append(prop).append(XML_R);
		opens.push(prop);

	}

	/**
	 * pobiera ze stosu i zamyka
	 * 
	 * @param sb
	 */
	private void xmlClose(final StringBuilder sb) {
		String prop = opens.pop();
		tabs(sb);
		sb.append(XML_LK).append(prop).append(XML_R);

	}

	/**
	 * dodaje zwykly text do xml'a
	 * 
	 * @param val
	 * @param sb
	 */
	private void xmlAdd(final String val, final StringBuilder sb) {
		tabs(sb);
		sb.append(val);
	}

	/**
	 * zamyka wszystkie istniejace na stosie
	 * 
	 * @param sb
	 */
	private void xmlCloseAll(final StringBuilder sb) {
		while (!opens.isEmpty())
			xmlClose(sb);

	}



}
