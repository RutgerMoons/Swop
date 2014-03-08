package car;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	private HashMap<String, List<CarPart>> data;

	public CarModelCatalogue(){
		CarPartCatalogue cat = new CarPartCatalogue();
		initializeCatalogue(new CarModelCatalogueFiller(cat).getInitialModels());
	}

	/**
	 * Returns a deep clone of the catalogue of car models.
	 */
	public HashMap<String, List<CarPart>> getCatalogue() {
		return clone(this.data);
	}

	/**
	 * Method for deep cloning a given HashMap. 
	 */
	public HashMap<String, List<CarPart>> clone(HashMap<String, List<CarPart>> map){
		HashMap<String, List<CarPart>> newMap = new HashMap<String,List<CarPart>>();
		Set<Entry<String,List<CarPart>>> set1 = map.entrySet();
		for (Entry<String, List<CarPart>> entry : set1){
			List<CarPart> test = new ArrayList<CarPart>();
			for(CarPart part : entry.getValue()){
				test.add(part);
			}
			newMap.put(entry.getKey(),test);
		}
		return newMap;
	}
	
	public void initializeCatalogue(ArrayList<CarModel> models){
		data = new HashMap<String,List<CarPart>>();
		for(CarModel model : models){
			data.put(model.getDescription(), model.getCarParts());
		}
	}

}
