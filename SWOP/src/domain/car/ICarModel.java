package domain.car;

import java.util.Map;

public interface ICarModel {

	/**
	 * This method returns an Immutable Map including all the car
	 * parts.
	 */
	public Map<CarPartType, CarPart> getCarParts();
	
}
