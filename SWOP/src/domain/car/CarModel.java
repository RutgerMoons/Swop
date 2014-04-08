package domain.car;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import domain.exception.AlreadyInMapException;


/**
 * Class representing a certain carmodel. Each carmodel has all the essential
 * carparts.
 * 
 */
public class CarModel implements ICarModel {

	private HashMap<CarPartType, CarPart> carParts;
	private CarModelTemplate template;
	private Map<CarPartType, Boolean> forcedOptionalTypes;
	public CarModel(CarModelTemplate template) {
		carParts = new HashMap<CarPartType, CarPart>();
		this.setTemplate(template);
		forcedOptionalTypes = new HashMap<>();
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


	@Override
	public String toString() {
		return template.getDescription();
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((carParts == null) ? 0 : carParts.hashCode());
		result = prime
				* result
				+ ((forcedOptionalTypes == null) ? 0 : forcedOptionalTypes
						.hashCode());
		result = prime * result
				+ ((template == null) ? 0 : template.hashCode());
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
		if (carParts == null) {
			if (other.carParts != null)
				return false;
		} else if (!carParts.equals(other.carParts))
			return false;
		if (forcedOptionalTypes == null) {
			if (other.forcedOptionalTypes != null)
				return false;
		} else if (!forcedOptionalTypes.equals(other.forcedOptionalTypes))
			return false;
		if (template == null) {
			if (other.template != null)
				return false;
		} else if (!template.equals(other.template))
			return false;
		return true;
	}

	public Map<CarPartType, Boolean> getForcedOptionalTypes() {
		return forcedOptionalTypes;
	}

	public void addForcedOptionalType(CarPartType type, boolean bool){
		forcedOptionalTypes.put(type, bool);
	}

	public CarModelTemplate getTemplate() {
		return template;
	}

	public void setTemplate(CarModelTemplate template) {
		this.template = template;
	}
}
