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

import pl.mgrProject.model.user.User;

@Stateful
@Name("userDAOBean")
@Scope(ScopeType.CONVERSATION)
public class UserDAOBean implements Serializable, UserDAO {

	@Logger
	private Log log;

	@In
	private EntityManager mgrDatabase;

	@DataModelSelection
	@In(required = false)
	@Out(required = false)
	private User user;

	@DataModel
	private List<User> users;


	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@End(beforeRedirect = true, root = true)
	public void merge(User u) {
		mgrDatabase.merge(getUser());
		StringBuilder sb = new StringBuilder();
		sb.append("Uaktualnono uzytkownika: ");
		sb.append(getUser().getFirstname());
		sb.append(" ").append(getUser().getLastname()).append(".");
		log.info(sb.toString());
	}


	@Factory("users")
	public List<User> getUsers() {
		Long liczba = (Long) mgrDatabase.createQuery(
				"SELECT COUNT(u) FROM User u").getSingleResult();
		log.info("Liczba uzytkownikow w bazie: " + liczba);

		if (this.users == null || this.users.size() != liczba) {
			users = mgrDatabase.createNamedQuery("wszyscyUzytkownicy")
					.getResultList();
			log.info("Pobrano " + users.size() + " uzytkownikow z bazy danych");
		}
		return users;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@End
	public void delete(User u) {
		if (u != null) {
			mgrDatabase.remove(u);
			users.remove(u);
			log.info("Kasowanie uzytkownika " + u.getUsername());
			u = null;
		}
	}

	@Destroy
	@Remove
	public void destory() {
	}
	
	@Begin(join = true)
	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}
	
	

	
}
