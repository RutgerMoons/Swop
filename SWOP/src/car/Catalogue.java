package car;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Catalogue {
	
	private Map<String, List<CarPart>> data;

	public Catalogue(){
		initializeCatalogue(new CatalogueFiller().getInitialModels());
	}


	public Map<String, List<CarPart>> getCatalogue() {
		return data;
	}
	
	public void initializeCatalogue(ArrayList<CarModel> models){
		data = new HashMap<String,List<CarPart>>();
		for(CarModel model : models){
			data.put(model.getDescription(), model.getCarParts());
		}
	}
	
}
