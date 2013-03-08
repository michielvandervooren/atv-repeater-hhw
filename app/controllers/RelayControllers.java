package controllers;

import models.RelayController;
import play.mvc.With;

@With(Secure.class)
@CRUD.For(RelayController.class)
@Check("admin")
public class RelayControllers extends CRUD {

}
