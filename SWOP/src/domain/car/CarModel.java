package domain.car;

import java.util.HashMap;
import java.util.Map;

import domain.exception.AlreadyInMapException;


/**
 * Class representing a certain CarModel. 
 * Each CarModel has specifically chosen CarOptions, 
 * which are chosen from the Options offered by the Specification according to which this Model will be built.
 * A CarModel also keeps track of which 'optional-properties' of the CarPartTypes need to be overwritten for this specific CarModel.
 */
public class CarModel implements ICarModel {

	private HashMap<CarOptionCategory, CarOption> carOptions;
	private CarModelSpecification specification;
	private Map<CarOptionCategory, Boolean> forcedOptionalTypes;
	
	/**
	 * Creates a new CarModel, which consists of zero CarOptions at this point.
	 * @param template
	 * 			The Specification according to which this CarModel will be built.
	 */
	public CarModel(CarModelSpecification template) {
		if(template==null)
			throw new IllegalArgumentException();
		carOptions = new HashMap<CarOptionCategory, CarOption>();
		this.setSpecification(template);
		forcedOptionalTypes = new HashMap<>();
	}

	/**
	 * Returns all the CarOptions of which this model currently consists.
	 */
	@Override
	public Map<CarOptionCategory, CarOption> getCarParts() {
		return carOptions;
	}

	/**
	 * Adds a CarOption to this CarModel. 
	 * @throws AlreadyInMapException
	 * 			If the model already has a CarOption of this Type.
	 */
	@Override
	public void addCarPart(CarOption part) throws AlreadyInMapException {
		if (part == null)
			throw new IllegalArgumentException();
		if (carOptions.containsKey(part.getType())
				&& !carOptions.get(part.getType()).equals(part))
			throw new AlreadyInMapException();
		carOptions.put(part.getType(), part);
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
				+ ((carOptions == null) ? 0 : carOptions.hashCode());
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
		CarModel other = (CarModel) obj;
		if (carOptions == null) {
			if (other.carOptions != null)
				return false;
		} else if (!carOptions.equals(other.carOptions))
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
	public Map<CarOptionCategory, Boolean> getForcedOptionalTypes() {
		return forcedOptionalTypes;
	}

	@Override
	public void addForcedOptionalType(CarOptionCategory type, boolean bool){
		forcedOptionalTypes.put(type, bool);
	}

	@Override
	public CarModelSpecification getSpecification() {
		return specification;
	}

	@Override
	public void setSpecification(CarModelSpecification template) {
		this.specification = template;
	}

	public boolean isValid() {
		for(CarOptionCategory type: CarOptionCategory.values()){
			if((!type.isOptional() || forcedOptionalTypes.get(type)==false) && carOptions.get(type)==null )
				return false;
		}
		return true;
	}
}
