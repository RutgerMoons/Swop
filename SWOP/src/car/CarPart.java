package car;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class CarPart {
	
	private ArrayList<String> possibleTypes;
	private String description;
	
	public CarPart() {
		this.possibleTypes = new ArrayList<String>();
	}
	
	public CarPart(ArrayList<String> types) {
		this.possibleTypes = types;
	}
		
	public void setDescription(String type){
		if(type == null || !possibleTypes.contains(type)) {
			throw new IllegalArgumentException(); 
		} else {
			this.description = type; 
		}
	}
	
	public String getDescription(){
		return this.description;
	}
	
	/*
	 * @return iterator<String> with all the types
	 * 
	 *  Iterator is niet manipuleerbaar en zorgt dus voor geen onverwachte dingen :)
	 */
	public Iterator<String> getTypes() {
		return possibleTypes.iterator();
	}
	 
	protected void setTypes(ArrayList<String> types){
		this.possibleTypes = types;
	}
}
