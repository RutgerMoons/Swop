package domain.vehicle.catalogue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import domain.vehicle.VehicleSpecification;

/**
 * A class representing a catalogue consisting of VehicleSpecifications.
 * 
 */
public class VehicleSpecificationCatalogue {

	private Map<String, VehicleSpecification> data;

	/**
	 * Create a new VehicleSpecificationCatalogue.
	 */
	public VehicleSpecificationCatalogue() {
		data = new HashMap<String, VehicleSpecification>();
	}

	/**
	 * Get the catalogue consisting of VehicleSpecifications.
	 */
	public Map<String, VehicleSpecification> getCatalogue() {
		return Collections.unmodifiableMap(data);
	}

	/**
	 * Method for initializing the catalogue. The given arrayList of VehicleSpecifications is
	 * included in the catalogue.
	 */
	public void initializeCatalogue(Set<VehicleSpecification> models) {
		for (VehicleSpecification model : models) {
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
	public void addModel(VehicleSpecification specification) {
		if (specification == null)
			throw new IllegalArgumentException();
		data.put(specification.getDescription(), specification);

	}
}
