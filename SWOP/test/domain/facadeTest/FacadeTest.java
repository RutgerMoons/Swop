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

import view.CustomVehicleCatalogueFiller;
import view.VehicleSpecificationCatalogueFiller;
import domain.assembly.assemblyLine.AssemblyLine;
import domain.assembly.assemblyLine.AssemblyLineState;
import domain.assembly.assemblyLine.IAssemblyLine;
import domain.assembly.workBench.IWorkBench;
import domain.assembly.workBench.WorkBench;
import domain.assembly.workBench.WorkBenchType;
import domain.clock.Clock;
import domain.clock.ImmutableClock;
import domain.company.Company;
import domain.exception.NotImplementedException;
import domain.exception.RoleNotYetAssignedException;
import domain.exception.UnmodifiableException;
import domain.facade.Facade;
import domain.job.task.ITask;
import domain.observer.observers.ClockObserver;
import domain.order.order.CustomOrder;
import domain.order.order.IOrder;
import domain.restriction.BindingRestriction;
import domain.restriction.OptionalRestriction;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreatorBatch;
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
		Clock clock = new Clock(360);
		clock.attachObserver(observer);
		VehicleSpecificationCatalogue catalogue = new VehicleSpecificationCatalogue();
		VehicleSpecificationCatalogueFiller filler = new VehicleSpecificationCatalogueFiller();
		catalogue.initializeCatalogue(filler.getInitialModels());
		List<AssemblyLine> lines = getInitialAssemblyLines(observer, clock.getImmutableClock(), catalogue);
		CustomVehicleCatalogue customCatalogue = new CustomVehicleCatalogue();
		CustomVehicleCatalogueFiller customFiller = new CustomVehicleCatalogueFiller();
		for(String str: customFiller.getInitialModels().keySet()){
			for(CustomVehicle vehicle: customFiller.getInitialModels().get(str))
			customCatalogue.addModel(str, vehicle);
		}
		company = new Company(new HashSet<BindingRestriction>(), new HashSet<OptionalRestriction>(), customCatalogue, catalogue, lines, new Clock(360));
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
		assertEquals(new ImmutableClock(1, 360), company.getImmutableClock());
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
		Vehicle vehicle = new Vehicle(new VehicleSpecification("test",Collections.<VehicleOption> emptySet() , new HashMap<WorkBenchType, Integer>()));
		vehicle.addVehicleOption(new VehicleOption("black", VehicleOptionCategory.COLOR));
		facade.processCustomOrder(vehicle, time);
		
		CustomVehicle custom = new CustomVehicle();
		for(VehicleOption option: vehicle.getVehicleOptions().values()){
			custom.addVehicleOption(option);
		}

		CustomOrder order = new CustomOrder(
				company.getCurrentUser(), custom, 1,
				company.getImmutableClock(), time);
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
		VehicleSpecification specification = company.getVehicleSpecificationFromCatalogue("model A");
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
		assertEquals(specification, vehicle.getVehicleSpecification());
		IAssemblyLine assemblyLine = facade.getAssemblyLines().get(0);
		IWorkBench bench = assemblyLine.getWorkBenches().get(0);
		
		assertNotNull(bench.getCurrentTasks());
		
		ITask task = bench.getCurrentTasks().get(0);
		
		
		facade.completeChosenTaskAtChosenWorkBench(assemblyLine, bench, task, new ImmutableClock(0, 0));
		
		assertTrue(task.isCompleted());
		
	}
	
	@Test
	public void testGetVehicleSpecificationFromCatalogue(){
		assertEquals("model A", facade.getVehicleSpecificationFromCatalogue("model A").getDescription());
	}
	
	@Test
	public void testGetVehicleSpecifications(){
		assertEquals(5, facade.getVehicleSpecifications().size());
	}
	
	@Test
	public void testGetVehicleOptionCategory(){
		assertEquals(VehicleOptionCategory.values().length, facade.getVehicleOptionCategory().size());
	}
	
	@Test
	public void testGetCompletedOrders(){
		facade.createAndAddUser("test", "worker");
		facade.login("test");
		
		VehicleOption option = new VehicleOption("black", VehicleOptionCategory.COLOR);
		VehicleSpecification specification = company.getVehicleSpecificationFromCatalogue("model A");
		facade.createNewVehicle(specification);
		
		
		facade.addPartToVehicle(new VehicleOption("bla", VehicleOptionCategory.BODY));
		facade.addPartToVehicle(new VehicleOption("bla", VehicleOptionCategory.ENGINE));
		facade.addPartToVehicle(new VehicleOption("bla", VehicleOptionCategory.GEARBOX));
		facade.addPartToVehicle(new VehicleOption("bla", VehicleOptionCategory.SEATS));
		facade.addPartToVehicle(new VehicleOption("bla", VehicleOptionCategory.WHEEL));
		facade.addPartToVehicle(option);
		
		facade.processOrder(1);
		
		assertEquals(company.getCompletedOrders("test"), facade.getCompletedOrders());
	}
	
	@Test
	public void testGetPendingOrders(){
		facade.createAndAddUser("test", "worker");
		facade.login("test");
		
		VehicleOption option = new VehicleOption("black", VehicleOptionCategory.COLOR);
		VehicleSpecification specification = company.getVehicleSpecificationFromCatalogue("model A");
		facade.createNewVehicle(specification);
		
		
		facade.addPartToVehicle(new VehicleOption("bla", VehicleOptionCategory.BODY));
		facade.addPartToVehicle(new VehicleOption("bla", VehicleOptionCategory.ENGINE));
		facade.addPartToVehicle(new VehicleOption("bla", VehicleOptionCategory.GEARBOX));
		facade.addPartToVehicle(new VehicleOption("bla", VehicleOptionCategory.SEATS));
		facade.addPartToVehicle(new VehicleOption("bla", VehicleOptionCategory.WHEEL));
		facade.addPartToVehicle(option);
		
		facade.processOrder(1);
		
		
		assertEquals(company.getPendingOrders("test"), facade.getPendingOrders());
		assertEquals(1, facade.getPendingOrders().size());
	}
	
	@Test
	public void testGetSchedulingAlgorithm(){
		assertEquals("Fifo", facade.getCurrentSchedulingAlgorithm());
	}
	
	@Test
	public void testGetCustomTasks(){
		assertEquals(company.getCustomTasksDescription(), facade.getCustomTasks());
	}
	
	@Test
	public void testGetRemainingVehicleOptions(){
		facade.createNewVehicle(company.getVehicleSpecification("model A"));
		assertEquals(company.getStillAvailableVehicleOptions(VehicleOptionCategory.COLOR), facade.getRemainingVehicleOptions(VehicleOptionCategory.COLOR));
	}
	
	@Test
	public void testGetCustomOptions(){
		
		assertEquals(6, facade.getCustomOptions("spraying car bodies").size());
	}
	
	
	@Test
	public void testGetAverageDays(){
		assertEquals(company.getAverageDays(), facade.getAverageDays());
	}
	
	@Test
	public void testGetMedianDays(){
		assertEquals(company.getMedianDays(), facade.getMedianDays());
	}
	
	@Test
	public void testGetDetailedDays(){
		assertEquals(company.getDetailedDays(), facade.getDetailedDays());
	}
	
	@Test
	public void testGetAverageDelays(){
		assertEquals(company.getAverageDelays(), facade.getAverageDelays());
	}
	
	@Test
	public void testGetMedianDelays(){
		assertEquals(company.getMedianDelays(), facade.getMedianDelays());
	}
	
	@Test
	public void testGetDetailedDelays(){
		assertEquals(company.getDetailedDelays().size(), facade.getDetailedDelays().size());
	}
	
	@Test
	public void testSwitchToSchedulingAlgorithm(){
		facade.switchToSchedulingAlgorithm(new SchedulingAlgorithmCreatorBatch(new ArrayList<VehicleOption>()));
		assertEquals("Batch", facade.getCurrentSchedulingAlgorithm());
	}
	
	@Test
	public void testChangeState(){
		facade.changeState(facade.getAssemblyLines().get(0), AssemblyLineState.BROKEN, new ImmutableClock(0,0));
		assertEquals(AssemblyLineState.BROKEN, facade.getAssemblyLines().get(0).getState());
	}
	
	@Test
	public void testGetAssemblyLineStates(){
		assertEquals(3, facade.getAssemblyLineStates().size());
	}
	
	@Test
	public void testGetPossibleSchedulingAlgorithms(){
		List<String> algorithms = new ArrayList<String>();
		algorithms = facade.getPossibleSchedulingAlgorithms();
		assertEquals(2,algorithms.size());
		assertTrue(algorithms.contains("Batch"));
		assertTrue(algorithms.contains("Fifo"));
	}
	
	
	private static List<AssemblyLine> getInitialAssemblyLines(ClockObserver clockObserver, ImmutableClock clock, VehicleSpecificationCatalogue catalogue) {
		List<AssemblyLine> assemblyLines = new ArrayList<AssemblyLine>();
		
		Set<VehicleSpecification> specifications1 = new HashSet<>();
		specifications1.add(catalogue.getCatalogue().get("model A"));
		specifications1.add(catalogue.getCatalogue().get("model B"));
		AssemblyLine line1 = new AssemblyLine(clockObserver, clock, AssemblyLineState.OPERATIONAL, specifications1);
		
		
		Set<VehicleSpecification> specifications2 = new HashSet<>();
		specifications2.add(catalogue.getCatalogue().get("model A"));
		specifications2.add(catalogue.getCatalogue().get("model B"));
		specifications2.add(catalogue.getCatalogue().get("model C"));
		AssemblyLine line2 = new AssemblyLine(clockObserver, clock, AssemblyLineState.OPERATIONAL, specifications2);
		
		Set<VehicleSpecification> specifications3 = new HashSet<>();
		specifications3.add(catalogue.getCatalogue().get("model A"));
		specifications3.add(catalogue.getCatalogue().get("model B"));
		specifications3.add(catalogue.getCatalogue().get("model C"));
		specifications3.add(catalogue.getCatalogue().get("model X"));
		specifications3.add(catalogue.getCatalogue().get("model Y"));
		AssemblyLine line3= new AssemblyLine(clockObserver, clock, AssemblyLineState.OPERATIONAL, specifications3);
		
		Set<String> responsibilities = new HashSet<>();
		responsibilities.add("Body");
		responsibilities.add("Color");
		WorkBench body1 = new WorkBench(responsibilities, WorkBenchType.BODY);
		WorkBench body2 = new WorkBench(responsibilities, WorkBenchType.BODY);
		WorkBench body3 = new WorkBench(responsibilities, WorkBenchType.BODY);
		
		responsibilities = new HashSet<>();
		responsibilities.add("Engine");
		responsibilities.add("Gearbox");
		WorkBench drivetrain1 = new WorkBench(responsibilities, WorkBenchType.DRIVETRAIN);
		WorkBench drivetrain2 = new WorkBench(responsibilities, WorkBenchType.DRIVETRAIN);
		WorkBench drivetrain3 = new WorkBench(responsibilities, WorkBenchType.DRIVETRAIN);
		
		responsibilities = new HashSet<>();
		responsibilities.add("Seat");
		responsibilities.add("Airco");
		responsibilities.add("Spoiler");
		responsibilities.add("Wheel");
		WorkBench accessories1 = new WorkBench(responsibilities, WorkBenchType.ACCESSORIES);
		WorkBench accessories2 = new WorkBench(responsibilities, WorkBenchType.ACCESSORIES);
		WorkBench accessories3 = new WorkBench(responsibilities, WorkBenchType.ACCESSORIES);
		
		responsibilities = new HashSet<>();
		responsibilities.add("Storage");
		responsibilities.add("Protection");
		WorkBench cargo = new WorkBench(responsibilities, WorkBenchType.CARGO);
		
		responsibilities = new HashSet<>();
		responsibilities.add("Certification");
		WorkBench certificiation = new WorkBench(responsibilities, WorkBenchType.CERTIFICATION);
		
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
