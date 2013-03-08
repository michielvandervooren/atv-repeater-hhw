package models.access;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Permission extends Model {

	@Required
	public String name;
	
	public String description;
	
	public String toString() {
		return this.name;
	}
}
