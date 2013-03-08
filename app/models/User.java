package models;

import javax.persistence.Entity;

import play.data.validation.Email;
import play.data.validation.Required;

@Entity
public class User extends models.access.User {

	@Required
	public String firstName;
	
	@Email
	public String email;
	
	public String toString() {
		return this.username;
	}
}
