package domain.car;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;

/**
 * A class representing a catalogue consisting of CarModelSpecifications.
 * 
 */
public class CarModelCatalogue {

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
	 * Returns an immutable Map of CarModelSpecifications with their names.
	 */
	public Map<String, CarModelSpecification> getCatalogue() {
		return new ImmutableMap.Builder<String, CarModelSpecification>()
				.putAll(data).build();
	}

	/**
	 * Method for initializing the catalogue. The given arrayList of CarModelSpecifications is
	 * included in the catalogue.
	 */
	public void initializeCatalogue(Set<CarModelSpecification> models) {
		for (CarModelSpecification model : models) {
			addModel(model);
		}
	}

	/**
	 * Add a new specification to the catalogue.
	 * 
	 * @param specification
	 *            The specification you want to add.
	 * @throws IllegalArgumentException
	 *             if specification==null
	 */
	public void addModel(CarModelSpecification specification) {
		if (specification == null)
			throw new IllegalArgumentException();
		data.put(specification.getDescription(), specification);

	}
}
