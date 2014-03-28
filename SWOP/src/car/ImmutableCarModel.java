package car;

import java.util.Map;

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
	public Map<Class<?>, CarPart> getCarParts() {
		return model.getCarParts();
	}

	@Override
	public String getDescription() {
		return model.getDescription();
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
}
