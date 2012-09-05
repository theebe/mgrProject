package pl.mgrProject.action.rest;

import javax.ws.rs.core.Response;

public interface RozkladService {

	public Response all(String format, String pp);

	public Response linie(String format, String pp);

	public Response linia(Integer numer, String format, String pp);

	public Response przystanki(String format, String pp);

	public Response przystanek(Long id, String format, String pp);

}
