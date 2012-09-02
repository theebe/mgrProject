package pl.mgrProject.action.rest;

import javax.ws.rs.core.Response;


public interface DijkstryService {

	
	public Response info(String format, String pp); 
	
	
	public Response run(String format,
			String path, String pp);
	
	public Response runEmpty(String format, String pp);
	
	
	
}
