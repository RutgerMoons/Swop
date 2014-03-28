package car;

import java.util.Map;

public interface ICarModel {

	
	public Map<Class<?>, CarPart> getCarParts();
	
	public String getDescription();
}
