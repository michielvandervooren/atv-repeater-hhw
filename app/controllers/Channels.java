package controllers;

import models.ATVRepeaterModel;
import models.Channel;
import play.mvc.After;
import play.mvc.With;

@With(Secure.class)
@CRUD.For(Channel.class)
@Check("admin")
public class Channels extends CRUD {

	@After(only="save")
	public static void pushChange() {
		ATVRepeaterModel.get().push();
	}
}
