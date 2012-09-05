package pl.mgrProject.action.algorithm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.postgis.Point;

import pl.mgrProject.model.Konfiguracja;
import pl.mgrProject.model.Linia;
import pl.mgrProject.model.Odjazd;
import pl.mgrProject.model.Przystanek;
import pl.mgrProject.model.PrzystanekTabliczka;

@Stateless
@Name("adjacencyMatrixBean")
public class AdjacencyMatrixBean implements AdjacencyMatrix {

	@Logger
	private Log log;
	@In
	private EntityManager mgrDatabase;
	/**
	 * Kontener z glabalna macierza sasiedztwa.
	 */
	private AdjacencyMatrixContainer M;
	/**
	 * Dynamiczna macierz sasiedztwa obliczana po uruchomieniu szukania
	 */
	private int[][] E;
	/**
	 * Tymczasowa, pomcnicza macierz potrzebna do omijania obliczonych
	 * wierzcholkow
	 */
	private int[][] Etmp;
	/**
	 * Konfiguracja.
	 */
	private Konfiguracja konf;
	/**
	 * Wszystkie tabliczki dostepne w bazie.
	 */
	private List<PrzystanekTabliczka> tabliczki;
	/**
	 * Mapa odwzorowujaca ID przystanow na indeksy.
	 */
	private Map<Long, Integer> idPrzystankowMap = null;
	/**
	 * Wektor z czasami odjazdow z kazdej tabliczki.
	 */
	private List<Calendar> hours;

