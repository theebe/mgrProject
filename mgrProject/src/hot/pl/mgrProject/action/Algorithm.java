package pl.mgrProject.action;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remove;

import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.remoting.WebRemote;

import pl.mgrProject.model.Przystanek;
import pl.mgrProject.model.PrzystanekTabliczka;

@Local
public interface Algorithm {
	@WebRemote
	public Boolean run();
	
	@WebRemote
	public Boolean setStartPoint(double x, double y);
	
	@WebRemote
	public Boolean setStopPoint(double x, double y);
	
	@WebRemote
	public List<PrzystanekTabliczka> getPath();
	
	public Przystanek getClosestToStart();
	
	public Przystanek getClosestToStop();
	
	@Destroy
	public void destroy();
	
	@Remove
	public void remove();
}
