package pl.mgrProject.action;

import java.util.EventListener;
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
	public List<Przystanek> getPrzystanekList();
	
	
	public void addListener(EventListener listener);
	
	
	public void delete(Przystanek p);
	public void merge(Przystanek p);
	
	public Przystanek getSelectedPrzystanek();
	
	public void setSelectedPrzystanek(Przystanek p);
	
	@Destroy
	@Remove
	public void destory();
}
