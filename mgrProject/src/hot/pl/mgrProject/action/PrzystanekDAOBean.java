package pl.mgrProject.action;

import java.io.Serializable;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

import org.ajax4jsf.event.PushEventListener;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.postgis.Point;

import pl.mgrProject.model.Przystanek;
import pl.mgrProject.model.TypKomunikacji;

/**
 * Klasa obslugujaca dodawanie oraz wyciaganie przystankow z bazy
 * 
 * @author bat
 * 
 */
@Stateful
@Name("przystanekDAO")
@Scope(ScopeType.CONVERSATION)
public class PrzystanekDAOBean implements Serializable, PrzystanekDAO {

	@Logger
	private Log log;

	@In
	private EntityManager mgrDatabase;

	private List<Przystanek> przystanekList;

	private PushEventListener listener;

	/**
	 * Metoda WebRemote
	 * 
	 * @return Przystanek zwraca null lub przystanek aktualnie dodany
	 */
	public Przystanek savePrzystanek(double lon, double lat, String nazwa,
			TypKomunikacji typ) {
		if (lon == 0 || lat == 0 || nazwa == null || typ == null)
			return null;

		Przystanek p = new Przystanek();
		p.setNazwa(nazwa);
		Point location = new Point(lon, lat);
		location.setSrid(4326);
		p.setLocation(location);
		p.setTyp(typ);
		try {
			mgrDatabase.persist(p);
			//zdarzenie uaktualnienia przystankow po stronie przegladarki
			listener.onEvent(new EventObject(this));
			log.info("Dodano obiekt przystanek do bazy, nazwa: " + p.getNazwa());
		} catch (Exception e) {
			log.info("Blad w zapisie przystanku do bazy, nazwa: "
					+ p.getNazwa());
			return null;
		}
		return p;
	}

	@Factory
	public List<Przystanek> getPrzystanekList() {
		// Liczba przystankow w bazie
		Long liczba = (Long) mgrDatabase.createQuery(
				"SELECT COUNT(p) FROM Przystanek p").getSingleResult();
		log.info("Liczba przystankow w bazie: " + liczba);

		// jezeli jeszcze nie pobrano z bazy lub liczba przystankow sie rozni
		if (przystanekList == null || liczba != przystanekList.size()) {
			przystanekList = mgrDatabase
					.createNamedQuery("wszystkiePrzystanki").getResultList();
			log.info("Pobrano z bazy " + przystanekList.size() + " przystankow");
		}
		return przystanekList;
	}

	public void addListener(EventListener listener) {
		synchronized (listener) {
			if (this.listener != listener) {
				this.listener = (PushEventListener) listener;
			}
		}
	}

	@Destroy
	@Remove
	public void destory() {
	}

}
