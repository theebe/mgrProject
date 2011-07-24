package pl.mgrProject.action;

import javax.ejb.Local;
import javax.ejb.Remove;

import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.remoting.WebRemote;

@Local
public interface PrzystanekDAO {

	

	@WebRemote
	public Boolean savePrzystanek(double lon, double lat, String nazwa);
	
	@Destroy
	@Remove
	public void destory();
}
