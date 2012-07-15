package pl.mgrProject.action.algorithm;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
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
import pl.mgrProject.model.TypDnia;

@Stateless
@Name("neighborhoodMatrixBean")
public class NeighborhoodMatrixBean implements NeighborhoodMatrix {
	@Logger
	private Log log;
	@In
	private EntityManager mgrDatabase;
	private List<PrzystanekTabliczka> tabliczki;
	/**
	 * Macierz sasiedztwa.
	 */
	private int[][] E;
	/**
	 * Wektor z identyfikatorami wierzcholkow.
	 */
	private Integer[] V;
	/**
	 * Liczba wierzcholkow w grafie.
	 */
	private int n;
	/**
	 * Wartosc nieskonczonosci. Oznacza brak krawedzi miedzy wierzcholkami.
	 */
	private int inf;
	/**
	 * Wektor z czasami odjazdow z kazdej tabliczki.
	 */
	private List<Calendar> hours;
	/**
	 * Godzina rozpoczecia podrozy.
	 */
	private Calendar startTime;
	/**
	 * Konfiguracja
	 */
	private Konfiguracja konf;
	
	/**
	 * Inicjalizuje wszystkie zmienne i uruchamia proces budowania macierzy sasiedztwa.
	 * @param startTime Godzina rozpoczecia podrozy.
	 */
	@Override
	public void create(Calendar startTime) {
		//pobranie konfiguracji
		konf = (Konfiguracja)mgrDatabase.createNamedQuery("konfiguracjaPoNazwie").setParameter("nazwa", "default").getSingleResult();
		inf = konf.getNieskonczonosc();
		this.startTime = startTime;
		this.n = tabliczki.size();
		hours = new ArrayList<Calendar>();
		E = new int[n][n];
		V = new Integer[n];
		
		//inicjalizacja macierzy sasiedztwa
		for (int i = 0; i < n; ++i) {
			for (int j = 0; j < n; ++j) {
				E[i][j] = i == j ? 0 : inf;
			}
			V[i] = i;
			//inicjalizacja wektora z czasami odjazdow dla kazdej tabliczki
			Calendar tmp = Calendar.getInstance();
			tmp.set(Calendar.YEAR, 2000); //rok 2000 niech oznacza null
			hours.add(tmp);
		}
		
		buildMatrix();
		joinTabs();
		printE();
	}
	
	/**
	 * Algorytm budowania macierzy sasiedztwa.
	 */
	private void buildMatrix() {
		List<Linia> linie = mgrDatabase.createNamedQuery("wszystkieLinie").getResultList();

		for (Linia l : linie) {
			//jesli linia nie posiada rozkladu to ja ignorujemy
			if (!scheduleExists(l)) continue;
			//w przeciwnym razie pobieramy wszystkie tabliczki z danej linii
			List<PrzystanekTabliczka> tabs = l.getPrzystanekTabliczka();
			
			//i laczymy je az do przedostatniej, gdyz ostatnia musi byc laczona w inny sposob
			int aktualna = -1, nastepna = -1;
			Calendar tmp = null;
			boolean ok = false; //jak znajdzie tabliczke z dobra godzina to true
			for (int i = 0; i < tabs.size()-1; ++i) {
				aktualna = getIndex(tabs.get(i).getId());
				nastepna = getIndex(tabs.get(i+1).getId());
				
				if (!ok) {
					tmp = checkTime(tabs.get(i));
					if (tmp == null) continue;
					hours.set(aktualna, (Calendar)tmp.clone());
					tmp.add(Calendar.MINUTE, tabs.get(i).getCzasDoNastepnego());
					ok = true;
				}
				E[aktualna][nastepna] = getEdgeWeight(tabliczki.get(aktualna));
				joinToNearest(tabs.get(i), tmp);
				hours.set(nastepna, (Calendar)tmp.clone());
				tmp.add(Calendar.MINUTE, tabs.get(i+1).getCzasDoNastepnego());
			}			
			
			//ostatnia tabliczka jest laczona z tabliczkami nalezacymi do innych linii
			//znajdujacych sie na najblizszych przystankach
			if (tmp == null) continue; //jesli null to oznacza, ze nie istnieje trasa
			joinToNearest(tabs.get(tabs.size()-1), tmp);
		}
	}
	
	/**
	 * Algorytm laczenia tabliczek z sasiednimi przystankami.
	 * @param tab Tabliczka, ktora ma byc polaczona z sasiadami.
	 * @param time Godzina przybycia pasazera na tabliczke 'tab'. Wzgledem tej godziny beda wyszukiwane odjazdy pozniejsze na sasiednich tabliczkach.
	 */
	private void joinToNearest(PrzystanekTabliczka tab, Calendar time) {
		int aktualna = -1, nastepna = -1;
		Set<PrzystanekTabliczka> nearestTabs = getNearest(tab.getPrzystanek(), konf.getOdlegloscPrzystankow());
		
		aktualna = getIndex(tab.getId());

		for (PrzystanekTabliczka t : nearestTabs) {
			nastepna = getIndex(t.getId());
			E[aktualna][nastepna] = getEdgeWeight(tabliczki.get(aktualna), tabliczki.get(nastepna), time);
		}
	}
	
