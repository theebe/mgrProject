package pl.mgrProject.action.algorithm;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.postgis.Point;

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
	 * Metoda inicjujaca wszystkie wymagane zmienne, tworzaca macierz sasiedztwa i uruchamiajaca algorytm wyszukiwania trasy.
	 */
	public Boolean run() {
		Przystanek pstart = getClosestToStart();
		Przystanek pstop  = getClosestToStop();

		List<PrzystanekTabliczka> tabForStart = mgrDatabase.createNamedQuery("tabliczkiPoPrzystanku").setParameter("przystanek", pstart).getResultList();
		List<PrzystanekTabliczka> tabForStop = mgrDatabase.createNamedQuery("tabliczkiPoPrzystanku").setParameter("przystanek", pstop).getResultList();
		
		log.info("Liczba tabliczek dla start: " + tabForStart.size());
		log.info("Liczba tabliczek dla stop: " + tabForStop.size());
		
		if (tabForStart.size() == 0 || tabForStop.size() == 0) {
			log.info("Nie mozna znalezc trasy.");
			return false;
		}
		
			//pobieranie pierwszej tabliczki z listy dla danego przystanku pocz¹tkowego
			//na razie nie wiadomo dla której tabliczki mozna znalezc krotsza trase.
			//Polaczenie tabliczek na tych samych przystankach zagwarantuje znalezienie najkrótszej trasy.
		PrzystanekTabliczka tabStart = tabForStart.get(0);
		PrzystanekTabliczka tabStop  = tabForStop.get(0);

		Long startID = tabStart.getId();
		Long stopID  = tabStop.getId();;
		
		tabliczki = mgrDatabase.createNamedQuery("wszystkieTabliczki").getResultList();
		neighborhoodMatrixBean.setTabliczki(tabliczki);
		int n = tabliczki.size();
		
		//odwzorowanie przystanku na indeks tablicy poniewaz algorytm operuje na indeksach tablicy
		start = neighborhoodMatrixBean.getIndex(startID);
		stop  = neighborhoodMatrixBean.getIndex(stopID);
		
		neighborhoodMatrixBean.create(start);
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

				log.info("AlgorithmBean: [Dijkstra] Algorytm zakonczony popomyslnie.");
				path = dijkstraBean.getPathTab(stop);
				printTrasa();
				return true;
			} catch(Exception e) {
				log.info("AlgorithmBean: [Dijkstra] Wystapil niepodziewany wyjatek");
				
				e.printStackTrace();
				return false;
			}
		} else {
			log.info("AlgorithmBean: Nie znaleziono przystanku startowego.");
			return false;
		}
	}
	
	public Boolean setStartPoint(Point p) {
		startPoint = p;
		log.info("[AlgorithmBean] startPoint set");
		return true;
	}
	
	public Boolean setStopPoint(Point p) {
		stopPoint = p;
		log.info("[AlgorithmBean] stopPoint set");
		return true;
	}
	
	public Przystanek getClosestToStart() {
		BigInteger id = (BigInteger)mgrDatabase.createNativeQuery("select foo.id from (select p.id, st_distance_sphere(p.location, ST_GeomFromText('POINT(" + startPoint.x + " " + startPoint.y + ")', 4326)) as odleglosc from przystanki p order by odleglosc limit 1) as foo").getResultList().get(0);
		Przystanek p = mgrDatabase.getReference(Przystanek.class, id.longValue());
		log.info("[AlgorithmBean] Znaleziono najblizszy przystanek: " + p.getNazwa());
		return p;
	}
	
	public Przystanek getClosestToStop() {
		BigInteger id = (BigInteger)mgrDatabase.createNativeQuery("select foo.id from (select p.id, st_distance_sphere(p.location, ST_GeomFromText('POINT(" + stopPoint.x + " " + stopPoint.y + ")', 4326)) as odleglosc from przystanki p order by odleglosc limit 1) as foo").getResultList().get(0);
		Przystanek p = mgrDatabase.getReference(Przystanek.class, id.longValue());
		log.info("[AlgorithmBean] Znaleziono najblizszy przystanek: " + p.getNazwa());
		return p;
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
		for (int i : path) {
			info += tabliczki.get(i).getPrzystanek().getNazwa() + " <- ";
		}
		log.info(info);
	}
	
	/**
	 * Zwraca znaleziona trase.
	 * @return Lista kolejnych tabliczek bedaca znaleziona przez algorytm trasa.
	 */
	public List<PrzystanekTabliczka> getPath() {
		if (path == null) {
			return new ArrayList<PrzystanekTabliczka>();
		}
		
		List<PrzystanekTabliczka> trasa = new ArrayList<PrzystanekTabliczka>();
		
		for (Integer i : path) {
			trasa.add(tabliczki.get(i));
		}
		Collections.reverse(trasa);
		return trasa;
	}
}
