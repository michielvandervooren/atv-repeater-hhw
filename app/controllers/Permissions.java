package controllers;

import controllers.CRUD;
import controllers.Check;
import controllers.Secure;
import controllers.CRUD.For;
import models.access.Permission;
import play.mvc.With;

@With(Secure.class)
@CRUD.For(Permission.class)
@Check("admin")
public class Permissions extends CRUD {

}
