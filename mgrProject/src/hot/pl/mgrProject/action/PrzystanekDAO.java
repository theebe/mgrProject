package pl.mgrProject.action;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remove;

import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.remoting.WebRemote;

import pl.mgrProject.model.Przystanek;
import pl.mgrProject.model.TypKomunikacji;

@Local
public interface PrzystanekDAO {

	

	@WebRemote
	public Przystanek savePrzystanek(double lon, double lat, String nazwa, TypKomunikacji typ);
	
	/**
	 * wysyla liste przystankow, bez taliby linie i poprzedniePrzystanki
	 * @return
	 */
	@WebRemote(exclude={"linie", "poprzedniePrzystanki"})
	public List<Przystanek> getAllPrzystanki();
	
	@Destroy
	@Remove
	public void destory();
}
