package pl.mgrProject.action;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remove;

import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Destroy;

import pl.mgrProject.model.user.User;

@Local
public interface UserDAO {

	public List<User> getUsers();

	public void merge(User u);

	public void delete(User u);

	public void setUser(User user);

	public User getUser();

	@Destroy
	@Remove
	public void destory();
}
