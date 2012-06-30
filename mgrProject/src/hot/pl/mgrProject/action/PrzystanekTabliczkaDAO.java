package pl.mgrProject.action;

import javax.ejb.Local;
import javax.ejb.Remove;

import org.jboss.seam.annotations.Destroy;

import pl.mgrProject.model.PrzystanekTabliczka;

/**
 * Interfejs do edycji tabliczek przystankowych
 * @author bat
 *
 */
@Local
public interface PrzystanekTabliczkaDAO {


	/**
	 * kasuje tabliczke z bazy danych
	 * @param pt
	 */
	public void delete(PrzystanekTabliczka pt);


	@Remove
	@Destroy
	public void destroy();

}