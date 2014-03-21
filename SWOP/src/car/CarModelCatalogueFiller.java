package car;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
/**
 * This class is used to initialize the CarModelCatalogue.
 * The class checks if every carModel is constructed with valid CarParts.
 * For this check uses it, the carPartModelCatalogue.
 */
public class CarModelCatalogueFiller {

	private CarPartCatalogue carPartCatalogue;

	/**
	 * Each carModelCatalogueFiller is associated with a certain carPartCatalogue.
	 * This is needed to know which carParts are valid for this certain carModelCatalogue.
	 * On initialization of a CarModelCatalogueFiller a carPartCatalogue is given for creating
	 * this association.
	 */
	public CarModelCatalogueFiller(CarPartCatalogue cat) {
		this.carPartCatalogue= cat;
	}

	/**
	 * Method that returns all the initial models. It checks for every
	 * car model if the car model is valid. If not, the car model isn't added
	 * to the list. When every car model is checked, the ArrayList is returned as result.
	 */
	public Set<CarModel> getInitialModels() {
		Set<CarModel> result = new HashSet<CarModel>();
		Set<CarModel> models = this.getModels();
		for(CarModel model : models){
			if(isValidCarModel(model.getCarParts())){
				result.add(model);
			}	
		}
		return result;
	}

	/**
	 * Method for checking if the carModel is a valid model. It checks
	 * if every carpart of the model is valid or not. If not, it return false.
	 */
	private boolean isValidCarModel(HashMap<Class<?>, CarPart> hashMap){
		for(CarPart part : hashMap.values()){
			if(!carPartCatalogue.isValidCarPart(part)){
				return false;
			}
		}
		return true;
	}

	/**
	 * Method with certain basic implementations of a carModel.
	 */
	private Set<CarModel> getModels(){
		Set<CarModel> initialModels = new HashSet<CarModel>();
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