package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Icon extends Model {
	
	public String description;
	
	public String className;
	
	@Override
	public String toString() {
		return description;
	}
}
