package domain.vehicle;

/**
 * Abstract class representing a part of a car.
 */
public class VehicleOption implements IVehicleOption{

	private final String description;
	private final VehicleOptionCategory type;

	public VehicleOption(String description, VehicleOptionCategory type) {
		if (description == null || type == null) {
			throw new IllegalArgumentException();
		}
		this.description = description;
		this.type = type;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return this.getType().toString() + ": " + getDescription();
	}

	@Override
	public VehicleOptionCategory getType() {
		return this.type;
	}

	@Override
	public String getTaskDescription() {
		return this.getType().toString();
	}
	
	@Override
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
