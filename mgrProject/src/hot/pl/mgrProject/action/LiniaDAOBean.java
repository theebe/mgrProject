package pl.mgrProject.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.End;
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
import pl.mgrProject.model.Odjazd;
import pl.mgrProject.model.Przystanek;
import pl.mgrProject.model.PrzystanekTabliczka;
import pl.mgrProject.model.TypDnia;
import pl.mgrProject.model.TypKomunikacji;

/**
 * Implementacja interfejsu LiniaDAO. 
 * Komponent Seam o nazwie 'liniaDAO', komponent stanowy o zasiegu konwersacji
 * @author bat
 *
 */
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

	
	@Out(required=false)
	private Linia selectedLinia;
	
	
	/**
	 * Zapisuje linie
	 * @param Integer numer linii
	 * @param TypKomunikacji typ linii
	 * @param List<Long> lista id przystankow
	 * @param Boolean czy utworzyc linie powrotna
	 */
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

	/**
	 * Zapisuje linie do bazy danych
	 */
	public void saveLinia(Linia l) {

		mgrDatabase.persist(l);
		log.info("Dodano nowa linie do bazy, NUMER LINII: " + l.getNumer()
				+ ", TYP: " + l.getTyp());

	}
	
	/**
	 * Pobiera linie o zadanym id 
	 */
	public Linia getLinia(Long id){
		Linia l = mgrDatabase.find(Linia.class, id);
		//lazy loading :(
		for(PrzystanekTabliczka pt: l.getPrzystanekTabliczka()){
			pt.getPrzystanek().getId();
		}
		return l;
	}
	
	/**
	 * Pobiera wszystkie linie
	 */
	@Factory("liniaList")
	@Begin(join = true)
	public List<Linia> getLiniaList() {

		// pobierz jeszcze raz
		liniaList = mgrDatabase.createNamedQuery("wszystkieLinie")
				.getResultList();
		log.info("Pobrano z bazy " + liniaList.size() + " linii");
		return liniaList;
	}

	
	
	/** 
	 * Kasuje linie
	 * @param linia do skasownania
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@End
	public void delete(Linia l) {
		
		if (l != null) {
			mgrDatabase.remove(l);
			liniaList.remove(l);
			
			log.info("Kasowanie linii nr " + l.getNumer());
			l = null;
		}
	}

	/**
	 * przerywa edycje linii
	 */
	public void cancel() {
		this.selectedLinia = null;
		getLiniaList();
	}

	/**
	 * Pobiera wybrana linie
	 */
	public Linia getSelectedLinia() {
		return selectedLinia;
	}

	
	public void setSelectedLinia(Linia l) {
		this.selectedLinia = l;
	}
	
	public List<Linia> getAll(){
		List resultList = mgrDatabase.createNamedQuery("wszystkieLinie").getResultList();
		return resultList;
	}
	
	
	
	
	@Destroy
	@Remove
	public void destory() {}


	/**
	 * Pobiera okreg czasowy
	 */
	public TimeZone getTimeZone(){
		TimeZone tz = new GregorianCalendar().getTimeZone();
		return tz;
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
			if (i == listaIdPrzystankow.size() - 1) {
				pt.setNastepnyPrzystanek(null);
			} else if (i == 0) {
				pt.setPoprzedniPrzystanek(null);
				pt.setNastepnyPrzystanek(przystTablList.get(j - 1));
				przystTablList.get(j - 1).setPoprzedniPrzystanek(pt);
			} else {
				pt.setNastepnyPrzystanek(przystTablList.get(j - 1));
				przystTablList.get(j - 1).setPoprzedniPrzystanek(pt);
			}
			pt.setCzasDoNastepnego(1);
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
