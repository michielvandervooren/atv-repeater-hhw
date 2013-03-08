package models;

import java.util.List;

import javax.persistence.Transient;

import models.enums.Action;
import models.enums.ControllerId;
import play.libs.F;

public class ATVRepeaterModel {
	
	private static final ATVRepeaterModel instance = new ATVRepeaterModel();
	public static final F.EventStream<ATVRepeaterModel> event = new F.EventStream<ATVRepeaterModel>();
	
	transient RelayController audioController;

	transient RelayController videoController;
	
	transient Relay resetNicamRelay;
	
	public List<Channel> channels;
	
	public boolean resetting;
	
	private ATVRepeaterModel() {
		this.resetting = false;
	}
	
	public static ATVRepeaterModel get() {
		if (instance.audioController == null) {
			instance.init();
		}
		instance.channels = Channel.findAll();
		instance.resetNicamRelay = Relay.find("byAction", Action.RESET_NICAM).first();
		instance.audioController.sync();
		instance.videoController.sync();
		
		return instance;
	}

	public synchronized boolean switchRelay(Relay relay) {
		RelayController controller =  findController(relay.controllerId);
		boolean scheduleStop = false;
		if (resetting && relay.equals(resetNicamRelay)) {
			return scheduleStop;
		}
		controller.switchRelay(relay);

		//notify
		push();

		if (relay.equals(resetNicamRelay)) {
			resetting = true;
			scheduleStop = true;
		}
		return scheduleStop;
	}
	
	public synchronized void reverseSwitch(Relay relay) {
		RelayController controller =  findController(relay.controllerId);
		if (resetting  && relay.equals(resetNicamRelay)) {
			resetting = false;
			controller.switchRelay(relay);
			
			//notify
			push();
		}
	}
	
	public void push() {
		event.publish(this);
	}
	
	private void init() {
		audioController = RelayController.find("byControllerId", ControllerId.AUDIO).first();
		videoController = RelayController.find("byControllerId", ControllerId.VIDEO).first();

		audioController.connect();
		videoController.connect();
	}
	
	private static RelayController findController(ControllerId id) {
		return instance.audioController.controllerId == id ? instance.audioController : instance.videoController;
	}
	
}
