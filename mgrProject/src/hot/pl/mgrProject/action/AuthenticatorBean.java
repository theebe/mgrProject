package pl.mgrProject.action;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;
import org.jboss.seam.security.RunAsOperation;
import org.jboss.seam.security.management.IdentityManager;

import flexjson.JSONSerializer;

import pl.mgrProject.model.user.Role;
import pl.mgrProject.model.user.User;

@Stateless
@Name("authenticator")
public class AuthenticatorBean implements Authenticator {
	@Logger
	private Log log;
 
	@In
	private EntityManager mgrDatabase;

	@Out(required = false, scope = ScopeType.SESSION)
	private User user;

	@In
	Identity identity;
	@In
	Credentials credentials;

	public boolean authenticate() {
		 
		log.info("authenticating {0}", credentials.getUsername());

	
		  try {
			  
		        this.user = (User) mgrDatabase.createQuery(
		            "from User where username = :username and password = :password")
		            .setParameter("username", credentials.getUsername())
		            .setParameter("password", credentials.getPassword())
		            .getSingleResult();
		         if (user.getRoles() != null) {
		            for (Role mr : user.getRoles()) 
		               identity.addRole(mr.getRolename());
		         }
		         System.out.println("JSON\n" + new JSONSerializer().serialize(user));
		         return true;
		      }
		      catch (NoResultException ex) {
		         return false;
		      }

	}

}
