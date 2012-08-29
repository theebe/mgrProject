package pl.mgrProject.action.rest;

import javax.ejb.Local;
import javax.ejb.Remove;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.jboss.seam.annotations.Destroy;


public interface DijkstryService {

	
	public String info(String format); 
	
	
	public String run(String format,
			String path);
	
	public String runEmpty(String format);
	
	
	
}
