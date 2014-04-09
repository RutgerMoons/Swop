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
		parts.add(new CarOption("sport", CarOptionCategory.BODY));
		
		parts.add(new CarOption("black", CarOptionCategory.COLOR));
		parts.add(new CarOption("white", CarOptionCategory.COLOR));
		
		parts.add(new CarOption("performance 2.5l V6", CarOptionCategory.ENGINE));
		parts.add(new CarOption("ultra 3l V8", CarOptionCategory.ENGINE));
	
		parts.add(new CarOption("6 speed manual", CarOptionCategory.GEARBOX));
		
		parts.add(new CarOption("leather white", CarOptionCategory.SEATS));
		parts.add(new CarOption("leather black", CarOptionCategory.SEATS));
		
		parts.add(new CarOption("manual", CarOptionCategory.AIRCO));
		parts.add(new CarOption("automatic", CarOptionCategory.AIRCO));
		
		parts.add(new CarOption("winter", CarOptionCategory.WHEEL));
		parts.add(new CarOption("sports", CarOptionCategory.WHEEL));
		
		parts.add(new CarOption("high", CarOptionCategory.SPOILER));
		parts.add(new CarOption("low", CarOptionCategory.SPOILER));
		return new CarModelSpecification("model C", parts, 60);
	}

	private CarModelSpecification getModelB() {
		Set<CarOption> parts = new HashSet<>();
		parts.add(new CarOption("sedan", CarOptionCategory.BODY));
		parts.add(new CarOption("break", CarOptionCategory.BODY));
		parts.add(new CarOption("sport", CarOptionCategory.BODY));
		
		parts.add(new CarOption("red", CarOptionCategory.COLOR));
		parts.add(new CarOption("blue", CarOptionCategory.COLOR));
		parts.add(new CarOption("green", CarOptionCategory.COLOR));
		parts.add(new CarOption("yellow", CarOptionCategory.COLOR));
		
		parts.add(new CarOption("standard 2l V4", CarOptionCategory.ENGINE));
		parts.add(new CarOption("performance 2.5l V6", CarOptionCategory.ENGINE));
		parts.add(new CarOption("ultra 3l V8", CarOptionCategory.ENGINE));
	
		parts.add(new CarOption("6 speed manual", CarOptionCategory.GEARBOX));
		parts.add(new CarOption("5 speed automatic", CarOptionCategory.GEARBOX));
		
		parts.add(new CarOption("leather white", CarOptionCategory.SEATS));
		parts.add(new CarOption("leather black", CarOptionCategory.SEATS));
		parts.add(new CarOption("vinyl grey", CarOptionCategory.SEATS));
		
		parts.add(new CarOption("manual", CarOptionCategory.AIRCO));
		parts.add(new CarOption("automatic", CarOptionCategory.AIRCO));
		
		parts.add(new CarOption("winter", CarOptionCategory.WHEEL));
		parts.add(new CarOption("comfort", CarOptionCategory.WHEEL));
		parts.add(new CarOption("sports", CarOptionCategory.WHEEL));
		
		parts.add(new CarOption("low", CarOptionCategory.SPOILER));
		return new CarModelSpecification("model B", parts, 70);
	}

	private CarModelSpecification getModelA() {
		Set<CarOption> parts = new HashSet<>();
		parts.add(new CarOption("sedan", CarOptionCategory.BODY));
		parts.add(new CarOption("break", CarOptionCategory.BODY));
		
		parts.add(new CarOption("red", CarOptionCategory.COLOR));
		parts.add(new CarOption("blue", CarOptionCategory.COLOR));
		parts.add(new CarOption("black", CarOptionCategory.COLOR));
		parts.add(new CarOption("white", CarOptionCategory.COLOR));
		
		parts.add(new CarOption("standard 2l V4", CarOptionCategory.ENGINE));
		parts.add(new CarOption("performance 2.5l V6", CarOptionCategory.ENGINE));
		
		parts.add(new CarOption("6 speed manual", CarOptionCategory.GEARBOX));
		parts.add(new CarOption("5 speed manual", CarOptionCategory.GEARBOX));
		parts.add(new CarOption("5 speed automatic", CarOptionCategory.GEARBOX));
		
		parts.add(new CarOption("leather white", CarOptionCategory.SEATS));
		parts.add(new CarOption("leather black", CarOptionCategory.SEATS));
		parts.add(new CarOption("vinyl grey", CarOptionCategory.SEATS));
		
		parts.add(new CarOption("manual", CarOptionCategory.AIRCO));
		
		parts.add(new CarOption("winter", CarOptionCategory.WHEEL));
		parts.add(new CarOption("comfort", CarOptionCategory.WHEEL));
		parts.add(new CarOption("sports", CarOptionCategory.WHEEL));
		
		return new CarModelSpecification("model A", parts, 50);
	}

}