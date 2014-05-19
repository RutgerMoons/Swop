package view;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import domain.assembly.workBench.WorkBenchType;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

/**
 * This class is used to initialize the VehicleSpecificationCatalogue.
 */
public class VehicleSpecificationCatalogueFiller {


	/**
	 * Method that returns all the initial models. It checks for every Vehicle
	 * if the model is valid. If not, the Vehicle isn't added to the list.
	 * When every Vehicle is checked, the ArrayList is returned as result.
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
		
		Map<WorkBenchType, Integer> timesAtWorkbench = new HashMap<WorkBenchType, Integer>();
		timesAtWorkbench.put(WorkBenchType.BODY, 120);
		timesAtWorkbench.put(WorkBenchType.ACCESSORIES, 120);
		timesAtWorkbench.put(WorkBenchType.DRIVETRAIN, 120);
		timesAtWorkbench.put(WorkBenchType.CARGO, 45);
		timesAtWorkbench.put(WorkBenchType.CERTIFICATION, 45);
		
		Set<VehicleOption> options = new HashSet<>();
		options.add(new VehicleOption("Cargo", VehicleOptionCategory.CARGO));
		options.add(new VehicleOption("Certification", VehicleOptionCategory.CERTIFICATION));
		
		
		return new VehicleSpecification("model Y", parts, timesAtWorkbench, options);
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
		
		Map<WorkBenchType, Integer> timesAtWorkbench = new HashMap<WorkBenchType, Integer>();
		timesAtWorkbench.put(WorkBenchType.BODY, 90);
		timesAtWorkbench.put(WorkBenchType.ACCESSORIES, 90);
		timesAtWorkbench.put(WorkBenchType.DRIVETRAIN, 90);
		timesAtWorkbench.put(WorkBenchType.CARGO, 30);
		timesAtWorkbench.put(WorkBenchType.CERTIFICATION, 30);
		
		Set<VehicleOption> options = new HashSet<>();
		options.add(new VehicleOption("Cargo", VehicleOptionCategory.CARGO));
		options.add(new VehicleOption("Certification", VehicleOptionCategory.CERTIFICATION));
		return new VehicleSpecification("model X", parts, timesAtWorkbench, options);
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
		
		Map<WorkBenchType, Integer> timesAtWorkbench = new HashMap<WorkBenchType, Integer>();
		timesAtWorkbench.put(WorkBenchType.BODY, 60);
		timesAtWorkbench.put(WorkBenchType.ACCESSORIES, 60);
		timesAtWorkbench.put(WorkBenchType.DRIVETRAIN, 60);
		timesAtWorkbench.put(WorkBenchType.CARGO, 0);
		timesAtWorkbench.put(WorkBenchType.CERTIFICATION, 0);
		
		return new VehicleSpecification("model C", parts, timesAtWorkbench, new HashSet<VehicleOption>());
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
		
		Map<WorkBenchType, Integer> timesAtWorkbench = new HashMap<WorkBenchType, Integer>();
		timesAtWorkbench.put(WorkBenchType.BODY, 70);
		timesAtWorkbench.put(WorkBenchType.ACCESSORIES, 70);
		timesAtWorkbench.put(WorkBenchType.DRIVETRAIN, 70);
		timesAtWorkbench.put(WorkBenchType.CARGO, 0);
		timesAtWorkbench.put(WorkBenchType.CERTIFICATION, 0);
		
		return new VehicleSpecification("model B", parts, timesAtWorkbench, new HashSet<VehicleOption>());
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
		
		Map<WorkBenchType, Integer> timesAtWorkbench = new HashMap<WorkBenchType, Integer>();
		timesAtWorkbench.put(WorkBenchType.BODY, 50);
		timesAtWorkbench.put(WorkBenchType.ACCESSORIES, 50);
		timesAtWorkbench.put(WorkBenchType.DRIVETRAIN, 50);
		timesAtWorkbench.put(WorkBenchType.CARGO, 0);
		timesAtWorkbench.put(WorkBenchType.CERTIFICATION, 0);
		
		return new VehicleSpecification("model A", parts, timesAtWorkbench, new HashSet<VehicleOption>());
	}

}