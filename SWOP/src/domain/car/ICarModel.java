package domain.car;

import java.util.Map;

import domain.exception.AlreadyInMapException;
import domain.exception.ImmutableException;
import domain.exception.NotImplementedException;

public interface ICarModel {

	/**
	 * Returns all the CarOptions of which this model currently consists.
	 */
	public Map<CarOptionCategory, CarOption> getCarParts();
	
	/**
	 * Adds a CarOption to this CarModel. 
	 * @throws AlreadyInMapException
	 * 			If the model already has a CarOption of this Category.
	 */
	public void addCarPart(CarOption part) throws AlreadyInMapException, ImmutableException;
	
	/**
	 * Get the CarOptions that need to be forced into the CarModel.
	 */
	public Map<CarOption, Boolean> getForcedOptionalTypes() throws NotImplementedException;
	
	/**
	 * Add a forced Optional type, so it has to be in the model.
	 */
	public void addForcedOptionalType(CarOption type, boolean bool) throws ImmutableException, NotImplementedException;
	
	/**
	 * Get the specification where the CarModel is built from.
	 */
	public CarModelSpecification getSpecification() throws NotImplementedException;
	
	/**
	 * Set a new specification from which the CarModel has to be build.
	 */
	public void setSpecification(CarModelSpecification template) throws ImmutableException, NotImplementedException;

	/**
	 * Get the time at the workbench of this CarModel.
	 */
	public int getTimeAtWorkBench();
}