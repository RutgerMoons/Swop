package car;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Seat extends CarPart {

	private static ArrayList<String> possibleSeats = new ArrayList<>(Arrays.asList("leather black", "leather white", "vinyl grey"));

	public Seat(String description) {
		this.setDescription(description);
	}

	public static void addPossibleSeat(String type) {
		if (type == null) {
			throw new IllegalArgumentException();
		} 
		possibleSeats.add(type); 
	}

	public static Iterator<String> getPossibleSeats() {
		return possibleSeats.iterator();		
	}

	@Override
	public void setDescription(String description) {
		if (description == null || !possibleSeats.contains(description)) {
			throw new IllegalArgumentException();
		} 
		else {
			super.setDescription(description);
		}
	}

}
