package car;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Wheel extends CarPart {

	private static ArrayList<String> possibleWheels = new ArrayList<>(Arrays.asList("comfort", "sports (low profile)"));


	public Wheel(String description) {
		this.setDescription(description);
	}

	public static void addPossibleWheel(String type) {
		if (type == null) {
			throw new IllegalArgumentException();
		} 
		possibleWheels.add(type); 
	}

	public static Iterator<String> getPossibleWheel() {
		return possibleWheels.iterator();		
	}

	@Override
	public void setDescription(String description) {
		if (description == null || !possibleWheels.contains(description)) {
			throw new IllegalArgumentException();
		} 
		else {
			super.setDescription(description);
		}
	}

}
