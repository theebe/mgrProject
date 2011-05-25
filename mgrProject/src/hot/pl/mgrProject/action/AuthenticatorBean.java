package pl.mgrProject.action;

import javax.ejb.Stateless;
import static org.jboss.seam.ScopeType.SESSION;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;

import pl.mgrProject.model.User;

/**
 * Klasa odpowiedzialna za logowanie i autentyfikacje admina
 * @author bat
 *
 */
@Stateless
@Name("authenticator")
public class AuthenticatorBean implements Authenticator
{
    @Logger private Log log;

	@PersistenceContext	private EntityManager entityManager;

	@In(required = false)
	@Out(required = false, scope = SESSION)
	private User user;

	@In	Identity identity;
	@In	Credentials credentials;
    public boolean authenticate()
    {
    	log.info("authenticating {0}", credentials.getUsername());
		List results = entityManager
				.createQuery(
						"select u from User u where u.username=#{identity.username} and u.password=#{identity.password}")
				.getResultList();

		if (results.size() == 0) {
			return false;
		} else {
			user = (User) results.get(0);
			if(user.getUsername().equals("admin")) identity.addRole("admin");
			return true;
		}
		
    }

}
