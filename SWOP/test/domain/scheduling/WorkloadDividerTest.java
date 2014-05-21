package domain.scheduling;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;

import domain.assembly.assemblyLine.AssemblyLine;
import domain.assembly.assemblyLine.AssemblyLineState;
import domain.assembly.assemblyLine.IAssemblyLine;
import domain.assembly.workBench.IWorkBench;
import domain.assembly.workBench.WorkBench;
import domain.assembly.workBench.WorkBenchType;
import domain.clock.Clock;
import domain.clock.ImmutableClock;
import domain.exception.TimeToStartNewDayException;
import domain.job.job.IJob;
import domain.observer.observers.AssemblyLineObserver;
import domain.observer.observers.ClockObserver;
import domain.observer.observers.OrderBookObserver;
import domain.order.order.StandardOrder;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreatorFifo;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicle.Vehicle;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

public class WorkloadDividerTest {

	private WorkloadDivider workloadDivider;
	private OrderBookObserver orderBookObserver;
	private AssemblyLineObserver assemblyLineObserver;
	private Set<VehicleSpecification> specifications;


	@Before
	public void testConstructor2() {
		Clock clock = new Clock(360);
		ClockObserver clockObserver = new ClockObserver();
		clock.attachObserver(clockObserver);
		ImmutableClock currentTime = clock.getImmutableClock();

		Set<VehicleOption> parts = new HashSet<>();
		parts.add(new VehicleOption("sport", VehicleOptionCategory.BODY));
		Map<WorkBenchType, Integer> map = new HashMap<WorkBenchType, Integer>();
		map.put(WorkBenchType.ACCESSORIES, 20);
		map.put(WorkBenchType.BODY, 20);
		map.put(WorkBenchType.DRIVETRAIN, 20);
		VehicleSpecification template = new VehicleSpecification("model", parts, map, new HashSet<VehicleOption>());
		VehicleSpecification template2 = new VehicleSpecification("model B", parts, new HashMap<WorkBenchType, Integer>(), new HashSet<VehicleOption>());
		VehicleSpecification template3 = new VehicleSpecification("model C", parts, new HashMap<WorkBenchType, Integer>(), new HashSet<VehicleOption>());
		specifications = new HashSet<VehicleSpecification>();
		specifications.add(template);
		specifications.add(template2);
		specifications.add(template3);

		ArrayList<AssemblyLine> lines = new ArrayList<>();

		AssemblyLine line2 = new AssemblyLine(clockObserver, currentTime, AssemblyLineState.BROKEN, specifications);
		lines.add(line2);

		AssemblyLine line1 = new AssemblyLine(clockObserver, currentTime, AssemblyLineState.OPERATIONAL, specifications);
		lines.add(line1);

		orderBookObserver = new OrderBookObserver();
		assemblyLineObserver = new AssemblyLineObserver();

		workloadDivider = new WorkloadDivider(lines, orderBookObserver, assemblyLineObserver);
		assertNotNull(workloadDivider);
	}

