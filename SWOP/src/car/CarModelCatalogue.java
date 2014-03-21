package car;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Multimap;

/**
 * A class representing a catalogue consisting out of carmodels.
 * 
 */
public class CarModelCatalogue {

	// private HashMap<String, HashMap<Class<?>, CarPart>> data;
	private Map<String, CarModel> data;

	/**
	 * Default constructor. When a carModelCatalogue is constructed, a
	 * carModelCatalogueFiller is created and the newly constructed
	 * carModelCatalogue is filled with the basic carModels thanks to the
	 * carModelCatalogueFiller.
	 */
	public CarModelCatalogue() {
		CarPartCatalogue cat = new CarPartCatalogue();
		initializeCatalogue(new CarModelCatalogueFiller(cat).getInitialModels());
	}

	/**
	 * Returns a deep clone of the catalogue of car models.
	 */
	public Map<String, CarModel> getCatalogue() {
		return data;
	}

	/**
	 * Method for deep cloning a given HashMap.
	 */
	/*
	public HashMap<String, HashMap<Class<?>, CarPart>> clone(
			HashMap<String, HashMap<Class<?>, CarPart>> data2) {
		HashMap<String, HashMap<Class<?>, CarPart>> newMap = new HashMap<String, HashMap<Class<?>, CarPart>>();

		Set<Entry<String, HashMap<Class<?>, CarPart>>> set1 = data2.entrySet();

		for (Entry<String, HashMap<Class<?>, CarPart>> entry : set1) {

			HashMap<Class<?>, CarPart> test = new HashMap<Class<?>, CarPart>();
			Collection<CarPart> listOfCarParts = entry.getValue().values();
			for (CarPart part : listOfCarParts) {
				test.put(part.getClass(), part);
			}
			newMap.put(entry.getKey(), test);
		}
		return newMap;
	}*/

	/**
	 * Method for initializing the catalogue. The given arrayList of models is
	 * included in the catalogue. This method is used by the
	 * carModelCatalogueFiller.
	 */
	public void initializeCatalogue(Set<CarModel> models) {
		data = new HashMap<String, CarModel>();
		for (CarModel model : models) {
			data.put(model.getDescription(), model);
		}
	}

}
