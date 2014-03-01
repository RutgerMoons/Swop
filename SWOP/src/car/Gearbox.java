package car;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Gearbox extends CarPart {
	
	private static ArrayList<String> possibleGearboxes = new ArrayList<>(Arrays.asList("6 speed manual", "5 speed automatic"));
	
	public Gearbox(String description) {
		this.setDescription(description);
	}
	
	public static void addPossibleGearbox(String type) {
		if (type == null) {
			throw new IllegalArgumentException();
		} 
		possibleGearboxes.add(type); 
	}
	
	public static Iterator<String> getPossibleGearbox() {
		return possibleGearboxes.iterator();		
	}
	
	@Override
	public void setDescription(String description) {
		if (description == null || !possibleGearboxes.contains(description)) {
			throw new IllegalArgumentException();
		} 
		else {
			super.setDescription(description);
		}
	}
	
}
