package pl.mgrProject.action;

import java.io.Serializable;
import java.util.List;

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

import pl.mgrProject.model.Konfiguracja;
import pl.mgrProject.model.Przystanek;

@Stateful
@Name("konfiguracjaDAO")
@Scope(ScopeType.CONVERSATION)
public class KonciguracjaDAOBean implements Serializable, KonfiguracjaDAO {

	@Logger
	private Log log;

	@In
	private EntityManager mgrDatabase;

	@In(required = false)
	@Out(required = true)
	private Konfiguracja konfiguracja;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void merge() {
		mgrDatabase.merge(getKonfiguracja());
		log.info("Uaktualniono konfiguracje " + getKonfiguracja().getName());
	}

	@Begin(join = true)
	public void setKonfiguracja(Konfiguracja konfiguracja) {
		this.konfiguracja = konfiguracja;
	}

	@Factory
	public Konfiguracja getKonfiguracja() {
		if (konfiguracja == null) {
			setKonfiguracja((Konfiguracja) mgrDatabase
					.createNamedQuery("konfiguracjaPoNazwie")
					.setParameter("nazwa", "default").getSingleResult());
		}
		return this.konfiguracja;
	}

	@Destroy
	@Remove
	public void destory() {
	}

}
