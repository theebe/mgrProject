package pl.mgrProject.action.algorithm;

import java.io.Serializable;
import java.util.Calendar;

public class AdjacencyMatrixContainer implements Serializable {

	private static final long serialVersionUID = -315394405178669976L;
	/**
	 * Macierz sasiedztwa.
	 */
	private int[][] E;
	/**
	 * Wektor z identyfikatorami wierzcholkow.
	 */
	private Integer[] V;
	/**
	 * Wartosc nieskonczonosci. Oznacza brak krawedzi miedzy wierzcholkami.
	 */
	private int inf;
	
	public AdjacencyMatrixContainer(int size, int inf) {
		E = new int[size][size];
		V = new Integer[size];
		this.inf = inf;
		
		//inicjalizacja macierzy sasiedztwa
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				E[i][j] = i == j ? 0 : inf;
			}
			V[i] = i;
		}
	}

	public void set(int i, int j, int value) {
		E[i][j] = value;
	}

	public int getInf() {
		return inf;
	}

	public void setInf(int inf) {
		this.inf = inf;
	}
	
	@Override
	public String toString() {
		StringBuilder info = new StringBuilder("");
		for (int i = 0; i < E.length; ++i) {
			info.append("[");
			for (int j = 0; j < E[i].length; ++j) {
				info.append(E[i][j]+",");
			}
			info.append("]\n");
		}
		
		return info.toString();
	}
	
	public Integer[] getV() {
		return V;
	}

	public int[][] getE() {
		int[][] copy = new int[E.length][E.length]; 
		
		for (int i = 0; i < E.length; ++i) {
			for (int j = 0; j < E.length; ++j) {
				copy[i][j] = E[i][j];
			}
		}
		
		return copy;
	}
}
