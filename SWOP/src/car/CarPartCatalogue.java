package car;

import java.util.HashMap;
import java.util.List;

/**
 * this class will be used to validate CarParts
 * this class will be initialized upon creation
 */
public class CarPartCatalogue {
	
	private HashMap<Class<?>, List<CarPart>> carPartCatalogue;
	
	public CarPartCatalogue() {
		this.carPartCatalogue = new HashMap<>();
		CarPartCatalogueFiller filler = new CarPartCatalogueFiller();
		filler.initializeCarParts();
	}
	
	/**
	 * checks whether a given CarPart is valid (three cases):
	 * 		CarPart is null -> false
	 * 		CarPart has incorrect description -> false
	 * 		CarPart has correct description -> true
	 * 
	 * note: the description of CarPart cannot equal null
	 */
	public boolean isValidCarPart(CarPart carPart) {
		return carPart != null && 
				carPartCatalogue.get(carPart.getClass()).contains(carPart.getType());
	}
	
	/**
	 * This methods adds an entry for a carPart if
	 * 		the class is valid
	 * 		the list is not null
	 */
	protected void addListForCarPart(Class<?> key, List<CarPart> value) {
		carPartCatalogue.put(key, value);
	}
	
	
}
