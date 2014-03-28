package car;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;

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
	public CarModelCatalogue(CarPartCatalogue cat) {
		this.cat = cat;
		data = new HashMap<String, CarModel>();
	}

	/**
	 * Returns an immutable Map of carmodels with their names.
	 */
	public Map<String, CarModel> getCatalogue() {
		return new ImmutableMap.Builder<String, CarModel>()
				.putAll(data).build();
	}

	/**
	 * Method for initializing the catalogue. The given arrayList of models is
	 * included in the catalogue. This method is used by the
	 * carModelCatalogueFiller.
	 */
	public void initializeCatalogue(Set<CarModel> models) {
		for (CarModel model : models) {
			addModel(model);
		}
	}

	/**
	 * Add a new model to the catalogue.
	 * 
	 * @param model
	 *            The model you want to add.
	 * @throws IllegalArgumentException
	 *             if model==null or not is valid
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
