package pl.mgrProject.action.algorithm;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import org.postgis.Point;

import pl.mgrProject.model.Przystanek;
import pl.mgrProject.model.PrzystanekTabliczka;

@Local
public interface Algorithm {

	public Boolean run();
	
	public Boolean setStartPoint(Point p);
	
	public Boolean setStopPoint(Point p);
	
	public List<PrzystanekTabliczka> getPath();
	
	public Przystanek getClosestTo(Point point);
	
	public void setStartTime(Date startTime);
	
	public List<Date> getHours();
}
