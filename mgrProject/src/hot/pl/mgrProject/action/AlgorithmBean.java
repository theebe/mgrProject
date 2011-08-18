package pl.mgrProject.action;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
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
//@Scope(ScopeType.CONVERSATION)
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
		
		List<PrzystanekTabliczka> tabForStart = mgrDatabase.createNamedQuery("tabliczniPoPrzystanku").setParameter("przystanek", pstart).getResultList();
		List<PrzystanekTabliczka> tabForStop = mgrDatabase.createNamedQuery("tabliczniPoPrzystanku").setParameter("przystanek", pstop).getResultList();
		log.info("Liczba tabliczek dla start: " + tabForStart.size());
		log.info("Liczba tabliczek dla stop: " + tabForStop.size());
			//pobieranie pierwszej tabliczki z listy dla danego przystanku pocz¹tkowego
			//na razie nie wiadomo dla której tabliczki mozna znalezc krotsza trase.
			//bedzie sie trzeba nad tym jeszcze zastanowic
		PrzystanekTabliczka tabStart = tabForStart.get(0);
		PrzystanekTabliczka tabStop  = tabForStop.get(0);

		Long startID = tabStart.getId();
		Long stopID  = tabStop.getId();;
		int start = -1;
		int stop  = -1;
//		Dijkstra d = null;
		int maxErrors = 3; //maksymalna liczba prob wznowienia algorytmu
		
		tabliczki = mgrDatabase.createNamedQuery("wszystkieTabliczki").getResultList();
		neighborhoodMatrixBean.setTabliczki(tabliczki);
		int n = tabliczki.size();
		
		//odwzorowanie przystanku na indeks tablicy poniewaz algorytm operuje na indeksach tablicy
		start = neighborhoodMatrixBean.getIndex(startID);
		stop  = neighborhoodMatrixBean.getIndex(stopID);
		
		
		Integer[] V = new Integer[n]; //wektor wierzcholkow
		
		for (int i = 0; i < n; ++i) {
			V[i] = i;
		}
		
		neighborhoodMatrixBean.create(start);
		int[][] E = neighborhoodMatrixBean.getE();
		
		//==================
		// Uruchomienie algorytmu Dijkstry
		//==================
		if (start != -1) { //jesli przystanek startowy istnieje
			try {
				boolean error = true;
				
				String tabE = "";
				for (int i = 0; i < E.length; ++i) {
					tabE += "[";
					for (int j = 0; j < E[i].length; ++j) {
						tabE += E[i][j] + ",";
					}
					tabE += "]\n";
				}
				log.info("[Dijkstra] n: " + n);
				log.info("[Dijkstra] E:\n" + tabE);
				log.info("[Dijkstra] V:\n" + Arrays.toString(V));
				log.info("[Dijkstra] start: " + start);
				log.info("[Dijkstra] stop: " + stop);
				dijkstraBean.init(n, E, V, start);
				int errors = 0;
				while (error) {
					try {
						log.info("HomeBean: [Dijkstra] 1uruchamianie algorytmu wyszukiwania trasy.");
						dijkstraBean.run();
					} catch(ArrayIndexOutOfBoundsException e2) {
						log.info("HomeBean: [Dijkstra] podczas wykonywania algorytmu wystapil blad.");
						for (int k = 0; k < e2.getStackTrace().length; ++k) {
							log.info(e2.getStackTrace()[k].toString());
						}
						if (++errors < maxErrors) {
							//proba naprawienia sytuacji
							dijkstraBean.init(n, E, V, start);
						}
					}
					error = false;
				}
				log.info("HomeBean: [Dijkstra] Algorytm zakonczony po " + errors + " bledach.");
				log.info("Path: " + dijkstraBean.getPath(stop, log));
				path = dijkstraBean.getPathTab(stop, log);
				printTrasa();
				return true;
			} catch(Exception e) {
				log.info("HomeBean: [Dijkstra] Wystapil niepodziewany wyjatek");
				return false;
			}
		} else {
			log.info("HomeBean: Nie znaleziono przystanku startowego.");
			return false;
		}
	}
	
	public Point getStartPoint() {
		return startPoint;
	}
	
	public Boolean setStartPoint(Point p) {
		startPoint = p;
		log.info("[AlgorithmBean] startPoint set");
		return true;
	}
	
	public Point getStopPoint() {
		return stopPoint;
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
	private void printTrasa() {
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
		List<PrzystanekTabliczka> trasa = new ArrayList<PrzystanekTabliczka>();
		
		for (Integer i : path) {
			trasa.add(tabliczki.get(i));
		}
		
		return trasa;
	}
}
