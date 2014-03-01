package car;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Color extends CarPart {

	private static ArrayList<String> possibleColors = new ArrayList<String>(Arrays.asList("red","blue","black","white"));
	
	public Color(String description){
		this.setDescription(description);
	}
	
	
	public static void addPossibleColor(String type){
		if (type == null){
			throw new IllegalArgumentException();
		}
		possibleColors.add(type);
	}
	
	public static Iterator<String> getPossibleColors(){
		return possibleColors.iterator();
	}
	
	@Override
	public void setDescription(String description) {
		if (description == null || !possibleColors.contains(description)) {
			throw new IllegalArgumentException();
		} 
		else {
			super.setDescription(description);
		}
	}
	
	
}
