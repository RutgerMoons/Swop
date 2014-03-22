package car;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Multimap;

/**
 * A class representing a catalogue consisting out of carmodels.
 * 
 */
public class CarModelCatalogue {

	// private HashMap<String, HashMap<Class<?>, CarPart>> data;
	private Map<String, CarModel> data;
	private CarPartCatalogue cat;

	/**
	 * Default constructor. When a carModelCatalogue is constructed, a
	 * carModelCatalogueFiller is created and the newly constructed
	 * carModelCatalogue is filled with the basic carModels thanks to the
	 * carModelCatalogueFiller.
	 */
	public CarModelCatalogue() {
		cat = new CarPartCatalogue();
		initializeCatalogue(new CarModelCatalogueFiller(cat).getInitialModels());
	}

	/**
	 * Returns an immutable Map of carmodels with their names.
	 */
	public Map<String, CarModel> getCatalogue() {
		ImmutableMap<String, CarModel> immutable = new ImmutableMap.Builder<String, CarModel>()
				.putAll(data).build();
		return immutable;
	}

	/**
	 * Method for initializing the catalogue. The given arrayList of models is
	 * included in the catalogue. This method is used by the
	 * carModelCatalogueFiller.
	 */
	public void initializeCatalogue(Set<CarModel> models) {
		data = new HashMap<String, CarModel>();
		for (CarModel model : models) {
			addModel(model);
		}
	}

	/**
	 * Add a new model to the catalogue.
	 * @param model
	 * 			The model you want to add.
	 * @throws IllegalArgumentException
	 * 			if model==null or not is valid
	 */
	public void addModel(CarModel model) {
		if (model == null || !isValidCarModel(model))
			throw new IllegalArgumentException();
		data.put(model.getDescription(), model);

	}

	/**
	 * Method for checking if the carModel is a valid model. It checks if every
	 * carpart of the model is valid or not. If not, it return false.
	 */
	private boolean isValidCarModel(CarModel model) {
		for (CarPart part : model.getCarParts().values()) {
			if (!cat.isValidCarPart(part)) {
				return false;
			}
		}
		return true;
	}

}
