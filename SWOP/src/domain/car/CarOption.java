package domain.car;

/**
 * Abstract class representing a part of a car.
 */
public class CarOption {

	private String description;
	private CarOptionCategogry type;

	public CarOption(String description, CarOptionCategogry type) {
		setDescription(description);
		setType(type);
	}

	/**
	 * 
	 * @param description
	 *            The type of this carpart.
	 * @post the description of this object equals type, | unless type == null |
	 *       or type is not one of the possible types for this CarPart
	 * @throws IllegalArgumentException
	 *             if description==null or isEmpty
	 */
	public void setDescription(String description) {
		if (description == null || description.isEmpty())
			throw new IllegalArgumentException();
		this.description = description;
	}

	/**
	 * Returns the description of this type.
	 */
	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return getType().toString() + ": " + getDescription();
	}

	public CarOptionCategogry getType() {
		return type;
	}

	public void setType(CarOptionCategogry type) {
		if (type == null)
			throw new IllegalArgumentException();
		this.type = type;
	}

	public String getTaskDescription() {
		switch (getType()) {
		case AIRCO:
			return "Airco";
		case BODY:
			return "Assembly";
		case COLOR:
			return "Paint";
		case ENGINE:
			return "Engine";
		case GEARBOX:
			return "Gearbox";
		case SEATS:
			return "Seats";
		case SPOILER:
			return "Spoiler";
		case WHEEL:
			return "Wheels";
		default:
			return "";
		}
	}
	
	public String getActionDescription() {
		return "Put on " + getDescription() + " " + getType().toString().toLowerCase();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + description.hashCode();
		result = prime * result + type.hashCode();
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
		CarOption other = (CarOption) obj;
		if (!description.equals(other.description))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

}
