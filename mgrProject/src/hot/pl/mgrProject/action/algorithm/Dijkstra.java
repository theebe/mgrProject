package pl.mgrProject.action.algorithm;

import java.util.ArrayList;
import javax.ejb.Local;

@Local
public interface Dijkstra {
	public void init(int n, int[][] E, Integer[] V, int startID) throws Exception;
	public void run();
	public String getPath(int stopID) throws Exception;
	public ArrayList<Integer> getPathTab(int stopID) throws Exception;
	
}
