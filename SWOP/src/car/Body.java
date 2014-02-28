package car;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Body extends CarPart {
	
	private static ArrayList<String> possibleBodies = new ArrayList<>(Arrays.asList("break", "sedan"));
	
	public static void addPossibleBody(String type) {
		if (type == null) {
			throw new IllegalArgumentException();
		} 
		possibleBodies.add(type); 
	}
	
	public static Iterator<String> getPossibleAirco() {
		return possibleBodies.iterator();		
	}
	
	public Body(String description) {
		this.setDescription(description);
	}
	
	public void setDescription(String description) {
		if (description == null || !possibleBodies.contains(description)) {
			throw new IllegalArgumentException();
		} 
		else {
			super.setDescription(description);
		}
	}
	
	
	
	
}
