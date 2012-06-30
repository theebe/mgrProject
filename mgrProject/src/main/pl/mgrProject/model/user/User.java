package pl.mgrProject.model.user;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.validator.Length;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.management.UserEnabled;
import org.jboss.seam.annotations.security.management.UserFirstName;
import org.jboss.seam.annotations.security.management.UserLastName;
import org.jboss.seam.annotations.security.management.UserPassword;
import org.jboss.seam.annotations.security.management.UserPrincipal;
import org.jboss.seam.annotations.security.management.UserRoles;

/**
 * Encja odpowiedzialna za u¿ytkownika Odwzorowanie na tabele "USERS"
 * @author bat
 */
@Entity
@Name("user")
@Scope(ScopeType.SESSION)
@Table(name = "USERS", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User implements Serializable {

	private Long id;
	private Integer version;
	private String username;
	private String password;
	private String firstname;
	private String lastname;

	private boolean enabled;
	private Set<Role> roles;

	/**
	 * Id
	 * @return
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * version
	 * @return
	 */
	@Version
	public Integer getVersion() {
		return version;
	}

	private void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * Haslo
	 * @return
	 */
	@UserPassword(hash = "md5")
	public String getPassword() {
		return password;
	}

	public void setPassword(String pass) {
		this.password = pass;
	}

	/**
	 * Nazwa uzytkownika
	 * @return
	 */
	@Length(max = 20)
	@UserPrincipal
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Imie
	 */
	@UserFirstName
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * Nazwisko
	 * @return
	 */
	@UserLastName
	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * Konto aktywne lub nieaktywne
	 * @return
	 */
	@UserEnabled
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Jakie prawa posiada uzytkownik
	 * @return
	 */
	@UserRoles
	@ManyToMany(targetEntity = Role.class)
	@JoinTable(name = "UserRoles", joinColumns = @JoinColumn(name = "UserId"), inverseJoinColumns = @JoinColumn(name = "RoleId"))
	public Set<Role> getRoles() {
		if (roles == null) {
			roles = new HashSet<Role>();
		}
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

}
