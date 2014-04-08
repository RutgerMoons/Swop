package domain.car;

import java.util.Map;

import domain.exception.AlreadyInMapException;
import domain.exception.ImmutableException;

public interface ICarModel {

	/**
	 * This method returns an Immutable Map including all the car
	 * parts.
	 */
	public Map<CarOptionCategogry, CarOption> getCarParts();
	
	
	public void addCarPart(CarOption part) throws AlreadyInMapException, ImmutableException;
	
	public Map<CarOptionCategogry, Boolean> getForcedOptionalTypes();
	
	public void addForcedOptionalType(CarOptionCategogry type, boolean bool) throws ImmutableException;
	
	public CarModelSpecification getSpecification();
	
	public void setSpecification(CarModelSpecification template) throws ImmutableException;
}