	@Test
	public void testConstructor1() {
		OrderBookObserver orderBookObserver = new OrderBookObserver();
		AssemblyLineObserver assemblyLineObserver = new AssemblyLineObserver();
		WorkloadDivider w = new WorkloadDivider(new ArrayList<AssemblyLine>(), orderBookObserver, assemblyLineObserver);
		assertNotNull(w);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testConstructorException1() {
		new WorkloadDivider(null, null, null);
	}

	@Test
	public void testGetCurrentSchedulingAlgorithmNoAssemblyLines() {
		WorkloadDivider w = new WorkloadDivider(new ArrayList<AssemblyLine>(), orderBookObserver, assemblyLineObserver);
		assertNotNull(w);
		assertEquals("There are no assemblyLines at the moment.", w.getCurrentSchedulingAlgorithm());
	} 

	@Test
	public void testGetCurrentSchedulingAlgorithmNoSchedulingAlgorithm() {
		assertEquals("No scheduling algorithm used at the moment.", workloadDivider.getCurrentSchedulingAlgorithm());
	}

	@Test
	public void testGetCurrentSchedulingAlgorithmWithSchedulingAlgorithm() {
		SchedulingAlgorithmCreatorFifo creator = new SchedulingAlgorithmCreatorFifo();
		workloadDivider.switchToSchedulingAlgorithm(creator);
		assertNotNull(workloadDivider);
		assertEquals("Fifo", workloadDivider.getCurrentSchedulingAlgorithm());
	}

	@Test
	public void testGetAssemblyLines() {
		assertEquals(2, workloadDivider.getAssemblyLines().size());

		Clock clock = new Clock(360);
		ClockObserver clockObserver = new ClockObserver();
		clock.attachObserver(clockObserver);
		ImmutableClock currentTime = clock.getImmutableClock();
		AssemblyLine line1 = new AssemblyLine(clockObserver, currentTime, AssemblyLineState.OPERATIONAL, specifications);
		AssemblyLine line2 = new AssemblyLine(clockObserver, currentTime, AssemblyLineState.BROKEN, specifications);
		ArrayList<AssemblyLine> lines = new ArrayList<>();
		lines.add(line1);
		lines.add(line2);

		WorkloadDivider w = new WorkloadDivider(lines, orderBookObserver, assemblyLineObserver);
		assertEquals(2, w.getAssemblyLines().size());
	}

	@Test
	public void testgetOperationalUnmodifiableAssemblyLines() {
		Clock clock = new Clock(360);
		ClockObserver clockObserver = new ClockObserver();
		clock.attachObserver(clockObserver);
		ImmutableClock currentTime = clock.getImmutableClock();
		AssemblyLine line1 = new AssemblyLine(clockObserver, currentTime, AssemblyLineState.OPERATIONAL, specifications);
		AssemblyLine line2 = new AssemblyLine(clockObserver, currentTime, AssemblyLineState.BROKEN, specifications);
		ArrayList<AssemblyLine> lines = new ArrayList<>();
		lines.add(line1);
		lines.add(line2);

		WorkloadDivider w = new WorkloadDivider(lines, orderBookObserver, assemblyLineObserver);
		assertEquals(2, w.getAssemblyLines().size());
		assertEquals(1, w.getOperationalUnmodifiableAssemblyLines().size());
		assertTrue(w.getOperationalUnmodifiableAssemblyLines().get(0).getState().equals(AssemblyLineState.OPERATIONAL));
	}

	@Test
	public void testgetBrokenUnmodifiableAssemblyLines() {
		Clock clock = new Clock(360);
		ClockObserver clockObserver = new ClockObserver();
		clock.attachObserver(clockObserver);
		ImmutableClock currentTime = clock.getImmutableClock();
		AssemblyLine line1 = new AssemblyLine(clockObserver, currentTime, AssemblyLineState.OPERATIONAL, specifications);
		AssemblyLine line2 = new AssemblyLine(clockObserver, currentTime, AssemblyLineState.BROKEN, specifications);
		ArrayList<AssemblyLine> lines = new ArrayList<>();
		lines.add(line1);
		lines.add(line2);

		WorkloadDivider w = new WorkloadDivider(lines, orderBookObserver, assemblyLineObserver);
		assertEquals(2, w.getAssemblyLines().size());
		assertEquals(1, w.getBrokenUnmodifiableAssemblyLines().size());
		assertTrue(w.getBrokenUnmodifiableAssemblyLines().get(0).getState().equals(AssemblyLineState.BROKEN));
	}

	@Test
	public void testgetMaintenanceUnmodifiableAssemblyLines() {
		Clock clock = new Clock(360);
		ClockObserver clockObserver = new ClockObserver();
		clock.attachObserver(clockObserver);
		ImmutableClock currentTime = clock.getImmutableClock();
		AssemblyLine line1 = new AssemblyLine(clockObserver, currentTime, AssemblyLineState.MAINTENANCE, specifications);
		AssemblyLine line2 = new AssemblyLine(clockObserver, currentTime, AssemblyLineState.BROKEN, specifications);
		ArrayList<AssemblyLine> lines = new ArrayList<>();
		lines.add(line1);
		lines.add(line2);

		WorkloadDivider w = new WorkloadDivider(lines, orderBookObserver, assemblyLineObserver);
		assertEquals(2, w.getAssemblyLines().size());
		assertEquals(1, w.getMaintenanceUnmodifiableAssemblyLines().size());
		assertTrue(w.getMaintenanceUnmodifiableAssemblyLines().get(0).getState().equals(AssemblyLineState.MAINTENANCE));
	}

	@Test
	public void testProcessNewOrder(){
		workloadDivider.getAssemblyLines().get(0).switchToSchedulingAlgorithm(new SchedulingAlgorithmCreatorFifo());
		workloadDivider.getAssemblyLines().get(1).switchToSchedulingAlgorithm(new SchedulingAlgorithmCreatorFifo());

		Set<String> responsibilities = new HashSet<>();
		responsibilities.add("Body");
		responsibilities.add("Color");
		WorkBench body1 = new WorkBench(WorkBenchType.BODY);

		workloadDivider.getAssemblyLines().get(1).addWorkBench(body1);

		Set<VehicleOption> parts = new HashSet<>();
		VehicleOption part = new VehicleOption("sport", VehicleOptionCategory.BODY);
		parts.add(part);
		Map<WorkBenchType, Integer> map = new HashMap<WorkBenchType, Integer>();
		map.put(WorkBenchType.ACCESSORIES, 20);
		map.put(WorkBenchType.BODY, 20);
		map.put(WorkBenchType.DRIVETRAIN, 20);
		VehicleSpecification template = new VehicleSpecification("model", parts, map, new HashSet<VehicleOption>());
		Vehicle vehicle = new Vehicle(template);
		vehicle.addVehicleOption(part);
		StandardOrder order = new StandardOrder("jef",vehicle,1,new ImmutableClock(0,0));

		workloadDivider.processNewOrder(order);

		Optional<IJob> optionalJob = workloadDivider.getAssemblyLines().get(1).getWorkBenches().get(0).getCurrentJob();		
		assertTrue(optionalJob.isPresent());

		IJob job = optionalJob.get();
		assertTrue(job.getOrder().equals(order));
		assertEquals("Body",job.getTasks().get(0).getTaskDescription());
	}

	@Test (expected = IllegalArgumentException.class)
	public void testProcessOrderException() {
		workloadDivider.processNewOrder(null);
	}

	@Test
	public void testGetBlockingWorkBenches(){
		OrderBookObserver orderBookObserver = new OrderBookObserver();
		AssemblyLineObserver assemblyLineObserver = new AssemblyLineObserver();
		List<AssemblyLine> listOfAssemblyLines = new ArrayList<>();

		WorkloadDivider emptyDivider = new WorkloadDivider(listOfAssemblyLines, orderBookObserver, assemblyLineObserver);

		List<IWorkBench> noLines = emptyDivider.getBlockingWorkBenches(null);
		assertTrue(noLines.isEmpty());

		List<IWorkBench> noBlocking = workloadDivider.getBlockingWorkBenches(workloadDivider.getAssemblyLines().get(1));
		assertTrue(noBlocking.isEmpty());

		workloadDivider.getAssemblyLines().get(0).switchToSchedulingAlgorithm(new SchedulingAlgorithmCreatorFifo());
		workloadDivider.getAssemblyLines().get(1).switchToSchedulingAlgorithm(new SchedulingAlgorithmCreatorFifo());

		Set<String> responsibilities = new HashSet<>();
		responsibilities.add("Body");
		responsibilities.add("Color");
		WorkBench body1 = new WorkBench(WorkBenchType.BODY);

		workloadDivider.getAssemblyLines().get(1).addWorkBench(body1);

		Set<VehicleOption> parts = new HashSet<>();
		VehicleOption part = new VehicleOption("sport", VehicleOptionCategory.BODY);
		parts.add(part);
		Map<WorkBenchType, Integer> map = new HashMap<WorkBenchType, Integer>();
		map.put(WorkBenchType.ACCESSORIES, 20);
		map.put(WorkBenchType.BODY, 20);
		map.put(WorkBenchType.DRIVETRAIN, 20);
		VehicleSpecification template = new VehicleSpecification("model", parts, map, new HashSet<VehicleOption>());
		Vehicle vehicle = new Vehicle(template);
		vehicle.addVehicleOption(part);
		StandardOrder order = new StandardOrder("jef",vehicle,1,new ImmutableClock(0,0));

		workloadDivider.processNewOrder(order);

		List<IWorkBench> firstBlocking = workloadDivider.getBlockingWorkBenches(workloadDivider.getAssemblyLines().get(1));
		assertTrue(firstBlocking.contains(body1));
	}

	@Test
	public void testCompleteChosenTaskAtChosenWorkbench(){
		workloadDivider.getAssemblyLines().get(0).switchToSchedulingAlgorithm(new SchedulingAlgorithmCreatorFifo());
		workloadDivider.getAssemblyLines().get(1).switchToSchedulingAlgorithm(new SchedulingAlgorithmCreatorFifo());

		Set<String> responsibilities = new HashSet<>();
		responsibilities.add("Body");
		responsibilities.add("Color");
		WorkBench body1 = new WorkBench(WorkBenchType.BODY);

		workloadDivider.getAssemblyLines().get(1).addWorkBench(body1);

		Set<VehicleOption> parts = new HashSet<>();
		VehicleOption part = new VehicleOption("sport", VehicleOptionCategory.BODY);
		parts.add(part);
		Map<WorkBenchType, Integer> map = new HashMap<WorkBenchType, Integer>();
		map.put(WorkBenchType.ACCESSORIES, 20);
		map.put(WorkBenchType.BODY, 20);
		map.put(WorkBenchType.DRIVETRAIN, 20);
		VehicleSpecification template = new VehicleSpecification("model", parts, map, new HashSet<VehicleOption>());
		Vehicle vehicle = new Vehicle(template);
		vehicle.addVehicleOption(part);
		StandardOrder order = new StandardOrder("jef",vehicle,1,new ImmutableClock(0,0));

		workloadDivider.processNewOrder(order);

		try {
			workloadDivider.completeChosenTaskAtChosenWorkBench(workloadDivider.getAssemblyLines().get(1), 
					workloadDivider.getAssemblyLines().get(1).getWorkBenches().get(0), 
					workloadDivider.getAssemblyLines().get(1).getWorkBenches().get(0).getCurrentTasks().get(0), 
					new ImmutableClock(0, 0));
		} catch (TimeToStartNewDayException e) {
			//doesn't happen
		}
		assertTrue(workloadDivider.getAssemblyLines().get(1).getWorkBenches().get(0).getCurrentTasks().get(0).isCompleted());

	}

	@Test (expected = IllegalStateException.class)
	public void testCompleteTaskexception(){
		workloadDivider.getAssemblyLines().get(0).switchToSchedulingAlgorithm(new SchedulingAlgorithmCreatorFifo());
		workloadDivider.getAssemblyLines().get(1).switchToSchedulingAlgorithm(new SchedulingAlgorithmCreatorFifo());

		Set<String> responsibilities = new HashSet<>();
		responsibilities.add("Body");
		responsibilities.add("Color");
		WorkBench body1 = new WorkBench(WorkBenchType.BODY);

		workloadDivider.getAssemblyLines().get(1).addWorkBench(body1);

		Set<VehicleOption> parts = new HashSet<>();
		VehicleOption part = new VehicleOption("sport", VehicleOptionCategory.BODY);
		parts.add(part);
		Map<WorkBenchType, Integer> map = new HashMap<WorkBenchType, Integer>();
		map.put(WorkBenchType.ACCESSORIES, 20);
		map.put(WorkBenchType.BODY, 20);
		map.put(WorkBenchType.DRIVETRAIN, 20);
		VehicleSpecification template = new VehicleSpecification("model", parts, map, new HashSet<VehicleOption>());
		Vehicle vehicle = new Vehicle(template);
		vehicle.addVehicleOption(part);
		StandardOrder order = new StandardOrder("jef",vehicle,1,new ImmutableClock(0,0));

		workloadDivider.processNewOrder(order);

		OrderBookObserver orderBookObserver = new OrderBookObserver();
		AssemblyLineObserver assemblyLineObserver = new AssemblyLineObserver();
		List<AssemblyLine> listOfAssemblyLines = new ArrayList<>();

		WorkloadDivider emptyDivider = new WorkloadDivider(listOfAssemblyLines, orderBookObserver, assemblyLineObserver);

		try {
			emptyDivider.completeChosenTaskAtChosenWorkBench(workloadDivider.getAssemblyLines().get(1), 
					workloadDivider.getAssemblyLines().get(1).getWorkBenches().get(0), 
					workloadDivider.getAssemblyLines().get(1).getWorkBenches().get(0).getCurrentTasks().get(0), 
					new ImmutableClock(0, 0));
		} catch (TimeToStartNewDayException e) {
			// doesn't happen
		}
	}

	@Test (expected = IllegalArgumentException.class)
	public void testCompleteTaskexception3(){
		try {
			workloadDivider.completeChosenTaskAtChosenWorkBench(null,null,null,null);
		} catch (TimeToStartNewDayException e) {
			// doesn't happen
		}
	}

	@Test (expected = TimeToStartNewDayException.class)
	public void testCompleteTaskexception2() throws TimeToStartNewDayException{
		workloadDivider.getAssemblyLines().get(0).switchToSchedulingAlgorithm(new SchedulingAlgorithmCreatorFifo());
		workloadDivider.getAssemblyLines().get(1).switchToSchedulingAlgorithm(new SchedulingAlgorithmCreatorFifo());

		WorkBench body1 = new WorkBench(WorkBenchType.BODY);

		workloadDivider.getAssemblyLines().get(0).addWorkBench(body1);
		workloadDivider.getAssemblyLines().get(1).addWorkBench(body1);

		Set<VehicleOption> parts = new HashSet<>();
		VehicleOption part = new VehicleOption("sport", VehicleOptionCategory.BODY);
		parts.add(part);
		Map<WorkBenchType, Integer> map = new HashMap<WorkBenchType, Integer>();
		map.put(WorkBenchType.ACCESSORIES, 20);
		map.put(WorkBenchType.BODY, 20);
		map.put(WorkBenchType.DRIVETRAIN, 20);
		VehicleSpecification template = new VehicleSpecification("model", parts, map, new HashSet<VehicleOption>());

		Vehicle vehicle = new Vehicle(template);
		vehicle.addVehicleOption(part);
		StandardOrder order = new StandardOrder("jef",vehicle,5,new ImmutableClock(0,0));

		workloadDivider.processNewOrder(order);

		workloadDivider.getAssemblyLines().get(0).setState(AssemblyLineState.FINISHED);
		workloadDivider.getAssemblyLines().get(1).setState(AssemblyLineState.FINISHED);

		workloadDivider.completeChosenTaskAtChosenWorkBench(workloadDivider.getAssemblyLines().get(1), 
				workloadDivider.getAssemblyLines().get(1).getWorkBenches().get(0), 
				workloadDivider.getAssemblyLines().get(1).getWorkBenches().get(0).getCurrentTasks().get(0), 
				new ImmutableClock(0, 0));

	}


	@Test
	public void getAllVehicleOptionsInPendingOrdersTest(){
		workloadDivider.getAssemblyLines().get(0).switchToSchedulingAlgorithm(new SchedulingAlgorithmCreatorFifo());
		workloadDivider.getAssemblyLines().get(1).switchToSchedulingAlgorithm(new SchedulingAlgorithmCreatorFifo());

		WorkBench body1 = new WorkBench(WorkBenchType.BODY);

		workloadDivider.getAssemblyLines().get(0).addWorkBench(body1);
		workloadDivider.getAssemblyLines().get(1).addWorkBench(body1);
		
		Set<Set<VehicleOption>> batches = workloadDivider.getAllVehicleOptionsInPendingOrders();
		assertTrue(batches.isEmpty());
		
		VehicleOption option = new VehicleOption("black", VehicleOptionCategory.COLOR);
		Set<VehicleOption> parts = new HashSet<>();
		parts.add(new VehicleOption("sport", VehicleOptionCategory.BODY));
		Map<WorkBenchType, Integer> map = new HashMap<WorkBenchType, Integer>();
		map.put(WorkBenchType.ACCESSORIES, 20);
		map.put(WorkBenchType.BODY, 20);
		map.put(WorkBenchType.DRIVETRAIN, 20);
		VehicleSpecification template = new VehicleSpecification("model", parts, map, new HashSet<VehicleOption>());
	
		Vehicle vehicle = new Vehicle(template);
		vehicle.addVehicleOption(new VehicleOption("bla", VehicleOptionCategory.BODY));
		vehicle.addVehicleOption(new VehicleOption("bla", VehicleOptionCategory.ENGINE));
		vehicle.addVehicleOption(new VehicleOption("bla", VehicleOptionCategory.GEARBOX));
		vehicle.addVehicleOption(new VehicleOption("bla", VehicleOptionCategory.SEATS));
		vehicle.addVehicleOption(new VehicleOption("bla", VehicleOptionCategory.WHEEL));
		vehicle.addVehicleOption(option);
		
		StandardOrder order = new StandardOrder("luigi", vehicle, 20, new ImmutableClock(0, 0));
		workloadDivider.processNewOrder(order);
		//		b)the system gives the sets of options
		//			--> in this case, it'll be one set with one VehicleOption
		Set<Set<VehicleOption>> batchOptions = workloadDivider.getAllVehicleOptionsInPendingOrders();
		assertEquals(63,batchOptions.size());
	}
	
	
	@Test
	public void testChangeState(){
		IAssemblyLine line = workloadDivider.getAssemblyLines().get(0);
		workloadDivider.changeState(line, AssemblyLineState.MAINTENANCE, new ClockObserver(), new ImmutableClock(0, 0));
		assertEquals(AssemblyLineState.MAINTENANCE, line.getState());
		
		workloadDivider.changeState(null, AssemblyLineState.BROKEN, new ClockObserver(), new ImmutableClock(0, 0));
		assertEquals(AssemblyLineState.MAINTENANCE, line.getState());
	}
}
