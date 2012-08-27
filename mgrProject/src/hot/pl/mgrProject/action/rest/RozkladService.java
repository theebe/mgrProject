package pl.mgrProject.action.rest;

import javax.ejb.Local;
import javax.ejb.Remove;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.jboss.seam.annotations.Destroy;

public interface RozkladService {

	public String all(String format);

	public String linie(String format);

	public String linia(Integer numer, String format);

	public String przystanki(String format);

	public String przystanek(Long id, String format);

}
