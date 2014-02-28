package car;

import java.util.ArrayList;
import java.util.Arrays;

public class Airco extends CarPart {
	
	private static ArrayList<String> possibleAircos = new ArrayList<>(Arrays.asList("manual", "automatic climate control"));
	
	public Airco() {
		this.setTypes(possibleAircos);
	}
	
	public Airco(ArrayList<String> types) {
		super(types);
	}
	
	public static void addPossibleAirco(String type) {
		if (type == null) {
			throw new IllegalArgumentException();
		} 
		possibleAircos.add(type); 
	}
	  
}
