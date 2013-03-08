package models.daeip2controller;

import java.util.HashMap;
import java.util.Map;

/**
 * Models a DAE (Denkovi Assembly Electronics, www.denkovi.com) IP2 Controller by its hostname and user credentials.
 * For each config instance a single {@link DaeIp2ControllerModel} exists. A model can be fetched either by it's config instance,
 * or by the String reference associated with that config instance. 
 * @author voorenmi
 *
 */
public class DaeIp2ControllerConfig {

	private static Map<Object, DaeIp2ControllerConfig> controllers = new HashMap<Object, DaeIp2ControllerConfig>();

	public String hostname;
	public String username;
	public String password;
	
	private DaeIp2ControllerConfig(String hostname, String username, String password) {
		this.hostname = hostname;
		this.username = username;
		this.password = password;
	}
	
	public static DaeIp2ControllerConfig create(Object ref, String hostname, String username, String password) {
		controllers.put(ref, new DaeIp2ControllerConfig(hostname, username, password));
		return get(ref);
	}
	
	public static DaeIp2ControllerConfig get(Object ref) {
		return controllers.get(ref);
	}
}
