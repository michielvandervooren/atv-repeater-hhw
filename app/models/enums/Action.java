package models.enums;

public enum Action {

	SWITCH("Schakel kanaal"), RESET_NICAM("Reset Nicam");
	
	
	private String description;
	
	private Action(String desc) {
		this.description = desc;
	}
	
	@Override
	public String toString() {
		return description;
	}
}