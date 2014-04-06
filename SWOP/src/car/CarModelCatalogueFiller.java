package car;

import java.util.HashSet;
import java.util.Set;

import exception.AlreadyInMapException;

/**
 * This class is used to initialize the CarModelCatalogue. The class checks if
 * every carModel is constructed with valid CarParts. For this check uses it,
 * the carPartModelCatalogue.
 */
public class CarModelCatalogueFiller {


	/**
	 * Method that returns all the initial models. It checks for every car model
	 * if the car model is valid. If not, the car model isn't added to the list.
	 * When every car model is checked, the ArrayList is returned as result.
	 */
	public Set<CarModel> getInitialModels() {
		Set<CarModel> result = new HashSet<CarModel>();
		Set<CarModel> models = this.getModels();
		for (CarModel model : models) {
			result.add(model);
		}
		return result;
	}

	/**
	 * Method with certain basic implementations of a carModel.
	 */
	private Set<CarModel> getModels() {
		Set<CarModel> initialModels = new HashSet<CarModel>();
		CarModel polo = new CarModel("Polo");
		try {
			polo.addCarPart(new CarPart("manual", true, CarPartType.AIRCO));
			polo.addCarPart(new CarPart("sedan", false, CarPartType.BODY));
			polo.addCarPart(new CarPart("red", false, CarPartType.COLOR));
			polo.addCarPart(new CarPart("standard 2l 4 cilinders", false, CarPartType.ENGINE));
			polo.addCarPart(new CarPart("6 speed manual", false, CarPartType.GEARBOX));
			polo.addCarPart(new CarPart("leather black", false, CarPartType.SEATS));
			polo.addCarPart(new CarPart("comfort", false, CarPartType.WHEEL));
			initialModels.add(polo);
			
			CarModel lada = new CarModel("Lada");
			lada.addCarPart(new CarPart("automatic climate control", true, CarPartType.AIRCO));
			lada.addCarPart(new CarPart("break", false, CarPartType.BODY));
			lada.addCarPart(new CarPart("blue", false, CarPartType.COLOR));
			lada.addCarPart(new CarPart("performance 2.5l 6 cilinders", false, CarPartType.ENGINE));
			lada.addCarPart(new CarPart("5 speed automatic", false, CarPartType.GEARBOX));
			lada.addCarPart(new CarPart("leather white", false, CarPartType.SEATS));
			lada.addCarPart(new CarPart("sports (low profile)", false, CarPartType.WHEEL));
			initialModels.add(lada);
		} catch (AlreadyInMapException e) {
		}
		return initialModels;
	}
}