package pl.mgrProject.action;

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
	
	public Przystanek getClosestToStart();
	
	public Przystanek getClosestToStop();

}
