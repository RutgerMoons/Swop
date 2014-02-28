package car;

import java.util.ArrayList;

public class Airco extends CarPart {
	
	public Airco() {
		ArrayList<String> possibleAircos = new ArrayList<String> ();
		possibleAircos.add("manual");
		possibleAircos.add("automatic climate control");
		this.setTypes(possibleAircos);
	}
	
	public Airco(ArrayList<String> types) {
		super(types);
	}
	 
}
