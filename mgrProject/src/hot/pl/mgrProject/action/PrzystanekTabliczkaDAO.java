package pl.mgrProject.action;

import javax.ejb.Local;
import javax.ejb.Remove;

import org.jboss.seam.annotations.Destroy;

import pl.mgrProject.model.PrzystanekTabliczka;

@Local
public interface PrzystanekTabliczkaDAO {

	public void delete(PrzystanekTabliczka pt);


	@Remove
	@Destroy
	public void destroy();

}