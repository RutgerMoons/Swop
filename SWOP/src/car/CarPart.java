package car;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class CarPart {
	
	private ArrayList<String> types;
	
	public CarPart() {
		this.types = new ArrayList<String>();
	}
	
	public CarPart(ArrayList<String> types) {
		this.types = types;
	}
	
	
	/*
	 * @return iterator<String> with all the types
	 * 
	 *  Iterator is niet manipuleerbaar en zorgt dus voor geen onverwachte dingen :)
	 */
	public Iterator<String> getTypes() {
		return types.iterator();
	}
	 
}
