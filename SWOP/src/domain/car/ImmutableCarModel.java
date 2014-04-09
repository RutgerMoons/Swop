package domain.car;

import java.util.Collections;
import java.util.Map;

import domain.exception.AlreadyInMapException;
import domain.exception.ImmutableException;

/**
 * Create an Immutable CarModel, only the getters are accessible.
 *
 */
public class ImmutableCarModel implements ICarModel {

	private ICarModel model;
	
	/**
	 * Create an Immutable CarModel.
	 * 
	 * @param model
	 * 			The mutable CarModel.
	 */
	public ImmutableCarModel(ICarModel model){
		if(model==null)
			throw new IllegalArgumentException();
		this.model = model;
	}
	@Override
	public Map<CarOptionCategory, CarOption> getCarParts() {
		return Collections.unmodifiableMap(model.getCarParts());
	}

	@Override
	public int hashCode(){
		return model.hashCode();
	}
	
	@Override
	public boolean equals(Object obj){
		return model.equals(obj);
	}

	@Override
	public String toString(){
		return model.toString();
	}
	@Override
	public void addCarPart(CarOption part) throws AlreadyInMapException,
			ImmutableException {
		throw new ImmutableException();
		
	}
	@Override
	public Map<CarOptionCategory, Boolean> getForcedOptionalTypes() {
		return Collections.unmodifiableMap(model.getForcedOptionalTypes());
	}
	@Override
	public void addForcedOptionalType(CarOptionCategory type, boolean bool)
			throws ImmutableException {
		throw new ImmutableException();
	}
	@Override
	public CarModelSpecification getSpecification() {
		return model.getSpecification();
	}
	@Override
	public void setSpecification(CarModelSpecification template)
			throws ImmutableException {
		throw new ImmutableException();
	}
}
