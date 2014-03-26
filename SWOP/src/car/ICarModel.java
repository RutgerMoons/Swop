package car;

import java.util.HashMap;

public interface ICarModel {

	
	public HashMap<Class<?>,CarPart> getCarParts();
	
	public String getDescription();
}
