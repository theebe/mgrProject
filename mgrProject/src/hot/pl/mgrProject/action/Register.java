package pl.mgrProject.action;

import javax.ejb.Local;

/**
 * Interfejs do rejestracji nowego uzytkownika
 * @author bat
 *
 */
@Local
public interface Register {
	
	/**
	 * rejestruje nowego uzytkownika
	 */
	public void register();
	
	/**
	 * sprawdza czy jest dostepna nazwa
	 */
	public void invalid();
	
	/**
	 * sprawdza czy wybrany uzytkownik juz istnieje
	 * @return
	 */
	public boolean isRegistered();
	
	
	public String getVerify();
	public void setVerify(String verify);

	public void remove();
}
