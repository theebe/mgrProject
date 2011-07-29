package pl.mgrProject.action;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

import org.hibernate.HibernateException;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import pl.mgrProject.model.Linia;
import pl.mgrProject.model.Przystanek;
import pl.mgrProject.model.PrzystanekTabliczka;
import pl.mgrProject.model.TypKomunikacji;


@Stateful
@Name("liniaDAO")
@Scope(ScopeType.SESSION)
public class LiniaDAOBean implements LiniaDAO{

	@Logger
	private Log log;

	@In
	private EntityManager mgrDatabase;

	private List<Linia> liniaList = null;
	
	
	public Linia saveLinia(Integer numer, TypKomunikacji typ, List<Integer> listaIdPrzystankow, Boolean liniaPowrotna) {

		if(numer==0 || typ==null || listaIdPrzystankow==null || listaIdPrzystankow.size()==0)
			return null;
		
		Linia linia = new Linia();
		linia.setNumer(numer);
		linia.setTyp(typ);
		//linia.setPrzystanekTabliczka( createTabliczkiFromPrzystanki(listaIdPrzystankow, linia) );
		try{
			mgrDatabase.persist(linia);
			log.info("Dodano nowa linie do bazy, NUMER LINII: " + linia.getNumer() + ", TYP: " + linia.getTyp() + ", LINIA POWROTNA" + liniaPowrotna);
		}
		//pewnie wyskoczylo z funkcji beforeCreate @PrePersist
		catch(HibernateException e){
			log.warn("Nie dodano lini! \n"+
					"Powod:" + e.getMessage());
			
			//przechwycenie po stronie klienta w kodzie js (funkcja saveLiniaExceptionHandler)
			//Athrow e;
		}
		return linia;
	}

	
	public List<Linia> getLiniaList() {
		//pobiera liczbe linii
		Long liczba = (Long)mgrDatabase.createQuery("SELECT COUNT(l) FROM Linia l").getSingleResult();
		log.info("Liczba przystankow w bazie: " + liczba);
		
		//jezeli jescze lista nie byla pobierana'
		//lub liczba linii jest inna niz aktualna
		if (liniaList == null || liczba!=liniaList.size()) {
			
			//pobierz jeszcze raz
			liniaList = mgrDatabase.createNamedQuery(
					"wszystkiePrzystanki").getResultList();
			log.info("Pobrano z bazy " + liniaList.size() + " linii");
		}
		return liniaList;
	}
	
	
	/**
	 * Funkcja od razu tworzy tabliczke dla kazdym przystanku dla danej linii
	 * tabliczki sa puste
	 * @param listaIdPrzystankow lista identyfikatorow przystankow (nie przystankow)
	 * @param linia aktualnie stworzona linia
	 * @return lista nowych tabliczek na przystankach
	 */
	private List<PrzystanekTabliczka> createTabliczkiFromPrzystanki(List<Integer>listaIdPrzystankow, Linia linia){
		if(listaIdPrzystankow == null || linia == null || listaIdPrzystankow.size()==0)
			return null;
		
		List<PrzystanekTabliczka> przystanekTabliczkas = new ArrayList<PrzystanekTabliczka>();
		
		//od konca (aby od razu ustawic pole nastepnyPrzystanekTabliczka
		for(int i=listaIdPrzystankow.size()-1; i>=0; --i){
			Przystanek p = mgrDatabase.find(Przystanek.class, listaIdPrzystankow.get(i));
			PrzystanekTabliczka pt = new PrzystanekTabliczka();
			pt.setLinia(linia);
			pt.setPrzystanek(p);
			if(i==listaIdPrzystankow.size()-1) pt.setNastepnyPrzystanek(null);
			else pt.setNastepnyPrzystanek(przystanekTabliczkas.get(przystanekTabliczkas.size()-1));
			przystanekTabliczkas.add(pt);
		}
		return przystanekTabliczkas;
	}
	
	@Destroy
	@Remove
	public void destory(){}


}
