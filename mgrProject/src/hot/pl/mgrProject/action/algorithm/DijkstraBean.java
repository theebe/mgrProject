package pl.mgrProject.action.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import pl.mgrProject.model.Konfiguracja;

@Stateless
@Name("dijkstraBean")
public class DijkstraBean implements Dijkstra {
	@Logger
	private Log log;
	@In
	private EntityManager mgrDatabase;
	/**
	 * Wierzcholek startowy.
	 */
	private int s;
	
	/**
	 * Liczba wierzcholkow grafu.
	 */
	private int n;
	
	/**
	 * Przechowuje wagi krawedzi.
	 */
	private int[][] E;
	
	/**
	 * Przechowuje ID wierzcholkow
	 */
	private Integer[] V;
	
	/**
	 * Wartosc nieskonczonosci.
	 */
	private int inf;
	
	/**
	 * Przechowuje sciezki tras.
	 */
	private static int[] p;
	
	/**
	 * Przechowuje odleglosci do wierzcholka startowego.
	 */
	private static int[] d;
	
	/**
	 * Liczba dostepnych procesorow. Algorytm bedzie staral sie dzialac na takiej liczbie watkow.
	 */
	private int nThreads;
	
	/**
	 * Zwraca liczbe dostepnych procesorow (rdzeni).
	 * @return Liczba dostepnych procesorow.
	 */
	private int getCores() {
		return Runtime.getRuntime().availableProcessors();
	}
	
	
	/**
	 * Konstruktor.
	 * @param n Liczba wierzcholkow/przystankow
	 * @param E Wagi krawedzi
	 * @param V Wierzcholki
	 * @param s ID przystanku poczatkowego
	 */
	public void init(int n, int[][] E, Integer[] V, int startID) throws Exception {	
		this.n = n;
		this.E = E.clone();
		this.V = V.clone();
		this.s = startID;
		//pobranie liczby watkow na jakich ma dzialac algorytm
		Konfiguracja konf = (Konfiguracja)mgrDatabase.createNamedQuery("konfiguracjaPoNazwie").setParameter("nazwa", "default").getSingleResult();
		int threads = konf.getLiczbaWatkow();
		//0 == automatyczne obliczenie liczby watkow
		int cores = threads == 0 ? getCores() : threads;
		nThreads = cores > n ? n : cores;
		p = new int[n]; 
		d = new int[n];
		inf = konf.getNieskonczonosc();
	}
	
	/**
	 * Uruchamia algorytm.
	 */
	public void run() {
		log.info("Liczba watkow: " + nThreads);
		List<Integer> S = new ArrayList<Integer>(); //wierzcholki dla ktorych zostaly policzone najkrotsze trasy
		int u = -1;
		List<Integer> Q = new ArrayList<Integer>(); //wierzcholki dla ktorych jeszcze nie zostaly obliczone najkrotsze trasy
		Collections.addAll(Q, V);
		
		for (int i = 0; i < n; ++i) {
			p[i] = 0;
			d[i] = inf;
		}
		
		d[s] = 0; //d[s-1] = 0;
		ExecutorService exec = Executors.newFixedThreadPool(nThreads);
		
		while(S.size() < n) {
			ArrayList<Future<Integer>> result = new ArrayList<Future<Integer>>();
			log.info("d[]: " + Arrays.toString(d));
			
			//szukanie wierzcholka o najmniejszym d.
			for (int i = 0; i < nThreads-1; ++i) {
				result.add(exec.submit(new FindMinD(Q, i*(n/nThreads), (i+1)*(n/nThreads))));
			}
			result.add(exec.submit(new FindMinD(Q, (nThreads-1)*(n/nThreads), n))); //ostatni watek bierze wektor do konca
			
			int minD = inf + 1;
			int minDIndex = -1;
			for (Future<Integer> f : result) {
				Integer i;
				try {
					i = f.get();
					if (i == -1) continue;
					if (d[i] < minD) {
						minD = d[i];
						minDIndex = i;
					}
				} catch (InterruptedException e) {
					log.info(e);
				} catch (ExecutionException e) {
					log.info(e);
				} finally {
					exec.shutdown();
				}
			}

			//czasami wychodzi tu poza tablice. Prawdopodobnie wtedy, gdy graf jest niepoprawny.
			try {
				u = Q.get(minDIndex);
			} catch(ArrayIndexOutOfBoundsException e) {
//				log.info("Q.size(): " + Q.size());
//				log.info("minDIndex: " + minDIndex);
				throw e;
			}
			Q.set(minDIndex, inf);
			S.add(u);
			
			//sprawdzanie sasiadow analizowanego wierzcholka.			
			exec = Executors.newFixedThreadPool(nThreads);
			
			for (int i = 0; i < nThreads-1; ++i) {
				exec.execute(new CheckNeighbors(u, E, i*(n/nThreads), (i+1)*(n/nThreads)));
			}
			exec.execute(new CheckNeighbors(u, E, (nThreads-1)*(n/nThreads), n));
			
			minD = inf + 1;
			minDIndex = -1;
		}
	}
	
