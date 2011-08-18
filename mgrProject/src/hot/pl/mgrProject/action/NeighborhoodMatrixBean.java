package pl.mgrProject.action;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

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
		//Algorytm budowania macierzy sasiedztwa. Na razie wagi krawedzi przypisane sa na sztywno.
		//Po testach wyglada na to, ze dziala poprawnie. Nie uwzglednia przystankow ktore nie sa dodane do zadnej linii.
		//TODO: Przemyslec dokladniej przypadki kiedy ostatni przystanek(tabliczka) linii nie ma nastepnego.
		//      Algorytm wymaga aby istaniala mozliwosc dotarcia do kazdego wierzcholka.
		//Propozycja: Ostatnia tabliczka linii bedzie wskazywac na dowolny, najblizszy przystanek nalezacy do innej linii.
		List<Integer> tabSprawdzone = new ArrayList<Integer>();
		List<Integer> tabTrace = new ArrayList<Integer>();
		
		int aktualny = start, nastepny = -1;
		boolean backTrace = false;
		
		while (tabSprawdzone.size() <= tabliczki.size()) {
			try {
				nastepny = getIndex(tabliczki.get(aktualny).getNastepnyPrzystanek().getId());
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
						nastepny = getIndex(tabliczki.get(tabTrace.get(i)).getId());
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
		printE();
	}
	
	private void joinTabs() {
		//========================
		// Laczenie tabliczek na tych samych przystankach.
		// Kazda z kazda oprocz tych, ktore nie maja zdefiniowanego nastepnego przystanku
		//========================
		int aktualny = -1, nastepny = -1;
		List<Long> tabSpr = new ArrayList<Long>();
		List<PrzystanekTabliczka> tabsForCurrent = new ArrayList<PrzystanekTabliczka>();
		Przystanek current = null;
		for (PrzystanekTabliczka tab : tabliczki) {
			if (tabSpr.contains(tab.getId())) {
				continue;
			}
			current = tab.getPrzystanek();
			tabsForCurrent = mgrDatabase.createNamedQuery("tabliczniPoPrzystanku").setParameter("przystanek", current).getResultList();
			for (int i = 0; i < tabsForCurrent.size(); ++i) {
				tabSpr.add(tabsForCurrent.get(i).getId());
				aktualny = getIndex(tabsForCurrent.get(i).getId());
				for(int j = 0; j < tabsForCurrent.size(); ++j) {
					if (i == j) {
						continue;
					}
					try {
						tabsForCurrent.get(j).getNastepnyPrzystanek();//jesli tabliczka nie ma nastepnego przystanku to wyrzuci wyjatek
						nastepny = getIndex(tabsForCurrent.get(j).getId());
						E[aktualny][nastepny] = 0;
					} catch(Exception e) { //tabliczka nie prowadzi do nastepnego przystanku
						continue;
					}
				}
			}
		}
	}
	
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
	
	public int[][] getE() {
		return E;
	}
	
	public Integer[] getV() {
		return V;
	}

	/**
	 * Zwraca indeks z tablicy 'tabliczki' odpowiadajacy podanemu ID tabliczki.
	 * @param id ID tabliczki.
	 * @return Index tabliczki, lub -1 jesli nie znaleziono.
	 */
	public int getIndex(Long id) {
		for (int i = 0; i < tabliczki.size(); ++i) {
			if (tabliczki.get(i).getId() == id) {
				return i;
			}
		}
		return -1;
	}
	
	public void setTabliczki(List<PrzystanekTabliczka> tabliczki) {
		this.tabliczki = tabliczki;
	}
}
