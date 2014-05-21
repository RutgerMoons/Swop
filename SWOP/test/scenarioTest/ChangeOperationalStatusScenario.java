package scenarioTest;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import view.CustomVehicleCatalogueFiller;
import view.VehicleSpecificationCatalogueFiller;

import com.google.common.collect.Multimap;

import domain.assembly.assemblyLine.AssemblyLine;
import domain.assembly.assemblyLine.AssemblyLineState;
import domain.assembly.assemblyLine.IAssemblyLine;
import domain.assembly.workBench.WorkBench;
import domain.assembly.workBench.WorkBenchType;
import domain.clock.Clock;
import domain.clock.ImmutableClock;
import domain.company.Company;
import domain.facade.Facade;
import domain.observer.observers.ClockObserver;
import domain.restriction.BindingRestriction;
import domain.restriction.OptionalRestriction;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreatorFifo;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.catalogue.CustomVehicleCatalogue;
import domain.vehicle.catalogue.VehicleSpecificationCatalogue;
import domain.vehicle.vehicle.CustomVehicle;
import domain.vehicle.vehicleOption.VehicleOption;

public class ChangeOperationalStatusScenario {

	private Facade facade;
	private Clock clock;
	@Before
	public void initialize(){		
		Company company = this.initializeCompany();
		facade = new Facade(company);
		facade.createAndAddUser("jef", "custom car shop manager");
		facade.login("jef");
	}

	/**
	 * Test the use case to change the status of an AssemblyLine.
	 * 		1. We choose the assembly line
	 * 		2. We choose the new state of the assebmly line
	 * 		3. We give the time that has been passed
	 */
	@Test
	public void test() {

		//We choose the assembly line.
		IAssemblyLine assemblyLine = facade.getAssemblyLines().get(0);

		//We choose the new state of the assembly line, we choose Broken.
		AssemblyLineState newState = AssemblyLineState.BROKEN;
		
		//We choose the time that has been passed.
		ImmutableClock advanceTime = new ImmutableClock(1, 1);

		//We change the state to broken.
		facade.changeState(assemblyLine, newState, advanceTime);

		//We check if the state of the assembly line is Broken. 
		assertEquals(AssemblyLineState.BROKEN, assemblyLine.getState());
		
		//We check if the time is advanced
		assertEquals(new ImmutableClock(1, 361), clock.getImmutableClock());
	}








	private Company initializeCompany(){
		Set<BindingRestriction> bindingRestrictions = new HashSet<>();
		Set<OptionalRestriction> optionalRestrictions = new HashSet<>();

		CustomVehicleCatalogue customCatalogue = new CustomVehicleCatalogue();
		CustomVehicleCatalogueFiller customCarModelFiller = new CustomVehicleCatalogueFiller();
		Multimap<String, CustomVehicle> customModels = customCarModelFiller
				.getInitialModels();
		for (String model : customModels.keySet()) {
			for (CustomVehicle customModel : customModels.get(model)) {
				customCatalogue.addModel(model, customModel);
			}
		}

		VehicleSpecificationCatalogue catalogue = new VehicleSpecificationCatalogue();
		VehicleSpecificationCatalogueFiller filler = new VehicleSpecificationCatalogueFiller();

		catalogue.initializeCatalogue(filler.getInitialModels());
		clock = new Clock(360);
		clock.advanceTime(360);
		ClockObserver clockObserver = new ClockObserver();
		clock.attachObserver(clockObserver);
		ImmutableClock immutableClock = new ImmutableClock(0, 0);
		List<AssemblyLine> assemblyLines = getInitialAssemblyLines(clockObserver, immutableClock, catalogue);

		return new Company(bindingRestrictions, optionalRestrictions, customCatalogue, catalogue, assemblyLines, clock);
	}

