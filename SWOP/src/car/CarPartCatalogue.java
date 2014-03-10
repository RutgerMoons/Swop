package car;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

/**
 * this class will be used to validate CarParts
 * this class will be initialized upon creation
 */
public class CarPartCatalogue {

	private HashMap<Class<?>, List<CarPart>> carPartCatalogue;

	public CarPartCatalogue() {
		this.carPartCatalogue = new HashMap<>();
		CarPartCatalogueFiller filler = new CarPartCatalogueFiller(this);
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
		return carPart != null && //carPartCatalogue.get(carPart.getClass()).contains(carPart.getDescription());
				//} 
				carPartCatalogue.get(carPart.getClass()).contains(carPart);
	}

	/**
	 * This methods adds an entry for a carPart if
	 * 		the class is valid
	 * 		the list is not null
	 */
	public void addListForCarPart(Class<?> key, List<CarPart> value) throws IllegalArgumentException {
		if (key == null) {
			throw new IllegalArgumentException(); 
		}
		else {
			carPartCatalogue.put(key, value);
		}
	}

	/**
	 * Returns a deep copy clone of the carPartCatalogue.
	 */
	public HashMap<Class<?>, List<CarPart>> getMap() {
		return clone(carPartCatalogue);
	}

	/**
	 * Method for making a deep copy of a certain hashmap of type <Class<?>, List<CarPart>>.
	 */
	private HashMap<Class<?>, List<CarPart>> clone(HashMap<Class<?>, List<CarPart>> carPartCatalogue2) {
		HashMap<Class<?>, List<CarPart>> newMap = new HashMap<Class<?>, List<CarPart>>();
		
		Set<Entry<Class<?>, List<CarPart>>> set1 = carPartCatalogue2.entrySet();
		
		for (Entry<Class<?>, List<CarPart>> entry : set1){
			List<CarPart> test = new ArrayList<CarPart>();
			for(CarPart part :entry.getValue()){
				test.add(part);
			}
			newMap.put(entry.getKey(),test);
		}
		return newMap;
	} 

}
