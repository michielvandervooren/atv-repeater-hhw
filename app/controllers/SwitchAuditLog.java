package controllers;

import java.util.Date;

import models.SwitchAction;
import models.enums.RelaySwitchType;
import play.mvc.Before;
import play.mvc.Controller;

public class SwitchAuditLog extends Controller {

	@Before
	static void logAction() {
		SwitchAction action = new SwitchAction();
		action.userName = session.get("username");
		action.actionType = request.actionMethod;
		action.date = new Date();
		action.save();;
	}
	
}
