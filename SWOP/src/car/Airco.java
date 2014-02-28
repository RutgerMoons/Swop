package car;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Airco extends CarPart {
	
	private static ArrayList<String> possibleAircos = new ArrayList<>(Arrays.asList("manual", "automatic climate control"));
	
	public static void addPossibleAirco(String type) {
		if (type == null) {
			throw new IllegalArgumentException();
		} 
		possibleAircos.add(type); 
	}
	
	public static Iterator<String> getPossibleAirco() {
		return possibleAircos.iterator();		
	}
	
	public Airco(String description) {
		this.setDescription(description);
	}
	
	public void setDescription(String description) {
		if (description == null || !possibleAircos.contains(description)) {
			throw new IllegalArgumentException();
		} 
		else {
			super.setDescription(description);
		}
	}
	   
}
  