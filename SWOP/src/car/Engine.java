package car;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Engine extends CarPart {

	private static ArrayList<String> possibleEngines = new ArrayList<>(Arrays.asList("standard 2l 4 cilinders", "performance 2.5l 6 cilinders"));

	public Engine(String description) {
		this.setDescription(description);
	}


	public static void addPossibleEngine(String type) {
		if (type == null) {
			throw new IllegalArgumentException();
		} 
		possibleEngines.add(type); 
	}

	public static Iterator<String> getPossibleEngines() {
		return possibleEngines.iterator();		
	}


	@Override
	public void setDescription(String description) {
		if (description == null || !possibleEngines.contains(description)) {
			throw new IllegalArgumentException();
		} 
		else {
			super.setDescription(description);
		}
	}

}
