package car;

import java.util.ArrayList;

public class CarModelCatalogueFiller {
	
	private CarPartCatalogue carPartCatalogue;
	
	public CarModelCatalogueFiller(CarPartCatalogue cat) {
		this.carPartCatalogue= cat;
	}
	
	
	public ArrayList<CarModel> getInitialModels() {
		ArrayList<CarModel> result = new ArrayList<CarModel>();
		ArrayList<CarModel> models = this.getModels();
		for(CarModel model : models){
			if(isValidCarModel(model.getCarParts())){
				result.add(model);
			}	
		}
		return result;
	}
	
	private boolean isValidCarModel(ArrayList<CarPart> list){
		for(CarPart part : list){
			if(!carPartCatalogue.isValidCarPart(part)){
				return false;
			}
			else{
				throw new IllegalArgumentException();
			}
		}
		return true;
	}
	
	private ArrayList<CarModel> getModels(){
		ArrayList<CarModel> initialModels = new ArrayList<CarModel>();
		initialModels.add(
			new CarModel("Polo",
				new Airco("manual"),
				new Body("sedan"),
				new Color("red"),
				new Engine("standard 2l 4 cilinders"),
				new Gearbox("6 speed manual"),
				new Seat("leather black"),
				new Wheel("comfort")));
		initialModels.add(
				new CarModel("Lada",
					new Airco("automatic climate control"),
					new Body("break"),
					new Color("blue"),
					new Engine("performance 2.5l 6 cilinders"),
					new Gearbox("5 speed automatic"),
					new Seat("leather white"),
					new Wheel("sports (low profile)")));
		return initialModels;
	}
}