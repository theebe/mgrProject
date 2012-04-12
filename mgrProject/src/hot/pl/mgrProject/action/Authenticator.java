package pl.mgrProject.action;

import javax.ejb.Local;

/**
 * Interfejs dla logowania 
 * @author bat
 *
 */
@Local
public interface Authenticator {

	/**
	 * metoda logujaca
	 * @return true or false
	 */
    boolean authenticate();

}