	/**
	 * Sprawdza czy istenieje rozklad jazdy dla danej linii.
	 * @param l Linia do sprawdzenia.
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
	 * Algorytm laczenia tabliczek w obrebie przystanku.
	 */
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
		int aktualny = -1, nastepny = -1;
		for (Przystanek p : przystanki) {
			//pobranie wszystkich tabliczek z aktualnego przystanku
			List<PrzystanekTabliczka> pTab = p.getPrzystanekTabliczki();
			for (int i = 0; i < pTab.size(); ++i) {
				aktualny = getIndex(pTab.get(i).getId());
				Calendar time = hours.get(aktualny);
				for (int j = 0; j < pTab.size(); ++j) {
					if (i == j) continue; //nie laczymy tabliczki samej z soba
					nastepny = getIndex(pTab.get(j).getId());
					E[aktualny][nastepny] = getEdgeWeight(tabliczki.get(aktualny), tabliczki.get(nastepny), time);
				}
			}
		}
	}
	
	/**
	 * Wyswietla macierz sasiedztwa.
	 */
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
	
	/**
	 * Zwraca macierz sasiedztwa.
	 * @return Macierz sasiedztwa.
	 */
	@Override
	public int[][] getE() {
		return E;
	}
	
	/**
	 * Zwraca wektor z identyfikatorami wierzcholkow grafu.
	 */
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
	 * Oblicza wagê krawedzi pomiedzy tabliczkami 'start' i 'stop.
	 * @param start Tablicka zrodlowa.
	 * @param stop Tablicka docelowa.
	 * @param time Godzina wyruszenia z tabliczki 'start'.
	 * @return Waga krawedzi.
	 */
	private int getEdgeWeight(PrzystanekTabliczka start, PrzystanekTabliczka stop, Calendar time) {
		int weight = -1;
		double v = konf.getPredkoscPasazera(); //predkosc pasazera w km/h
		Point st = start.getPrzystanek().getLocation();
		Point sp = stop.getPrzystanek().getLocation();
		
		//pobranie odleglosci miedzy przystankami
		Double s = (Double)mgrDatabase.createNativeQuery("select st_distance_sphere(ST_GeomFromText('POINT(" + st.x + " " + st.y + ")', 4326), ST_GeomFromText('POINT(" + sp.x + " " + sp.y + ")', 4326)) as odleglosc").getSingleResult();
		Double t = (s/(1000*v))*60; //czas podrozy pasazera miedzy przystankami w minutach
		time.add(Calendar.MINUTE, t.intValue());
		
		Calendar min = Calendar.getInstance();
		min.set(Calendar.YEAR, 2099);
		List<Odjazd> odj = stop.getOdjazdy();
		
		for (Odjazd o : odj) {
			Calendar tmp = dateToCalendar(o.getCzas());
			if (tmp.after(startTime) && tmp.after(time) && tmp.before(min)) {
				min = tmp;
			}
		}
		//jesli nie znaleziono odjazdu to weight = inf
		if (min.get(Calendar.YEAR) == 2099)
			return inf;
		
		//ró¿nica miêdzy min i tmp
		long a = Math.abs(min.getTimeInMillis() - time.getTimeInMillis());
		weight = (int)(a/60000) + t.intValue();
		
		return weight;
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
	
	/**
	 * Zwraca liste obliczonych godzin odjazdow ze wszystkich tabliczek.
	 * @return Lista obliczonych godzin odjazdow.
	 */
	@Override
	public List<Calendar> getHours() {
		return hours;
	}
	
	/**
	 * Ustawia godzine rozpoczecia podrozy.
	 * @param startTime Godzina rozpoczecia podrozy.
	 */
	@Override
	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}
	
	/**
	 * Metoda zwracajaca najwczesniejszy czas odjazdu z tabliczki pt.
	 * Czas ten musi byc pozniejszy niz czas rozpoczcia podrozy.
	 * @param pt Tabliczka do analizy.
	 * @return Najwczesniejszy czas odjazdu.
	 */
	private Calendar checkTime(PrzystanekTabliczka pt) {
		Calendar min = Calendar.getInstance();
		min.set(Calendar.YEAR, 2099);
		min.clear(Calendar.MILLISECOND);
		List<Odjazd> odj = pt.getOdjazdy();
		
		for (Odjazd o : odj) {
			Calendar tmp = dateToCalendar(o.getCzas());
			if ((tmp.after(startTime) || tmp.compareTo(startTime) == 0) && tmp.before(min)) {
				min = tmp;
			}
		}
		
		return min.get(Calendar.YEAR) == 2099 ? null : min;
	}
	
	/**
	 * Konwertuje obiekt Date na Calendar. Przepisuje jedynie godziny, minuty i sekundy,
	 * wiec oprócz godziny reszta daty pozostanie domyœlna czyli ustawiona na dzisiajszy dzieñ.
	 * @param d Obiekt, ktory ma zosrac poddany konwersji.
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
	
	/**
	 * Sprawdza czy podana data jest dniem swiatecznym czy tez nie.
	 * @param date Data do sprawdzenia
	 * @return 'true' jesli data jest dniem swiatecznym lub 'false' w przeciwnym wypadku.
	 */
	@Override
	public boolean isHoliday(Calendar date) {
		return date.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ? true : false;
	}

}
