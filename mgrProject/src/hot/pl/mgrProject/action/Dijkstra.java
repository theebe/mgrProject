package pl.mgrProject.action;

import java.util.ArrayList;

import javax.ejb.Local;

import org.jboss.seam.log.Log;

@Local
public interface Dijkstra {
	public void init(int n, int[][] E, Integer[] V, int startID) throws Exception;
	public void run();
	public String getPath(int stopID, Log log) throws Exception;
	public ArrayList<Integer> getPathTab(int stopID, Log log) throws Exception;
	
}
