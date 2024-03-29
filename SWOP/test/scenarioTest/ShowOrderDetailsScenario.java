package scenarioTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import view.ClientCommunication;
import view.CustomVehicleCatalogueFiller;
import view.IClientCommunication;
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
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

public class ShowOrderDetailsScenario {


	private Facade facade;
	private IClientCommunication communication;
	@Before
	public void initialize(){		
		Company company = this.initializeCompany();
		facade = new Facade(company);
		facade.createAndAddUser("jef", "garageholder");
		facade.login("jef");

		communication = new ClientCommunication();
	}

	/**
	 * Test the use case of showing the order details.
	 * 		1. We order a new vehicle
	 * 		2. We check the order details of the recently ordered order
	 */
	@Test
	public void test(){
		//We know the program didn't add any order, so the lists are empty.
		assertTrue(facade.getPendingOrders().isEmpty());
		assertTrue(facade.getCompletedOrders().isEmpty());

		//We check if the initialization has been done correctly.
		assertEquals(5, facade.getVehicleSpecifications().size());

		//We want to create a vehicle from "model A", so we get it from "model A".
		VehicleSpecification specification = facade.getVehicleSpecificationFromCatalogue("model A");

		//We create a new vehicle on the basis of "model A".
		facade.createNewVehicle(specification);

		Set<String> allParts = new HashSet<>();
		//Then we add the necessary options to the vehicle, so it is ready to be ordered.
		for(VehicleOption option: getOptions()){
			facade.addPartToVehicle(option);
			allParts.add(option.toString());
		}

		//We want 3 vehicles to be manufactured.
		int quantity = 3;

		//We process the order with our quantity.
		ImmutableClock clock = facade.processOrder(quantity);

		//The order is divided over the 3 assembly lines, so 1 vehicle on each line. 
		//We know that model A needs 50 minutes at a workbench and it has to be 
		//assembled at 3 workbenches. So it needs 150 minutes to be assembled.
		assertEquals(new ImmutableClock(0, 510), clock);

		//We check if the pending orders isn't empty, because we just ordered one.
		assertFalse(facade.getPendingOrders().isEmpty());

		//The completed orders must be empty, because no order is completed.
		assertTrue(facade.getCompletedOrders().isEmpty());

		//We know we ordered just 1 order, so the only order available in pending orders is ours.
		//We check if the model is build according to "model A", because we chose that one.
		assertEquals("model A", facade.getPendingOrders().get(0).getVehicleSpecification().getDescription());

		//We set the outputstream of the clientcommunication to a specific outputstream,
		//so we can see what the output is.
		ByteArrayOutputStream myout = new ByteArrayOutputStream();
		System.setOut(new PrintStream(myout));

		//We show the orderdetails of our recent ordered order.
		communication.showOrderDetails(facade.getPendingOrders().get(0));

		
		/*
		 * This is the structure of the output:
		 * 
		 * Orderdetails:
		 * 3 model A
		 * Chosen vehicle options: Body: sedan Seats: leather black Gearbox: 5 speed manual  Wheel: sports Color: red Engine: performance 2.5l V6 
		 * Order Time: day 0, 6 hours, 0 minutes.
		 * (Expected) Completion Time: day 0, 2 hours, 30 minutes.
		 */
		
		String result = myout.toString();
		assertTrue(result.contains("Orderdetails:"));
		assertTrue(result.contains("Chosen vehicle options:"));
		assertTrue(result.contains("Order Time: day 0, 6 hours, 0 minutes."));
		assertTrue(result.contains("(Expected) Completion Time: day 0, 8 hours, 30 minutes."));

		//We check if all the parts are in the result.
		for(String str: allParts){
			assertTrue(result.contains(str));
		}
	
	}



	private List<VehicleOption> getOptions(){
		List<VehicleOption> options = new ArrayList<>();
		for (VehicleOptionCategory type : facade.getVehicleOptionCategory()) {
			List<VehicleOption> parts = facade.getRemainingVehicleOptions(type);
			if(parts!=null && !parts.isEmpty()){
				options.add(parts.get(0));
			}
		}
		return options;
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
