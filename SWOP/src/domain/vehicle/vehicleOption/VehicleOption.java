package domain.vehicle.vehicleOption;


/**
 * A class representing a part of a vehicle, that can be added to an IVehicle.
 * It consists of a small description of what it is and which VehicleOptionCategory it belongs to.
 */
public class VehicleOption {

	private final String description;
	private final VehicleOptionCategory type;

	/**
	 * Create a new VehicleOption.
	 * 
	 * @param 	description
	 * 			A small description of what it is
	 * @param 	type
	 * 			The type the VehicleOption has to belong to.
	 */
	public VehicleOption(String description, VehicleOptionCategory type) {
		if (description == null || description.isEmpty() || type == null) {
			throw new IllegalArgumentException();
		}
		this.description = description;
		this.type = type;
	}

	/**
	 * Get the description of this VehicleOption.
	 */
	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return this.getType().toString() + ": " + getDescription();
	}

	/**
	 * Get the type where this VehicleOption belongs to.
	 */
	public VehicleOptionCategory getType() {
		return this.type;
	}

	/**
	 * Get the description of the task that has to be completed for this VehicleOption on the WorkBenches.
	 */
	public String getTaskDescription() {
		return this.getType().toString();
	}
	
	/**
	 * Get the description of the action that has to be completed for this VehicleOption on the WorkBenches.
	 */
	public String getActionDescription() {
		return "Put on " + this.getDescription() + " " + this.getType().toString().toLowerCase();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		VehicleOption other = (VehicleOption) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

}
