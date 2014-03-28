package car;

import java.util.Map;

public class ImmutableCarModel implements ICarModel {

	private ICarModel model;
	public ImmutableCarModel(ICarModel description){
		if(description==null)
			throw new IllegalArgumentException();
		this.model = description;
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
