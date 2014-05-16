package domain.vehicle.catalogue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import domain.vehicle.VehicleSpecification;

/**
 * A class representing a catalogue consisting of VehicleSpecifications.
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
	 * Get the VehicleSpecificationCatalogue consisting of VehicleSpecifications.
	 */
	public Map<String, VehicleSpecification> getCatalogue() {
		return Collections.unmodifiableMap(data);
	}

	/**
	 * Method for initializing the VehicleSpecificationCatalogue. 
	 * 
	 * @param	models
	 * 			The given set of VehicleSpecifications is included in the VehicleSpecificationCatalogue
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the parameter is null
	 */
	public void initializeCatalogue(Set<VehicleSpecification> models) {
		if(models==null){
			throw new IllegalArgumentException();
		}
		for (VehicleSpecification model : models) {
			addModel(model);
		}
	}

	/**
	 * Add a new specification to the VehicleSpecificationCatalogue.
	 * 
	 * @param 	specification
	 *			The specification you want to add.
	 * @throws 	IllegalArgumentException
	 *			Thrown when the specification is null
	 */
	public void addModel(VehicleSpecification specification) {
		if (specification == null)
			throw new IllegalArgumentException();
		data.put(specification.getDescription(), specification);

	}
}
