package pl.mgrProject.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

import org.ajax4jsf.event.PushEventListener;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.log.Log;

import pl.mgrProject.model.Linia;
import pl.mgrProject.model.Przystanek;
import pl.mgrProject.model.PrzystanekTabliczka;
import pl.mgrProject.model.TypKomunikacji;

@Stateful
@Name("liniaDAO")
@Scope(ScopeType.CONVERSATION)
public class LiniaDAOBean implements LiniaDAO, Serializable {

	@Logger
	private Log log;

	@In
	private EntityManager mgrDatabase;

	@DataModel
	private List<Linia> liniaList;

	@DataModelSelection
	@Out(required = false)
	private Linia selectedLinia;
	
	

	private PushEventListener listener;

	public Boolean saveLinia(Integer numer, TypKomunikacji typ,
			List<Long> listaIdPrzystankow, Boolean liniaPowrotna) {

		if (numer == 0 || typ == null || listaIdPrzystankow == null
				|| listaIdPrzystankow.size() == 0)
			return null;

		if (!czyLiniaDostepna(numer))
			return null;

		Linia linia = new Linia();
		linia.setNumer(numer);
		linia.setTyp(typ);
		linia.setPrzystanekTabliczka(createTabliczkiFromPrzystanki(
				listaIdPrzystankow, linia));

		try {
			mgrDatabase.persist(linia);
			listener.onEvent(new EventObject(this));
			log.info("Dodano nowa linie do bazy, NUMER LINII: "
					+ linia.getNumer() + ", TYP: " + linia.getTyp()
					+ ", LINIA POWROTNA" + liniaPowrotna);
		} catch (Exception ex) {
			log.error("Inny blad " + ex.getMessage());
			return null;
		}

		return true;
	}

	@Factory("liniaList")
	 @Begin(join=true)
	public List<Linia> getLiniaList() {
		// pobiera liczbe linii
		Long liczba = (Long) mgrDatabase.createQuery(
				"SELECT COUNT(l) FROM Linia l").getSingleResult();
		log.info("Liczba linii w bazie: " + liczba);

		// jezeli jescze lista nie byla pobierana'
		// lub liczba linii jest inna niz aktualna
		if (liniaList == null || liczba != liniaList.size()) {

			// pobierz jeszcze raz
			liniaList = mgrDatabase.createNamedQuery("wszystkieLinie")
					.getResultList();
			log.info("Pobrano z bazy " + liniaList.size() + " linii");
		}
		return liniaList;
	}

	public void delete() {
		if (selectedLinia != null) {
			mgrDatabase.remove(selectedLinia);
			log.info("Kasowanie linii nr \"#{selectedLinia.numer}\"");
			selectedLinia = null;
		}
	}

	/**
	 * Funkcja od razu tworzy tabliczke dla kazdym przystanku dla danej linii
	 * tabliczki sa puste
	 * 
	 * @param listaIdPrzystankow
	 *            lista identyfikatorow przystankow (nie przystankow)
	 * @param linia
	 *            aktualnie stworzona linia
	 * @return lista nowych tabliczek na przystankach
	 */
	private List<PrzystanekTabliczka> createTabliczkiFromPrzystanki(
			List<Long> listaIdPrzystankow, Linia linia) {
		if (listaIdPrzystankow == null || linia == null
				|| listaIdPrzystankow.size() == 0)
			return null;

		List<PrzystanekTabliczka> przystTablList = new ArrayList<PrzystanekTabliczka>();
		// od konca (aby od razu ustawic pole nastepnyPrzystanekTabliczka)
		for (int i = listaIdPrzystankow.size() - 1, j = 0; i >= 0; --i, ++j) {
			Przystanek p = mgrDatabase.getReference(Przystanek.class,
					listaIdPrzystankow.get(i));
			PrzystanekTabliczka pt = new PrzystanekTabliczka();
			pt.setLinia(linia);
			pt.setPrzystanek(p);
			if (i == listaIdPrzystankow.size() - 1)
				pt.setNastepnyPrzystanek(null);
			else
				pt.setNastepnyPrzystanek(przystTablList.get(j - 1));

			przystTablList.add(pt);
		}
		Collections.reverse(przystTablList);
		return przystTablList;
	}

	private boolean czyLiniaDostepna(Integer numer) {
		Long liczba = (Long) mgrDatabase.createQuery(
				"SELECT COUNT(l) FROM Linia l").getSingleResult();
		if (liczba >= 2)
			return false;

		return true;
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
