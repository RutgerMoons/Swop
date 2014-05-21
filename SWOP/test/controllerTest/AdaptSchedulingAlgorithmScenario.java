package controllerTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import domain.assembly.workBench.WorkBench;
import domain.assembly.workBench.WorkBenchType;
import domain.clock.Clock;
import domain.clock.ImmutableClock;
import domain.company.Company;
import domain.facade.Facade;
import domain.observer.observers.ClockObserver;
import domain.restriction.BindingRestriction;
import domain.restriction.OptionalRestriction;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreatorBatch;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreatorFifo;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.catalogue.CustomVehicleCatalogue;
import domain.vehicle.catalogue.VehicleSpecificationCatalogue;
import domain.vehicle.vehicle.CustomVehicle;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

/**
 * Scenario that tests the output for the use case: Adapt Scheduling Algorithm.
 *
 */
public class AdaptSchedulingAlgorithmScenario {

	private Facade facade;
	private Company company;
	
	/**
	 * Initialize a Facade and a Company together with all the attributes they need.
	 */
	@Before
	public void initialize(){
		this.initializeCompany();
		facade = new Facade(company);
		facade.createAndAddUser("jef", "manager");
		facade.login("jef");
	}
	
	/**
	 * Test the use case (switching to the fifo algorithm):
	 * 		1. the system shows the available algorithms
	 * 		2. the fifo algorithm is selected, so the system applies this new algorithm
	 */
	@Test
	public void testUseCaseToFifo() {
		//initially, the selected algorithm should already be the fifo algorithm
		assertTrue(this.facade.getCurrentSchedulingAlgorithm().equalsIgnoreCase("fifo"));
		
		//change the algorithm to the batch algorithm, so later on it's clear the algorithm was changed to fifo
		List<VehicleOption> batchList= new ArrayList<>();
		SchedulingAlgorithmCreatorBatch batch = new SchedulingAlgorithmCreatorBatch(batchList);
		facade.switchToSchedulingAlgorithm(batch);
		assertNotNull(facade);
		assertTrue(this.facade.getCurrentSchedulingAlgorithm().equalsIgnoreCase("batch"));
		
		//test the use case (switching to the fifo algorithm)
		//1.the system shows the availiable algorithms
		//	--> we want "Batch" and "Fifo" to be returned in the list we get from the Facade
		List<String> possible = facade.getPossibleSchedulingAlgorithms();
		assertEquals(2,possible.size());
		assertTrue(possible.contains("Batch"));
		assertTrue(possible.contains("Fifo"));
		
		//2.the fifo algorithm is selected
		//	--> we want the current scheduling algorithm to be the fifo algorithm
		SchedulingAlgorithmCreatorFifo fifo = new SchedulingAlgorithmCreatorFifo();
		facade.switchToSchedulingAlgorithm(fifo);
		assertTrue(this.facade.getCurrentSchedulingAlgorithm().equalsIgnoreCase("fifo"));
		assertNotNull(facade);
	}
	
	/**
	 * Test the alternate flow (switching to the batch algorithm):
	 * 		1. the system shows a list of the sets of vehicle options for which more than 3 orders are pending in the production queue. 
	 * 			a)there are no sets available
	 * 			b)the system gives the sets of options
	 * 		2. (only if in step 1 'b' applies) one of the sets from the previous sets is selected, and the algorithm is changed to batch
	 */
	@Test
	public void testUseCaseToBatch(){
		//initially, the selected algorithm should be the fifo algorithm
		assertTrue(this.facade.getCurrentSchedulingAlgorithm().equalsIgnoreCase("fifo"));
		
		//test the use case (switching to the fifo algorithm)
		//1. get the sets  of vehicle options
		//		a) there are no sets available
		//			--> this is what's expected, because there are no pending orders yet
		Set<Set<VehicleOption>> batches = facade.getAllVehicleOptionsInPendingOrders();
		assertTrue(batches.isEmpty());
		
		//make it so that 3 pendingorders are in the system with one VehicleOption that's the same
		VehicleOption option = new VehicleOption("black", VehicleOptionCategory.COLOR);
		VehicleSpecification specification = facade.getVehicleSpecificationFromCatalogue("model A");
		
		//create 5 cars and process it
		facade.createNewVehicle(specification);
		
		facade.addPartToVehicle(new VehicleOption("bla", VehicleOptionCategory.BODY));
		facade.addPartToVehicle(new VehicleOption("bla", VehicleOptionCategory.ENGINE));
		facade.addPartToVehicle(new VehicleOption("bla", VehicleOptionCategory.GEARBOX));
		facade.addPartToVehicle(new VehicleOption("bla", VehicleOptionCategory.SEATS));
		facade.addPartToVehicle(new VehicleOption("bla", VehicleOptionCategory.WHEEL));
		facade.addPartToVehicle(option);
		
		facade.processOrder(20);
		
		//		b)the system gives the sets of options
		//			--> in this case, it'll be one set with one VehicleOption
		Set<Set<VehicleOption>> batchOptions = facade.getAllVehicleOptionsInPendingOrders();
		List<Set<VehicleOption>> batchOptionsList = new ArrayList<Set<VehicleOption>>(batchOptions);
		assertEquals(63,batchOptions.size());
		
		//2.the batch algorithm is selected
		//	--> we want the current scheduling algorithm to be the batch algorithm
		List<VehicleOption> batchList = new ArrayList<VehicleOption>(batchOptionsList.get(0));
		SchedulingAlgorithmCreatorBatch batch = new SchedulingAlgorithmCreatorBatch(batchList);
		facade.switchToSchedulingAlgorithm(batch);
		assertNotNull(facade);
		assertTrue(this.facade.getCurrentSchedulingAlgorithm().equalsIgnoreCase("batch"));
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
