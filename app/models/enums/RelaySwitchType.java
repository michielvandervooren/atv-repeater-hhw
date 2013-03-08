package models.enums;

public enum RelaySwitchType {

	MUTEX_SWITCH("Exclusief schakelen"),
	TOGGLE_SWITCH("Aan/uit schakelen");	
	
	private String action;
	
	private RelaySwitchType(String action) {
		this.action = action;
	}
	
	@Override
	public String toString() {
		return action;
	}
	
}
