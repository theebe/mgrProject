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

public class MessageBuilderJson extends AbstractMessageBuilder {

	public MessageBuilderJson(boolean pp) {
		super(pp);
	}

	@Override
	public String buildMessage(String s) {
		StringBuilder sb = new StringBuilder();
		jsonOpenObject(sb);
		jsonAddKeyVal(INFO, s, sb);
		jsonCloseAll(sb);
		return sb.toString();
	}

	@Override
	public String buildMessage(Odpowiedz o) {
		StringBuilder sb = new StringBuilder();
		if (o == null || o.getDateList().isEmpty() || o.getPtList().isEmpty()
				|| !(o.getInfo().equals("OK"))) {
			sb.append(buildMessage("Brak wyniku. \n" + o.getInfo()));
			return sb.toString();
		}
		buildOdpowiedz(o, sb);
		jsonCloseAll(sb);
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

		jsonOpenObject(sb);
		jsonAddKey(PRZYSTANKI, sb);
		jsonOpenArray(sb);

		for (int i = 0; i < lp.size(); ++i) {
			if (i != 0) {
				sb.append(JSON_COMMA);
			}
			Przystanek p = lp.get(i);
			buildOnePrzystanek(p, sb, include);

		}
		jsonCloseAll(sb);
		return sb.toString();
	}

	@Override
	public String buildMessageLinia(List<Linia> ll, String... include) {
		StringBuilder sb = new StringBuilder();
		// brak przystankow
		if (ll == null || ll.isEmpty()) {
			sb.append(buildMessage("Brak linii o wybranym numerze."));
			return sb.toString();
		}

		jsonOpenObject(sb);
		jsonAddKey(LINIE, sb);
		jsonOpenArray(sb);

		for (int i = 0; i < ll.size(); ++i) {
			if (i != 0) {
				sb.append(JSON_COMMA);
			}
			Linia l = ll.get(i);
			buildOneLinia(l, sb, include);

		}
		jsonCloseAll(sb);
		return sb.toString();
	}

	// ////////////////

	private void buildOnePrzystanek(Przystanek p, StringBuilder sb,
			String... include) {
		jsonOpenObject(sb);
		{
			jsonAddKeyVal(ID, p.getId(), sb);
			jsonAddComma(sb);

			jsonAddKeyVal(NAZWA, p.getNazwa(), sb);
			jsonAddComma(sb);

			jsonAddKeyVal(TYP, typKomToString(p.getTyp()), sb);
			jsonAddComma(sb);

			jsonAddKey(LOCATION, sb);
			jsonOpenObject(sb);
			{
				jsonAddKeyVal(LON, p.getLocation().getX(), sb);
				jsonAddComma(sb);
				jsonAddKeyVal(LAT, p.getLocation().getY(), sb);
			}
			jsonClose(sb);

			if (include != null) {
				for (String inc : include) {
					jsonAddComma(sb);
					if (TABLICZKI.equals(inc)) {
						jsonAddKey(TABLICZKI, sb);
						jsonOpenArray(sb);

						List<PrzystanekTabliczka> lpt = p
								.getPrzystanekTabliczki();

						for (int i = 0; i < lpt.size(); ++i) {
							PrzystanekTabliczka pt = lpt.get(i);
							if (i != 0) {
								sb.append(JSON_COMMA);
							}
							buildOnePrzystanekTabliczka(pt, null, sb, LINIA,
									ODJAZDY);
						}

						jsonClose(sb);
					}
				}
			}
		}
		jsonClose(sb);
	}

	private void buildOnePrzystanekTabliczka(PrzystanekTabliczka pt, Date d,
			StringBuilder sb, String... include) {
		jsonOpenObject(sb);
		{
			jsonAddKeyVal(ID, pt.getId(), sb);
			jsonAddComma(sb);

			jsonAddKeyVal(CZAS_DO_NAST, pt.getCzasDoNastepnego(), sb);

			if (include != null) {
				for (String inc : include) {
					jsonAddComma(sb);

					if (PRZYSTANEK.equals(inc)) {
						jsonAddKey(PRZYSTANEK, sb);
						Przystanek p = pt.getPrzystanek();
						buildOnePrzystanek(p, sb);
					}
					if (LINIA.equals(inc)) {
						jsonAddKey(LINIA, sb);
						Linia l = pt.getLinia();
						buildOneLinia(l, sb);
					}
					if (ODJAZDY.equals(inc)) {
						jsonAddKey(ODJAZDY, sb);
						List<Odjazd> lo = pt.getOdjazdy();
						buildOdjazdy(lo, sb);
					}
					if (d != null && CZAS.equals(inc)) {
						jsonAddKeyVal(CZAS, dateToString(d), sb);
					}
				}
			}

		}
		jsonClose(sb);
	}

