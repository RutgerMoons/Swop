package domain.vehicle.vehicle;

import java.util.Map;

import domain.assembly.workBench.WorkBenchType;
import domain.exception.AlreadyInMapException;
import domain.exception.NotImplementedException;
import domain.exception.UnmodifiableException;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

/**
 * An interface describing every function that a vehicle can have.
 */
public interface IVehicle {

	/**
	 * Returns a map of all the VehicleOptions of which this model currently consists, 
	 * with the VehicleOptionCategory as key.
	 */
	public Map<VehicleOptionCategory, VehicleOption> getVehicleOptions();
	
	/**
	 * Adds a VehicleOption to this IVehicle. 
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the part is null
	 * 
	 * @throws 	AlreadyInMapException
	 * 			Thrown when the model already has a VehicleOption of this Category.
	 * 
	 * @throws	UnmodifiableException
	 * 			Thrown when it is an UnmodifiableVehicle
	 * 
	 * @throws	NotImplementedException
	 * 			Thrown when the IVehicle is a CustomVehicle
	 */
	public void addVehicleOption(VehicleOption part) throws AlreadyInMapException;
	
	/**
	 * Get a map of VehicleOptions that needed to be forced into the IVehicle.
	 */
	public Map<VehicleOption, Boolean> getForcedOptionalTypes();
	
	/**
	 * Add a forced Optional type, so it has to be in the IVehicle.
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the type is null
	 * 
	 * @throws	UnmodifiableException
	 * 			Thrown when it is an UnmodifiableVehicle
	 * 
	 * @throws	NotImplementedException
	 * 			Thrown when the IVehicle is a CustomVehicle
	 */
	public void addForcedOptionalType(VehicleOption type, boolean bool);
	
	/**
	 * Get the VehicleSpecification where the IVehicle is built from.
	 * 
	 * @throws	NotImplementedException
	 * 			Thrown when the IVehicle is a CustomVehicle
	 */
	public VehicleSpecification getVehicleSpecification();
	
	/**
	 * Set a new specification from which the IVehicle has to be build.
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the template is null
	 * 
	 *@throws	UnmodifiableException
	 * 			Thrown when it is an UnmodifiableVehicle
	 * 
	 *@throws	NotImplementedException
	 * 			Thrown when the Vehicle is a CustomVehicle
	 */
	public void setVehicleSpecification(VehicleSpecification template);

	/**
	 * Get a map of the times at each workbench.
	 */
	public Map<WorkBenchType, Integer> getTimeAtWorkBench();
}