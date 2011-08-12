package pl.mgrProject.action;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.postgis.Point;

import pl.mgrProject.action.utils.Dijkstra;
import pl.mgrProject.model.Przystanek;
import pl.mgrProject.model.PrzystanekTabliczka;

@Stateful
@Scope(ScopeType.CONVERSATION)
@Name("algorithmBean")
public class AlgorithmBean implements Algorithm {
	@Logger
	private Log log;
	@In
	private EntityManager mgrDatabase;
	private Point startPoint;
	private Point stopPoint;
	
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
		int inf = 1000; //nieskonczonosc. Oznacza brak krawedzi miedzy wierzcholkami.
		Dijkstra d = null;
		int maxErrors = 3; //maksymalna liczba prob wznowienia algorytmu
		
		List<PrzystanekTabliczka> tabliczki = mgrDatabase.createNamedQuery("wszystkieTabliczki").getResultList();
		int n = tabliczki.size();
		
		//odwzorowanie przystanku na indeks tablicy poniewaz algorytm operuje na indeksach tablicy
		start = getIndex(startID, tabliczki);
		stop  = getIndex(stopID,  tabliczki);
		
		int[][] E = new int[n][n];  //macierz sasiedztwa
		Integer[] V = new Integer[n]; //wektor wierzcholkow
		
		//inicjalizacja macierzy sasiedztwa
		for (int i = 0; i < n; ++i) {
			for (int j = 0; j < n; ++j) {
				E[i][j] = i == j ? 0 : inf;
			}
			V[i] = i;
		}
		
		List<Integer> tabSprawdzone = new ArrayList<Integer>();
		//tabSprawdzone.add(start);
		List<Integer> tabTrace = new ArrayList<Integer>();
		
		//Algorytm budowania macierzy sasiedztwa. Na razie wagi krawedzi przypisane sa na sztywno.
		//Po testach wyglada na to, ze dziala poprawnie. Nie uwzglednia przystankow ktore nie sa dodane do zadnej linii.
		//TODO: Przemyslec dokladniej przypadki kiedy ostatni przystanek(tabliczka) linii nie ma nastepnego.
		//      Algorytm wymaga aby kazdy wierzholek mial przynajmniej jedna krawedz wychodzaca.
		//Propozycja: Ostatnia tabliczka linii ma krawedz wychodzaca wskazujaca na ta siebie sama.
		int aktualny = start, nastepny = -1;
		boolean backTrace = false;
		while (tabSprawdzone.size() <= tabliczki.size()) {
			try {
				nastepny = getIndex(tabliczki.get(aktualny).getNastepnyPrzystanek().getId(), tabliczki);
				if (tabSprawdzone.contains(nastepny)) {
					E[aktualny][nastepny] = 10;
					log.info("[A]Laczenie: " + tabliczki.get(aktualny).getId() + ":" + tabliczki.get(nastepny).getId());
					tabTrace.add(aktualny);
					aktualny = nastepny;
					backTrace = true;
				} else {
					log.info("Nastepny: " + tabliczki.get(nastepny).getId());
					E[aktualny][nastepny] = 10;
					log.info("[B]Laczenie: " + tabliczki.get(aktualny).getId() + ":" + tabliczki.get(nastepny).getId());

					tabSprawdzone.add(nastepny);
					tabTrace.add(aktualny);
					aktualny = nastepny;
					
					log.info("TabTrace add " + tabliczki.get(aktualny).getId());
					log.info("Koniec petli: " + (tabSprawdzone.size() <= tabliczki.size()));
				}
			} catch(Exception e) {
				log.info("Nie ma nastepnego");
				backTrace = true;
			} finally {
				if (backTrace) {
					for (int i = tabTrace.size()-1; i >= 0; --i) {
						nastepny = getIndex(tabliczki.get(tabTrace.get(i)).getId(), tabliczki);
						log.info("Nastepny: " + tabliczki.get(nastepny).getId());
						E[aktualny][nastepny] = 10;
						log.info("[D]Laczenie: " + tabliczki.get(aktualny).getId() + ":" + tabliczki.get(nastepny).getId());
						aktualny = nastepny;
					}
					tabTrace.clear();
					log.info("TabTrace clear");
					backTrace = false;
					//==============
					for (int i = 0; i < tabliczki.size(); ++i) {
						if (!tabSprawdzone.contains(i)) {
							aktualny = i;
							tabSprawdzone.add(aktualny);
							break;
						} else {
							aktualny = -1;
						}
					}
					//dzieki temu petla while sie zakonczy
					if (aktualny == -1) {
						tabSprawdzone.add(-1);
					}
				}
			}
		}
		
		//TODO Laczenie tabliczek znajdujacych sie na tym samym przystanku (przesiadki piesze)
		//     uruchomienie algorytmu
		
		String info = "";
		for (int i = 0; i < n; ++i) {
			info += "[";
			for (int j = 0; j < n; ++j) {
				info += E[i][j]+",";
			}
			info += "]\n";
		}
		
		log.info(info);
		
		return true;
	}
	
	public Point getStartPoint() {
		return startPoint;
	}
	
	public Boolean setStartPoint(double x, double y) {
		if(x == 0 || y == 0) {
			return false;
		}
		
		startPoint = new Point(x, y);
		startPoint.setSrid(4326);
		log.info("[AlgorithmBean] startPoint set");
		return true;
	}
	
	public Point getStopPoint() {
		return stopPoint;
	}
	
	public Boolean setStopPoint(double x, double y) {
		if(x == 0 || y == 0) {
			return false;
		}
		
		stopPoint = new Point(x, y);
		stopPoint.setSrid(4326);
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
	 * Zwraca indeks z tablicy 'tabliczki' odpowiadajacy podanemu ID tabliczki.
	 * @param id
	 * @param tabliczki
	 * @return
	 */
	private int getIndex(Long id, List<PrzystanekTabliczka> tabliczki) {
		for (int i = 0; i < tabliczki.size(); ++i) {
			if (tabliczki.get(i).getId() == id) {
				return i;
			}
		}
		return -1;
	}
	
	private void printTrasa(ArrayList<Integer> trasa, List<PrzystanekTabliczka> tabliczki) {
		//PrzystanekTabliczka tmp;
		String info = "";
		for (int i : trasa) {
			info += tabliczki.get(i).getPrzystanek().getNazwa() + " <- ";
		}
		log.info(info);
	}
	
	@Destroy
	public void destroy() {
	}
	
	@Remove
	public void remove() {
	}
}
