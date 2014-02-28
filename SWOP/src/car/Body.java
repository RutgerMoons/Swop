package car;

import java.util.ArrayList;
import java.util.Arrays;

public class Body extends CarPart {
	
	private static ArrayList<String> possibleBodies = new ArrayList<>(Arrays.asList("break", "sedan"));
	
	public Body() {
		this.setTypes(possibleBodies);
	}
	
	public Body(ArrayList<String> types) {
		super(types);
	}
	
	public static void addPossibleBody(String type) {
		if (type == null) {
			throw new IllegalArgumentException();
		}
		possibleBodies.add(type);
	}
	
	
	
	
}
