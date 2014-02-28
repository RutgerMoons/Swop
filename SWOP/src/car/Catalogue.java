package car;

import java.util.ArrayList;
import java.util.HashMap;

public class Catalogue {
	
	private HashMap<String, ArrayList<CarPart>> catalogue;
	
	public HashMap<String, ArrayList<CarPart>> getCatalogue() {
		return catalogue;
	}
	
	public void initializeCatalogue(ArrayList<CarModel> models){
		for(CarModel model : models){
			catalogue.put(model.getDescription(), model.getCarParts());
		}
	}
	
}
