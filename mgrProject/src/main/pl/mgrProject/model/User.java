package pl.mgrProject.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.validator.Length;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.management.UserFirstName;
import org.jboss.seam.annotations.security.management.UserLastName;
import org.jboss.seam.annotations.security.management.UserPassword;
import org.jboss.seam.annotations.security.management.UserPrincipal;

/**
 * Encja odpowiedzialna za u¿ytkownika
 * Odwzorowanie na tabele "USERS"
 * @author bat
 *
 */
@Entity
@Name("user")
@Scope(ScopeType.SESSION)
@Table(name="USERS")
public class User implements Serializable
{
    
	private static final long serialVersionUID = -7157380483898412773L;
	private Long id;
    private Integer version;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    

    @Id @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Version
    public Integer getVersion() {
        return version;
    }

    private void setVersion(Integer version) {
        this.version = version;
    }

   
    
    @UserPassword(hash="md5")
    public String getPassword(){
    	return password;
    }
    
    public void setPassword(String pass){
    	this.password = pass;
    }
    
    
    @Length(max = 20)
    @UserPrincipal
    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
	@UserFirstName
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@UserLastName
	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	

}
