package pl.mgrProject.action;

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
import pl.mgrProject.model.TypKomunikacji;


@Stateful
@Name("liniaDAO")
@Scope(ScopeType.CONVERSATION)
public class LiniaDAOBean implements LiniaDAO{

	@Logger
	private Log log;

	@In
	private EntityManager mgrDatabase;

	private List<Linia> liniaList = null;
	
	
	public Linia saveLinia(Integer numer, TypKomunikacji typ, List<Integer> listaIdPrzystankow) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public List<Linia> getLiniaList() {
		Long liczba = (Long)mgrDatabase.createQuery("SELECT COUNT(l) FROM Linia l").getSingleResult();
		log.info("Liczba przystankow w bazie: " + liczba);
		if (liniaList == null || liczba!=liniaList.size()) {
			liniaList = mgrDatabase.createNamedQuery(
					"wszystkiePrzystanki").getResultList();
			log.info("Pobrano z bazy " + liniaList.size() + " linii");
		}
		return liniaList;
	}
	
	
	@Destroy
	@Remove
	public void destory(){}


}
