package domain.car;

import java.util.Map;

import domain.exception.AlreadyInMapException;
import domain.exception.ImmutableException;

public interface ICarModel {

	/**
	 * This method returns an Immutable Map including all the car
	 * parts.
	 */
	public Map<CarOptionCategory, CarOption> getCarParts();
	
	
	public void addCarPart(CarOption part) throws AlreadyInMapException, ImmutableException;
	
	public Map<CarOptionCategory, Boolean> getForcedOptionalTypes();
	
	public void addForcedOptionalType(CarOptionCategory type, boolean bool) throws ImmutableException;
	
	public CarModelSpecification getSpecification();
	
	public void setSpecification(CarModelSpecification template) throws ImmutableException;
}
