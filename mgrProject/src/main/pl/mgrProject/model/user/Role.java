package pl.mgrProject.model.user;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.jboss.seam.annotations.security.management.RoleConditional;
import org.jboss.seam.annotations.security.management.RoleGroups;
import org.jboss.seam.annotations.security.management.RoleName;

/**
 * Klasa zawiera role praw dostepu
 * @author bat
 *
 */
@Entity
@Table(name="ROLE")
public class Role implements Serializable {
  private Integer roleId;
  private String rolename;
  private boolean conditional;
 // private Set<Role> groups;
  
  @Id @GeneratedValue
  public Integer getRoleId() { return roleId; }
  public void setRoleId(Integer roleId) { this.roleId = roleId; }
  
  @RoleName
  public String getRolename() { return rolename; }
  public void setRolename(String rolename) { this.rolename = rolename; }
  
  @RoleConditional
  public boolean isConditional() { return conditional; }
  public void setConditional(boolean conditional) { this.conditional = conditional; }
  
  /*
  @RoleGroups
  @ManyToMany(targetEntity = Role.class)
  @JoinTable(name = "RoleGroups", 
    joinColumns = @JoinColumn(name = "RoleId"),
    inverseJoinColumns = @JoinColumn(name = "GroupId"))
  public Set<Role> getGroups() { return groups; }
  public void setGroups(Set<Role> groups) { this.groups = groups; }
  */
}