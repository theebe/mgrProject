package pl.mgrProject.action;

import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import pl.mgrProject.model.Linia;
import pl.mgrProject.model.PrzystanekTabliczka;

/**
 * Implementacja intrrfejsu do edycji tabliczek przystankowych
 * @author bat
 *
 */
@Stateful
@Name("przystanekTabliczkaDAO")
@Scope(ScopeType.CONVERSATION)
public class PrzystanekTabliczkaDAOBean implements PrzystanekTabliczkaDAO {

	@In
	private EntityManager mgrDatabase;

	@Logger
	private Log log;

	/**
	 * Kasuje tabliczne przystankowa z bayz danych
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void delete(PrzystanekTabliczka pt) {
		deleteFromLiniaList(pt);
		mgrDatabase.remove(pt);
		log.info("Usunieto przystanekTabliczke, nazwa: "
				+ pt.getPrzystanek().getNazwa() + " z bazy danych");
	}

	/**
	 * Wlasny @PreRemove
	 * 
	 * @param pt
	 *            przystanekTabl
	 */
	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	private void deleteFromLiniaList(PrzystanekTabliczka pt) {

		PrzystanekTabliczka poprzedni = pt.getPoprzedniPrzystanek();
		PrzystanekTabliczka nastepny = pt.getNastepnyPrzystanek();
		
		if(poprzedni == null && nastepny==null)
			return;
		
		if (poprzedni != null) {
			poprzedni.setNastepnyPrzystanek(pt.getNastepnyPrzystanek());
			mgrDatabase.merge(poprzedni);
		}
		if (nastepny != null) {
			nastepny.setPoprzedniPrzystanek(pt.getPoprzedniPrzystanek());
			mgrDatabase.merge(nastepny);
		}
		Linia l = pt.getLinia();
		l.getPrzystanekTabliczka().remove(pt);
		mgrDatabase.merge(l);

		log.info("Usunieto przystanekTabliczke, nazwa: "
				+ pt.getPrzystanek().getNazwa() + ", z linii: "
				+ pt.getLinia().getNumer());
	}

	

	
	
	@Remove
	@Destroy
	public void destroy() {
	}

}
