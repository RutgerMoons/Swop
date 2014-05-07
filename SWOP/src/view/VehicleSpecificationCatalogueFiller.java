package view;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import domain.vehicle.VehicleOption;
import domain.vehicle.VehicleOptionCategory;
import domain.vehicle.VehicleSpecification;


/**
 * This class is used to initialize the CarModelCatalogue.
 */
public class VehicleSpecificationCatalogueFiller {


	/**
	 * Method that returns all the initial models. It checks for every car model
	 * if the model is valid. If not, the car model isn't added to the list.
	 * When every car model is checked, the ArrayList is returned as result.
	 */
	public Set<VehicleSpecification> getInitialModels() {
		Set<VehicleSpecification> models = new HashSet<VehicleSpecification>();
		models.add(getModelA());
		models.add(getModelB());
		models.add(getModelC());
		return ImmutableSet.copyOf(models);
	}

	private VehicleSpecification getModelC() {
		Set<VehicleOption> parts = new HashSet<>();
		parts.add(new VehicleOption("sport", VehicleOptionCategory.BODY));
		
		parts.add(new VehicleOption("black", VehicleOptionCategory.COLOR));
		parts.add(new VehicleOption("white", VehicleOptionCategory.COLOR));
		
		parts.add(new VehicleOption("performance 2.5l V6", VehicleOptionCategory.ENGINE));
		parts.add(new VehicleOption("ultra 3l V8", VehicleOptionCategory.ENGINE));
	
		parts.add(new VehicleOption("6 speed manual", VehicleOptionCategory.GEARBOX));
		
		parts.add(new VehicleOption("leather white", VehicleOptionCategory.SEATS));
		parts.add(new VehicleOption("leather black", VehicleOptionCategory.SEATS));
		
		parts.add(new VehicleOption("manual", VehicleOptionCategory.AIRCO));
		parts.add(new VehicleOption("automatic", VehicleOptionCategory.AIRCO));
		
		parts.add(new VehicleOption("winter", VehicleOptionCategory.WHEEL));
		parts.add(new VehicleOption("sports", VehicleOptionCategory.WHEEL));
		
		parts.add(new VehicleOption("high", VehicleOptionCategory.SPOILER));
		parts.add(new VehicleOption("low", VehicleOptionCategory.SPOILER));
		return new VehicleSpecification("model C", parts, 60);
	}

	private VehicleSpecification getModelB() {
		Set<VehicleOption> parts = new HashSet<>();
		parts.add(new VehicleOption("sedan", VehicleOptionCategory.BODY));
		parts.add(new VehicleOption("break", VehicleOptionCategory.BODY));
		parts.add(new VehicleOption("sport", VehicleOptionCategory.BODY));
		
		parts.add(new VehicleOption("red", VehicleOptionCategory.COLOR));
		parts.add(new VehicleOption("blue", VehicleOptionCategory.COLOR));
		parts.add(new VehicleOption("green", VehicleOptionCategory.COLOR));
		parts.add(new VehicleOption("yellow", VehicleOptionCategory.COLOR));
		
		parts.add(new VehicleOption("standard 2l V4", VehicleOptionCategory.ENGINE));
		parts.add(new VehicleOption("performance 2.5l V6", VehicleOptionCategory.ENGINE));
		parts.add(new VehicleOption("ultra 3l V8", VehicleOptionCategory.ENGINE));
	
		parts.add(new VehicleOption("6 speed manual", VehicleOptionCategory.GEARBOX));
		parts.add(new VehicleOption("5 speed automatic", VehicleOptionCategory.GEARBOX));
		
		parts.add(new VehicleOption("leather white", VehicleOptionCategory.SEATS));
		parts.add(new VehicleOption("leather black", VehicleOptionCategory.SEATS));
		parts.add(new VehicleOption("vinyl grey", VehicleOptionCategory.SEATS));
		
		parts.add(new VehicleOption("manual", VehicleOptionCategory.AIRCO));
		parts.add(new VehicleOption("automatic", VehicleOptionCategory.AIRCO));
		
		parts.add(new VehicleOption("winter", VehicleOptionCategory.WHEEL));
		parts.add(new VehicleOption("comfort", VehicleOptionCategory.WHEEL));
		parts.add(new VehicleOption("sports", VehicleOptionCategory.WHEEL));
		
		parts.add(new VehicleOption("low", VehicleOptionCategory.SPOILER));
		return new VehicleSpecification("model B", parts, 70);
	}

	private VehicleSpecification getModelA() {
		Set<VehicleOption> parts = new HashSet<>();
		parts.add(new VehicleOption("sedan", VehicleOptionCategory.BODY));
		parts.add(new VehicleOption("break", VehicleOptionCategory.BODY));
		
		parts.add(new VehicleOption("red", VehicleOptionCategory.COLOR));
		parts.add(new VehicleOption("blue", VehicleOptionCategory.COLOR));
		parts.add(new VehicleOption("black", VehicleOptionCategory.COLOR));
		parts.add(new VehicleOption("white", VehicleOptionCategory.COLOR));
		
		parts.add(new VehicleOption("standard 2l V4", VehicleOptionCategory.ENGINE));
		parts.add(new VehicleOption("performance 2.5l V6", VehicleOptionCategory.ENGINE));
		
		parts.add(new VehicleOption("6 speed manual", VehicleOptionCategory.GEARBOX));
		parts.add(new VehicleOption("5 speed manual", VehicleOptionCategory.GEARBOX));
		parts.add(new VehicleOption("5 speed automatic", VehicleOptionCategory.GEARBOX));
		
		parts.add(new VehicleOption("leather white", VehicleOptionCategory.SEATS));
		parts.add(new VehicleOption("leather black", VehicleOptionCategory.SEATS));
		parts.add(new VehicleOption("vinyl grey", VehicleOptionCategory.SEATS));
		
		parts.add(new VehicleOption("manual", VehicleOptionCategory.AIRCO));
		
		parts.add(new VehicleOption("winter", VehicleOptionCategory.WHEEL));
		parts.add(new VehicleOption("comfort", VehicleOptionCategory.WHEEL));
		parts.add(new VehicleOption("sports", VehicleOptionCategory.WHEEL));
		
		return new VehicleSpecification("model A", parts, 50);
	}

}