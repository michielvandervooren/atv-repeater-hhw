package controllers.access;

import play.Logger;
import play.db.jpa.GenericModel.JPAQuery;
import play.utils.Java;
import models.access.User;
import controllers.Secure.Security;

public class AccessController extends Security {
	
	static boolean authenticate(String userId, String password) {
		User user = User.findByLogin(userId);
		
		if (user != null) {
			return user.authenticate(password);
		}
		
		return false;
	}
	
	static String connected() {
		return session.get("username");
	}
	
	static boolean check(String profile) throws Throwable {
		User user = User.findByLogin(connected());
		if (user != null) {
			return user.hasPermission(profile);
		}
		
		return false;
	}
	
}
