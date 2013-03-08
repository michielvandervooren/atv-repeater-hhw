package controllers;

import models.access.User;
import play.Logger;
import play.db.Model;
import play.exceptions.TemplateNotFoundException;
import play.mvc.Before;
import play.mvc.Router;
import play.mvc.Router.Route;
import play.mvc.With;
import controllers.CRUD.ObjectType;
import controllers.Secure.Security;

@With(Secure.class)
@Check("admin")
public class Users extends CRUD {
	
	@Before(only="delete")
	public static void preventCurrentUserFromDeletion() throws Exception {
		User object = User.findByLogin(Security.connected());
		if (object != null && String.valueOf(object.id).equals(params.get("id"))) {
	        ObjectType type = ObjectType.get(getControllerClass());
			flash.put("error", "Kan huidige gebruiker niet verwijderen");
            render("CRUD/show.html", type, object);
		}
	}
}
