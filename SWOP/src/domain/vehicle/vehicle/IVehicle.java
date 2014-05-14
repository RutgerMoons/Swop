package domain.vehicle.vehicle;

import java.util.Map;
import java.util.Set;

import domain.assembly.workBench.WorkbenchType;
import domain.exception.AlreadyInMapException;
import domain.exception.NotImplementedException;
import domain.exception.UnmodifiableException;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

/**
 * Interface for limiting access to standard CarModels.
 */
public interface IVehicle {

	/**
	 * Returns all the CarOptions of which this model currently consists.
	 */
	public Map<VehicleOptionCategory, VehicleOption> getVehicleOptions();
	
	/**
	 * Adds a CarOption to this CarModel. 
	 * @throws AlreadyInMapException
	 * 			Thrown when the model already has a CarOption of this Category.
	 * @throws	UnmodifiableException
	 * 			Thrown when it is an UnmodifiableVehicle
	 */
	public void addCarPart(VehicleOption part) throws AlreadyInMapException;
	
	/**
	 * Get the CarOptions that need to be forced into the CarModel.
	 */
	public Map<VehicleOption, Boolean> getForcedOptionalTypes();
	
	/**
	 * Add a forced Optional type, so it has to be in the model.
	 *  @throws	UnmodifiableException
	 * 			Thrown when it is an UnmodifiableVehicle
	 */
	public void addForcedOptionalType(VehicleOption type, boolean bool);
	
	/**
	 * Get the specification where the CarModel is built from.
	 */
	public VehicleSpecification getSpecification();
	
	/**
	 * Set a new specification from which the CarModel has to be build.
	 *  @throws	UnmodifiableException
	 * 			Thrown when it is an UnmodifiableVehicle
	 * @throws	NotImplementedException
	 * 			Thrown when the Vehicle is a CustomVehicle
	 */
	public void setSpecification(VehicleSpecification template);

	/**
	 * Get the time at the workbench of this CarModel.
	 */
	public Map<WorkbenchType, Integer> getTimeAtWorkBench();

	/**
	 * Check if the vehicle can be handled by the responsibilities.
	 * @param 	responsibilities
	 * 			The responsibilities of the assemblyline
	 */
	public boolean canBeHandled(Set<VehicleSpecification> responsibilities);
}