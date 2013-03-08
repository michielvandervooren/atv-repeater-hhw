package models;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

import models.Color;
import models.Icon;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Channel extends Model {

	@Required
	public String name;
	
	public String description;
	
	@OneToOne
	public Relay audioRelay;
	
	@OneToOne
	public Relay videoRelay;
	
	@OneToOne
	public Icon icon;
	
	@OneToOne
	public Color color;
	
	@Override
	public String toString() {
		return description;
	}
}
