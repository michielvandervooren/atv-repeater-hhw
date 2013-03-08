package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import models.daeip2controller.DaeIp2ControllerConfig;
import models.daeip2controller.DaeIp2ControllerModel;
import models.enums.ControllerId;
import models.enums.RelayId;
import models.enums.RelaySwitchType;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

@Entity
public class RelayController extends Model {

	@Unique
	@Required
	@Enumerated(EnumType.STRING)
	public ControllerId controllerId;
	
	public String url;
	
	public String userName;
	
	public String password;
	
	@Transient
	public List<Relay> relays;
	
	@Transient
	DaeIp2ControllerModel ipController;
	
	public void connect() {
		DaeIp2ControllerConfig.create(controllerId, url, userName, password);
		ipController = DaeIp2ControllerModel.findByRef(controllerId);
	}
	
	public void sync() {
		relays = Relay.find("byControllerId", controllerId).fetch();
		for (Relay relay : relays) {
			if (relay.relayId.isLowByte()) {
				relay.on = selected(relay.relayId.bitMask(), ipController.p3);
			} else {
				relay.on = selected(relay.relayId.bitMask(), ipController.p5);
			}
		}
	}
	
	public boolean connected() {
		return ipController != null;
	}
	
	public void switchRelay(Relay r) {
		if (connected()) {
			Relay relay = getRelay(r);
			if (relay != null) {
				if (relay.switchType == RelaySwitchType.MUTEX_SWITCH) {
					switchOff();
					relay.on = true;
				} else if (relay.switchType == RelaySwitchType.TOGGLE_SWITCH) {
					relay.on = !relay.on;
				}
				syncIpController();
				ipController.save();
			}
		}
	}
	
	@Override
	public String toString() {
		return controllerId + ":" + url ;
	}
	
	private static boolean selected(int mask, int array) {
		return (array & mask) > 0;
	}
	
	private void syncIpController() {
		int p3 = 0;
		int p5 = 0;
		
		for (Relay relay : relays) {
			if (relay.on) {
				if (relay.relayId.isLowByte()) {
					p3 |= relay.relayId.bitMask();
				} else {
					p5 |= relay.relayId.bitMask();
				}
			}
		}
		
		ipController.p3 = p3;
		ipController.p5 = p5;
	}
	
	private void switchOff() {
		for (Relay relay : relays) {
			if (relay.switchType == RelaySwitchType.MUTEX_SWITCH) {
				relay.on = false;
			}
		}
	}
	
	private Relay getRelay(Relay r) {
		for (Relay relay : relays) {
			if (relay.equals(r)) {
				return relay;
			}
		}
		return null;
	}

}
