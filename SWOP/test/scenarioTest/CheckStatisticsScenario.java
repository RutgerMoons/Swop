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
import domain.assembly.workBench.WorkBench;
import domain.assembly.workBench.WorkBenchType;
import domain.clock.Clock;
import domain.clock.ImmutableClock;
import domain.company.Company;
import domain.facade.Facade;
import domain.observer.observers.ClockObserver;
import domain.order.Delay;
import domain.restriction.BindingRestriction;
import domain.restriction.OptionalRestriction;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreatorFifo;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.catalogue.CustomVehicleCatalogue;
import domain.vehicle.catalogue.VehicleSpecificationCatalogue;
import domain.vehicle.vehicle.CustomVehicle;
import domain.vehicle.vehicleOption.VehicleOption;


/**
 * Scenario that tests the output for the use case: Check Production Statistics.
 *
 */
public class CheckStatisticsScenario {
	
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
	 * Test the use case:
	 * 		1. the system shows the available statistics
	 */
	@Test
	public void noStatisticsAvailableTest(){
		//get the average amount of vehicles produced in a day
		assertEquals(0,facade.getAverageDays());
		//get the median amount of vehicles produced in a day
		assertEquals(0,facade.getMedianDays());
		// get the amount of cars produced for a number amount of days
		//	--> it'll be asked for 2 days, so the List should contain 2 values
		List<Integer> detailedDays = facade.getDetailedDays();
		assertEquals(2,detailedDays.size());
		Integer zero = 0;
		assertEquals(zero,detailedDays.get(0));
		assertEquals(zero,detailedDays.get(1));
		// get the average delay on an order
		assertEquals(0,facade.getAverageDelays());
		// get the median delay on an order
		assertEquals(0,facade.getMedianDelays());
		// The details of the latest delays.
		//	--> the List should contain 2 values
		List<Delay> detailedDelays = facade.getDetailedDelays();
		assertEquals(2,detailedDelays.size());
		Delay noDelay = new Delay(new ImmutableClock(0,0), new ImmutableClock(0,0));
		int delay = noDelay.getDelay();
		assertEquals(delay,detailedDelays.get(0).getDelay());
		assertEquals(delay,detailedDelays.get(1).getDelay());
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
