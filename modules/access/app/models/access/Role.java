package models.access;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Role extends Model {
	
	@Required
	public String name;
	
	public String description;
	
	@ManyToMany
	@JoinTable(name = "role_permission", joinColumns = @JoinColumn(name = "roleId"), inverseJoinColumns = @JoinColumn(name = "permissionId"))
	public List<Permission> permissions;
	
	public String toString() {
		return this.name;
	}

}
