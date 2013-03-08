package controllers;

import controllers.CRUD;
import controllers.Check;
import controllers.Secure;
import controllers.CRUD.For;
import models.access.Role;
import play.mvc.With;

@With(Secure.class)
@CRUD.For(Role.class)
@Check("admin")
public class Roles extends CRUD {

}
