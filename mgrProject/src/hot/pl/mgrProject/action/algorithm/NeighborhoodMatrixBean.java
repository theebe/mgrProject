package pl.mgrProject.action.algorithm;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import pl.mgrProject.model.Linia;
import pl.mgrProject.model.Przystanek;
import pl.mgrProject.model.PrzystanekTabliczka;

@Stateless
@Name("neighborhoodMatrixBean")
public class NeighborhoodMatrixBean implements NeighborhoodMatrix {
	@Logger
	private Log log;
	@In
	private EntityManager mgrDatabase;
	private int start;
	private List<PrzystanekTabliczka> tabliczki;
	private int[][] E;  //macierz sasiedztwa
	private Integer[] V; //wektor wierzcholkow
	private int n;
	private int inf = 1000; //nieskonczonosc. Oznacza brak krawedzi miedzy wierzcholkami.
	
	@Override
	public void create(int start) {
		this.start = start;
		this.n = tabliczki.size();
		E = new int[n][n];
		V = new Integer[n];
		
		//inicjalizacja macierzy sasiedztwa
		for (int i = 0; i < n; ++i) {
			for (int j = 0; j < n; ++j) {
				E[i][j] = i == j ? 0 : inf;
			}
			V[i] = i;
		}
		
		buildMatrix();
		joinTabs();
	}
	
	private void buildMatrix() {
		List<Linia> linie = mgrDatabase.createNamedQuery("wszystkieLinie").getResultList();
		
		for (Linia l : linie) {
			//jesli linia nie posiada rozkladu to ja ignorujemy
			if (!scheduleExists(l)) continue;
			//w przeciwnym razie pobieramy wszystkie tabliczki z danej linii
			List<PrzystanekTabliczka> tabs = l.getPrzystanekTabliczka();
			
			//i laczymy je az do przedostatniej, gdyz ostatnia musi byc laczona w inny sposob
			int aktualna = -1, nastepna = -1;
			for (int i = 0; i < tabs.size()-1; ++i) {
				aktualna = getIndex(tabs.get(i).getId());
				nastepna = getIndex(tabs.get(i+1).getId());
				E[aktualna][nastepna] = getEdgeWeight(tabliczki.get(aktualna));
			}
			
			//ostatnia tabliczka jest laczona z tabliczkami nalezacymi do innych linii
			//znajdujacych sie na najblizszych przystankach
			PrzystanekTabliczka last = tabs.get(tabs.size()-1);
			Set<PrzystanekTabliczka> tabsForLast = getNearest(last.getPrzystanek(), 200); //200 metrow
			log.info("tabsForLast: " + tabsForLast.size());
			aktualna = getIndex(last.getId());
			log.info("Przystanek last: " + last.getPrzystanek().getNazwa());
			for (PrzystanekTabliczka t : tabsForLast) {
				if (last.getLinia().getNumer() == t.getLinia().getNumer()) {
					log.info("Ta sama linia");
					continue;
				}
				log.info("Laczenie z: " + t.getPrzystanek().getNazwa());
				nastepna = getIndex(t.getId());
				E[aktualna][nastepna] = 0;
				//E[aktualna][getIndex(tabs.get(0).getId())] = 0; //zapetlenie linii na potrzeby testu
			}
		}
		printE();
	}
	
	/**
	 * Sprawdza czy istenieje rozklad jazdy dla danej linii.
	 * @param l Linia do sprawdzenia.
	 * @return True - rozklad istnieje, False - brak rozkladu.
	 */
	private boolean scheduleExists(Linia l) {
		List<PrzystanekTabliczka> pt = l.getPrzystanekTabliczka();
		
		if (pt.size() == 0 || pt.get(0).getOdjazdy().size() == 0)
			return false;
		return true;
	}
	
