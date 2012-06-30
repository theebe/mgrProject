package pl.mgrProject.action;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remove;

import org.jboss.seam.annotations.Destroy;

import pl.mgrProject.model.Linia;
import pl.mgrProject.model.Odjazd;
import pl.mgrProject.model.PrzystanekTabliczka;

/**
 * Interfejs do edycji Linii
 * @author bat
 *
 */
@Local
public interface LiniaEditer {

	/** 
	 * Ustawia edytowana linie
	 * @param id linii
	 */
	public void setEditedLinia(Long id);
	
	/**
	 * Pobiera edytowana linie
	 * @return linia
	 */
	public Linia getEditedLinia();
	
	/**
	 * Zapisuje do bazy
	 */
	public void merge();
	
	/**
	 * anuluje edycje
	 */
	public void cancel();

	/**
	 * Funckcja przelicza godziny na poczegolnych przystankach, po zmianie godziny lub odleglosci pomiedzy przystankami
	 */
	public void change();
	
	/**
	 * Usuwa dany odjazd z linii
	 * @param o 
	 */
	public void removeOdjazd(Odjazd o);
	
	/**
	 * Dodaje odjazd w swieta
	 */
	public void addOdjazdSw();
	
	/**
	 * Dodaje odjazd w dzien poweszedni
	 * 
	 */
	public void addOdjazdPo();
	
	public List<PrzystanekTabliczka> getPtList();
	
	@Destroy
	@Remove
	public void destory();
}
