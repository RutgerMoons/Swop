package car;

/**
 * Abstract class representing a part of a car.
 */
public class CarPart {

	private String description;
	private boolean isOptional;
	private CarPartType type;

	public CarPart(String description, boolean isOptional, CarPartType type) {
		setDescription(description);
		setOptional(isOptional);
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

	public boolean isOptional() {
		return isOptional;
	}

	public void setOptional(boolean isOptional) {
		this.isOptional = isOptional;
	}

	public CarPartType getType() {
		return type;
	}

	public void setType(CarPartType type) {
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
		result = prime * result + (isOptional ? 1231 : 1237);
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
		CarPart other = (CarPart) obj;
		if (!description.equals(other.description))
			return false;
		if (isOptional != other.isOptional)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

}
