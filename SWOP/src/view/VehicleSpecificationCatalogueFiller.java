package view;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import domain.assembly.workBench.WorkbenchType;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;


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
		models.add(getModelX());
		models.add(getModelY());
		return ImmutableSet.copyOf(models);
	}

	private VehicleSpecification getModelY(){
		Set<VehicleOption> parts = new HashSet<>();
		parts.add(new VehicleOption("platform", VehicleOptionCategory.BODY));
		
		parts.add(new VehicleOption("black", VehicleOptionCategory.COLOR));
		parts.add(new VehicleOption("white", VehicleOptionCategory.COLOR));
		
		parts.add(new VehicleOption("standard", VehicleOptionCategory.ENGINE));
		parts.add(new VehicleOption("hybrid", VehicleOptionCategory.ENGINE));
		
		parts.add(new VehicleOption("8 speed manual", VehicleOptionCategory.GEARBOX));
		parts.add(new VehicleOption("automatic", VehicleOptionCategory.GEARBOX));
		
		parts.add(new VehicleOption("vinyl grey", VehicleOptionCategory.SEATS));
		parts.add(new VehicleOption("vinyl black", VehicleOptionCategory.SEATS));
		
		parts.add(new VehicleOption("manual", VehicleOptionCategory.AIRCO));
		parts.add(new VehicleOption("automatic", VehicleOptionCategory.AIRCO));
		
		parts.add(new VehicleOption("standard", VehicleOptionCategory.WHEEL));
		parts.add(new VehicleOption("heavy-duty", VehicleOptionCategory.WHEEL));
		
		Map<WorkbenchType, Integer> timesAtWorkbench = new HashMap<WorkbenchType, Integer>();
		timesAtWorkbench.put(WorkbenchType.BODY, 120);
		timesAtWorkbench.put(WorkbenchType.ACCESSORIES, 120);
		timesAtWorkbench.put(WorkbenchType.DRIVETRAIN, 120);
		timesAtWorkbench.put(WorkbenchType.CARGO, 45);
		timesAtWorkbench.put(WorkbenchType.CERTIFICATION, 45);
		
		return new VehicleSpecification("model Y", parts, timesAtWorkbench);
	}
	
	private VehicleSpecification getModelX(){
		Set<VehicleOption> parts = new HashSet<>();
		parts.add(new VehicleOption("platform", VehicleOptionCategory.BODY));
		parts.add(new VehicleOption("closed", VehicleOptionCategory.BODY));
		
		parts.add(new VehicleOption("black", VehicleOptionCategory.COLOR));
		parts.add(new VehicleOption("white", VehicleOptionCategory.COLOR));
		
		parts.add(new VehicleOption("standard", VehicleOptionCategory.ENGINE));
		parts.add(new VehicleOption("hybrid", VehicleOptionCategory.ENGINE));
		
		parts.add(new VehicleOption("8 speed manual", VehicleOptionCategory.GEARBOX));
		parts.add(new VehicleOption("automatic", VehicleOptionCategory.GEARBOX));
		
		parts.add(new VehicleOption("vinyl grey", VehicleOptionCategory.SEATS));
		parts.add(new VehicleOption("vinyl black", VehicleOptionCategory.SEATS));
		
		parts.add(new VehicleOption("manual", VehicleOptionCategory.AIRCO));
		parts.add(new VehicleOption("automatic", VehicleOptionCategory.AIRCO));
		
		parts.add(new VehicleOption("standard", VehicleOptionCategory.WHEEL));
		parts.add(new VehicleOption("heavy-duty", VehicleOptionCategory.WHEEL));
		
		Map<WorkbenchType, Integer> timesAtWorkbench = new HashMap<WorkbenchType, Integer>();
		timesAtWorkbench.put(WorkbenchType.BODY, 90);
		timesAtWorkbench.put(WorkbenchType.ACCESSORIES, 90);
		timesAtWorkbench.put(WorkbenchType.DRIVETRAIN, 90);
		timesAtWorkbench.put(WorkbenchType.CARGO, 30);
		timesAtWorkbench.put(WorkbenchType.CERTIFICATION, 30);
		
		return new VehicleSpecification("model X", parts, timesAtWorkbench);
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
		
		Map<WorkbenchType, Integer> timesAtWorkbench = new HashMap<WorkbenchType, Integer>();
		timesAtWorkbench.put(WorkbenchType.BODY, 60);
		timesAtWorkbench.put(WorkbenchType.ACCESSORIES, 60);
		timesAtWorkbench.put(WorkbenchType.DRIVETRAIN, 60);
		timesAtWorkbench.put(WorkbenchType.CARGO, 0);
		timesAtWorkbench.put(WorkbenchType.CERTIFICATION, 0);
		
		return new VehicleSpecification("model C", parts, timesAtWorkbench);
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
		
		Map<WorkbenchType, Integer> timesAtWorkbench = new HashMap<WorkbenchType, Integer>();
		timesAtWorkbench.put(WorkbenchType.BODY, 70);
		timesAtWorkbench.put(WorkbenchType.ACCESSORIES, 70);
		timesAtWorkbench.put(WorkbenchType.DRIVETRAIN, 70);
		timesAtWorkbench.put(WorkbenchType.CARGO, 0);
		timesAtWorkbench.put(WorkbenchType.CERTIFICATION, 0);
		
		return new VehicleSpecification("model B", parts, timesAtWorkbench);
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
		
		Map<WorkbenchType, Integer> timesAtWorkbench = new HashMap<WorkbenchType, Integer>();
		timesAtWorkbench.put(WorkbenchType.BODY, 50);
		timesAtWorkbench.put(WorkbenchType.ACCESSORIES, 50);
		timesAtWorkbench.put(WorkbenchType.DRIVETRAIN, 50);
		timesAtWorkbench.put(WorkbenchType.CARGO, 0);
		timesAtWorkbench.put(WorkbenchType.CERTIFICATION, 0);
		
		return new VehicleSpecification("model A", parts, timesAtWorkbench);
	}

}