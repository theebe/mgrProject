package pl.mgrProject.action.algorithm;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import pl.mgrProject.model.Linia;
import pl.mgrProject.model.PrzystanekTabliczka;

@Local
public interface AdjacencyMatrix {
	public void create();
	public void setTabliczki(List<PrzystanekTabliczka> tabliczki);
	public boolean scheduleExists(Linia l);
	public int getIndex(Long id);
	public void compileMap();
	public int[][] getE();
	public Integer[] getV();
	public void init();
	public void processHours(Calendar startTime, Long startId);
	public Calendar dateToCalendar(Date d);
	public List<Calendar> getHours();
}
