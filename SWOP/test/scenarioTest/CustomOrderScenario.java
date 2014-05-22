package scenarioTest;

import static org.junit.Assert.*;

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
import domain.vehicle.vehicle.IVehicle;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

/**
 * Scenario that tests the output for the use case: Order Single Task.
 *
 */
public class CustomOrderScenario {

	private Facade facade;
	
	/**
	 * Initialize a Facade and a Company together with all the attributes they need.
	 */
	@Before
	public void initialize(){
		Company company = this.initializeCompany();
		facade = new Facade(company);
		facade.createAndAddUser("jef", "custom car shop manager");
		facade.login("jef");
	}
	
	/**
	 * Test the use case:
	 * 		1. the system returns the list of available tasks one of these tasks is chosen by the user
	 * 		2. the system returns the options for the chosen task
	 * 		3. the new order is processed and the system returns an estimated completion date
	 */
	@Test
	public void customOrderTest(){
		//1.all the available custom tasks are returned by the system
		Set<String> allCustomTasks = facade.getCustomTasks();
		assertTrue(allCustomTasks.contains("installing custom seats"));
		assertTrue(allCustomTasks.contains("spraying car bodies"));
		
		//2.the task 'spraying car bodies' is chosen by the user, the system returns the options for this task
		//		--> the List of IVehicles that is returned by the system should contain 6 IVehicles, one for each color-option the user will be offered
		String customTaskSpraying = "spraying car bodies";

		//check if  the List contains 6 IVehicles
		List<IVehicle> specificVehiclesSpraying= facade.getCustomOptions(customTaskSpraying);
		assertEquals(6,specificVehiclesSpraying.size());
		
		//check if all of the needed color-options were represented in the List returned by the system
		List<String> specificTasksSpraying = new ArrayList<String>();
		for(IVehicle customVehicle: specificVehiclesSpraying){
			specificTasksSpraying.add(customVehicle.getVehicleOptions().get(VehicleOptionCategory.COLOR).getDescription());
		}
		assertTrue(specificTasksSpraying.contains("white"));
		assertTrue(specificTasksSpraying.contains("black"));
		assertTrue(specificTasksSpraying.contains("yellow"));
		assertTrue(specificTasksSpraying.contains("red"));
		assertTrue(specificTasksSpraying.contains("blue"));
		assertTrue(specificTasksSpraying.contains("green"));
		
		//2.the task 'installing custom seats' is chosen by the user, the system returns the options for this task
		//		--> the List of IVehicles that is returned by the system should contain 1 IVehicle, 
		//		      one for each seats-option the user will be offered: in this case the only option the user has are custom seats
		String customTaskInstalling = "installing custom seats";
		
		//check if the list contains 1 IVehicle
		List<IVehicle> specificVehiclesInstalling = facade.getCustomOptions(customTaskInstalling);
		assertEquals(1,specificVehiclesInstalling.size());
		
		//check if the needed seats-option was represented in the Lst returned by the system
		List<String> specificTasksInstalling = new ArrayList<String>();
		for(IVehicle customVehicle: specificVehiclesInstalling){
			specificTasksInstalling.add(customVehicle.getVehicleOptions().get(VehicleOptionCategory.SEATS).getDescription());
		}
		assertTrue(specificTasksInstalling.contains("custom"));
		
		//3.the new order is processed and the estimated time is returned
		IVehicle model = specificVehiclesInstalling.get(0);
		ImmutableClock deadline = new ImmutableClock(1,123);
		
		//check if the estimated time is right
		ImmutableClock time = facade.processCustomOrder(model, deadline);
		assertEquals("day 1, 8 hours, 3 minutes.",time.toString());
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
		Clock clock = new Clock(360);
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
