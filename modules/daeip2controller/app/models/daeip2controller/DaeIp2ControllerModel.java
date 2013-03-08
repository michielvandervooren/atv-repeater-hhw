package models.daeip2controller;

import java.util.HashMap;
import java.util.Map;

import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.mvc.Http;

/**
 * Models the single DaeIp2ControllerModel for a given DaeIp2ControllerConfig descriptor
 * @author voorenmi
 *
 */
public class DaeIp2ControllerModel {

	public DaeIp2ControllerConfig config;
	public int p3;
	public int p5;
	
	private final static String GET_PATH = "/ioreg.js";
	private final static String SET_PATH = "/iochange.cgi";
	
	/**
	 * Get the model instance for the given controller
	 * @return the model instance
	 */
	public static DaeIp2ControllerModel find(DaeIp2ControllerConfig config) {
		if (config == null || config.hostname == null) return null;
		HttpResponse resp = WS.url(toGetUrl(config.hostname)).authenticate(config.username, config.password).get();
		DaeIp2ControllerModel result = null;
		if (resp.getStatus().intValue() == Http.StatusCode.OK) {
			result = new DaeIp2ControllerModel();
			result.config = config;
			parseResult(resp.getString(), result);
		}
		
		return result;
	}
	
	/**
	 * Gets the model instance for the given reference
	 * @param controllerRef the string reference as used for creating the controller config.
	 * @return the model instances
	 */
	public static DaeIp2ControllerModel findByRef(Object controllerRef) {
		if (controllerRef == null) return null;
		return find(DaeIp2ControllerConfig.get(controllerRef));
	}
	
	public boolean save() {
		//localhost:9000/iochange.cgi?ref=re-done&01=0E&02=1C
		HttpResponse resp = WS.url(toSetUrl(config.hostname)).authenticate(config.username, config.password).params(parameters()).get();
		if (resp.getStatus().intValue() == Http.StatusCode.OK) {
			return true;
		}
		return false;
	}
	
	private Map<String, Object> parameters() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ref", "re-done");
		params.put("01", String.format("%02X", this.p3));
		params.put("02", String.format("%02X", this.p5));
		
		return params;
	}
	
	private static String toGetUrl(String hostName) {
		return hostName + GET_PATH;
	}
	
	private static String toSetUrl(String hostName) {
		return hostName + SET_PATH;
	}	
	
	private static void parseResult(String response, DaeIp2ControllerModel model) {
		//"var IO=new Array (0x%s,0x%s,0x00,0x001E,0x001D,0x001B,0x0018,0x0018,0x0016,0x0018,0x001b)"
		final int p3 = Integer.parseInt(response.substring(20, 22), 16);
		final int p5 = Integer.parseInt(response.substring(25, 27), 16);
		model.p3 = p3;
		model.p5 = p5;
	}
}
