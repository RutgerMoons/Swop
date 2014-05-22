package domain.company;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
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
import domain.job.task.ITask;
import domain.job.task.Task;
import domain.observer.observers.ClockObserver;
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
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

public class CompanyTest {

	private Company company;
	@Before
	public void initialise() {
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
	}

	@Test (expected = IllegalArgumentException.class)
	public void test() {
		new Company(null, null, null, null, null, null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void test2() {
		new Company(new HashSet<BindingRestriction>(), null, null, null, null, null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void test3() {
		new Company(new HashSet<BindingRestriction>(), new HashSet<OptionalRestriction>(), null, null, null, null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void test4() {
		new Company(new HashSet<BindingRestriction>(), new HashSet<OptionalRestriction>(), new CustomVehicleCatalogue(), null, null, null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void test5() {
		new Company(new HashSet<BindingRestriction>(), new HashSet<OptionalRestriction>(), new CustomVehicleCatalogue(), new VehicleSpecificationCatalogue(), null, null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void test6() {
		new Company(new HashSet<BindingRestriction>(), new HashSet<OptionalRestriction>(), new CustomVehicleCatalogue(), new VehicleSpecificationCatalogue(), new ArrayList<AssemblyLine>(), null);
	}
	
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testAddPartToModel(){
		company.addPartToModel(null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testCompleteChosenTaskAtWorkBench(){
		company.completeChosenTaskAtChosenWorkBench(null, null, null, null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testCompleteChosenTaskAtWorkBench2(){
		IAssemblyLine a = new AssemblyLine(new ClockObserver(), new ImmutableClock(0, 0), AssemblyLineState.BROKEN, new HashSet<VehicleSpecification>());
		
		company.completeChosenTaskAtChosenWorkBench(a, null, null, null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testCompleteChosenTaskAtWorkBench3(){
		IAssemblyLine a = new AssemblyLine(new ClockObserver(), new ImmutableClock(0, 0), AssemblyLineState.BROKEN, new HashSet<VehicleSpecification>());
		IWorkBench bench = new WorkBench(WorkBenchType.ACCESSORIES);
		company.completeChosenTaskAtChosenWorkBench(a, bench, null, null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testCompleteChosenTaskAtWorkBench4(){
		IAssemblyLine a = new AssemblyLine(new ClockObserver(), new ImmutableClock(0, 0), AssemblyLineState.BROKEN, new HashSet<VehicleSpecification>());
		IWorkBench bench = new WorkBench(WorkBenchType.ACCESSORIES);
		company.completeChosenTaskAtChosenWorkBench(a, bench, new Task("blabla"), null);
	}
	
	@Test
	public void testCompleteChosenTaskAtWorkBench5(){
		IAssemblyLine a = company.getAssemblyLines().get(0);
		IWorkBench bench = new WorkBench(WorkBenchType.ACCESSORIES);
		company.completeChosenTaskAtChosenWorkBench(a, bench, new Task("blabla"), new ImmutableClock(0, 0));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testCreateAndAddUser(){
		company.createAndAddUser("", "");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testCreateAndAddUser2(){
		company.createAndAddUser(null, "");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testCreateAndAddUser3(){
		company.createAndAddUser("a", "");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testCreateAndAddUser4(){
		company.createAndAddUser("a", null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testCreateNewVehicle(){
		company.createNewVehicle(null);
	}
	
	@Test
	public void testLoginLogoutGetAccessRights(){
		company.createAndAddUser("jos", "garageholder");
		company.login("jos");
		assertEquals("jos", company.getCurrentUser());
		assertTrue(company.getAccessRights().contains(AccessRight.ORDER));
		company.logout();
	}

	@Test (expected = IllegalArgumentException.class)
	public void testGetVehicleSpecificationFromCatalogue(){
		company.getVehicleSpecificationFromCatalogue(null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testGetVehicleSpecificationFromCatalogue2(){
		company.getVehicleSpecificationFromCatalogue("");
	}
	
	@Test
	public void testGetCurrentSchedulingAlgorithm(){
		assertEquals("Fifo", company.getCurrentSchedulingAlgorithm());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testLogin(){
		company.login(null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testLogin2(){
		company.login("");
	}
	
	@Test
	public void testStartNewDay(){
		company.startNewDay();
		assertEquals(new ImmutableClock(1, 360), company.getImmutableClock());
	}
	
	@Test
	public void testSwitchToSchedulingAlgorithm(){
		company.switchToSchedulingAlgorithm(new SchedulingAlgorithmCreatorBatch(new ArrayList<VehicleOption>()));
		assertEquals("Batch", company.getCurrentSchedulingAlgorithm());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testSwitchToIllegalSchedulingAlgorithm(){
		company.switchToSchedulingAlgorithm(null);
	}
	
	@Test
	public void testGetVehicleSpecification(){
		assertEquals("model A", company.getVehicleSpecification("model A").getDescription());
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testGetIllegalVehicleSpecification(){
		company.getVehicleSpecification(null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testGetIllegalVehicleSpecification2(){
		company.getVehicleSpecification("");
	}
	
	@Test
	public void testGetVehicleSpecifications(){
		assertTrue(company.getVehicleSpecifications().contains("model A"));
	}
	
	@Test
	public void testGetCompletedOrders(){
		company.createAndAddUser("jos", "garageholder");
		company.login("jos");
		assertNotNull(company.getCompletedOrders("jos"));
	}

	@Test (expected = IllegalArgumentException.class)
	public void testGetIllegalCompletedOrders(){
		company.getCompletedOrders(null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testGetIllegalCompletedOrders2(){
		company.getCompletedOrders("");
	}

	@Test
	public void testGetCustomTasksDescription(){
		assertNotNull(company.getCustomTasksDescription());
		
		assertTrue(company.getCustomTasksDescription().contains("spraying car bodies"));
	}
	
	@Test
	public void testGetStillAvailableVehicleOptions(){
		company.createNewVehicle(company.getVehicleSpecification("model A"));
		assertNotNull(company.getStillAvailableVehicleOptions(VehicleOptionCategory.BODY));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testIllegalGetStillAvailableVehicleOptions(){
		company.getStillAvailableVehicleOptions(null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testGetPendingOrdersIllegal(){
		company.getPendingOrders(null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testGetPendingOrdersIllegal2(){
		company.getPendingOrders("");
	}
	
	@Test
	public void testGetSpecificCustomTasks(){
		assertNotNull(company.getSpecificCustomTasks("spraying car bodies"));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testGetIllegalSpecificCustomTasks(){
		company.getSpecificCustomTasks(null);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testGetIllegalSpecificCustomTasks2(){
		company.getSpecificCustomTasks("");
	}
	@Test
	public void testGetAverageDays(){
		assertEquals(0, company.getAverageDays());
	}
	
	@Test
	public void testGetMedianDays(){
		assertEquals(0, company.getMedianDays());
	}
	
	@Test
	public void testGetDetailedDays(){
		company.startNewDay();
		assertNotNull(company.getDetailedDays());
	}
	
	@Test
	public void testGetDetailedDays2(){
		company.startNewDay();
		company.startNewDay();
		company.startNewDay();
		assertNotNull(company.getDetailedDays());
	}
	
	@Test
	public void testGetAverageDelays(){
		assertEquals(0, company.getAverageDelays());
	}
	
	@Test
	public void testGetMedianDelays(){
		assertEquals(0, company.getMedianDelays());
	}
	
	@Test
	public void testGetDetailedDelays(){
		company.startNewDay();
		assertNotNull(company.getDetailedDelays());
	}
	
	@Test
	public void testAddOrder(){
		CustomVehicle vehicle = new CustomVehicle();
		company.createAndAddUser("jos", "custom car shop manager");
		company.login("jos");
		company.addOrder(vehicle, new ImmutableClock(0, 0));
		assertFalse(company.getPendingOrders("jos").isEmpty());
		
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testAddIllegalOrder(){
		company.addOrder(null, null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testAddIllegalOrder2(){
		company.addOrder(new CustomVehicle(), null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testProcessIllegalOrder(){
		company.processOrder(0);
	}
	
	@Test (expected = IllegalStateException.class)
	public void testProcessOrderIllegalVehicle(){
		company.createNewVehicle(company.getVehicleSpecification("model A"));
		company.processOrder(1);
	}
	
	@Test
	public void testGetAllVehicleOptionsInPendingOrders(){
		assertNotNull(company.getAllVehicleOptionsInPendingOrders());
	}
	
	
	@Test
	public void testChangeState(){
		IAssemblyLine assemblyLine = company.getAssemblyLines().get(0);
		company.changeState(assemblyLine, AssemblyLineState.MAINTENANCE, new ImmutableClock(0, 0));
		assertEquals(AssemblyLineState.MAINTENANCE, assemblyLine.getState());
		
		company.changeState(assemblyLine, AssemblyLineState.BROKEN, new ImmutableClock(0, 0));
		assertEquals(AssemblyLineState.BROKEN, assemblyLine.getState());
		
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testChangeIllegalState(){
		company.changeState(null, null, null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testChangeIllegalState2(){
		company.changeState(company.getAssemblyLines().get(0), null, null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testChangeIllegalState3(){
		company.changeState(company.getAssemblyLines().get(0), AssemblyLineState.BROKEN, null);
	}
	
	@Test
	public void testAddAndCompleteAndProcessAndPendingAndAssemblyLines(){
		company.createAndAddUser("jos", "garageholder");
		company.login("jos");
		VehicleOption option = new VehicleOption("black", VehicleOptionCategory.COLOR);
		VehicleSpecification specification = company.getVehicleSpecificationFromCatalogue("model A");
		company.createNewVehicle(specification);
		
		
		company.addPartToModel(new VehicleOption("bla", VehicleOptionCategory.BODY));
		company.addPartToModel(new VehicleOption("bla", VehicleOptionCategory.ENGINE));
		company.addPartToModel(new VehicleOption("bla", VehicleOptionCategory.GEARBOX));
		company.addPartToModel(new VehicleOption("bla", VehicleOptionCategory.SEATS));
		company.addPartToModel(new VehicleOption("bla", VehicleOptionCategory.WHEEL));
		company.addPartToModel(option);
		
		company.processOrder(1);
		
		IOrder order = company.getPendingOrders("jos").get(0);
		IVehicle vehicle = order.getDescription();
		
		assertTrue(vehicle.getVehicleOptions().containsValue(option));
		assertEquals(specification, vehicle.getVehicleSpecification());
		IAssemblyLine assemblyLine = company.getAssemblyLines().get(0);
		IWorkBench bench = assemblyLine.getWorkBenches().get(0);
		
		assertNotNull(bench.getCurrentTasks());
		
		ITask task = bench.getCurrentTasks().get(0);
		
		
		company.completeChosenTaskAtChosenWorkBench(assemblyLine, bench, task, new ImmutableClock(0, 200));
		
		assertTrue(task.isCompleted());
		assertEquals(new ImmutableClock(0, 200), company.getImmutableClock());
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
		WorkBench body1 = new WorkBench( WorkBenchType.BODY);
		WorkBench body2 = new WorkBench( WorkBenchType.BODY);
		WorkBench body3 = new WorkBench( WorkBenchType.BODY);
		
		responsibilities = new HashSet<>();
		responsibilities.add("Engine");
		responsibilities.add("Gearbox");
		WorkBench drivetrain1 = new WorkBench( WorkBenchType.DRIVETRAIN);
		WorkBench drivetrain2 = new WorkBench( WorkBenchType.DRIVETRAIN);
		WorkBench drivetrain3 = new WorkBench( WorkBenchType.DRIVETRAIN);
		
		responsibilities = new HashSet<>();
		responsibilities.add("Seat");
		responsibilities.add("Airco");
		responsibilities.add("Spoiler");
		responsibilities.add("Wheel");
		WorkBench accessories1 = new WorkBench( WorkBenchType.ACCESSORIES);
		WorkBench accessories2 = new WorkBench( WorkBenchType.ACCESSORIES);
		WorkBench accessories3 = new WorkBench( WorkBenchType.ACCESSORIES);
		
		responsibilities = new HashSet<>();
		responsibilities.add("Storage");
		responsibilities.add("Protection");
		WorkBench cargo = new WorkBench( WorkBenchType.CARGO);
		
		responsibilities = new HashSet<>();
		responsibilities.add("Certification");
		WorkBench certificiation = new WorkBench( WorkBenchType.CERTIFICATION);
		
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
