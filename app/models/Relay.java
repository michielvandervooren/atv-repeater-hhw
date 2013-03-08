package models;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import models.access.Permission;
import models.enums.Action;
import models.enums.ControllerId;
import models.enums.RelayId;
import models.enums.RelaySwitchType;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Relay extends Model {
	
	@Required
	@Enumerated(EnumType.STRING)
	public ControllerId controllerId;
	
	@Required
	@Enumerated(EnumType.STRING)
	public RelayId relayId;
	
	@Required
	@Enumerated(EnumType.STRING)
	public RelaySwitchType switchType;
	
	public boolean enabled;
	
	@OneToOne
	public Permission permission;
	
	@Enumerated(EnumType.STRING)
	public Action action;
	
	@Transient
	public boolean on;
	
	@Override
	public boolean equals(Object o) {
		return (o != null && o instanceof Relay && ((Relay) o).id.equals(id));
	}
	
	@Override
	public String toString() {
		return controllerId + " " + relayId;
	}
	
}
