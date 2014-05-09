package domain.vehicle;

import java.util.Map;

import domain.exception.AlreadyInMapException;
import domain.exception.UnmodifiableException;
import domain.exception.NotImplementedException;

/**
 * Interface for limiting access to standard CarModels.
 */
public interface IVehicle {

	/**
	 * Returns all the CarOptions of which this model currently consists.
	 */
	public Map<VehicleOptionCategory, IVehicleOption> getVehicleOptions();
	
	/**
	 * Adds a CarOption to this CarModel. 
	 * @throws AlreadyInMapException
	 * 			If the model already has a CarOption of this Category.
	 */
	public void addCarPart(IVehicleOption part) throws AlreadyInMapException, UnmodifiableException;
	
	/**
	 * Get the CarOptions that need to be forced into the CarModel.
	 */
	public Map<IVehicleOption, Boolean> getForcedOptionalTypes() throws NotImplementedException;
	
	/**
	 * Add a forced Optional type, so it has to be in the model.
	 */
	public void addForcedOptionalType(IVehicleOption type, boolean bool) throws UnmodifiableException, NotImplementedException;
	
	/**
	 * Get the specification where the CarModel is built from.
	 */
	public VehicleSpecification getSpecification() throws NotImplementedException;
	
	/**
	 * Set a new specification from which the CarModel has to be build.
	 */
	public void setSpecification(VehicleSpecification template) throws UnmodifiableException, NotImplementedException;

	/**
	 * Get the time at the workbench of this CarModel.
	 */
	public int getTimeAtWorkBench();
}