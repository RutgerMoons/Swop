package car;

import java.util.HashMap;

public class ImmutableCarModel implements ICarModel {

	private CarModel model;
	public ImmutableCarModel(CarModel model){
		if(model==null)
			throw new IllegalArgumentException();
		this.model = model;
	}
	@Override
	public HashMap<Class<?>, CarPart> getCarParts() {
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
