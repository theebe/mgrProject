package pl.mgrProject.action.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class RestServiceApplication extends Application {

	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> classes = new HashSet<Class<?>>();

	public RestServiceApplication() {
		classes.add(DijkstryServiceBean.class);
		classes.add(RozkladServiceBean.class);
	}

	@Override
	public Set<Class<?>> getClasses() {
		return classes;
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}

}
