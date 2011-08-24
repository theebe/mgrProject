package pl.mgrProject.action.algorithm;

import java.util.List;

import javax.ejb.Local;

import pl.mgrProject.model.PrzystanekTabliczka;

@Local
public interface NeighborhoodMatrix {
	public void create(int start);
	public int[][] getE();
	public Integer[] getV();
	public int getIndex(Long id);
	public void setTabliczki(List<PrzystanekTabliczka> tabliczki);
}
