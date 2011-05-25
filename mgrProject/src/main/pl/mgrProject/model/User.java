package pl.mgrProject.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Pattern;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Entity
@Name("user")
@Scope(ScopeType.SESSION)
@Table(name = "users")
public class User implements Serializable {

	private static final long serialVersionUID = 1029123968209789700L;

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	@Length(min = 4, max = 15)
	@Pattern(regex = "^\\w*$", message = "not a valid username")
	private String username;

	@NotNull
	@Length(min = 5, max = 32)
	private String password;

	@Length(max = 100)
	private String name;

	

	public User() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
	@Override
	public String toString() {
		return "User(" + username + ")";
	}

}