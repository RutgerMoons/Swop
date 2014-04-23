package domain.car;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;

/**
 * A class representing a catalogue consisting of carmodels.
 * 
 */
public class CarModelCatalogue {

	// private HashMap<String, HashMap<Class<?>, CarPart>> data;
	private Map<String, CarModelSpecification> data;

	/**
	 * Default constructor. When a carModelCatalogue is constructed, a
	 * carModelCatalogueFiller is created and the newly constructed
	 * carModelCatalogue is filled with the basic carModels thanks to the
	 * carModelCatalogueFiller.
	 */
	public CarModelCatalogue() {
		data = new HashMap<String, CarModelSpecification>();
	}

	/**
	 * Returns an immutable Map of carmodels with their names.
	 */
	public Map<String, CarModelSpecification> getCatalogue() {
		return new ImmutableMap.Builder<String, CarModelSpecification>()
				.putAll(data).build();
	}

	/**
	 * Method for initializing the catalogue. The given arrayList of models is
	 * included in the catalogue. This method is used by the
	 * carModelCatalogueFiller.
	 */
	public void initializeCatalogue(Set<CarModelSpecification> models) {
		for (CarModelSpecification model : models) {
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
	public void addModel(CarModelSpecification model) {
		if (model == null)
			throw new IllegalArgumentException();
		data.put(model.getDescription(), model);

	}
}
