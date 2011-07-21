package pl.mgrProject.action;

import java.util.Date;

import javax.ejb.Local;
import javax.ejb.Remove;

import org.jboss.seam.annotations.Destroy;
import org.postgis.Point;

@Local
public interface Home {
	public Point getStartPoint();

	public void setStartPoint(Point startPoint) ;

	public Point getEndPoint() ;

	public void setEndPoint(Point endPoint) ;

	
	public Date getStartTime() ;

	public void setStartTime(Date startTime) ;
	
	public Date getData() ;

	public void setData(Date data) ;

	@Destroy
	@Remove
	public void destory();
}
