package jobs;

import models.ATVRepeaterModel;
import models.Relay;
import play.jobs.Job;

public class StopResetNicam extends Job {

	private Relay relay;
	
	public StopResetNicam(Relay relay) {
		this.relay = relay;
	}
	
	public void doJob() {
		ATVRepeaterModel.get().reverseSwitch(relay);
	}
}
