package models;

import java.util.Date;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class SwitchAction extends Model {

	public String userName;
	public Date date;
	public String actionType;
}
