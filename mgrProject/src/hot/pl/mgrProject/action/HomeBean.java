package pl.mgrProject.action;

import java.io.Serializable;
import java.util.Date;

import javax.ejb.Remove;
import javax.ejb.Stateful;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.postgis.Point;

/**
 * Klasa obs³uguj¹ca zdarzenia ze strony g³ownej
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
	
	private Point endPoint ;

	private Date startTime;
	
	private Date data;

	
	
	public Point getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}

	
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	

	@Destroy
	@Remove
	public void destory(){};
	
}
