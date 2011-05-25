package pl.mgrProject.action;

import java.util.List;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import pl.mgrProject.model.User;

/**
 * Klasa odpowiedzialna za rejestracje nowych urzytkownikow
 * @author bat
 *
 */
@Stateful
@Scope(ScopeType.EVENT)
@Name("register")
public class RegisterAction implements Register {
	
	@Logger
	private Log log;

	@In
	private User user;
	

	@PersistenceContext
	private EntityManager entityManager;

	@In
	private FacesMessages facesMessages;

	private String verify;

	private boolean registered;

	public void register() {
		if (user.getPassword().equals(verify)) {
			List existing = entityManager
					.createQuery(
							"select u.username from User u where u.username=#{user.username}")
					.getResultList();
			if (existing.size() == 0) {
				entityManager.persist(user);
				facesMessages
						.add("Successfully registered as #{user.username}");
				registered = true;
				log.info("Uzytkownik #{user.username} dodany do bazy.");
			} else {
				facesMessages.addToControl("username",
						"Username #{user.username} already exists");
			}
		} else {
			facesMessages.addToControl("verify", "Re-enter your password");
			verify = null;
		}
	}

	public void invalid() {
		facesMessages.add("Please try again");
	}

	public boolean isRegistered() {
		return registered;
	}

	public String getVerify() {
		return verify;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}

	@Destroy
	@Override
	public void destroy() {}

	@Remove
	public void remove() {}

}