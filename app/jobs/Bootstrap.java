package jobs;

import java.util.Arrays;

import models.Channel;
import models.Color;
import models.Icon;
import models.Relay;
import models.RelayController;
import models.User;
import models.access.Permission;
import models.access.Role;
import models.enums.Action;
import models.enums.ControllerId;
import models.enums.RelayId;
import models.enums.RelaySwitchType;
import play.Logger;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
public class Bootstrap extends Job {

	int GRID = 5;
	int MINMAX = 6;
	int RESET = 9;
	int CAM = 10;
	
	public void doJob() {
		Permission adminPermission = null;
		Permission nicamResetPermission = null;
		Role adminRole = null;
		Role powerUserRole = null;
		Role userRole = null;
		if (Permission.count() == 0) {
			adminPermission = new Permission();
			adminPermission.name = "admin";
			adminPermission.save();
			Logger.info("Permission '%s' created", adminPermission);
			nicamResetPermission = new Permission();
			nicamResetPermission.name = "resetNicam";
			nicamResetPermission.save();
			Logger.info("Permission '%s' created", nicamResetPermission);
		}
		if (Role.count() == 0) {
			adminRole = new Role();
			adminRole.name = "Admin";
			adminRole.permissions = Arrays.asList(adminPermission, nicamResetPermission);
			adminRole.save();
			Logger.info("Role '%s' created", adminRole);
			powerUserRole = new Role();
			powerUserRole.name = "Super user";
			powerUserRole.permissions = Arrays.asList(nicamResetPermission);
			powerUserRole.save();
			Logger.info("Role '%s' created", powerUserRole);
			userRole = new Role();
			userRole.name = "User";
			userRole.save();
			Logger.info("Role '%s' created", userRole);
		}
		if (User.count() == 0) {
			User user = new User();
			user.username = "admin";
			user.password = "admin";
			user.firstName = "Administrator";
			user.roles = Arrays.asList(adminRole);
			user.save();
			Logger.info("User '%s' created", user);
		}
		if (RelayController.count() == 0) {
			RelayController relayController = new RelayController();
			relayController.controllerId = ControllerId.VIDEO;
			relayController.url = Play.configuration.getProperty("video.relais.url");
			relayController.userName = Play.configuration.getProperty("video.relais.username");
			relayController.password = Play.configuration.getProperty("video.relais.password");
			relayController.save();
			Logger.info("RelayController '%s' created", relayController);
			
			relayController = new RelayController();
			relayController.controllerId = ControllerId.AUDIO;
			relayController.url = Play.configuration.getProperty("audio.relais.url");
			relayController.userName = Play.configuration.getProperty("audio.relais.username");
			relayController.password = Play.configuration.getProperty("audio.relais.password");
			relayController.save();
			Logger.info("RelayController '%s' created", relayController);
		}
		if (Relay.count() == 0) {
			Relay relay = null;
			for (int i = 0; i < RelayId.values().length; i++) {
				relay = new Relay();
				relay.controllerId = ControllerId.VIDEO;
				relay.action = Action.SWITCH;
				relay.relayId = RelayId.values()[i];
				if (i == MINMAX || i == RESET) {
					relay.switchType = RelaySwitchType.TOGGLE_SWITCH;
				} else {
					relay.switchType = RelaySwitchType.MUTEX_SWITCH;
				}
				if (i == RESET) {
					relay.permission = nicamResetPermission;
					relay.action = Action.RESET_NICAM;
				}
				relay.enabled = true;
				relay.save();
				Logger.info("Relay '%s' created", relay);
			}
			for (int i = 8; i < RelayId.values().length; i++) {
				relay = new Relay();
				relay.controllerId = ControllerId.AUDIO;
				relay.relayId = RelayId.values()[i];
				relay.switchType = RelaySwitchType.TOGGLE_SWITCH;
				relay.enabled = true;
				relay.action = Action.SWITCH;
				relay.save();
				Logger.info("Relay '%s' created", relay);
			}
		}
		if (Color.count() == 0) {
			for (models.enums.Color c : models.enums.Color.values()) {
				Color color = new Color();
				color.description = c.desc;
				color.className = c.className;
				color.save();
				Logger.info("Color '%s' created", color);
			}
		}
		if (Icon.count() == 0) {
			for (models.enums.Icon i : models.enums.Icon.values()) {
				Icon icon = new Icon();
				icon.description = i.desc;
				icon.className = i.className;
				icon.save();
				Logger.info("Icon '%s' created", icon);
			}
		}
		if (Channel.count() == 0) {
			Channel channel = null;
			RelayId[] relays = RelayId.values();
			int audioPortIdx = 8;
			int index = 1;
			for (int i = 0; i < relays.length; i++) {
				channel = new Channel();

				if (i == GRID) {
					channel.icon = getIcon(models.enums.Icon.GRID_VIEW);
					channel.name = "2 x 2";
					channel.color = getColor(models.enums.Color.RED);
				} else if (i == MINMAX) {
					channel.icon = getIcon(models.enums.Icon.NEW_TAB_2);
					channel.name = "Fullscreen";
					channel.color = getColor(models.enums.Color.RED);
				} else if (i == RESET) {
					channel.icon = getIcon(models.enums.Icon.LIGHTNING);
					channel.name = "Reset nicam";
					channel.color = getColor(models.enums.Color.ORANGE_DARK);
				} else if (i == CAM) {
					channel.icon = getIcon(models.enums.Icon.CAMERA_2);
					channel.name = "Webcam";
					channel.color = getColor(models.enums.Color.RED);
				} else if (i > 3 && i != GRID && i != MINMAX && i != RESET && i != CAM) {
					channel.audioRelay = Relay.find("byRelayIdAndControllerId", relays[audioPortIdx++], ControllerId.AUDIO).first();
					channel.name = String.valueOf(index++);
					channel.color = getColor(models.enums.Color.BLUE_DARK);
				} else {
					channel.name = String.valueOf(index++);
					channel.color = getColor(models.enums.Color.GREEN_LIGHT);
				}
				channel.description = String.format("Kanaal %d omschrijving", (i + 1));
				channel.videoRelay = Relay.find("byRelayIdAndControllerId", relays[i], ControllerId.VIDEO).first();
				channel.save();
				Logger.info("Channel '%s' created", channel);
			}
		}
	}
	
	private Icon getIcon(models.enums.Icon icon) {
		return Icon.find("byDescription", icon.desc).first();
	}
	
	private Color getColor(models.enums.Color color) {
		return Color.find("byDescription", color.desc).first();
	}
	
}