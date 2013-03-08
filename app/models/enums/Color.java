package models.enums;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public enum Color {

	BLUE("blauw", "bg-color-blue"),
	BLUE_LIGHT("lichtblauw", "bg-color-blueLight"),
	BLUE_DARK("donkerblauw", "bg-color-blueDark"),
	GREEN("groen", "bg-color-green"),
	GREEN_LIGHT("lichtgroen", "bg-color-greenLight"),
	GREEN_DARK("donkergroen","bg-color-greenDark"),
	RED("rood", "bg-color-red"),
	YELLOW("geel", "bg-color-yellow"),
	ORANGE("oranje", "bg-color-orange"),
	ORANGE_DARK("donkeroranje", "bg-color-orangeDark"),
	PINK("roze", "bg-color-pink"),
	PINK_DARK("donkerroze", "bg-color-pinkDark"),
	PURPLE("paars", "bg-color-purple"),
	DARKEN("donker", "bg-color-darken"),
	LIGHTEN("licht", "bg-color-lighten"),
	WHITE("wit", "bg-color-white"),
	GREY_DARK("donkergrijs", "bg-color-grayDark");
	
	public String desc;
	public String className;
	
	private Color(String desc, String className) {
		this.desc = desc;
		this.className = className;
	}
	
	@Override
	public String toString() {
		return desc;
	}
	
}
