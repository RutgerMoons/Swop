package domain.car;

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
	private Map<String, CarModelTemplate> data;

	/**
	 * Default constructor. When a carModelCatalogue is constructed, a
	 * carModelCatalogueFiller is created and the newly constructed
	 * carModelCatalogue is filled with the basic carModels thanks to the
	 * carModelCatalogueFiller.
	 */
	public CarModelCatalogue() {
		data = new HashMap<String, CarModelTemplate>();
	}

	/**
	 * Returns an immutable Map of carmodels with their names.
	 */
	public Map<String, CarModelTemplate> getCatalogue() {
		return new ImmutableMap.Builder<String, CarModelTemplate>()
				.putAll(data).build();
	}

	/**
	 * Method for initializing the catalogue. The given arrayList of models is
	 * included in the catalogue. This method is used by the
	 * carModelCatalogueFiller.
	 */
	public void initializeCatalogue(Set<CarModelTemplate> models) {
		for (CarModelTemplate model : models) {
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
	public void addModel(CarModelTemplate model) {
		if (model == null)
			throw new IllegalArgumentException();
		data.put(model.getDescription(), model);

	}
}
