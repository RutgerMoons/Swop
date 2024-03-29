package domain.vehicle.vehicle;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import domain.assembly.workBench.WorkBenchType;
import domain.exception.AlreadyInMapException;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

/**
 * A class representing a vehicle, which consist of a set of VehicleOptions. The
 * vehicle is being built on the base of a VehicleSpecification. A Vehicle also
 * keeps track of forcedOptionalTypes, which need to be forced into the Vehicle,
 * when it's being built.
 */
public class Vehicle implements IVehicle {

	private HashMap<VehicleOptionCategory, VehicleOption> vehicleOptions;
	private VehicleSpecification specification;
	private Map<VehicleOption, Boolean> forcedOptionalTypes;

	/**
	 * Creates a new Vehicle, which consists of zero VehicleOptions at this
	 * point.
	 * 
	 * @param 	template
	 *          The VehicleSpecification according to which this Vehicle will
	 *          be built
	 * 
	 * @throws 	IllegalArgumentException
	 *          Thrown when the template is null
	 */
	public Vehicle(VehicleSpecification template) {
		if (template == null)
			throw new IllegalArgumentException();
		vehicleOptions = new HashMap<>();
		this.setVehicleSpecification(template);
		forcedOptionalTypes = new HashMap<>();
	}

	@Override
	public Map<VehicleOptionCategory, VehicleOption> getVehicleOptions() {
		return Collections.unmodifiableMap(vehicleOptions);
	}

	@Override
	public void addVehicleOption(VehicleOption part) {
		if (part == null)
			throw new IllegalArgumentException();
		if (vehicleOptions.containsKey(part.getType()))
			throw new AlreadyInMapException();
		vehicleOptions.put(part.getType(), part);
	}

	@Override
	public String toString() {
		return specification.getDescription();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + vehicleOptions.hashCode();
		result = prime * result + forcedOptionalTypes.hashCode();
		result = prime * result + specification.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		IVehicle other;
		try {
			other = (IVehicle) obj;
		} catch (ClassCastException e) {
			return false;
		}
		if (!vehicleOptions.equals(other.getVehicleOptions()))
			return false;
		if (!forcedOptionalTypes.equals(other.getForcedOptionalTypes()))
			return false;
		if (!specification.equals(other.getVehicleSpecification()))
			return false;
		return true;
	}

	@Override
	public Map<VehicleOption, Boolean> getForcedOptionalTypes() {
		return Collections.unmodifiableMap(forcedOptionalTypes);
	}

	@Override
	public void addForcedOptionalType(VehicleOption type, boolean bool) {
		if (type == null) {
			throw new IllegalArgumentException();
		}
		forcedOptionalTypes.put(type, bool);
	}

	@Override
	public VehicleSpecification getVehicleSpecification() {
		return specification;
	}

	@Override
	public void setVehicleSpecification(VehicleSpecification template) {
		if (template == null) {
			throw new IllegalArgumentException();
		}
		this.specification = template;
	}

	/**
	 * Checks if this Vehicle is a valid Vehicle.
	 * 
	 * @return True if and only if the Vehicle contains all the mandatory
	 *         VehicleOptions and the forced VehicleOptions.
	 */
	public boolean isValid() {
		for (VehicleOptionCategory type : VehicleOptionCategory.values()) {
			if ((!type.isOptional() || (forcedOptionalTypes.get(type) != null && forcedOptionalTypes
					.get(type) == false)) && vehicleOptions.get(type) == null)
				return false;
		}
		return true;
	}

	@Override
	public Map<WorkBenchType, Integer> getTimeAtWorkBench() {
		return Collections.unmodifiableMap(this.getVehicleSpecification()
				.getTimeAtWorkBench());
	}
}