	private List<AssemblyLine> getInitialAssemblyLines(ClockObserver clockObserver, ImmutableClock clock, VehicleSpecificationCatalogue catalogue) {
		List<AssemblyLine> assemblyLines = new ArrayList<AssemblyLine>();

		Map<WorkBenchType, Integer> timeAtWorkBench = new HashMap<WorkBenchType, Integer>();
		for(WorkBenchType type: WorkBenchType.values()){
			timeAtWorkBench.put(type, 60);
		}
		Set<VehicleOption> obligatory = new HashSet<>();
		VehicleSpecification customSpecification = new VehicleSpecification("custom", new HashSet<VehicleOption>(), timeAtWorkBench,obligatory);


		Set<VehicleSpecification> specifications = new HashSet<>();
		specifications.add(catalogue.getCatalogue().get("model A"));
		specifications.add(catalogue.getCatalogue().get("model B"));
		specifications.add(customSpecification);
		AssemblyLine line1 = new AssemblyLine(clockObserver, clock, AssemblyLineState.OPERATIONAL, specifications);


		specifications = new HashSet<>();
		specifications.add(catalogue.getCatalogue().get("model A"));
		specifications.add(catalogue.getCatalogue().get("model B"));
		specifications.add(catalogue.getCatalogue().get("model C"));
		specifications.add(customSpecification);
		AssemblyLine line2 = new AssemblyLine(clockObserver, clock, AssemblyLineState.OPERATIONAL, specifications);

		specifications = new HashSet<>();
		specifications.add(catalogue.getCatalogue().get("model A"));
		specifications.add(catalogue.getCatalogue().get("model B"));
		specifications.add(catalogue.getCatalogue().get("model C"));
		specifications.add(catalogue.getCatalogue().get("model X"));
		specifications.add(catalogue.getCatalogue().get("model Y"));
		specifications.add(customSpecification);
		AssemblyLine line3= new AssemblyLine(clockObserver, clock, AssemblyLineState.OPERATIONAL, specifications);

		Set<String> responsibilities = new HashSet<>();
		responsibilities.add("Body");
		responsibilities.add("Color");
		WorkBench body1 = new WorkBench(WorkBenchType.BODY);
		WorkBench body2 = new WorkBench(WorkBenchType.BODY);
		WorkBench body3 = new WorkBench(WorkBenchType.BODY);

		responsibilities = new HashSet<>();
		responsibilities.add("Engine");
		responsibilities.add("Gearbox");
		WorkBench drivetrain1 = new WorkBench(WorkBenchType.DRIVETRAIN);
		WorkBench drivetrain2 = new WorkBench(WorkBenchType.DRIVETRAIN);
		WorkBench drivetrain3 = new WorkBench(WorkBenchType.DRIVETRAIN);

		responsibilities = new HashSet<>();
		responsibilities.add("Seat");
		responsibilities.add("Airco");
		responsibilities.add("Spoiler");
		responsibilities.add("Wheel");
		WorkBench accessories1 = new WorkBench(WorkBenchType.ACCESSORIES);
		WorkBench accessories2 = new WorkBench(WorkBenchType.ACCESSORIES);
		WorkBench accessories3 = new WorkBench(WorkBenchType.ACCESSORIES);

		responsibilities = new HashSet<>();
		responsibilities.add("Storage");
		responsibilities.add("Protection");
		WorkBench cargo = new WorkBench(WorkBenchType.CARGO);

		responsibilities = new HashSet<>();
		responsibilities.add("Certification");
		WorkBench certificiation = new WorkBench(WorkBenchType.CERTIFICATION);

		line1.addWorkBench(body1);
		line1.addWorkBench(drivetrain1);
		line1.addWorkBench(accessories1);

		line2.addWorkBench(body2);
		line2.addWorkBench(drivetrain2);
		line2.addWorkBench(accessories2);

		line3.addWorkBench(body3);
		line3.addWorkBench(cargo);
		line3.addWorkBench(drivetrain3);
		line3.addWorkBench(accessories3);
		line3.addWorkBench(certificiation);

		assemblyLines.add(line1);
		assemblyLines.add(line2);
		assemblyLines.add(line3);

		line1.switchToSchedulingAlgorithm(new SchedulingAlgorithmCreatorFifo());
		line2.switchToSchedulingAlgorithm(new SchedulingAlgorithmCreatorFifo());
		line3.switchToSchedulingAlgorithm(new SchedulingAlgorithmCreatorFifo());

		return assemblyLines;
	}
}
