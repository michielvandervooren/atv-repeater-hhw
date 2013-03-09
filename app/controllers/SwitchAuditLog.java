package controllers;

import java.util.Date;

import controllers.Secure.Security;

import models.Relay;
import models.SwitchAction;
import models.enums.Action;
import models.enums.ControllerId;
import models.enums.RelaySwitchType;
import play.mvc.Before;
import play.mvc.Controller;

public class SwitchAuditLog extends Controller {

	@Before(only="switchRelay")
	static void logAction() {
		SwitchAction action = new SwitchAction();
		Long id = (Long) request.args.get("id");
		Relay r = Relay.findById(id);
		if (r != null) {
			action.userName = Security.connected();
			action.actionType = getAction(r);
			action.date = new Date();
			action.save();
		}
	}
	
	private static String getAction(Relay r) {
		String action = "";
		ControllerId cId = r.controllerId;
		if (cId == ControllerId.AUDIO) {
			action = "schakelt audio";
		}
		else {
			if(r.action == Action.RESET_NICAM) {
				action = "reset nicam";
			} else {
				action = "schakelt video";
			}
		}
		
		return action;
	}
	
}
