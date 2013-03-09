package controllers;

import static play.libs.F.Matcher.ClassOf;
import controllers.Secure.Security;
import jobs.StopResetNicam;
import models.ATVRepeaterModel;
import models.Relay;
import models.User;
import models.enums.Color;
import models.enums.Icon;
import play.Logger;
import play.Play;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.WebSocketController;
import play.mvc.With;

@With({Secure.class})
public class Application extends Controller {

	/**
	 * Render single page html
	 */
    public static void index() {
    	User user = currentUser();
    	render(user);
    }
    
    /**
     * Render JSON representatie van het repeatermodel
     */
    public static void show() {
    	renderJSON(ATVRepeaterModel.get());
    }
    
    /**
     * Muteer repeatermodel: selecteer een relais
     * Render JSON representatie van het repeatermodel
     * @param id het id van het geselecteerde relais
     */
    public static void switchRelay(long id) {
    	ATVRepeaterModel model = ATVRepeaterModel.get();
    	Relay relay = Relay.findById(id);
    	notFoundIfNull(relay);
    	if (relay.permission != null && !currentUser().hasPermission(relay.permission.name)) {
    		forbidden();
    	}
    	if (relay.enabled) {
    		if (model.switchRelay(relay)) {
    			new StopResetNicam(relay).in(Play.configuration.getProperty("nicam.reset.wait", "5s"));
    		}
    	}
    	renderJSON(model);
    }
    
    
    public static class WebSocket extends WebSocketController {
    	
    	@SuppressWarnings("unused")
    	public static void listen() {
    		Logger.info("Listening on socket");
    		while(inbound.isOpen()) {
    			F.Either<Http.WebSocketEvent, ATVRepeaterModel> e = await(F.Promise.waitEither(
    					inbound.nextEvent(),
    					ATVRepeaterModel.get().event.nextEvent()
    		         ));
    			// Case: The socket has been closed
    			for (Http.WebSocketClose closed : Http.WebSocketEvent.SocketClosed.match(e._1)) {
        			Logger.debug("socket closing");
    				disconnect();
    			}
    			// Case: ATVRepeaterModel published - send the value to the client.
    			for (ATVRepeaterModel model : ClassOf(ATVRepeaterModel.class).match(e._2)) {
        			Logger.debug("broadcasting model");
    				outbound.sendJson(model);
    			}
    		}
    	}
	}
    
    static User currentUser() {
    	String username = Security.connected();
    	if (username == null) {
    		return null;
    	}
    	return (User) User.findByLogin(username);
    }

}