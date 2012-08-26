package pl.mgrProject.action;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remove;

import org.jboss.seam.annotations.Destroy;

import pl.mgrProject.model.Konfiguracja;

@Local
public interface KonfiguracjaDAO {

	public void merge();

	public void setKonfiguracja(Konfiguracja konfiguracja);

	public Konfiguracja getKonfiguracja();

	@Destroy
	@Remove
	public void destory();
	
	
}
