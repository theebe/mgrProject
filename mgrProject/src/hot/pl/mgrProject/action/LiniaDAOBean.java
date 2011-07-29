package pl.mgrProject.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

import pl.mgrProject.model.Linia;
import pl.mgrProject.model.Przystanek;
import pl.mgrProject.model.PrzystanekTabliczka;
import pl.mgrProject.model.TypKomunikacji;

@Stateful
@Name("liniaDAO")
@Scope(ScopeType.SESSION)
public class LiniaDAOBean implements LiniaDAO {

	@Logger
	private Log log;

	@In
	private EntityManager mgrDatabase;

	private List<Linia> liniaList = null;

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
		createTabliczkiFromPrzystanki(listaIdPrzystankow, linia);

		try {
			mgrDatabase.persist(linia);
			log.info("Dodano nowa linie do bazy, NUMER LINII: "
					+ linia.getNumer() + ", TYP: " + linia.getTyp()
					+ ", LINIA POWROTNA" + liniaPowrotna);
		} catch (Exception ex) {
			log.error("Inny blad " + ex.getMessage());
			return null;
		}


		return true;
	}

	public List<Linia> getLiniaList() {
		// pobiera liczbe linii
		Long liczba = (Long) mgrDatabase.createQuery(
				"SELECT COUNT(l) FROM Linia l").getSingleResult();
		log.info("Liczba przystankow w bazie: " + liczba);

		// jezeli jescze lista nie byla pobierana'
		// lub liczba linii jest inna niz aktualna
		if (liniaList == null || liczba != liniaList.size()) {

			// pobierz jeszcze raz
			liniaList = mgrDatabase.createNamedQuery("wszystkiePrzystanki")
					.getResultList();
			log.info("Pobrano z bazy " + liniaList.size() + " linii");
		}
		return liniaList;
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
	private void createTabliczkiFromPrzystanki(List<Long> listaIdPrzystankow,
			Linia linia) {
		if (listaIdPrzystankow == null || linia == null
				|| listaIdPrzystankow.size() == 0)
			return;

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
				pt.setNastepnyPrzystanek(linia.getPrzystanekTabliczka().get(
						j - 1));

			linia.addPrzystanekTabliczka(pt);
		}
		return;
	}

	private boolean czyLiniaDostepna(Integer numer) {
		Long liczba = (Long) mgrDatabase.createQuery(
				"SELECT COUNT(l) FROM Linia l").getSingleResult();
		if (liczba > 2)
			return false;

		return true;
	}

	@Destroy
	@Remove
	public void destory() {
	}

}
