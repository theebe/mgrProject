package pl.mgrProject.action;

import java.math.BigInteger;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.postgis.Point;

import pl.mgrProject.model.Przystanek;

@Stateful
@Scope(ScopeType.CONVERSATION)
@Name("algorithmBean")
public class AlgorithmBean implements Algorithm {
	@Logger
	private Log log;
	@In
	private EntityManager mgrDatabase;
	private Point startPoint;
	private Point stopPoint;
	
	public Boolean run() {
		Przystanek p = getClosestToStart();
		p = getClosestToStop();

		return true;
	}
	
	public Point getStartPoint() {
		return startPoint;
	}
	
	public Boolean setStartPoint(double x, double y) {
		if(x == 0 || y == 0) {
			return false;
		}
		
		startPoint = new Point(x, y);
		startPoint.setSrid(4326);
		log.info("[AlgorithmBean] startPoint set");
		return true;
	}
	
	public Point getStopPoint() {
		return stopPoint;
	}
	
	public Boolean setStopPoint(double x, double y) {
		if(x == 0 || y == 0) {
			return false;
		}
		
		stopPoint = new Point(x, y);
		stopPoint.setSrid(4326);
		log.info("[AlgorithmBean] stopPoint set");
		return true;
	}
	
	public Przystanek getClosestToStart() {
		BigInteger id = (BigInteger)mgrDatabase.createNativeQuery("select foo.id from (select p.id, st_distance_sphere(p.location, ST_GeomFromText('POINT(" + startPoint.x + " " + startPoint.y + ")', 4326)) as odleglosc from przystanki p order by odleglosc limit 1) as foo").getResultList().get(0);
		Przystanek p = mgrDatabase.getReference(Przystanek.class, id.longValue());
		log.info("[AlgorithmBean] Znaleziono najblizszy przystanek: " + p.getNazwa());
		return p;
	}
	
	public Przystanek getClosestToStop() {
		BigInteger id = (BigInteger)mgrDatabase.createNativeQuery("select foo.id from (select p.id, st_distance_sphere(p.location, ST_GeomFromText('POINT(" + stopPoint.x + " " + stopPoint.y + ")', 4326)) as odleglosc from przystanki p order by odleglosc limit 1) as foo").getResultList().get(0);
		Przystanek p = mgrDatabase.getReference(Przystanek.class, id.longValue());
		log.info("[AlgorithmBean] Znaleziono najblizszy przystanek: " + p.getNazwa());
		return p;
	}
	
	@Destroy
	public void destroy() {
	}
	
	@Remove
	public void remove() {
	}
}
