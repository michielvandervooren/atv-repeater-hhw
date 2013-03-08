package models.enums;

public enum ControllerId {
	VIDEO("Video controller"), AUDIO("Audio controller");
	
	private String description;
	
	private ControllerId(String desc) {
		description = desc;
	}
	
	@Override
	public String toString() {
		return description;
	}
}
