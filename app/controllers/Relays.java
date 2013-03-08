package controllers;

import models.ATVRepeaterModel;
import models.Relay;
import play.mvc.After;
import play.mvc.With;

@With(Secure.class)
@CRUD.For(Relay.class)
@Check("admin")
public class Relays extends CRUD {

	@After(only="save")
	public static void pushChange() {
		ATVRepeaterModel.get().push();
	}
}
