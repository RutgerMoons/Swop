package scenarioTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
import domain.assembly.workBench.IWorkBench;
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
import domain.vehicle.vehicleOption.VehicleOptionCategory;

/**
 * Scenario that tests the output for the use case: Perform Assembly Tasks.
 *
 */
public class PerformAssemblyTasksScenario {
	
	private Facade facade;
	private Company company;
	
	/**
	 * Initialize a Facade and a Company together with all the attributes they need.
	 */
	@Before
	public void initialize() {
		this.initializeCompany();
		facade = new Facade(company);
		this.placeOrder();
		facade.createAndAddUser("jef", "worker");
		facade.login("jef");
	}

	/**
	 * Test the use case:
	 * 		1. show all assemblyLines and choose an assemblyLine
	 * 		2. show all workbench for this chosen assemblyLine and choose a workbench
	 * 		3. show all tasks on the chosen workbench and choose a task
	 * 		4. execute the chosen task and notify the Facade
	 */
	@Test
	public void PerformUseCase(){
		//get a List of all the assembly lines
		//-->there should be 3 assembly lines
		List<IAssemblyLine> allAssemblyLines = facade.getAssemblyLines();
		assertEquals(3,allAssemblyLines.size());
		
		//each assembly line with the right specifications (which models can be built on that particular line)
		IAssemblyLine assemblyLine1 = allAssemblyLines.get(0);
		IAssemblyLine assemblyLine2 = allAssemblyLines.get(1);
		IAssemblyLine assemblyLine3 = allAssemblyLines.get(2);
		VehicleSpecification modelA = facade.getVehicleSpecificationFromCatalogue("model A");
		VehicleSpecification modelB = facade.getVehicleSpecificationFromCatalogue("model B");
		VehicleSpecification modelC = facade.getVehicleSpecificationFromCatalogue("model C");
		VehicleSpecification modelX = facade.getVehicleSpecificationFromCatalogue("model X");
		VehicleSpecification modelY = facade.getVehicleSpecificationFromCatalogue("model Y");
		
		assertTrue(assemblyLine1.getResponsibilities().contains(modelA));
		assertTrue(assemblyLine1.getResponsibilities().contains(modelB));
		
		assertTrue(assemblyLine2.getResponsibilities().contains(modelA));
		assertTrue(assemblyLine2.getResponsibilities().contains(modelB));
		assertTrue(assemblyLine2.getResponsibilities().contains(modelC));
		
		assertTrue(assemblyLine3.getResponsibilities().contains(modelA));
		assertTrue(assemblyLine3.getResponsibilities().contains(modelB));
		assertTrue(assemblyLine3.getResponsibilities().contains(modelC));
		assertTrue(assemblyLine3.getResponsibilities().contains(modelX));
		assertTrue(assemblyLine3.getResponsibilities().contains(modelY));
		
		//choose an assembly line
		IAssemblyLine chosenAssemblyLine = assemblyLine1;
		
		//get the workbenches from the assemblyline
		//--> there should be 3 workbenches
		List<IWorkBench> allWorkbenches = chosenAssemblyLine.getWorkBenches();
		assertEquals(3,allWorkbenches.size());
		
		//each workbench woth the right type
		IWorkBench workBench1 = allWorkbenches.get(0);
		IWorkBench workBench2 = allWorkbenches.get(1);
		IWorkBench workBench3 = allWorkbenches.get(2);
		
		assertEquals(WorkBenchType.BODY,workBench1.getWorkbenchType());
		assertEquals(WorkBenchType.DRIVETRAIN,workBench2.getWorkbenchType());
		assertEquals(WorkBenchType.ACCESSORIES,workBench3.getWorkbenchType());
		
		//choose a workbench
		IWorkBench chosenWorkbench = workBench1;
		
		//theworkbench should have a job from which it can complete tasks
		assertTrue(chosenWorkbench.getCurrentJob().isPresent());
		
		//there should be two tasks to complete at this workbench (body and color), which are not complete
		assertEquals(2,chosenWorkbench.getCurrentTasks().size());
		assertTrue(chosenWorkbench.getCurrentTasks().get(0).getTaskDescription().equalsIgnoreCase("body") || chosenWorkbench.getCurrentTasks().get(0).getTaskDescription().equalsIgnoreCase("color"));
		assertTrue(chosenWorkbench.getCurrentTasks().get(1).getTaskDescription().equalsIgnoreCase("body") || chosenWorkbench.getCurrentTasks().get(1).getTaskDescription().equalsIgnoreCase("color"));
		assertFalse(chosenWorkbench.getCurrentTasks().get(0).isCompleted());
		assertFalse(chosenWorkbench.getCurrentTasks().get(1).isCompleted());
		
		//complete one of the tasks
		facade.completeChosenTaskAtChosenWorkBench(assemblyLine1, chosenWorkbench, chosenWorkbench.getCurrentTasks().get(0), new ImmutableClock(0, 0));
		
		//now there should be only one task left
		assertTrue(chosenWorkbench.getCurrentTasks().get(0).isCompleted());
	}
	
	private void placeOrder(){
		facade.createAndAddUser("jos", "garageholder");
		facade.login("jos");

		VehicleOption option = new VehicleOption("black", VehicleOptionCategory.COLOR);
		VehicleSpecification specification = facade.getVehicleSpecificationFromCatalogue("model A");
		
		facade.createNewVehicle(specification);
		
		facade.addPartToVehicle(new VehicleOption("bodyType", VehicleOptionCategory.BODY));
		facade.addPartToVehicle(new VehicleOption("engineType", VehicleOptionCategory.ENGINE));
		facade.addPartToVehicle(new VehicleOption("gearboxType", VehicleOptionCategory.GEARBOX));
		facade.addPartToVehicle(new VehicleOption("seatsType", VehicleOptionCategory.SEATS));
		facade.addPartToVehicle(new VehicleOption("wheelType", VehicleOptionCategory.WHEEL));
		facade.addPartToVehicle(option);
		
		facade.processOrder(5);
		
		facade.logout();
	}

	private void initializeCompany(){
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
		
		company = new Company(bindingRestrictions, optionalRestrictions, customCatalogue, catalogue, assemblyLines, clock);
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
