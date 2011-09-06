package pl.mgrProject.action;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remove;

import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.remoting.WebRemote;
import org.postgis.Point;

import pl.mgrProject.model.PrzystanekTabliczka;

@Local
public interface Home {
	 

	@WebRemote
	public Point getStartPoint();

	@WebRemote 
	public Boolean setStartPoint(double x, double y) ;

	@WebRemote
	public Point getStopPoint() ;

	@WebRemote 
	public Boolean setStopPoint(double x, double y) ;

	@WebRemote
	public Date getStartTime() ;

	@WebRemote
	public Boolean setStartTime(Date startTime);
	
	@WebRemote
	public Odpowiedz findRoute();
	

	@Destroy
	@Remove
	public void destory();
}
