package domain.facadeTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import domain.assembly.assemblyLine.AssemblyLine;
import domain.assembly.assemblyLine.AssemblyLineState;
import domain.assembly.assemblyLine.IAssemblyLine;
import domain.assembly.workBench.IWorkBench;
import domain.assembly.workBench.WorkBench;
import domain.assembly.workBench.WorkbenchType;
import domain.clock.Clock;
import domain.clock.ImmutableClock;
import domain.company.Company;
import domain.exception.NotImplementedException;
import domain.exception.RoleNotYetAssignedException;
import domain.exception.UnmodifiableException;
import domain.facade.Facade;
import domain.observer.observers.ClockObserver;
import domain.order.CustomOrder;
import domain.order.IOrder;
import domain.restriction.BindingRestriction;
import domain.restriction.OptionalRestriction;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreatorFifo;
import domain.users.AccessRight;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.catalogue.CustomVehicleCatalogue;
import domain.vehicle.catalogue.VehicleSpecificationCatalogue;
import domain.vehicle.vehicle.CustomVehicle;
import domain.vehicle.vehicle.IVehicle;
import domain.vehicle.vehicle.Vehicle;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

public class FacadeTest {

	private Facade facade;
	private Company company;
	@Before
	public void initialize() {
		ClockObserver observer = new ClockObserver();
		Clock clock = new Clock();
		clock.attachObserver(observer);
		VehicleSpecificationCatalogue catalogue = new VehicleSpecificationCatalogue();
		List<AssemblyLine> lines = getInitialAssemblyLines(observer, clock.getImmutableClock(), catalogue);
		
		company = new Company(new HashSet<BindingRestriction>(), new HashSet<OptionalRestriction>(), new CustomVehicleCatalogue(), catalogue, lines, new Clock());
		facade = new Facade(company);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void illegalConstructor(){
		new Facade(null);
	}
	
	@Test
	public void teststartNewDay() {
		assertNotNull(facade);
		facade.startNewDay();
		assertNotNull(facade);
		assertEquals(new ImmutableClock(1, 360), company.getUnmodifiableClock());
	}
	
	@Test
	public void getAccessRightsTest() {
		facade.createAndAddUser("Bartje", "worker");
		facade.login("Bartje");
		List<AccessRight> AR = facade.getAccessRights();
		assertEquals(2, AR.size());
		assertTrue(AR.contains(AccessRight.ASSEMBLE));
		assertTrue(AR.contains(AccessRight.CHECKLINE));
	}
	
	@Test (expected = NullPointerException.class)
	public void getAccessRightsTestError() {
		facade.getAccessRights();
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void getCarModelSpecificationFromCatalogueTestError() {
		facade.getVehicleSpecificationFromCatalogue("Error");
	}
	
	@Test
	public void loginTestError() {
		try {
			facade.login("test");
		} catch (RoleNotYetAssignedException r) {}
	}
	
	@Test
	public void loginTest() {
		facade.createAndAddUser("test", "worker");
		facade.login("test");
		assertEquals("test", company.getCurrentUser());
	}
	
	@Test (expected=NullPointerException.class)
	public void logout() {
		facade.createAndAddUser("test", "worker");
		facade.login("test");
		facade.logout();
		assertEquals("test", company.getCurrentUser());
	}
	
	@Test
	public void getAllCarOptionsInPendingOrdersTest() {
		assertEquals(0, facade.getAllVehicleOptionsInPendingOrders().size());
	}
	
	@Test
	public void processCustomOrderTest() {
		facade.createAndAddUser("test", "worker");
		facade.login("test");
		ImmutableClock time = new ImmutableClock(2, 30);
		Vehicle vehicle = new Vehicle(new VehicleSpecification("test",Collections.<VehicleOption> emptySet() , new HashMap<WorkbenchType, Integer>()));
		vehicle.addCarPart(new VehicleOption("black", VehicleOptionCategory.COLOR));
		facade.processCustomOrder(vehicle, time);
		
		CustomVehicle custom = new CustomVehicle();
		for(VehicleOption option: vehicle.getVehicleOptions().values()){
			custom.addCarPart(option);
		}

		CustomOrder order = new CustomOrder(
				company.getCurrentUser(), custom, 1,
				company.getUnmodifiableClock(), time);
		company.addOrder(order);
		assertTrue(facade.getPendingOrders().contains(order));
	
	}
	
	@Test (expected = NullPointerException.class)
	public void processOrdertest() throws IllegalStateException, UnmodifiableException, NotImplementedException {
		facade.processOrder(5);
	}

	@Test
	public void testAddToVehicleAndCompleteAtWorkbench(){
		facade.createAndAddUser("test", "worker");
		facade.login("test");
		
		VehicleOption option = new VehicleOption("black", VehicleOptionCategory.COLOR);
		Set<VehicleOption> options = new HashSet<>();
		options.add(new VehicleOption("bla", VehicleOptionCategory.BODY));
		options.add(new VehicleOption("bla", VehicleOptionCategory.ENGINE));
		options.add(new VehicleOption("bla", VehicleOptionCategory.GEARBOX));
		options.add(new VehicleOption("bla", VehicleOptionCategory.SEATS));
		options.add(new VehicleOption("bla", VehicleOptionCategory.WHEEL));
		options.add(option);
		VehicleSpecification specification = new VehicleSpecification("model A", options, new HashMap<WorkbenchType, Integer>());
		facade.createNewVehicle(specification);
		
		
		facade.addPartToVehicle(new VehicleOption("bla", VehicleOptionCategory.BODY));
		facade.addPartToVehicle(new VehicleOption("bla", VehicleOptionCategory.ENGINE));
		facade.addPartToVehicle(new VehicleOption("bla", VehicleOptionCategory.GEARBOX));
		facade.addPartToVehicle(new VehicleOption("bla", VehicleOptionCategory.SEATS));
		facade.addPartToVehicle(new VehicleOption("bla", VehicleOptionCategory.WHEEL));
		facade.addPartToVehicle(option);
		
		facade.processOrder(1);
		
		IOrder order = facade.getPendingOrders().get(0);
		IVehicle vehicle = order.getDescription();
		
		assertTrue(vehicle.getVehicleOptions().containsValue(option));
		assertEquals(specification, vehicle.getSpecification());
		IAssemblyLine assemblyLine = facade.getAssemblyLines().get(0);
		IWorkBench bench = assemblyLine.getWorkbenches().get(0);
		
		assertNotNull(bench.getCurrentTasks());
	}
	
	@Test
	public void testCompleteChosenTaskAtWorkbench(){
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private static List<AssemblyLine> getInitialAssemblyLines(ClockObserver clockObserver, ImmutableClock clock, VehicleSpecificationCatalogue catalogue) {
		List<AssemblyLine> assemblyLines = new ArrayList<AssemblyLine>();
		
		Set<VehicleSpecification> specifications = new HashSet<>();
		specifications.add(catalogue.getCatalogue().get("model A"));
		specifications.add(catalogue.getCatalogue().get("model B"));
		AssemblyLine line1 = new AssemblyLine(clockObserver, clock, AssemblyLineState.OPERATIONAL, specifications);
		
		
		specifications = new HashSet<>();
		specifications.add(catalogue.getCatalogue().get("model A"));
		specifications.add(catalogue.getCatalogue().get("model B"));
		specifications.add(catalogue.getCatalogue().get("model C"));
		AssemblyLine line2 = new AssemblyLine(clockObserver, clock, AssemblyLineState.OPERATIONAL, specifications);
		
		specifications = new HashSet<>();
		specifications.add(catalogue.getCatalogue().get("model A"));
		specifications.add(catalogue.getCatalogue().get("model B"));
		specifications.add(catalogue.getCatalogue().get("model C"));
		specifications.add(catalogue.getCatalogue().get("model X"));
		specifications.add(catalogue.getCatalogue().get("model Y"));
		AssemblyLine line3= new AssemblyLine(clockObserver, clock, AssemblyLineState.OPERATIONAL, specifications);
		
		Set<String> responsibilities = new HashSet<>();
		responsibilities.add("Body");
		responsibilities.add("Color");
		WorkBench body1 = new WorkBench(responsibilities, WorkbenchType.BODY);
		WorkBench body2 = new WorkBench(responsibilities, WorkbenchType.BODY);
		WorkBench body3 = new WorkBench(responsibilities, WorkbenchType.BODY);
		
		responsibilities = new HashSet<>();
		responsibilities.add("Engine");
		responsibilities.add("Gearbox");
		WorkBench drivetrain1 = new WorkBench(responsibilities, WorkbenchType.DRIVETRAIN);
		WorkBench drivetrain2 = new WorkBench(responsibilities, WorkbenchType.DRIVETRAIN);
		WorkBench drivetrain3 = new WorkBench(responsibilities, WorkbenchType.DRIVETRAIN);
		
		responsibilities = new HashSet<>();
		responsibilities.add("Seat");
		responsibilities.add("Airco");
		responsibilities.add("Spoiler");
		responsibilities.add("Wheel");
		WorkBench accessories1 = new WorkBench(responsibilities, WorkbenchType.ACCESSORIES);
		WorkBench accessories2 = new WorkBench(responsibilities, WorkbenchType.ACCESSORIES);
		WorkBench accessories3 = new WorkBench(responsibilities, WorkbenchType.ACCESSORIES);
		
		responsibilities = new HashSet<>();
		responsibilities.add("Storage");
		responsibilities.add("Protection");
		WorkBench cargo = new WorkBench(responsibilities, WorkbenchType.CARGO);
		
		responsibilities = new HashSet<>();
		responsibilities.add("Certification");
		WorkBench certificiation = new WorkBench(responsibilities, WorkbenchType.CERTIFICATION);
		
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