	/**
	 * Zwraca znaleziona najkrotsza sciezke pod postacia Stringa.
	 * @param stopID ID wierzcholka/przystanku docelowego
	 * @return String z reprezentacja najkrotszej sciezki
	 * @throws Exception
	 */
	public String getPath(int stopID) throws Exception {
		int i = p[stopID];
		//log.info("p[]: " + Arrays.toString(p));
		//log.info("d[]: " + Arrays.toString(d));
		String result = "" + stopID;
		
		while (i != 0) {
			result += " <- " + i;
			i = p[i];
		}
		
		result += " <- " + s;
		
		return result;
	}
	
	/**
	 * Zwraca liste ID tabliczek, ktorych kolejnosc definiuje znaleziona trase.
	 * @param stopID ID przystanku koncowego
	 * @param log Logger
	 * @return Lista kolejnych tabliczek definujaca trase przejazdu.
	 * @throws Exception
	 */
	public ArrayList<Integer> getPathTab(int stopID) throws Exception {
		ArrayList<Integer> tab = new ArrayList<Integer>();
		tab.add(stopID);
		int i = p[stopID];
		
		while (i != 0) {
			tab.add(i);
			i = p[i];
		}
		
		tab.add(s);
		
		return tab;
	}
	
	private class FindMinD implements Callable<Integer> {
		private int start;
		private int stop;
		private List<Integer> Q;
		
		public FindMinD(List<Integer> Q, int start, int stop) {
			this.start = start;
			this.stop = stop;
			this.Q = Q;
		}

		@Override
		public Integer call() throws Exception {
			int minD = inf + 1; //zeby bylo troche wieksze od zdefiniowanej nieskonczonosci
			int minDIndex = -1;
			
			for (int i = start; i < stop; ++i) {
				if (d[i] < minD && Q.contains(i)) { //Q.contains(i+1)
					minD = d[i];
					minDIndex = i;
				}
			}
			//log.info("========== start: " + start + ", stop: " + stop + ", index: " + minDIndex + ", d[i]: " + d[minDIndex]);
			return minDIndex;
		}
	}
	
	private class CheckNeighbors implements Runnable {
		private int[][] E;
		private int start;
		private int stop;
		private int u;
		
		public CheckNeighbors(int u, int[][] E, int start, int stop) {
			this.E = E;
			this.start = start;
			this.stop = stop;
			this.u = u;
		}

		@Override
		public void run() {
			int neighbor = -1;
			
			for (int i = start; i < stop; ++i) {
				neighbor = E[u][i]; //E[u-1][i]
				if (neighbor != inf) {
					if (d[u] + neighbor < d[i]) { //d[u-1]
						p[i] = u;
						d[i] = d[u] + neighbor; //d[u-1]
					}
				}
			}
		}	
	}
}
