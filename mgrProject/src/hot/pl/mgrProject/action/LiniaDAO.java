package pl.mgrProject.action;

import java.util.EventListener;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remove;

import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.remoting.WebRemote;

import pl.mgrProject.model.Linia;
import pl.mgrProject.model.TypKomunikacji;

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
	
	@WebRemote
	public void saveLinia(Linia l);
	/**
	 * Pobiera liste linii
	 * @return lista liniii
	 */
	@WebRemote
	public List<Linia> getLiniaList();


	public void delete(Linia l);
	public void merge(Linia l);
	
	public Linia getSelectedLinia();
	
	public void setSelectedLinia(Linia l);
	
	
	
	@Destroy
	@Remove
	public void destory();
}
