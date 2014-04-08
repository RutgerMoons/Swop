package domain.car;

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
	public Set<CarModelSpecification> getInitialModels() {
		Set<CarModelSpecification> models = new HashSet<CarModelSpecification>();
		models.add(getModelA());
		models.add(getModelB());
		models.add(getModelC());
		return models;
	}

	private CarModelSpecification getModelC() {
		Set<CarOption> parts = new HashSet<>();
		parts.add(new CarOption("sport", CarOptionCategogry.BODY));
		
		parts.add(new CarOption("black", CarOptionCategogry.COLOR));
		parts.add(new CarOption("white", CarOptionCategogry.COLOR));
		
		parts.add(new CarOption("performance 2.5l V6", CarOptionCategogry.ENGINE));
		parts.add(new CarOption("ultra 3l V8", CarOptionCategogry.ENGINE));
	
		parts.add(new CarOption("6 Speed Manual", CarOptionCategogry.GEARBOX));
		
		parts.add(new CarOption("Leather White", CarOptionCategogry.SEATS));
		parts.add(new CarOption("Leather Black", CarOptionCategogry.SEATS));
		
		parts.add(new CarOption("Manual", CarOptionCategogry.AIRCO));
		parts.add(new CarOption("Automatic", CarOptionCategogry.AIRCO));
		
		parts.add(new CarOption("Winter", CarOptionCategogry.WHEEL));
		parts.add(new CarOption("Sports", CarOptionCategogry.WHEEL));
		
		parts.add(new CarOption("high", CarOptionCategogry.SPOILER));
		parts.add(new CarOption("low", CarOptionCategogry.SPOILER));
		return new CarModelSpecification("model C", parts, 60);
	}

	private CarModelSpecification getModelB() {
		Set<CarOption> parts = new HashSet<>();
		parts.add(new CarOption("sedan", CarOptionCategogry.BODY));
		parts.add(new CarOption("break", CarOptionCategogry.BODY));
		parts.add(new CarOption("sport", CarOptionCategogry.BODY));
		
		parts.add(new CarOption("red", CarOptionCategogry.COLOR));
		parts.add(new CarOption("blue", CarOptionCategogry.COLOR));
		parts.add(new CarOption("green", CarOptionCategogry.COLOR));
		parts.add(new CarOption("yellow", CarOptionCategogry.COLOR));
		
		parts.add(new CarOption("standard 2l V4", CarOptionCategogry.ENGINE));
		parts.add(new CarOption("performance 2.5l V6", CarOptionCategogry.ENGINE));
		parts.add(new CarOption("ultra 3l V8", CarOptionCategogry.ENGINE));
	
		parts.add(new CarOption("6 Speed Manual", CarOptionCategogry.GEARBOX));
		parts.add(new CarOption("5 Speed Automatic", CarOptionCategogry.GEARBOX));
		
		parts.add(new CarOption("Leather White", CarOptionCategogry.SEATS));
		parts.add(new CarOption("Leather Black", CarOptionCategogry.SEATS));
		parts.add(new CarOption("Vinyl Grey", CarOptionCategogry.SEATS));
		
		parts.add(new CarOption("Manual", CarOptionCategogry.AIRCO));
		parts.add(new CarOption("Automatic", CarOptionCategogry.AIRCO));
		
		parts.add(new CarOption("Winter", CarOptionCategogry.WHEEL));
		parts.add(new CarOption("Comfort", CarOptionCategogry.WHEEL));
		parts.add(new CarOption("Sports", CarOptionCategogry.WHEEL));
		
		parts.add(new CarOption("low", CarOptionCategogry.SPOILER));
		return new CarModelSpecification("model B", parts, 70);
	}

	private CarModelSpecification getModelA() {
		Set<CarOption> parts = new HashSet<>();
		parts.add(new CarOption("sedan", CarOptionCategogry.BODY));
		parts.add(new CarOption("break", CarOptionCategogry.BODY));
		
		parts.add(new CarOption("red", CarOptionCategogry.COLOR));
		parts.add(new CarOption("blue", CarOptionCategogry.COLOR));
		parts.add(new CarOption("black", CarOptionCategogry.COLOR));
		parts.add(new CarOption("white", CarOptionCategogry.COLOR));
		
		parts.add(new CarOption("standard 2l V4", CarOptionCategogry.ENGINE));
		parts.add(new CarOption("performance 2.5l V6", CarOptionCategogry.ENGINE));
		
		parts.add(new CarOption("6 Speed Manual", CarOptionCategogry.GEARBOX));
		parts.add(new CarOption("5 Speed Manual", CarOptionCategogry.GEARBOX));
		parts.add(new CarOption("5 Speed Automatic", CarOptionCategogry.GEARBOX));
		
		parts.add(new CarOption("Leather White", CarOptionCategogry.SEATS));
		parts.add(new CarOption("Leather Black", CarOptionCategogry.SEATS));
		parts.add(new CarOption("Vinyl Grey", CarOptionCategogry.SEATS));
		
		parts.add(new CarOption("Manual", CarOptionCategogry.AIRCO));
		
		parts.add(new CarOption("Winter", CarOptionCategogry.WHEEL));
		parts.add(new CarOption("Comfort", CarOptionCategogry.WHEEL));
		parts.add(new CarOption("Sports", CarOptionCategogry.WHEEL));
		
		return new CarModelSpecification("model A", parts, 50);
	}

}