package pl.mgrProject.action.rest;

import javax.ejb.Local;
import javax.ejb.Remove;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.jboss.seam.annotations.Destroy;

@Local
public interface RozkladService {

	public Response all();

	public String linie();

	public String linia(Integer numer);

	public Response przystanki();

	public Response przystanek(Integer id);

	
	@Destroy
	@Remove
	public void destory();
}
