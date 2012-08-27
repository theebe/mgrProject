package pl.mgrProject.action.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
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
	 * Przechowuje wagi krawedzi.
	 */
	private int[][] E = new int[0][0];
	 /**
	 * Przechowuje ID wierzcholkow.
	 */
	private Integer[] V;
	/**
	 * Lista wierzcholkow do przetworzenia przez algorytm.
	 */
	private List<Integer> Q;
	/**
	 * Wektor poprzednikow.
	 */
	private int[] p;
	/**
	 * Wektor odleglosci.
	 */
	private int[] d;
	/**
	 * Wartosc nieskonczonosci.
	 */
	private int inf;
	/**
	 * ID wierzcholka zrodlowego.
	 */
	private int start;
	/**
	 * Liczba watkow na jakich ma dzialac algorytm.
	 */
	private int nThreads;
	/**
	 * Liczba wierzcholkow w grafie.
	 */
	private int n;
	/**
	 * ID wierzcholka o najmniejszym koszcie.
	 */
	private Integer u;
	
	 /**
	 * Konstruktor.
	 * @param n Liczba wierzcholkow/przystankow
	 * @param E Wagi krawedzi
	 * @param V Wierzcholki
	 * @param s ID przystanku poczatkowego
	 */
	@Override
	public void init(int nv, int[][] E, Integer[] V, int startID) throws Exception {
		Konfiguracja konf = (Konfiguracja) mgrDatabase.createNamedQuery("konfiguracjaPoNazwie")
				.setParameter("nazwa", "default").getSingleResult();
		start = startID;
		n = E.length;
		Q = new ArrayList<Integer>(n);
		p = new int[n];
		d = new int[n];
		u = -1;
		inf = konf.getNieskonczonosc();
		this.E = E;
		this.V = V;
		
		int threads = konf.getLiczbaWatkow();
		// 0 == automatyczne obliczenie liczby watkow
		int cores = threads == 0 ? getCores() : threads;
		nThreads = cores > n ? n : cores;
		
		for (int i = 0; i < n; ++i) {
			p[i] = 0;
			d[i] = inf;
			Q.add(i);
		}
		
		d[start] = 0;
		p[start] = 0;
		
		
	}
	
	 /**
	 * Uruchamia algorytm.
	 */
	@Override
	public void run() {
		ExecutorService exec = Executors.newFixedThreadPool(nThreads);
		ArrayList<Future<Integer>> result = new ArrayList<Future<Integer>>();
		ArrayList<Future<Boolean>> result2 = new ArrayList<Future<Boolean>>();
		
		while (!Q.isEmpty()) {
			// szukanie wierzcholka o najmniejszym d.
			for (int i = 0; i < nThreads - 1; ++i) {
				result.add(exec.submit(new FindMinD(i * (V.length / nThreads), (i + 1) * (V.length / nThreads))));
			}
			//ostatni watek bierze reszte wektora
		    result.add(exec.submit(new FindMinD((nThreads - 1) * (V.length / nThreads), V.length)));
		    
		    int minD = inf + 1;
		    
			for (Future<Integer> f : result) {
				try {
					Integer i = f.get();
					if (i == -1)
						continue;
					if (d[i] < minD) {
						minD = d[i];
						u = i;
					}
				} catch (InterruptedException e) {
					System.out.println("[InterruptedException] " + e);
					e.printStackTrace();
				} catch (ExecutionException e) {
					System.out.println("[ExecutionException] " + e);
					e.printStackTrace();
				}
			}
			
			result.clear();
		    Q.remove(u);

			// sprawdzanie sasiadow analizowanego wierzcholka.
			for (int i = 0; i < nThreads - 1; ++i) {
				result2.add(exec.submit(new CheckNeighbors(u, i * (V.length / nThreads), (i + 1) * (V.length / nThreads))));
			}
			result2.add(exec.submit(new CheckNeighbors(u, (nThreads - 1) * (V.length / nThreads), V.length)));
			
			// oczekiwanie na zakonczenie dzialania watkow
			try {
				for (Future<Boolean> f : result2) {
					f.get();
				}
			} catch (InterruptedException e) {
				System.out.println(e);
			} catch (ExecutionException e) {
				System.out.println(e);
			}

			result2.clear();
		}
		
		exec.shutdown();
	}
	
	 /**
	 * Zwraca znaleziona najkrotsza sciezke pod postacia Stringa.
	 * @param stop ID wierzcholka/przystanku docelowego
	 * @return String z reprezentacja najkrotszej sciezki
	 * @throws Exception
	 */
	@Override
	public String getPath(int stop) throws Exception {
		String path = "" + stop;

		int index = stop;
		while (index != start && index != 0) {
			path += " <- " + V[index];
			index = p[index];
		}
		path += " <- " + V[start];

		return path;
	}
	
	 /**
	 * Zwraca liste ID tabliczek, ktorych kolejnosc definiuje znaleziona
	 trase.
	 * @param stop ID przystanku koncowego
	 * @param log Logger
	 * @return Lista kolejnych tabliczek definujaca trase przejazdu.
	 * @throws Exception
	 */
	@Override
	public ArrayList<Integer> getPathTab(int stop) throws Exception {
		log.info("[getPathTab] stop: " + stop);
		log.info("[getPathTab] p: " + Arrays.toString(p));
		log.info("[getPathTab] d: " + Arrays.toString(d));
		ArrayList<Integer> path = new ArrayList<Integer>();
		
		int index = stop;
		while (index != start && index != 0) {
			path.add(V[index]);
			index = p[index];
		}
		path.add(V[start]);

		return path;
	}
	
	/**
	 * Zwraca liczbe dostepnych procesorow (rdzeni).
	 * 
	 * @return Liczba dostepnych procesorow.
	 */
	private int getCores() {
		return Runtime.getRuntime().availableProcessors();
	}
	
	private class FindMinD implements Callable<Integer> {
		private int begin;
		private int end;

		public FindMinD(int begin, int end) {
			this.begin = begin;
			this.end = end;
		}

		@Override
		public Integer call() {
			int min = inf + 1;
			int index = -1;
			try {
				for (int i = begin; i < end; ++i) {
					if (d[i] < min && Q.contains(new Integer(i))) {
						min = d[i];
						index = i;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return index;
		}
	}
	
	private class CheckNeighbors implements Callable<Boolean> {
		private int begin;
		private int end;
		private int u;

		public CheckNeighbors(int u, int begin, int end) {
			this.begin = begin;
			this.end = end;
			this.u = u;
		}

		@Override
		public Boolean call() {
			try {
				//Dijkstra
				for (int v = begin; v < end; ++v) {
					if (E[u][v] != inf) {
						if (d[u] + E[u][v] < d[v]) {
							d[v] = d[u] + E[u][v];
							p[v] = u;
						}
					}
				}
				//***
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
	}
}