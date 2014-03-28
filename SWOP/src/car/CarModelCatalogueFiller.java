package car;

import java.util.HashSet;
import java.util.Set;

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
		initialModels.add(new CarModel("Polo", new Airco("manual"), new Body(
				"sedan"), new Color("red"), new Engine(
				"standard 2l 4 cilinders"), new Gearbox("6 speed manual"),
				new Seat("leather black"), new Wheel("comfort")));
		initialModels.add(new CarModel("Lada", new Airco(
				"automatic climate control"), new Body("break"), new Color(
				"blue"), new Engine("performance 2.5l 6 cilinders"),
				new Gearbox("5 speed automatic"), new Seat("leather white"),
				new Wheel("sports (low profile)")));
		return initialModels;
	}
}