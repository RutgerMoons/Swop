package car;

import java.util.Map;

public interface ICarModel {

	/**
	 * This method returns an Immutable Map including all the car
	 * parts.
	 */
	public Map<CarPartType, CarPart> getCarParts();
	
	/**
	 * Returns the description/name of the model.
	 */
	public String getDescription();
}
