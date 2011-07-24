package pl.mgrProject.action;

import java.io.Serializable;
import java.util.Date;

import javax.ejb.Remove;
import javax.ejb.Stateful;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.postgis.Point;

import flexjson.JSONSerializer;

import pl.mgrProject.action.utils.PointConverter;

/**
 * Klasa obs³uguj¹ca zdarzenia ze strony g³ownej poprzez ajax
 * 
 * @author bat
 *
 */
@Name("homeBean")
@Stateful
@Scope(ScopeType.CONVERSATION)
public class HomeBean implements Serializable, Home {

	
	@Logger
	private Log log;
	
	private Point startPoint ;
	
	private Point stopPoint ;

	private Date startTime;
	 

	
	 
	public Point getStartPoint() {
		//startPoint.(arg0)
		return startPoint; 
	}
	
	public Boolean setStartPoint(double x, double y){
		if(x==0 || y==0) return false;
		this.startPoint = new Point(x, y);
		this.startPoint.setSrid(4326);
		log.info("homeBean: ustawiono startPoint na wspolrzedne: "+String.valueOf(x) + ", " + String.valueOf(y));
		return true;
	}

	public Point getStopPoint() {
		return stopPoint;
	}
	
	
	public Boolean setStopPoint(double x, double y){
		if(x==0 || y==0) return false;
		this.stopPoint = new Point(x, y);
		this.stopPoint.setSrid(4326);
		log.info("homeBean: ustawiono stopPoint na wspolrzedne: "+String.valueOf(x) + ", " + String.valueOf(y));
		return true;
	}

	
	public Date getStartTime() {
		return startTime;
	}

	public Boolean setStartTime(Date startTime) {
		if(startTime == null)
			return false;
		this.startTime = startTime;
		return true;
	}
	
	
	

	@Destroy
	@Remove
	public void destory(){}


}
