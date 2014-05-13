package domain.vehicle.vehicle;

import java.util.HashMap;
import java.util.Map;

import domain.exception.AlreadyInMapException;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;


/**
 * Class representing a certain CarModel. 
 * Each CarModel has specifically chosen CarOptions, 
 * which are chosen from the Options offered by the Specification according to which this Model will be built.
 * A CarModel also keeps track of which 'optional-properties' of the CarPartTypes need to be overwritten for this specific CarModel.
 */
public class Vehicle implements IVehicle {

	private HashMap<VehicleOptionCategory, VehicleOption> vehicleOptions;
	private VehicleSpecification specification;
	private Map<VehicleOption, Boolean> forcedOptionalTypes;
	
	/**
	 * Creates a new CarModel, which consists of zero CarOptions at this point.
	 * @param template
	 * 			The Specification according to which this CarModel will be built.
	 */
	public Vehicle(VehicleSpecification template) {
		if(template==null)
			throw new IllegalArgumentException();
		vehicleOptions = new HashMap<>();
		this.setSpecification(template);
		forcedOptionalTypes = new HashMap<>();
	}

	
	@Override
	public Map<VehicleOptionCategory, VehicleOption> getVehicleOptions() {
		return vehicleOptions;
	}

	
	@Override
	public void addCarPart(VehicleOption part) {
		if (part == null)
			throw new IllegalArgumentException();
		if (vehicleOptions.containsKey(part.getType())
				&& !vehicleOptions.get(part.getType()).equals(part))
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
		result = prime * result
				+ ((vehicleOptions == null) ? 0 : vehicleOptions.hashCode());
		result = prime
				* result
				+ ((forcedOptionalTypes == null) ? 0 : forcedOptionalTypes
						.hashCode());
		result = prime * result
				+ ((specification == null) ? 0 : specification.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vehicle other = (Vehicle) obj;
		if (vehicleOptions == null) {
			if (other.vehicleOptions != null)
				return false;
		} else if (!vehicleOptions.equals(other.vehicleOptions))
			return false;
		if (forcedOptionalTypes == null) {
			if (other.forcedOptionalTypes != null)
				return false;
		} else if (!forcedOptionalTypes.equals(other.forcedOptionalTypes))
			return false;
		if (specification == null) {
			if (other.specification != null)
				return false;
		} else if (!specification.equals(other.specification))
			return false;
		return true;
	}

	
	@Override
	public Map<VehicleOption, Boolean> getForcedOptionalTypes() {
		return forcedOptionalTypes;
	}

	
	@Override
	public void addForcedOptionalType(VehicleOption type, boolean bool){
		forcedOptionalTypes.put(type, bool);
	}

	
	@Override
	public VehicleSpecification getSpecification() {
		return specification;
	}

	
	@Override
	public void setSpecification(VehicleSpecification template) {
		this.specification = template;
	}

	/**
	 * Checks if this model is valid.
	 * @return
	 * 		True if the model contains a CarOption of each mandatory CarOptionCategory.
	 */
	public boolean isValid() {
		for(VehicleOptionCategory type: VehicleOptionCategory.values()){
			if((!type.isOptional() || (forcedOptionalTypes.get(type)!=null && forcedOptionalTypes.get(type)==false)) && vehicleOptions.get(type)==null )
				return false;
		}
		return true;
	}

	
	@Override
	public int getTimeAtWorkBench() {
		return this.getSpecification().getTimeAtWorkBench();
	}
}
