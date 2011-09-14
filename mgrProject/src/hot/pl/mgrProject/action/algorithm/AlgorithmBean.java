package pl.mgrProject.action.algorithm;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.postgis.Point;

import pl.mgrProject.model.Konfiguracja;
import pl.mgrProject.model.Odjazd;
import pl.mgrProject.model.Przystanek;
import pl.mgrProject.model.PrzystanekTabliczka;

@Stateless
@Name("algorithmBean")
public class AlgorithmBean implements Algorithm {
	@Logger
	private Log log;
	@In
	private EntityManager mgrDatabase;
	@In(create=true)
	private NeighborhoodMatrix neighborhoodMatrixBean;
	@In(create=true)
	private Dijkstra dijkstraBean;
	/**
	 * Punkt startowy wybrany na mapie.
	 */
	private Point startPoint;
	/**
	 * Punkt koncowy wybrany na mapie.
	 */
	private Point stopPoint;
	/**
	 * Indeks punktu startowego.
	 */
	private int start = -1;
	/**
	 * Indeks punktu kocowego.
	 */
	private int stop  = -1;
	/**
	 * Trasa obliczona przez algorytm w postaci listy ID tabliczek.
	 */
	private ArrayList<Integer> path;
	/**
	 * Wszystkie tabliczki pobrane z bazy
	 */
	private List<PrzystanekTabliczka> tabliczki;
	/**
	 * Czas rozpoczecia.
	 */
	private Calendar startTime;
	/**
	 * Czas odjazdu z pierwszego przystanku
	 */
	private Calendar odjazd;
	
	
	/**
	 * Metoda inicjujaca wszystkie wymagane zmienne, tworzaca macierz sasiedztwa i uruchamiajaca algorytm wyszukiwania trasy.
	 */
	@Override
	public Boolean run() {
		Przystanek pstart = getClosestTo(startPoint);
		Przystanek pstop  = getClosestTo(stopPoint);
		
		if (pstart == null || pstop == null) {
			log.info("Nie mozna znalezc trasy.");
			return false;
		}

		List<PrzystanekTabliczka> tabForStart = mgrDatabase.createNamedQuery("tabliczkiPoPrzystanku").setParameter("przystanek", pstart).getResultList();
		List<PrzystanekTabliczka> tabForStop = mgrDatabase.createNamedQuery("tabliczkiPoPrzystanku").setParameter("przystanek", pstop).getResultList();
		
		log.info("Liczba tabliczek dla start: " + tabForStart.size());
		log.info("Liczba tabliczek dla stop: " + tabForStop.size());
		
		if (tabForStart.size() == 0 || tabForStop.size() == 0) {
			log.info("Nie mozna znalezc trasy.");
			return false;
		}
		
		//PrzystanekTabliczka tabStart = tabForStart.get(0);
		//pobieramy tabliczke dla start, dla ktorej jest dostepny najwczesniejszy odjazd
		PrzystanekTabliczka tabStart = checkTime(tabForStart);
		if (tabStart == null) {
			log.info("Brak rozkladu jazdy dla przystanku startowego.");
			return false;
		}
		PrzystanekTabliczka tabStop  = tabForStop.get(0);

		Long startID = tabStart.getId();
		Long stopID  = tabStop.getId();;
		
		tabliczki = mgrDatabase.createNamedQuery("wszystkieTabliczki").getResultList();
		neighborhoodMatrixBean.setTabliczki(tabliczki);
		int n = tabliczki.size();
		
		//odwzorowanie przystanku na indeks tablicy poniewaz algorytm operuje na indeksach tablicy
		start = neighborhoodMatrixBean.getIndex(startID);
		stop  = neighborhoodMatrixBean.getIndex(stopID);
		
		neighborhoodMatrixBean.create(odjazd);
		int[][] E = neighborhoodMatrixBean.getE();
		Integer[] V = neighborhoodMatrixBean.getV();
		
		//==================
		// Uruchomienie algorytmu Dijkstry
		//==================
		if (start != -1) { //jesli przystanek startowy istnieje
			try {
				log.info("[Dijkstra] n: " + n);
				log.info("[Dijkstra] V:\n" + Arrays.toString(V));
				log.info("[Dijkstra] start: " + start);
				log.info("[Dijkstra] stop: " + stop);
				dijkstraBean.init(n, E, V, start);
				
				try {
					log.info("AlgorithmBean: [Dijkstra] uruchamianie algorytmu wyszukiwania trasy.");
					dijkstraBean.run();
				} catch(ArrayIndexOutOfBoundsException e2) {
					log.info("AlgorithmBean: [Dijkstra] podczas wykonywania algorytmu wystapil blad.");
					for (int k = 0; k < e2.getStackTrace().length; ++k) {
						log.info(e2.getStackTrace()[k].toString());
					}
				}

				log.info("AlgorithmBean: [Dijkstra] Algorytm zakonczony pomyslnie.");
				path = dijkstraBean.getPathTab(stop);
				printTrasa();
				return true;
			} catch(Exception e) {
				log.info("AlgorithmBean: [Dijkstra] Wystapil niespodziewany wyjatek");
				
				e.printStackTrace();
				return false;
			}
		} else {
			log.info("AlgorithmBean: Nie znaleziono przystanku startowego.");
			return false;
		}
	}
	
