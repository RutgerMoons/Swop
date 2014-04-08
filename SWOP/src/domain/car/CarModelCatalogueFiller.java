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
	public Set<CarModelTemplate> getInitialModels() {
		Set<CarModelTemplate> models = new HashSet<CarModelTemplate>();
		models.add(getModelA());
		models.add(getModelB());
		models.add(getModelC());
		return models;
	}

	private CarModelTemplate getModelC() {
		Set<CarPart> parts = new HashSet<>();
		parts.add(new CarPart("sport", CarPartType.BODY));
		
		parts.add(new CarPart("black", CarPartType.COLOR));
		parts.add(new CarPart("white", CarPartType.COLOR));
		
		parts.add(new CarPart("performance 2.5l V6", CarPartType.ENGINE));
		parts.add(new CarPart("ultra 3l V8", CarPartType.ENGINE));
	
		parts.add(new CarPart("6 Speed Manual", CarPartType.GEARBOX));
		
		parts.add(new CarPart("Leather White", CarPartType.SEATS));
		parts.add(new CarPart("Leather Black", CarPartType.SEATS));
		
		parts.add(new CarPart("Manual", CarPartType.AIRCO));
		parts.add(new CarPart("Automatic", CarPartType.AIRCO));
		
		parts.add(new CarPart("Winter", CarPartType.WHEEL));
		parts.add(new CarPart("Sports", CarPartType.WHEEL));
		
		parts.add(new CarPart("high", CarPartType.SPOILER));
		parts.add(new CarPart("low", CarPartType.SPOILER));
		return new CarModelTemplate("model C", parts, 60);
	}

	private CarModelTemplate getModelB() {
		Set<CarPart> parts = new HashSet<>();
		parts.add(new CarPart("sedan", CarPartType.BODY));
		parts.add(new CarPart("break", CarPartType.BODY));
		parts.add(new CarPart("sport", CarPartType.BODY));
		
		parts.add(new CarPart("red", CarPartType.COLOR));
		parts.add(new CarPart("blue", CarPartType.COLOR));
		parts.add(new CarPart("green", CarPartType.COLOR));
		parts.add(new CarPart("yellow", CarPartType.COLOR));
		
		parts.add(new CarPart("standard 2l V4", CarPartType.ENGINE));
		parts.add(new CarPart("performance 2.5l V6", CarPartType.ENGINE));
		parts.add(new CarPart("ultra 3l V8", CarPartType.ENGINE));
	
		parts.add(new CarPart("6 Speed Manual", CarPartType.GEARBOX));
		parts.add(new CarPart("5 Speed Automatic", CarPartType.GEARBOX));
		
		parts.add(new CarPart("Leather White", CarPartType.SEATS));
		parts.add(new CarPart("Leather Black", CarPartType.SEATS));
		parts.add(new CarPart("Vinyl Grey", CarPartType.SEATS));
		
		parts.add(new CarPart("Manual", CarPartType.AIRCO));
		parts.add(new CarPart("Automatic", CarPartType.AIRCO));
		
		parts.add(new CarPart("Winter", CarPartType.WHEEL));
		parts.add(new CarPart("Comfort", CarPartType.WHEEL));
		parts.add(new CarPart("Sports", CarPartType.WHEEL));
		
		parts.add(new CarPart("low", CarPartType.SPOILER));
		return new CarModelTemplate("model B", parts, 70);
	}

	private CarModelTemplate getModelA() {
		Set<CarPart> parts = new HashSet<>();
		parts.add(new CarPart("sedan", CarPartType.BODY));
		parts.add(new CarPart("break", CarPartType.BODY));
		
		parts.add(new CarPart("red", CarPartType.COLOR));
		parts.add(new CarPart("blue", CarPartType.COLOR));
		parts.add(new CarPart("black", CarPartType.COLOR));
		parts.add(new CarPart("white", CarPartType.COLOR));
		
		parts.add(new CarPart("standard 2l V4", CarPartType.ENGINE));
		parts.add(new CarPart("performance 2.5l V6", CarPartType.ENGINE));
		
		parts.add(new CarPart("6 Speed Manual", CarPartType.GEARBOX));
		parts.add(new CarPart("5 Speed Manual", CarPartType.GEARBOX));
		parts.add(new CarPart("5 Speed Automatic", CarPartType.GEARBOX));
		
		parts.add(new CarPart("Leather White", CarPartType.SEATS));
		parts.add(new CarPart("Leather Black", CarPartType.SEATS));
		parts.add(new CarPart("Vinyl Grey", CarPartType.SEATS));
		
		parts.add(new CarPart("Manual", CarPartType.AIRCO));
		
		parts.add(new CarPart("Winter", CarPartType.WHEEL));
		parts.add(new CarPart("Comfort", CarPartType.WHEEL));
		parts.add(new CarPart("Sports", CarPartType.WHEEL));
		
		return new CarModelTemplate("model A", parts, 50);
	}

}