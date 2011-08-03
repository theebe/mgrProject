package pl.mgrProject.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

/**
 * Klasa obs³uguj¹ca zdarzenia ze strony g³ownej poprzez ajax
 * 
 * @author bat
 *
 */
@Name("homeBean")
@Stateful
@Scope(ScopeType.CONVERSATION)
public class HomeBean implements Serializable, Home {
 

	@Logger
	private Log log;
	
	private Point startPoint ;
	
	private Point stopPoint ;

	private Date startTime;
	@In
	private EntityManager mgrDatabase;
	

	public Point getStartPoint() {
		//startPoint.(arg0)
		return startPoint; 
	}
	
	public Boolean setStartPoint(double x, double y){
		if(x==0 || y==0) return false;
		this.startPoint = new Point(x, y);
		this.startPoint.setSrid(4326);
		log.info("homeBean: ustawiono startPoint na wspolrzedne: "+String.valueOf(x) + ", " + String.valueOf(y));
		return true;
	}

	public Point getStopPoint() {
		return stopPoint;
	}
	
	
	public Boolean setStopPoint(double x, double y){
		if(x==0 || y==0) return false;
		this.stopPoint = new Point(x, y);
		this.stopPoint.setSrid(4326);
		log.info("homeBean: ustawiono stopPoint na wspolrzedne: "+String.valueOf(x) + ", " + String.valueOf(y));
		return true;
	}

	
	public Date getStartTime() {
		return startTime;
	}

	public Boolean setStartTime(Date startTime) {
		if(startTime == null)
			return false;
		this.startTime = startTime;
		return true;
	}
	
	public Boolean runAlgorithm() {
		List<PrzystanekTabliczka> tabliczki = mgrDatabase.createNamedQuery("wszystkieTabliczki").getResultList();
		int n = tabliczki.size();
		log.info("Liczba tabliczek: " + n);
		//tymczasowo do testow pobierany jest na sztywno pierwszy przystanek z listy
		//jako punkt startowy. Pozniej na podstawie StartPoint bedzie trzeba zrobic
		//wyszukiwanie najbli¿szego przystanku.
		Long startID = tabliczki.get(0).getId();
		//do testow pobierany jest ostatni prystanek z listy jako punkt koncowy
		Long stopID  = tabliczki.get(tabliczki.size()-1).getId();
		int start = -1;
		int stop  = -1;
		int inf = 1000; //nieskonczonosc. Oznacza brak krawedzi miedzy wierzcholkami.
		Dijkstra d = null;
		int maxErrors = 3; //maksymalna liczba prob wznowienia algorytmu
		
		//odwzorowanie przystanku na indeks tablicy poniewaz algorytm operuje na indeksach tablicy
		for (int i = 0; i < n; ++i) {
			if (tabliczki.get(i).getId() == startID) {
				start = i;
			}
			if (tabliczki.get(i).getId() == stopID) {
				stop = i;
			}
		}
		
		int[][] E = new int[n][n];  //macierz sasiedztwa
		Integer[] V = new Integer[n]; //wektor wierzcholkow
		
		//inicjalizacja macierzy sasiedztwa
		for (int i = 0; i < n; ++i) {
			for (int j = 0; j < n; ++j) {
				E[i][j] = i == j ? 0 : inf;
			}
			V[i] = i;
		}
		
		//wypelnienie macierzy sasiedztwa
		int aktualny = -1, nastepny = -1;
		for (PrzystanekTabliczka tab : tabliczki) {
			aktualny = getIndex(tab.getId(), tabliczki);
//			nastepny = getIndex(getNastepnaTabliczka(tab.getNastepnyPrzystanek(), tabliczki).getId(), tabliczki);
			nastepny = getIndex(tab.getNastepnyPrzystanek().getId(), tabliczki);
			//tutaj bedzie obliczanie wagi krawedzi. Na razie do testow przypisanie na sztywno.
			//waga krawedzi pomiedzy przytankiem aktualnym i nastepnym
			E[aktualny][nastepny] = 10;
		}
		//przesiadka piesza?
		//E[1][4] = 0;
		
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
				d = new Dijkstra(n, E, V, start);
				int errors = 0;
				while (error) {
					try {
						log.info("HomeBean: [Dijkstra] 1uruchamianie algorytmu wyszukiwania trasy.");
						d.run();
					} catch(ArrayIndexOutOfBoundsException e2) {
						log.info("HomeBean: [Dijkstra] podczas wykonywania algorytmu wystapil blad.");
						for (int k = 0; k < e2.getStackTrace().length; ++k) {
							log.info(e2.getStackTrace()[k].toString());
						}
						if (++errors < maxErrors) {
							//proba naprawienia sytuacji
							//byc moze trzeba tu jeszcze raz przygotowac graf (do przemyslenia)
							d = new Dijkstra(n, E, V, start);
						}
						//continue;
					}
					error = false;
				}
				log.info("HomeBean: [Dijkstra] Algorytm zakonczony po " + errors + " bledach.");
				log.info("Path: " + d.getPath(stop));
				printTrasa(d.getPathTab(stop), tabliczki);
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
	
	/**
	 * Zwraca obiekt tabliczki z nastepnego przystanku.
	 * @param p
	 * @param tabliczki
	 */
	private PrzystanekTabliczka getNastepnaTabliczka(Przystanek p, List<PrzystanekTabliczka> tabliczki) {
		for (PrzystanekTabliczka pt : tabliczki) {
			if (pt.getPrzystanek().equals(p)) {
				return pt;
			}
		}
		return null;
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
	@Remove
	public void destory(){}


	

}
