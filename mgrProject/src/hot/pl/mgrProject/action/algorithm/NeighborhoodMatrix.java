package pl.mgrProject.action.algorithm;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import pl.mgrProject.model.PrzystanekTabliczka;

@Local
public interface NeighborhoodMatrix {
	public void create(Calendar startTime);
	public int[][] getE();
	public Integer[] getV();
	public int getIndex(Long id);
	public void setTabliczki(List<PrzystanekTabliczka> tabliczki);
	public void printE();
	public List<Calendar> getHours();
	public void setStartTime(Calendar startTime);
	public Calendar dateToCalendar(Date d);
}
