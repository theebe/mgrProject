package pl.mgrProject.action;

import java.util.List;
import java.util.TimeZone;

import javax.ejb.Local;
import javax.ejb.Remove;

import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.remoting.WebRemote;

import pl.mgrProject.model.Linia;
import pl.mgrProject.model.TypKomunikacji;

/**
 * Lokalny interfejs dostepu do obiektow typu Linia.  Wybrane metody udostepnione sa przy pomocy Seam Remote
 * @author bat
 *
 */
@Local
public interface LiniaDAO {

	

	/**
	 * Zapisuje nowa linie do bazy
	 * @param numer
	 * @param typ
	 * @param listaIdPrzystankow
	 * @param liniaPowrotna
	 * @return wiadomosc o powodzeniu
	 */
	@WebRemote
	public String saveLinia(Integer numer, TypKomunikacji typ, List<Long> listaIdPrzystankow, Boolean liniaPowrotna);

	public void saveLinia(Linia l);
	
	/**
	 * Pobiera liste linii
	 * @return lista liniii
	 */
	@WebRemote
	public List<Linia> getLiniaList();

	/**
	 * Kasuje linie
	 * @param l linia do skasowania
	 */
	public void delete(Linia l);

	/**
	 * Pobiera wybrana linie
	 * @return Linia
	 */
	@WebRemote
	public Linia getSelectedLinia();
	public void setSelectedLinia(Linia l);
	
	/**
	 * Pobiera linie o podanym id
	 * @param id id linii
	 * @return Linia
	 */
	@WebRemote
	public Linia getLinia(Long id);

	public TimeZone getTimeZone();
	public void cancel();
	
	@Destroy
	@Remove
	public void destory();
}