	@Override
	public void compileMap() {
		idPrzystankowMap = null;
		tabliczki = mgrDatabase.createNamedQuery("wszystkieTabliczki")
				.getResultList();
		create();

		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream("matrix.obj"));
			out.writeObject(M);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void init() {
		if (M == null) {
			log.info("Inicjalizacja globalnej macierzy sasiedztwa...");
			try {
				ObjectInputStream in = new ObjectInputStream(
						new FileInputStream("matrix.obj"));
				M = (AdjacencyMatrixContainer) in.readObject();
				in.close();
			} catch (FileNotFoundException e) {
				log.info("Nie znaleziono skompilowanej mapy. Tworzenie nowej kompilacji.");
				compileMap();

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		log.info("Inicjalizacja wektora czasow...");
		hours = new ArrayList<Calendar>(tabliczki.size());
		for (int i = 0; i < tabliczki.size(); ++i) {
			hours.add(null);
		}

		log.info("Inicjalizacja dynamicznej macierzy sasiedztwa...");
		E = M.getE();
		Etmp = M.getE();
	}

	@Override
	public void create() {
		log.info("Pobieranie konfiguracji...");
		konf = (Konfiguracja) mgrDatabase
				.createNamedQuery("konfiguracjaPoNazwie")
				.setParameter("nazwa", "default").getSingleResult();
		log.info("Inicjalizacja macierzy sasiedztwa...");
		M = new AdjacencyMatrixContainer(tabliczki.size(),
				konf.getNieskonczonosc());

		log.info("Przetwarzanie linii...");
		processLines();
		log.info("Laczenie tabliczek...");
		processTabs();
	}

	/**
	 * Laczenie tabliczek w obrebie jednej linii.
	 */
	private void processLines() {
		List<Linia> linie = mgrDatabase.createNamedQuery("wszystkieLinie")
				.getResultList();
		int n = 0;

		for (Linia l : linie) {
			// informacja o postepie
			++n;
			log.info((100 * n) / linie.size() + "%");

			// jesli linia nie posiada rozkladu to ja ignorujemy
			if (!scheduleExists(l))
				continue;
			// w przeciwnym razie pobieramy wszystkie tabliczki z danej linii
			List<PrzystanekTabliczka> tabs = l.getPrzystanekTabliczka();

			// i po kolei laczymy je zapisujac wagi krawedzi do macierzy
			int aktualna = -1, nastepna = -1;

			for (int i = 0; i < tabs.size() - 1; ++i) {
				aktualna = getIndex(tabs.get(i).getId());
				nastepna = getIndex(tabs.get(i + 1).getId());
				M.set(aktualna, nastepna,
						getEdgeWeight(tabliczki.get(aktualna)));
			}
		}
	}

	/**
	 * Laczenie tabliczek pomiedzy roznymi liniami.
	 */
	private void processTabs() {
		List<PrzystanekTabliczka> tabliczki = mgrDatabase.createNamedQuery(
				"wszystkieTabliczki").getResultList();
		int n = 0;

		for (PrzystanekTabliczka aktualna : tabliczki) {
			++n;
			log.info((100 * n) / tabliczki.size() + "%");

			// pobranie tabliczek z najblizszych przystankow
			Set<PrzystanekTabliczka> nearest = getNearest(
					aktualna.getPrzystanek(), konf.getOdlegloscPrzystankow());
			for (PrzystanekTabliczka nastepna : nearest) {
				int aktualnaIndex = getIndex(aktualna.getId());
				int nastepnaIndex = getIndex(nastepna.getId());
				// jesli sa to te same tabliczki lub z tej samej linii to
				// pomijamy
				if (aktualnaIndex == nastepnaIndex
						|| aktualna.getLinia().getId()
								.equals(nastepna.getLinia().getId())) {
					continue;
				}
				// jesli tabliczki sa na tym samym przystanku to nie liczymy
				// czasu przejscia
				// pasazera tylko odrazu wpisujemy wage '0'
				if (aktualna.getPrzystanek().getId()
						.equals(nastepna.getPrzystanek().getId())) {
					M.set(aktualnaIndex, nastepnaIndex, 0);
					// w przeciwnym razie liczymy czas przejscia
				} else {
					M.set(aktualnaIndex, nastepnaIndex,
							getEdgeWeight(aktualna, nastepna));
				}
			}
		}
	}

	/**
	 * Obliczenie godzin przybycia na przystanki, aktualizacja wag krawedzi i
	 * okreslenie czasow przesiadek.
	 * 
	 * @param startTime
	 *            Godzina rozpoczecia podrozy.
	 * @param startId
	 *            ID tabliczki startowej.
	 * @param E
	 *            Tymczasowa kopia macierzy sasiedztwa.
	 */
	@Override
	public void processHours(Calendar startTime, Long startId) {
		int start = getIndex(startId);
		if (hours.get(start) == null) {
			hours.set(start, (Calendar) startTime.clone());
		} else if (hours.get(start).before(startTime)) {
			startTime = hours.get(start);
		}

		for (int i = 0; i < E[start].length; ++i) {
			// Etmp - jesli jest tu INF to znaczy ze krawedz zostala juz
			// wczesniej przetworzona lub nie istnieje
			if (Etmp[start][i] != M.getInf() && i != start) {
				// ustawienie nieskonczonosci aby krawedz nie byla ponownie
				// przetwarzana
				Etmp[start][i] = M.getInf();
				// ta sama linia wiec po obliczeniu godziny mozna isc dalej
				if (tabliczki.get(start).getLinia().getId()
						.equals(tabliczki.get(i).getLinia().getId())) {
					// kopiowanie startTime gdyz wywolania rekurencyjne nadpisza
					// wartosci obiektu,
					// a w daszych iteracjach petli bedzie potrzebna stara
					// wartosc
					Calendar tmp = (Calendar) startTime.clone();
					// dolicz do godziny czas przybycia na przystanek
					tmp.add(Calendar.MINUTE, E[start][i]);
					processHours(tmp, tabliczki.get(i).getId());
				} else {
					Calendar tmp = (Calendar) startTime.clone();
					tmp.add(Calendar.MINUTE, E[start][i]);
					// znajdz najwczesniejszy odjazd z 'i' pozniejszy od
					// startTime
					Calendar nextTime = checkTime(tabliczki.get(i), tmp);
					// jesli nie ma pozniejszego odjazdu to skasuj krawedz
					if (nextTime == null) {
						E[start][i] = M.getInf();
					} else {
						// obliczenie czasu oczekiwania
						int waitingTime = (int) (nextTime.getTimeInMillis() - tmp
								.getTimeInMillis()) / 60000;
						// dodanie czasu do wagi krawedzi
						E[start][i] += waitingTime;
						// przetwarzaj rekurencyjnie kolejna tabliczke
						processHours(nextTime, tabliczki.get(i).getId());
					}
				}
			}
		}
	}

	/**
	 * Metoda zwracajaca najwczesniejszy czas odjazdu z tabliczki pt. Czas ten
	 * musi byc pozniejszy niz czas 'time'.
	 * 
	 * @param pt
	 *            Tabliczka do analizy.
	 * @param time
	 *            Czas po ktorym ma byc odnajdywana kolejna godzina.
	 * @return Najwczesniejszy czas odjazdu pozniejszy od 'time' lub null jesli
	 *         brak odjazdu.
	 */
	private Calendar checkTime(PrzystanekTabliczka pt, Calendar time) {
		Calendar min = Calendar.getInstance();
		min.set(Calendar.YEAR, 2099);
		min.clear(Calendar.MILLISECOND);
		List<Odjazd> odj = pt.getOdjazdy();

		for (Odjazd o : odj) {
			Calendar tmp = dateToCalendar(o.getCzas());
			if ((tmp.after(time) || tmp.compareTo(time) == 0)
					&& tmp.before(min)) {
				min = tmp;
			}
		}

		return min.get(Calendar.YEAR) == 2099 ? null : min;
	}

	/**
	 * Konwertuje obiekt Date na Calendar. Przepisuje jedynie godziny, minuty i
	 * sekundy, wiec oprócz godziny reszta daty pozostanie domy?lna czyli
	 * ustawiona na dzisiajszy dzieñ.
	 * 
	 * @param d
	 *            Obiekt, ktory ma zosrac poddany konwersji.
	 * @return Obiekt Calendar bedacy wynikiem konwersji.
	 */
	@Override
	public Calendar dateToCalendar(Date d) {
		Calendar c = Calendar.getInstance();
		Calendar ctmp = Calendar.getInstance();

		ctmp.setTime(d);
		c.set(Calendar.HOUR_OF_DAY, ctmp.get(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, ctmp.get(Calendar.MINUTE));
		c.set(Calendar.SECOND, ctmp.get(Calendar.SECOND));
		c.clear(Calendar.MILLISECOND);

		return c;
	}

	@Override
	public int[][] getE() {
		return E;
	}

	@Override
	public void setTabliczki(List<PrzystanekTabliczka> tabliczki) {
		this.tabliczki = tabliczki;
	}

	/**
	 * Sprawdza czy istenieje rozklad jazdy dla danej linii.
	 * 
	 * @param l
	 *            Linia do sprawdzenia.
	 * @return True - rozklad istnieje, False - brak rozkladu.
	 */
	@Override
	public boolean scheduleExists(Linia l) {
		List<PrzystanekTabliczka> pt = l.getPrzystanekTabliczka();

		if (pt.size() == 0 || pt.get(0).getOdjazdy().size() == 0)
			return false;
		return true;
	}

	/**
	 * Zwraca wage krawedzi (czas) od tabliczki pt do jej nastepnika.
	 * 
	 * @param pt
	 *            Tabliczka, dla ktorej ma zostac zwrocana waga do jej
	 *            nastepnika/
	 * @return Waga krawedzi.
	 */
	private int getEdgeWeight(PrzystanekTabliczka pt) {
		return pt.getCzasDoNastepnego();
	}

	/**
	 * Zwraca indeks z tablicy 'tabliczki' odpowiadajacy podanemu ID tabliczki.
	 * 
	 * @param id
	 *            ID tabliczki.
	 * @return Index tabliczki, lub null jesli nie znaleziono.
	 */
	@Override
	public int getIndex(Long id) {
		if (this.idPrzystankowMap == null) {
			log.info("Tworzenie mapy odwzoruajcej relacje id do i w tablicy tabliczki adjacencymatrix");
			idPrzystankowMap = new HashMap<Long, Integer>();
			for (int i = 0; i < tabliczki.size(); ++i) {
				idPrzystankowMap.put(tabliczki.get(i).getId(), i);
			}
		}

		return idPrzystankowMap.get(id);
	}

	/**
	 * Dla podanego przystanku zwraca najblizsze tabliczki z przystankow
	 * znajdujacych sie w zadanym dystansie.
	 * 
	 * @param p
	 *            Przystanek, dla ktorego maja zostac zwrocone najblizsze
	 *            tabliczki.
	 * @param distance
	 *            Dystans w metrach, do ktorego maja byc poszukiwane przystanki.
	 * @return Lista tabliczek ze znalezionych najblizszych przystankow.
	 */
	private Set<PrzystanekTabliczka> getNearest(Przystanek p, int distance) {
		// pobranie id najblizszych przystankow
		List<BigInteger> nearest = mgrDatabase
				.createNativeQuery(
						"select foo.id from (select p.id, st_distance_sphere(p.location, ST_GeomFromText('POINT("
								+ p.getLocation().x
								+ " "
								+ p.getLocation().y
								+ ")', 4326)) as odleglosc from przystanki p order by odleglosc) as foo where foo.odleglosc < "
								+ distance).getResultList();
		List<Przystanek> przyst = new ArrayList<Przystanek>();

		// wyciagniecie obiektow przystankow
		for (BigInteger id : nearest) {
			przyst.add(mgrDatabase.getReference(Przystanek.class,
					id.longValue()));
		}

		// dodanie do jednej kolekcji tabliczek ze wszystkich znalezionych
		// przystankow
		List<PrzystanekTabliczka> tabliczki = new ArrayList<PrzystanekTabliczka>();
		for (Przystanek pr : przyst) {
			tabliczki.addAll(pr.getPrzystanekTabliczki());
		}

		// bez powtorzen
		Set<PrzystanekTabliczka> result = new LinkedHashSet<PrzystanekTabliczka>(
				tabliczki);

		return result;
	}

	/**
	 * Oblicza wagê krawedzi pomiedzy tabliczkami 'start' i 'stop.
	 * 
	 * @param start
	 *            Tablicka zrodlowa.
	 * @param stop
	 *            Tablicka docelowa.
	 * @return Waga krawedzi.
	 */
	private int getEdgeWeight(PrzystanekTabliczka start,
			PrzystanekTabliczka stop) {
		double v = konf.getPredkoscPasazera(); // predkosc pasazera w km/h
		Point st = start.getPrzystanek().getLocation();
		Point sp = stop.getPrzystanek().getLocation();

		// pobranie odleglosci miedzy przystankami
		Double s = (Double) mgrDatabase.createNativeQuery(
				"select st_distance_sphere(ST_GeomFromText('POINT(" + st.x
						+ " " + st.y + ")', 4326), ST_GeomFromText('POINT("
						+ sp.x + " " + sp.y + ")', 4326)) as odleglosc")
				.getSingleResult();
		Double t = (s / (1000 * v)) * 60; // czas podrozy pasazera miedzy
											// przystankami w minutach

		return Math.round(t.floatValue());
	}

	@Override
	public List<Calendar> getHours() {
		return hours;
	}

	@Override
	public Integer[] getV() {
		return M.getV();
	}
}
