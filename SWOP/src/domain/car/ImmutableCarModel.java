package domain.car;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

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
	public Map<CarPartType, CarPart> getCarParts() {
		return new ImmutableMap.Builder<CarPartType, CarPart>()
				.putAll(model.getCarParts()).build();
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