	private void joinTabs() {
		List<Linia> linie;
		List<Przystanek> przystankiTmp = new ArrayList<Przystanek>();
		
		//pobranie tylko istniejacych linii aby nie laczyc przystankow, ktore nie sa w zadnej linii
		linie = mgrDatabase.createNamedQuery("wszystkieLinie").getResultList();
		
		//pobranie przystankow dla istniejacych linii
		for (Linia l : linie) {
			przystankiTmp.addAll(mgrDatabase.createNamedQuery("przystankiPoLinii")
					.setParameter("linia", l)
					.getResultList());
		}
		Set<Przystanek> przystanki = new LinkedHashSet<Przystanek>(przystankiTmp);
		log.info("Liczba przystankow: " + przystanki.size());
		
		//laczenie tabliczek na tych samych przystankach. Przesiadki piesze bez przemieszczania sie.
		//TODO: Mozliwosc zrownoleglenia
		int aktualny = -1, nastepny = -1;
		for (Przystanek p : przystanki) {
			//pobranie wszystkich tabliczek z aktualnego przystanku
			List<PrzystanekTabliczka> pTab = p.getPrzystanekTabliczki();
			log.info("pTab: " + pTab.size());
			for (int i = 0; i < pTab.size(); ++i) {
				aktualny = getIndex(pTab.get(i).getId());
				for (int j = 0; j < pTab.size(); ++j) {
					if (i == j) continue; //nie laczymy tabliczki samej z soba
					nastepny = getIndex(pTab.get(j).getId());
					E[aktualny][nastepny] = 0; //przesiadka piesza
				}
			}
		}
		printE();
	}
	
	@Override
	public void printE() {
		String info = "";
		for (int i = 0; i < n; ++i) {
			info += "[";
			for (int j = 0; j < n; ++j) {
				info += E[i][j]+",";
			}
			info += "]\n";
		}
		
		log.info(info);
	}
	
	@Override
	public int[][] getE() {
		return E;
	}
	
	@Override
	public Integer[] getV() {
		return V;
	}

	/**
	 * Zwraca indeks z tablicy 'tabliczki' odpowiadajacy podanemu ID tabliczki.
	 * @param id ID tabliczki.
	 * @return Index tabliczki, lub -1 jesli nie znaleziono.
	 */
	@Override
	public int getIndex(Long id) {
		for (int i = 0; i < tabliczki.size(); ++i) {
			if (tabliczki.get(i).getId() == id) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public void setTabliczki(List<PrzystanekTabliczka> tabliczki) {
		this.tabliczki = tabliczki;
	}
	
	/**
	 * Zwraca wage krawedzi (czas) od tabliczki pt do jej nastepnika.
	 * @param pt Tabliczka, dla ktorej ma zostac zwrocana waga do jej nastepnika/
	 * @return Waga krawedzi.
	 */
	private int getEdgeWeight(PrzystanekTabliczka pt) {
		return pt.getCzasDoNastepnego();
	}
	
	/**
	 * Dla podanego przystanku zwraca najblizsze tabliczki z przystankow znajdujacych sie
	 * w zadanym dystansie.
	 * @param p Przystanek, dla ktorego maja zostac zwrocone najblizsze tabliczki.
	 * @param distance Dystans w metrach, do ktorego maja byc poszukiwane przystanki.
	 * @return Lista tabliczek ze znalezionych najblizszych przystankow.
	 */
	private Set<PrzystanekTabliczka> getNearest(Przystanek p, int distance) {
		//pobranie id najblizszych przystankow
		List<BigInteger> nearest = mgrDatabase.createNativeQuery("select foo.id from (select p.id, st_distance_sphere(p.location, ST_GeomFromText('POINT(" + p.getLocation().x + " " + p.getLocation().y + ")', 4326)) as odleglosc from przystanki p order by odleglosc) as foo where foo.odleglosc < " + distance).getResultList();
		List<Przystanek> przyst = new ArrayList<Przystanek>();
		
		//wyciagniecie obiektow przystankow
		for (BigInteger id : nearest) {
			przyst.add(mgrDatabase.getReference(Przystanek.class, id.longValue()));
		}
		
		//dodanie do jednej kolekcji tabliczek ze wszystkich znalezionych przystankow
		List<PrzystanekTabliczka> tabliczki = new ArrayList<PrzystanekTabliczka>();
		for (Przystanek pr : przyst) {
			tabliczki.addAll(pr.getPrzystanekTabliczki());
		}
		
		//bez powtorzen
		Set<PrzystanekTabliczka> result = new LinkedHashSet<PrzystanekTabliczka>(tabliczki);
		
		return result;
	}
}
