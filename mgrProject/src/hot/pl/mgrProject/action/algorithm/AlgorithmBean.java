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
import pl.mgrProject.model.TypDnia;

/**
 * Bezstanowy komponent Seam o nazwie 'algorithmBean' <br />
 * Implenetuje metody interfejsu Algorithm
 * @author bat
 *
 */
@Stateless
@Name("algorithmBean")
public class AlgorithmBean implements Algorithm {
	@Logger
	private Log log;
	@In
	private EntityManager mgrDatabase;
	@In(create=true)
	private AdjacencyMatrix adjacencyMatrixBean;
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
	 * Okresla czy dzien startu jest swietem czy tez nie
	 */
	private boolean holiday;
	/**
	 * Konfiguracja
	 */
	private Konfiguracja konf;
	/**
	 * Pomocnicze pole dla tabliczki startowej
	 */
	private int pstartIndex;
	/**
	 * Godziny odjazdow
	 */
	private List<Calendar> hours;
	
	
	/**
	 * Metoda inicjujaca wszystkie wymagane zmienne, tworzaca macierz sasiedztwa i uruchamiajaca algorytm wyszukiwania trasy.
	 */
	@Override
	public Boolean run() {
		konf = (Konfiguracja)mgrDatabase.createNamedQuery("konfiguracjaPoNazwie").setParameter("nazwa", "default").getSingleResult();
		//pobranie najblizszych przystankow dla start i stop
		List<Przystanek> pstart = getClosestList(startPoint, konf.getOdlegloscDoStartStop());
		List<Przystanek> pstop  = getClosestList(stopPoint, konf.getOdlegloscDoStartStop());
		
		if (pstart == null || pstart.isEmpty() || pstop == null || pstop.isEmpty()) {
			log.info("Brak pobliskich przystakow. Nie mozna znalezc trasy.");
			return false;
		}
		
		//proby wyznaczania tras z roznych tabliczek startowych az do skutku
		while (true) {
			List<PrzystanekTabliczka> tabForStart = null;
			for (int i = 0; i < pstart.size(); ++i) {
				List<Przystanek> tmp = new ArrayList<Przystanek>();
				tmp.add(pstart.get(i));
				tabForStart = getWszystkieTabliczkiZListyPrzystankow(tmp);
				if (tabForStart.isEmpty()) {
					pstart.remove(i);
				} else {
					pstartIndex = i;
					break;
				}
			}
			
			log.info("Liczba tabliczek dla start: " + tabForStart.size());
			
			//pobieramy tabliczke dla start, dla ktorej jest dostepny najwczesniejszy odjazd
			PrzystanekTabliczka tabStart = checkTime(tabForStart);
			
			if (tabStart == null) {
				log.info("Brak rozkladu jazdy dla przystanku startowego.");
				return false;
			}
			
			Long startID = tabStart.getId();
			
			tabliczki = mgrDatabase.createNamedQuery("wszystkieTabliczki").getResultList();
			int n = tabliczki.size();
			
			adjacencyMatrixBean.setTabliczki(tabliczki);
			adjacencyMatrixBean.init();
			
			//obliczenie godzin przyjazdow na przystanki, aktualizacja wag w macierzy sasiedztwa
			//w celu okreslenia przesiadek (sa zalezne od godziny rozpoczecia podrozy)
			log.info("Okreslanie godzin przybycia na przystanki...");
			adjacencyMatrixBean.processHours((Calendar)odjazd.clone(), startID);
			
			//odwzorowanie przystanku na indeks tablicy poniewaz algorytm operuje na indeksach tablicy
			start = adjacencyMatrixBean.getIndex(startID);
			int[][] E = adjacencyMatrixBean.getE();
			Integer[] V = adjacencyMatrixBean.getV();
			
			//==================
			// Uruchomienie algorytmu Dijkstry
			//==================
			if (start != -1) { //jesli przystanek startowy istnieje
				try {
					log.info("[Dijkstra] n: " + n);
					log.info("[Dijkstra] V:\n" + Arrays.toString(V));
					log.info("[Dijkstra] start: " + start);
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
					
					//poszukiwanie najblizszego przystanku dla 'stop', dla ktorego mozliwe jest wyznaczenie poprawnej trasy
					for (Przystanek p : pstop) {
						boolean flag = false;
						for (PrzystanekTabliczka pt : p.getPrzystanekTabliczki()) {
							stop = adjacencyMatrixBean.getIndex(pt.getId());
							path = dijkstraBean.getPathTab(stop);
							
							//sprawdzenie czy zostala wyznaczona poprawna trasa dla 'stop'
							if (path.size() < 2 || adjacencyMatrixBean.getHours().get(stop) == null)
								continue;
							
							flag = true;
							break;
						}
						if (flag == true)
							break;
					}
					
					//jesli dla wszystkich tabliczek koncowych nie udalo sie wyznaczyc trasy
					//usun problematyczny przystanek startowyc,
					//wroc na poczatek i probuj dla innej tabliczki startowej z innego przystanku
					if (adjacencyMatrixBean.getHours().get(stop) == null || dijkstraBean.getPathTab(stop).size() < 2) {
						pstart.remove(pstartIndex);
						continue;
					}
					
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
		
		List<Calendar> hourstmp = adjacencyMatrixBean.getHours();
		
		for (Integer i : path) {
			if (i >= hourstmp.size() || i == null || hourstmp.get(i) == null) break;
			hours += hourstmp.get(i).getTime() + " <- ";
		}
		//getHours potrzebuje zeby najpierw wywolac getPath()
		//pewnie mozna bylo to zrobic lepiej... Moze kiedys
		getPath();
		getHours();
		log.info(hours);
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
	
	@Override
	public Przystanek getClosestTo(Point point) {
		List<Przystanek> nearest = getClosestList(point, 0);
		
		for (Przystanek p : nearest) {
			//sprawdzenie czy istnieje rozklad jazdy dla danego przystanku
			if (p.getPrzystanekTabliczki().size() > 0 && p.getPrzystanekTabliczki().get(0).getOdjazdy().size() > 0) {
				return p;
			}
		}

		return null;
	}
	
	//Zwaraca liste latbliczek (pusta lista jesli nie znajdzie)
	@Override
	public List<Przystanek> getClosestList(Point point, int distance) {
		int d;
		if (distance == 0) {
			d = konf.getOdlegloscDoStartStop();
		} else {
			d = distance;
		}
		List<BigInteger> nearest = mgrDatabase.createNativeQuery("select foo.id from (select p.id, st_distance_sphere(p.location, ST_GeomFromText('POINT(" + point.x + " " + point.y + ")', 4326)) as odleglosc from przystanki p order by odleglosc) as foo where foo.odleglosc < " + d).getResultList();
		List<Przystanek> result = new ArrayList<Przystanek>(nearest.size());
		
		for (BigInteger i : nearest) {
			Przystanek p = mgrDatabase.getReference(Przystanek.class, i.longValue());
			//sprawdzenie czy istnieje rozklad jazdy dla danego przystanku
			if (p.getPrzystanekTabliczki().size() > 0 && p.getPrzystanekTabliczki().get(0).getOdjazdy().size() > 0) {
				result.add(p);
			}
		}

		return result;
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
		
		hours = adjacencyMatrixBean.getHours();
		
		List<PrzystanekTabliczka> trasa = new ArrayList<PrzystanekTabliczka>();
		
		for (Integer i : path) {
			trasa.add(tabliczki.get(i));
		}
		
		//naprawienie wsiadania do pojazdu i natychmiastowego wysiadania
		if (trasa.size() >= 1 && !trasa.get(1).getLinia().getId().equals(trasa.get(0).getLinia().getId())) {
			trasa.remove(0);
		}
		if (trasa.size() >= 1 && !trasa.get(trasa.size()-2).getLinia().getId().equals(trasa.get(trasa.size()-1).getLinia().getId())) {
			trasa.remove(trasa.size()-1);
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
		odjazd = Calendar.getInstance();
		odjazd.set(Calendar.YEAR, 2099);
		
		for (int i = 0; i < tabs.size(); ++i) {
			List<Odjazd> odj = tabs.get(i).getOdjazdy();
			log.info("odj.size(): " + odj.size());
			if (odj.size() == 0) continue;
			Calendar min = Calendar.getInstance();
			min.set(Calendar.YEAR, 2099);

			for (int j = 0; j < odj.size(); ++j) {
				if (holiday) {
					if (odj.get(j).getTypDnia() != TypDnia.SWIETA) continue;
				} else {
					if (odj.get(j).getTypDnia() != TypDnia.DZIEN_POWSZEDNI) continue;
				}
				Calendar tmp = adjacencyMatrixBean.dateToCalendar(odj.get(j).getCzas());
				if (tmp.after(startTime) && tmp.before(min) && tmp.before(odjazd)) {
					index = i;
					min = tmp;
				}
			}
			odjazd = (Calendar)min.clone();
		}
		
		return index == -1 ? null : tabs.get(index);
	}
	
	/**
	 * Ustawia date rozpoczecia trasy.
	 */
	@Override
	public void setStartTime(Date startTime) {
		this.startTime = adjacencyMatrixBean.dateToCalendar(startTime);
		Calendar tmp = Calendar.getInstance();
		tmp.setTime(startTime);
		holiday = isHoliday(tmp);
	}
	
	/**
	 * Zwraca liste godzin dla kolejnych punktow trasy.
	 * Przed wywolaniem tej funkcji nalezy najpierw wywolac getPath().
	 * @return Lista godzin przyporzadkowanych do kolejnych punktow znalezionej trasy.
	 */
	@Override
	public List<Date> getHours() {
		if (path == null) {
			return new ArrayList<Date>();
		}
		
		List<Date> result = new ArrayList<Date>();
		
		for (Integer i : path) {
			if (i >= hours.size() || i == null || hours.get(i) == null) break;
			result.add(hours.get(i).getTime());
		}
		
		Collections.reverse(result);
		return result;
	}
	
	
	/**
	 * Funkcja tworzy jedna liste tabliczkow przystankowych z listy przystankow
	 * @param pstart
	 * @return
	 */
	private List<PrzystanekTabliczka> getWszystkieTabliczkiZListyPrzystankow(
			List<Przystanek> pstart) {
		List<PrzystanekTabliczka> ret = new ArrayList<PrzystanekTabliczka>();

		if (pstart == null || pstart.size() == 0)
			return ret;

		for (int i = 0; i < pstart.size(); i++) {
			ret.addAll(pstart.get(i).getPrzystanekTabliczki());
		}
		log.info("Wybrano " + ret.size() + " tabliczek startowych");
		return ret;
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