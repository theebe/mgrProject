package pl.mgrProject.action.algorithm;

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
					E[aktualny][nastepny] = getEdgeWeight(tabliczki.get(aktualny));
					log.info("[A]Laczenie: " + tabliczki.get(aktualny).getId() + ":" + tabliczki.get(nastepny).getId());
					tabTrace.add(aktualny);
					aktualny = nastepny;
					backTrace = true;
				} else {
					log.info("Nastepny: " + tabliczki.get(nastepny).getId());
					E[aktualny][nastepny] = getEdgeWeight(tabliczki.get(aktualny));
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
						E[aktualny][nastepny] = getEdgeWeight(tabliczki.get(aktualny));
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
		List<Linia> linie;
		List<Przystanek> przystankiTmp = new ArrayList<Przystanek>();
		
		//pobranie tylko istniejacych linii aby nie laczyc przystankow, ktore nie sa w zadnej linii
		linie = mgrDatabase.createNamedQuery("wszystkieLinie").getResultList();
		log.info("Liczba linii: " + linie.size());
		
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
	
	private int getEdgeWeight(PrzystanekTabliczka pt) {
		int weight = -1;
		
		weight = pt.getCzasDoNastepnego();
		
		return weight;
	}

}
