package pl.mgrProject.action;

import java.util.EventListener;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remove;

import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.remoting.WebRemote;
import org.richfaces.model.selection.Selection;

import pl.mgrProject.model.Przystanek;
import pl.mgrProject.model.TypKomunikacji;

/**
 * Interfejs lokalny do edycji Przystanku, niektore metody dostepne sa przez Seam Remote
 * @author bat
 *
 */
@Local
public interface PrzystanekDAO {

	/**
	 * Dodaje nowy przystanek do bazy
	 * @param lon szerokosc geograficzna
	 * @param lat dlugosc geograficzna
	 * @param nazwa nazwa przystanku
	 * @param typ typ przystanku
	 * @return zwraca nowy przystanek
	 */
	@WebRemote
	public Przystanek savePrzystanek(double lon, double lat, String nazwa, TypKomunikacji typ);
	
	/**
	 * wysyla liste przystankow, bez taliby linie i poprzedniePrzystanki
	 * @return
	 */
	@WebRemote(exclude={"linie", "poprzedniePrzystanki"})
	public List<Przystanek> getPrzystanekList();
	
	
	
	/**
	 * Kasuje przystanek z bazy danych
	 * @param p
	 */
	public void delete(Przystanek p);
	
	/**
	 * Uaktualnia przystanek w bazie danych
	 * @param p
	 */
	public void merge(Przystanek p);
	
	public Przystanek getSelectedPrzystanek();
	
	public void setSelectedPrzystanek(Przystanek p);

	public List<Przystanek> getAll();
	
	@Destroy
	@Remove
	public void destory();
}
