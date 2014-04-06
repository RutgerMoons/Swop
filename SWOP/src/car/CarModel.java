package car;

import java.util.HashMap;
import java.util.Map;

import exception.AlreadyInMapException;

/**
 * Class representing a certain carmodel. Each carmodel has all the essential
 * carparts.
 * 
 */
public class CarModel implements ICarModel {

	private String description;
	private HashMap<CarPartType, CarPart> carParts;

	public CarModel(String description) {
		carParts = new HashMap<CarPartType, CarPart>();
		this.setDescription(description);
	}

	public Map<CarPartType, CarPart> getCarParts() {
		return carParts;
	}

	public void addCarPart(CarPart part) throws AlreadyInMapException {
		if (part == null)
			throw new IllegalArgumentException();
		if (carParts.containsKey(part.getType())
				&& !carParts.get(part.getType()).equals(part))
			throw new AlreadyInMapException();
		carParts.put(part.getType(), part);
	}

	/**
	 * Method for giving naming this model. The method checks if the name is
	 * different from null and if it is different from the empty string.
	 */
	private void setDescription(String description) {
		if (description == null || description.isEmpty())
			throw new IllegalArgumentException();
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	@Override
	public String toString() {
		return getDescription();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + carParts.hashCode();
		result = prime * result + description.hashCode();
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
		CarModel other = (CarModel) obj;
		if (!description.equals(other.description))
			return false;
		if (!carParts.equals(other.carParts))
			return false;
		return true;
	}

}
