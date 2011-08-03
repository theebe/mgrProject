package pl.mgrProject.action;

import java.util.Date;

import javax.ejb.Local;
import javax.ejb.Remove;

import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.remoting.WebRemote;
import org.postgis.Point;

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
	public Boolean runAlgorithm();

	public void setListaLinii(Boolean listaLinii);

	public Boolean isListaLinii() ;
	
	public void setPage(String in);

	public String getPage() ;

	@Destroy
	@Remove
	public void destory();
}