	private void buildOdjazdy(List<Odjazd> lo, StringBuilder sb) {
		jsonOpenArray(sb);

		for (int i = 0; i < lo.size(); ++i) {

			if (i != 0) {
				sb.append(JSON_COMMA);
			}
			Odjazd o = lo.get(i);
			jsonOpenObject(sb);

			jsonAddKeyVal(CZAS, dateToString(o.getCzas()), sb);
			jsonAddComma(sb);

			jsonAddKeyVal(TYP_DNIA, typDniaToString(o.getTypDnia()), sb);

			jsonClose(sb);

		}

		jsonClose(sb);

	}

	private void buildOneLinia(Linia l, StringBuilder sb, String... include) {
		jsonOpenObject(sb);
		{
			jsonAddKeyVal(ID, l.getId(), sb);
			jsonAddComma(sb);

			jsonAddKeyVal(NUMER, l.getNumer(), sb);
			jsonAddComma(sb);

			jsonAddKeyVal(TYP, typKomToString(l.getTyp()), sb);

			if (include != null) {
				for (String inc : include) {
					jsonAddComma(sb);

					if (TABLICZKI.equals(inc)) {
						jsonAddKey(TABLICZKI, sb);
						jsonOpenArray(sb);

						List<PrzystanekTabliczka> lpt = l
								.getPrzystanekTabliczka();

						for (int i = 0; i < lpt.size(); ++i) {
							PrzystanekTabliczka pt = lpt.get(i);
							if (i != 0) {
								sb.append(JSON_COMMA);
							}
							buildOnePrzystanekTabliczka(pt, null, sb,
									PRZYSTANEK, ODJAZDY);
						}

						jsonClose(sb);
					}
					if (PRZYSTANEK.equals(inc)) {
						jsonAddKey(PRZYSTANKI, sb);
						jsonOpenArray(sb);

						List<PrzystanekTabliczka> lpt = l
								.getPrzystanekTabliczka();
						for (int i = 0; i < lpt.size(); ++i) {
							PrzystanekTabliczka pt = lpt.get(i);
							if (i != 0) {
								sb.append(JSON_COMMA);
							}
							Przystanek p = pt.getPrzystanek();
							buildOnePrzystanek(p, sb);
						}

						jsonClose(sb);
					}
				}
			}

		}
		jsonClose(sb);
	}

	private void buildOdpowiedz(Odpowiedz o, StringBuilder sb) {
		jsonOpenObject(sb);
		{
			jsonAddKeyVal(INFO, o.getInfo(), sb);
			jsonAddComma(sb);

			jsonAddKey(TABLICZKI, sb);
			jsonOpenArray(sb);

			List<PrzystanekTabliczka> lpt = o.getPtList();
			List<Date> ldate = o.getDateList();
			for (int i = 0; i < lpt.size(); ++i) {
				if (i != 0) {
					jsonAddComma(sb);
				}
				PrzystanekTabliczka pt = lpt.get(i);
				Date d = ldate.get(i);
				buildOnePrzystanekTabliczka(pt, d, sb, LINIA, PRZYSTANEK, CZAS);
			}

			jsonClose(sb);
		}
		jsonClose(sb);

	}

	// // // / real builder :)

	private void jsonOpenObject(StringBuilder sb) {
		opens.push(JSON_RO);
		tabs(sb);
		sb.append(JSON_LO);
	}

	private void jsonOpenArray(StringBuilder sb) {
		sb.append(JSON_LA);
		opens.push(JSON_RA);
	}

	private void jsonAddKey(String key, StringBuilder sb) {
		sb.append(JSON_QM).append(key).append(JSON_QM).append(JSON_COLON);
	}

	private void jsonAddVal(String val, StringBuilder sb) {
		sb.append(JSON_QM).append(val).append(JSON_QM);
	}

	private void jsonAddComma(StringBuilder sb) {
		sb.append(JSON_COMMA);
		tabs(sb);
	}

	private void jsonClose(StringBuilder sb) {
		tabs(sb);
		String val = opens.pop();
		sb.append(val);
	}

	private void jsonCloseAll(final StringBuilder sb) {
		while (!opens.isEmpty())
			jsonClose(sb);
	}

	// helper methods

	private void jsonAddKeyVal(String key, String val, StringBuilder sb) {
		jsonAddKey(key, sb);
		jsonAddVal(val, sb);
	}

	private void jsonAddKeyVal(String key, Long val, StringBuilder sb) {
		jsonAddKey(key, sb);
		jsonAddVal(String.valueOf(val), sb);
	}

	private void jsonAddKeyVal(String key, Integer val, StringBuilder sb) {
		jsonAddKey(key, sb);
		jsonAddVal(String.valueOf(val), sb);
	}

	private void jsonAddKeyVal(String key, Double val, StringBuilder sb) {
		jsonAddKey(key, sb);
		jsonAddVal(String.valueOf(val), sb);
	}

}
