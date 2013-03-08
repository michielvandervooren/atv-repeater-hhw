package models.enums;

public enum RelayId {
	P31,P32,P33,P34,
	P35,P36,P37,P38,
	P51,P52,P53,P54,
	P55,P56,P57,P58;
	
	public String toString() {
		return String.format("%s [%s]", name().substring(0, 2), name().substring(2));
	}
	
	public boolean isLowByte() {
		return name().substring(1,2).equals("3");
	}
	
	public int bitMask() {
		int bit = Integer.parseInt(name().substring(2));
		return (int) Math.pow(2, (bit - 1));
	}
}