	@Override
	public Boolean setStartPoint(Point p) {
		startPoint = p;
		log.info("[AlgorithmBean] startPoint set");
		return true;
	}
	
	@Override
	public Boolean setStopPoint(Point p) {
		stopPoint = p;
		log.info("[AlgorithmBean] stopPoint set");
		return true;
	}
	
	
	public Przystanek getClosestToStart2() {
		BigInteger id = (BigInteger)mgrDatabase.createNativeQuery("select foo.id from (select p.id, st_distance_sphere(p.location, ST_GeomFromText('POINT(" + startPoint.x + " " + startPoint.y + ")', 4326)) as odleglosc from przystanki p order by odleglosc limit 1) as foo").getResultList().get(0);
		Przystanek p = mgrDatabase.getReference(Przystanek.class, id.longValue());
		log.info("[AlgorithmBean] Znaleziono najblizszy przystanek: " + p.getNazwa());
		return p;
	}
	
	@Override
	public Przystanek getClosestTo(Point point) {
		Konfiguracja konf = (Konfiguracja)mgrDatabase.createNamedQuery("konfiguracjaPoNazwie").setParameter("nazwa", "default").getSingleResult();
		List<BigInteger> nearest = mgrDatabase.createNativeQuery("select foo.id from (select p.id, st_distance_sphere(p.location, ST_GeomFromText('POINT(" + point.x + " " + point.y + ")', 4326)) as odleglosc from przystanki p order by odleglosc) as foo where foo.odleglosc < " + konf.getOdlegloscPrzystankow()).getResultList();
		
		for (BigInteger i : nearest) {
			Przystanek p = mgrDatabase.getReference(Przystanek.class, i.longValue());
			//sprawdzenie czy istnieje rozklad jazdy dla danego przystanku
			if (p.getPrzystanekTabliczki().size() > 0 && p.getPrzystanekTabliczki().get(0).getOdjazdy().size() > 0) {
				return p;
			}
		}

		return null;
	}
	
	/**
	 * Wyswietla w oknie logow trase jaka zostala znaleziona przez algorytm.
	 */
	private void printTrasa() throws Exception {
		if (path == null) {
			return;
		}
		
		log.info("Route: " + dijkstraBean.getPath(stop));
		String info = "";
		String hours = "";
		for (int i : path) {
			info += tabliczki.get(i).getPrzystanek().getNazwa() + " <- ";
		}
		log.info(info);
		
		List<Calendar> hourstmp = neighborhoodMatrixBean.getHours();
		
		for (Integer i : path) {
			hours += hourstmp.get(i).getTime() + " <- ";
		}
		getHours();
		log.info(hours);
	}
	
	/**
	 * Zwraca znaleziona trase.
	 * @return Lista kolejnych tabliczek bedaca znaleziona przez algorytm trasa.
	 */
	@Override
	public List<PrzystanekTabliczka> getPath() {
		if (path == null) {
			return new ArrayList<PrzystanekTabliczka>();
		}
		
		List<PrzystanekTabliczka> trasa = new ArrayList<PrzystanekTabliczka>();
		
		PrzystanekTabliczka poprzednia, aktualna;
		
		for (Integer i : path) {
			trasa.add(tabliczki.get(i));
		}
		
		Collections.reverse(trasa);
		
		return trasa;
	}
	
	/**
	 * Zwraca tabliczke, z ktorej jest dostepny najwczesniejszy odjazd.
	 * @return Tabliczka z najwczesniejszym odjazdem.
	 */
	private PrzystanekTabliczka checkTime(List<PrzystanekTabliczka> tabs) {
		int index = -1; //indeks przystanku z najwczesniejszym odjazdem
		
		for (int i = 0; i < tabs.size(); ++i) {
			List<Odjazd> odj = tabs.get(i).getOdjazdy();
			if (odj.size() == 0) continue;
			Calendar min = Calendar.getInstance();
			min.set(Calendar.YEAR, 2099);

			for (int j = 0; j < odj.size(); ++j) {
				Calendar tmp = neighborhoodMatrixBean.dateToCalendar(odj.get(j).getCzas());
				if (tmp.after(startTime) && tmp.before(min)) {
					index = i;
					min = tmp;
				}
			}
			log.info("Odjazd: " + min.getTime());
			odjazd = (Calendar)min.clone();
		}
		
		return index == -1 ? null : tabs.get(index);
	}
	
	/**
	 * Ustawia date rozpoczecia trasy.
	 */
	@Override
	public void setStartTime(Date startTime) {
		this.startTime = neighborhoodMatrixBean.dateToCalendar(startTime);
	}
	
	@Override
	public List<Date> getHours() {
		if (path == null) {
			new ArrayList<Date>();
		}
		
		List<Date> result = new ArrayList<Date>();
		List<Calendar> hours = neighborhoodMatrixBean.getHours();
		
		for (Integer i : path) {
			result.add(hours.get(i).getTime());
		}
		
		Collections.reverse(result);
		return result;
	}
}
