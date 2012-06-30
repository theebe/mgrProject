package pl.mgrProject.action;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remove;

import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.remoting.WebRemote;
import org.postgis.Point;

import pl.mgrProject.model.PrzystanekTabliczka;

/**
 * Interfejs EJB lokalny. Oferuje dostep do algorytmu wyszukiwania poprzez Seam Remote
 * 
 * @author bat
 *
 */
@Local
public interface Home {
	 

	/**
	 * Pobiera punkt startowy
	 * @return
	 */
	@WebRemote
	public Point getStartPoint();

	@WebRemote 
	public Boolean setStartPoint(double x, double y) ;

	/**
	 * Pobiera punkt koncowy
	 * @return
	 */
	@WebRemote
	public Point getStopPoint() ;

	@WebRemote 
	public Boolean setStopPoint(double x, double y) ;

	/**
	 * Pobiera godzine startu podrozy
	 * @return
	 */
	@WebRemote
	public Date getStartTime() ;

	@WebRemote
	public Boolean setStartTime(Date startTime);
	
	/**
	 * Startuje algorytm oraz pobiera odpowiedz
	 * @return Odpowiedz algorytmu
	 */
	@WebRemote
	public Odpowiedz findRoute();
	

	@Destroy
	@Remove
	public void destory();
}
