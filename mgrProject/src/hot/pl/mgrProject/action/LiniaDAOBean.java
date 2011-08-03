package pl.mgrProject.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

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

	@DataModelSelection("liniaList")
	@In(required = false)
	@Out(required = false)
	private Linia selectedLinia;


	public String saveLinia(Integer numer, TypKomunikacji typ,
			List<Long> listaIdPrzystankow, Boolean liniaPowrotna) {

		if (numer == 0 || typ == null || listaIdPrzystankow == null
				|| listaIdPrzystankow.size() == 0)
			return "Brak numeru lub typu, lub listy przystankow";

		if (!czyLiniaDostepna(numer))
			return "Istniej¹ juz 2 linie o tym numerze, wybierz inny";

	
		Linia linia = new Linia();
		linia.setNumer(numer);
		linia.setTyp(typ);
		linia.setPrzystanekTabliczka(createTabliczkiFromPrzystanki(
				listaIdPrzystankow, linia));
		saveLinia(linia);
		
		return "success";
	}

	public void saveLinia(Linia l) {

		mgrDatabase.persist(l);
		log.info("Dodano nowa linie do bazy, NUMER LINII: " + l.getNumer()
				+ ", TYP: " + l.getTyp());
		
	}

	@Factory("liniaList")
	@Begin(join = true)
	public List<Linia> getLiniaList() {

		// pobierz jeszcze raz
		liniaList = mgrDatabase.createNamedQuery("wszystkieLinie")
				.getResultList();
		log.info("Pobrano z bazy " + liniaList.size() + " linii");
		return liniaList;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void merge(Linia l) {

		mgrDatabase.merge(l);
		log.info("Uaktualniono linie nr " + l.getNumer());
	}

	public void delete(Linia l) {
		System.out.println("Delete called!!!!!!!!!!!!!!!!!!!!!!!!!!");
		if (l != null) {
			mgrDatabase.remove(l);
			liniaList.remove(l);
			System.out.println("REMOVE!!!!!!!!!!!!!!!");
			log.info("Kasowanie linii nr " + l.getNumer());
			l = null;
		}
	}
	

	public Linia getSelectedLinia() {
		return selectedLinia;
	}

	public void setSelectedLinia(Linia l) {
		this.selectedLinia = l;
	}


	@Destroy
	@Remove
	public void destory() {
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
		Long liczba = (Long) mgrDatabase
				.createQuery(
						"SELECT COUNT(l) FROM Linia l WHERE l.numer = :numer")
				.setParameter("numer", numer).getSingleResult();
		log.info("Liczba linii w bazie: " + liczba);
		if (liczba >= 2)
			return false;

		return true;
	}

}
