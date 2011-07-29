package pl.mgrProject.action;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remove;

import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.remoting.WebRemote;

import pl.mgrProject.model.Linia;
import pl.mgrProject.model.TypKomunikacji;

@Local
public interface LiniaDAO {

	@WebRemote
	public Linia saveLinia(Integer numer, TypKomunikacji typ, List<Integer> listaIdPrzystankow, Boolean liniaPowrotna);
	
	@WebRemote
	public List<Linia> getLiniaList();
	
	@Destroy
	@Remove
	public void destory();
}
