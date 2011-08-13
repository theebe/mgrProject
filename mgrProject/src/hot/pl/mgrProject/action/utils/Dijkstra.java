package pl.mgrProject.action.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.jboss.seam.log.Log;

public class Dijkstra {
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
	private int inf = 1000;
	
	/**
	 * Przechowuje sciezki tras.
	 */
	private int[] p;
	
	/**
	 * Przechowuje odleglosci do wierzcholka startowego.
	 */
	private int[] d;
	
	
	/**
	 * Konstruktor.
	 * @param n Liczba wierzcholkow/przystankow
	 * @param E Wagi krawedzi
	 * @param V Wierzcholki
	 * @param s ID przystanku poczatkowego
	 */
	public Dijkstra(int n, int[][] E, Integer[] V, int startID) throws Exception {	
		this.n = n;
		this.E = E.clone();
		this.V = V.clone();
		this.s = startID;
		p = new int[n]; 
		d = new int[n];
	}
	
	/**
	 * Uruchamia algorytm.
	 */
	public void run() {
		List<Integer> S = new ArrayList<Integer>(); //wierzcholki dla ktorych zostaly policzone najkrotsze trasy
		int u = -1;
		List<Integer> Q = new ArrayList<Integer>(); //wierzcholki dla ktorych jeszcze nie zostaly obliczone najkrotsze trasy
		Collections.addAll(Q, V);
		
		for (int i = 0; i < n; ++i) {
			p[i] = 0;
			d[i] = inf;
		}
		
		d[s] = 0; //d[s-1] = 0;
		int minD = 1000;
		int minDindex = -1;
		
		while(S.size() < n) {
			//szukanie wierzcholka o najmniejszym d.
			for (int i = 0; i < n; ++i) {
				if (d[i] < minD && Q.contains(i)) { //Q.contains(i+1)
					minD = d[i];
					minDindex = i;
				} else {
					continue;
				}
			}
			//czasami wychodzi tu poza tablice. Prawdopodobnie wtedy, gdy graf jest niepoprawny.
			try {
				u = Q.get(minDindex);
			} catch(ArrayIndexOutOfBoundsException e) {
				throw e;
			}
			Q.set(minDindex, 1000);
			S.add(u);
			
			//sprawdzanie sasiadow analizowanego wierzcholka.
			int neighbor = -1;
			
			for (int i = 0; i < n; ++i) {
				neighbor = E[u][i]; //E[u-1][i]
				if (neighbor != inf) {
					if (d[u] + neighbor < d[i]) { //d[u-1]
						p[i] = u;
						d[i] = d[u] + neighbor; //d[u-1]
					}
				}
			}
			
			minD = 1000;
			minDindex = -1;
		}
	}
	
	/**
	 * Zwraca znaleziona najkrotsza sciezke pod postacia Stringa.
	 * @param stopID ID wierzcholka/przystanku docelowego
	 * @return String z reprezentacja najkrotszej sciezki
	 * @throws Exception
	 */
	public String getPath(int stopID, Log log) throws Exception {
		int i = p[stopID];
		log.info("p[]: " + Arrays.toString(p));
		log.info("d[]: " + Arrays.toString(d));
		String result = "" + stopID;
		while (i != 0) {
			result += " <- " + i;
			i = p[i];
			log.info("getPath: " + i);
		}
		
		//result += " <- " + s;
		
		return result;
	}
	
	public ArrayList<Integer> getPathTab(int stopID, Log log) throws Exception {
		ArrayList<Integer> tab = new ArrayList<Integer>();
		tab.add(stopID);
		int i = p[stopID];
		while (i != 0) {
			tab.add(i);
			i = p[i];
			log.info("getPathTab: " + i);
		}
		
		//tab.add(s);
		
		return tab;
	}
}
