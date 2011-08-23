package pl.mgrProject.action;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remove;

import org.jboss.seam.annotations.Destroy;

import pl.mgrProject.model.Linia;
import pl.mgrProject.model.Odjazd;
import pl.mgrProject.model.PrzystanekTabliczka;

@Local
public interface LiniaEditer {

	public void setEditedLinia(Long id);
	public Linia getEditedLinia();
	
	public void merge();
	
	public void cancel();

	public void change();
	
	public void removeOdjazd(Odjazd o);
	public void addOdjazdSw();
	public void addOdjazdPo();
	
	public List<PrzystanekTabliczka> getPtList();
	
	@Destroy
	@Remove
	public void destory();
}
