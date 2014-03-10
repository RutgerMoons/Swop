package car;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

/**
 * A class representing a catalogue consisting out of carmodels.
 *
 */
public class CarModelCatalogue {
	/**
	 * TODO : add functie maken
	 */
	private HashMap<String, HashMap<Class<?>, CarPart>> data;

	public CarModelCatalogue(){
		CarPartCatalogue cat = new CarPartCatalogue();
		initializeCatalogue(new CarModelCatalogueFiller(cat).getInitialModels());
	}

	/**
	 * Returns a deep clone of the catalogue of car models.
	 */
	public HashMap<String, HashMap<Class<?>, CarPart>> getCatalogue() {
		return clone(this.data);
	}

	/**
	 * Method for deep cloning a given HashMap. 
	 */
	public HashMap<String, HashMap<Class<?>,CarPart>> clone(HashMap<String, HashMap<Class<?>, CarPart>> data2){
		HashMap<String, HashMap<Class<?>,CarPart>> newMap = new HashMap<String,HashMap<Class<?>,CarPart>>();

		Set<Entry<String,HashMap<Class<?>,CarPart>>> set1 = data2.entrySet();
		
		for (Entry<String, HashMap<Class<?>,CarPart>> entry : set1){
			
			HashMap<Class<?>,CarPart> test = new HashMap<Class<?>,CarPart>();
			Collection<CarPart> listOfCarParts = entry.getValue().values();
			for(CarPart part : listOfCarParts){
				test.put(part.getClass(), part);
			}
			newMap.put(entry.getKey(),test);
		}
		return newMap;
	}
	

	public void initializeCatalogue(ArrayList<CarModel> models){
		data = new HashMap<String,HashMap<Class<?>,CarPart>>();
		for(CarModel model : models){
			data.put(model.getDescription(), model.getCarParts());
		}
	}

}
