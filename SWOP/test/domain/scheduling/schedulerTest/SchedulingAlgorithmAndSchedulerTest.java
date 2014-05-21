package domain.scheduling.schedulerTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;

import domain.assembly.workBench.WorkBenchType;
import domain.clock.ImmutableClock;
import domain.job.job.IJob;
import domain.job.job.Job;
import domain.observer.observers.ClockObserver;
import domain.order.order.CustomOrder;
import domain.order.order.StandardOrder;
import domain.order.order.UnmodifiableOrder;
import domain.restriction.BindingRestriction;
import domain.restriction.OptionalRestriction;
import domain.scheduling.Scheduler;
import domain.scheduling.schedulingAlgorithm.SchedulingAlgorithmFifo;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreatorBatch;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreatorFifo;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.catalogue.VehicleSpecificationCatalogue;
import domain.vehicle.vehicle.CustomVehicle;
import domain.vehicle.vehicle.Vehicle;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

public class SchedulingAlgorithmAndSchedulerTest {

	private Scheduler scheduler;
	private ClockObserver clock;
	private Vehicle model;
	private VehicleSpecification template;
	private Set<BindingRestriction> bindingRestrictions;
	private Set<OptionalRestriction> optionalRestrictions;
	private HashMap<WorkBenchType, Integer> timeAtWorkBench;
	private List<WorkBenchType> workBenchTypes;
	
	
	@Before
	public void initialize() {
		//facade = new Facade(bindingRestrictions, optionalRestrictions);
		//clientCommunication = new ClientCommunication();
		clock = new ClockObserver();
		scheduler = new Scheduler(clock, new ImmutableClock(0,600));
		workBenchTypes = new ArrayList<>();
		workBenchTypes.add(WorkBenchType.BODY);
		workBenchTypes.add(WorkBenchType.CARGO);
		workBenchTypes.add(WorkBenchType.ACCESSORIES);
		SchedulingAlgorithmCreatorFifo fifo = new SchedulingAlgorithmCreatorFifo();
		scheduler.switchToAlgorithm(fifo, workBenchTypes);
		timeAtWorkBench = new HashMap<WorkBenchType, Integer>();
		timeAtWorkBench.put(WorkBenchType.ACCESSORIES, 60);
		timeAtWorkBench.put(WorkBenchType.BODY, 60);
		timeAtWorkBench.put(WorkBenchType.CARGO, 60);
		timeAtWorkBench.put(WorkBenchType.CERTIFICATION, 60);
		timeAtWorkBench.put(WorkBenchType.DRIVETRAIN, 60);
	}
	
	public void initializeRestrictions(){
		bindingRestrictions = new HashSet<>();
		optionalRestrictions = new HashSet<>();
		optionalRestrictions.add(new OptionalRestriction(new VehicleOption("sport", VehicleOptionCategory.BODY), VehicleOptionCategory.SPOILER, false));
		
		bindingRestrictions.add(new BindingRestriction(new VehicleOption("sport", VehicleOptionCategory.BODY), new VehicleOption("performance 2.5l V6", VehicleOptionCategory.ENGINE)));
		bindingRestrictions.add(new BindingRestriction(new VehicleOption("sport", VehicleOptionCategory.BODY), new VehicleOption("ultra 3l V8", VehicleOptionCategory.ENGINE)));
		
		bindingRestrictions.add(new BindingRestriction(new VehicleOption("ultra 3l V8", VehicleOptionCategory.ENGINE), new VehicleOption("manual", VehicleOptionCategory.AIRCO)));
		
		VehicleSpecificationCatalogue catalogue = new VehicleSpecificationCatalogue();
		Set<VehicleOption> parts = new HashSet<>();
		parts.add(new VehicleOption("sport", VehicleOptionCategory.BODY));
		
		parts.add(new VehicleOption("black", VehicleOptionCategory.COLOR));
		parts.add(new VehicleOption("white", VehicleOptionCategory.COLOR));
		
		parts.add(new VehicleOption("performance 2.5l V6", VehicleOptionCategory.ENGINE));
		parts.add(new VehicleOption("ultra 3l V8", VehicleOptionCategory.ENGINE));
	
		parts.add(new VehicleOption("6 speed manual", VehicleOptionCategory.GEARBOX));
		
		parts.add(new VehicleOption("leather white", VehicleOptionCategory.SEATS));
		parts.add(new VehicleOption("leather black", VehicleOptionCategory.SEATS));
		
		parts.add(new VehicleOption("manual", VehicleOptionCategory.AIRCO));
		parts.add(new VehicleOption("automatic", VehicleOptionCategory.AIRCO));
		
		parts.add(new VehicleOption("winter", VehicleOptionCategory.WHEEL));
		parts.add(new VehicleOption("sports", VehicleOptionCategory.WHEEL));
		
		parts.add(new VehicleOption("high", VehicleOptionCategory.SPOILER));
		parts.add(new VehicleOption("low", VehicleOptionCategory.SPOILER));
		VehicleSpecification template = new VehicleSpecification("model", parts, timeAtWorkBench, new HashSet<VehicleOption>());
		catalogue.addModel(template);
	}
	
	@Test
	public void constructorTest(){
		assertNotNull(scheduler);	
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void constructorTest2(){
		scheduler = new Scheduler(null, new ImmutableClock(0,100));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void constructorTest3(){
		scheduler = new Scheduler(clock,null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void constructorSATestNull() {
		new SchedulingAlgorithmFifo(null);
	}
	
	@Test
	public void addCustomJobTest(){
		CustomVehicle customModel = new CustomVehicle();
		ImmutableClock ordertime = new ImmutableClock(0, 0);
		ImmutableClock deadline = new ImmutableClock(10, 800);
		CustomOrder customOrder = new CustomOrder("Mario", customModel, 5, ordertime, deadline);
		IJob job = new Job(customOrder);
		scheduler.addJobToAlgorithm(job, new ArrayList<Optional<IJob>>());;
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void addCustomJobNullTest(){
		scheduler.addJobToAlgorithm(null, new ArrayList<Optional<IJob>>());;
	}
	
	@Test
	public void addStandardJobTest(){
		Set<VehicleOption> parts = new HashSet<>();
		Set<VehicleOption> set = new HashSet<VehicleOption>();
		template = new VehicleSpecification("model", parts, timeAtWorkBench, set);
		model = new Vehicle(template);
		ImmutableClock ordertime1 = new ImmutableClock(2, 360); 
		int quantity =5;
		StandardOrder order1 = new StandardOrder("Luigi", model, quantity, ordertime1);
		IJob job = new Job(order1);
		scheduler.addJobToAlgorithm(job, new ArrayList<Optional<IJob>>());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void addUnmodifiableJobTest(){
		Set<VehicleOption> parts = new HashSet<>();
		Set<VehicleOption> set = new HashSet<VehicleOption>();
		template = new VehicleSpecification("model", parts, timeAtWorkBench, set);
		model = new Vehicle(template);
		ImmutableClock ordertime1 = new ImmutableClock(2, 360); 
		int quantity =5;
		StandardOrder order1 = new StandardOrder("Luigi", model, quantity, ordertime1);
		UnmodifiableOrder order2 = new UnmodifiableOrder(order1);
		IJob job = new Job(order2);
		scheduler.addJobToAlgorithm(job, new ArrayList<Optional<IJob>>());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void addStandardJobTestNullJob() {
		scheduler.addJobToAlgorithm(null, new ArrayList<Optional<IJob>>());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void addStandardJobTestNull2Job() {
		SchedulingAlgorithmFifo fifo = new SchedulingAlgorithmFifo(new ArrayList<WorkBenchType>());
		fifo.addStandardJob(null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void addCustomJobTestNullJob() {
		SchedulingAlgorithmFifo fifo = new SchedulingAlgorithmFifo(new ArrayList<WorkBenchType>());
		fifo.addCustomJob(null);
	}
	
	@Test
	public void switchToFifoTest(){
		SchedulingAlgorithmCreatorFifo fifo = new SchedulingAlgorithmCreatorFifo();
		List<WorkBenchType> workBenchTypes = new ArrayList<>();
		workBenchTypes.add(WorkBenchType.BODY);
		workBenchTypes.add(WorkBenchType.CARGO);
		workBenchTypes.add(WorkBenchType.ACCESSORIES);
		assertNotNull(workBenchTypes);
		scheduler.switchToAlgorithm(fifo, workBenchTypes);
	}
	
	@Test
	public void switchToBatchTest(){
		List<VehicleOption> options = new ArrayList<VehicleOption>();
		options.add(new VehicleOption("manual", VehicleOptionCategory.AIRCO));
		SchedulingAlgorithmCreatorBatch batch = new SchedulingAlgorithmCreatorBatch(options);
		List<WorkBenchType> workBenchTypes = new ArrayList<>();
		workBenchTypes.add(WorkBenchType.BODY);
		workBenchTypes.add(WorkBenchType.CARGO);
		workBenchTypes.add(WorkBenchType.ACCESSORIES);
		scheduler.switchToAlgorithm(batch, workBenchTypes);
	}
	
	@Test 
	public void switchToAlgorithm(){
		ClockObserver observer = new ClockObserver();
		Scheduler schedul = new Scheduler(observer, new ImmutableClock(0,360));
		schedul.switchToAlgorithm(new SchedulingAlgorithmCreatorFifo(), workBenchTypes);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void switchToAlgorithmError1(){
		scheduler.switchToAlgorithm(null, workBenchTypes);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void switchToAlgorithmError2(){
		scheduler.switchToAlgorithm(new SchedulingAlgorithmCreatorFifo(), null);
	}
	
	@Test
	public void advanceTimeTest(){
		scheduler.advanceTime(new ImmutableClock(0,540));
	}
	
	@Test
	public void advanceTimeTest2(){
		scheduler.advanceTime(null);
	}
	
	@Test
	public void startNewDayTest(){
		scheduler.startNewDay(new ImmutableClock(1,420));
	}

	@Test (expected = IllegalArgumentException.class)
	public void startNewDayTest2(){
		scheduler.startNewDay(null);
	}
	
	@Test
	public void getEstimatedTimeInMinutes() {
		Set<VehicleOption> parts = new HashSet<>();
		template = new VehicleSpecification("model", parts, this.timeAtWorkBench, new HashSet<VehicleOption>());
		model = new Vehicle(template);
		ImmutableClock ordertime1 = new ImmutableClock(2, 360); 
		int quantity =5;
		StandardOrder order1 = new StandardOrder("Luigi", model, quantity, ordertime1);
		IJob job = new Job(order1);
		scheduler.addJobToAlgorithm(job, new ArrayList<Optional<IJob>>());
		assertEquals(540, job.getOrder().getEstimatedTime().getMinutes());
	}
	
	@Test
	public void retrieveNextJobTest() {
		// Standard job not containing necessary parts of list.
		Set<VehicleOption> parts = new HashSet<>();
		assertNotNull(this.timeAtWorkBench);
		template = new VehicleSpecification("model", parts, this.timeAtWorkBench, new HashSet<VehicleOption>());
		model = new Vehicle(template);
		ImmutableClock ordertime1 = new ImmutableClock(0, 660); // om 6 uur op dag 2
		int quantity =5;
		StandardOrder order1 = new StandardOrder("Luigi", model, quantity, ordertime1); // 420 minuten op de band
		IJob job1 = new Job(order1);
		IJob job2 = new Job(order1);
		//try {
			scheduler.addJobToAlgorithm(job1, new ArrayList<Optional<IJob>>());
			scheduler.addJobToAlgorithm(job2, new ArrayList<Optional<IJob>>());
			Optional<IJob> job = scheduler.retrieveNextJob(new ArrayList<Optional<IJob>>());
			assertEquals(job1, job.get());
		//} catch (NoSuitableJobFoundException e1) {}
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void retrieveNextJobTestError(){
		scheduler.retrieveNextJob(null);
	}
	
	@Test
	public void addWorkBenchTest() {
		ArrayList<WorkBenchType> types = new ArrayList<WorkBenchType>();
		SchedulingAlgorithmFifo fifo = new SchedulingAlgorithmFifo(types);
		assertEquals(0, types.size());
		fifo.addWorkBenchType(WorkBenchType.CERTIFICATION);
		assertEquals(1, types.size());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void addWorkBenchNullTest() {
		ArrayList<WorkBenchType> types = new ArrayList<WorkBenchType>();
		SchedulingAlgorithmFifo fifo = new SchedulingAlgorithmFifo(types);
		assertEquals(0, types.size());
		fifo.addWorkBenchType(null);
	}
	
	@Test
	public void removeUnscheduledJobsTest() {
		ArrayList<WorkBenchType> types = new ArrayList<WorkBenchType>();
		SchedulingAlgorithmFifo fifo = new SchedulingAlgorithmFifo(types);
		assertEquals(0, fifo.removeUnscheduledJobs().size());
		
		Set<VehicleOption> parts = new HashSet<>();
		assertNotNull(this.timeAtWorkBench);
		template = new VehicleSpecification("model", parts, this.timeAtWorkBench, new HashSet<VehicleOption>());
		model = new Vehicle(template);
		ImmutableClock ordertime1 = new ImmutableClock(0, 660); // om 6 uur op dag 2
		int quantity =5;
		StandardOrder order1 = new StandardOrder("Luigi", model, quantity, ordertime1); // 420 minuten op de band
		IJob job1 = new Job(order1);
		
		CustomVehicle customModel = new CustomVehicle();
		ImmutableClock ordertime = new ImmutableClock(0, 0);
		ImmutableClock deadline = new ImmutableClock(10, 800);
		CustomOrder customOrder = new CustomOrder("Mario", customModel, 5, ordertime, deadline);
		IJob job2 = new Job(customOrder);
		
		fifo.addStandardJob(job1);
		fifo.addCustomJob(job2);
		
		assertEquals(2, fifo.removeUnscheduledJobs().size());
		assertEquals(0, scheduler.removeUnscheduledJobs().size());
	}
	
	@Test
	public void getCustomJobsTest() {
		ArrayList<WorkBenchType> types = new ArrayList<WorkBenchType>();
		SchedulingAlgorithmFifo fifo = new SchedulingAlgorithmFifo(types);
		
		CustomVehicle customModel = new CustomVehicle();
		ImmutableClock ordertime = new ImmutableClock(0, 0);
		ImmutableClock deadline = new ImmutableClock(10, 800);
		CustomOrder customOrder = new CustomOrder("Mario", customModel, 5, ordertime, deadline);
		IJob job2 = new Job(customOrder);
		
		fifo.addCustomJob(job2);
		
		assertEquals(1, fifo.getCustomJobs().size());
	}
	
	@Test
	public void getCurrentSchedulingAlgorithmTest(){
		assertEquals("Fifo", scheduler.getCurrentSchedulingAlgorithm());
		ClockObserver obs = new ClockObserver();
		Scheduler schedule = new Scheduler(obs, new ImmutableClock(0, 360));
		assertEquals("No scheduling algorithm used at the moment.", schedule.getCurrentSchedulingAlgorithm());
	}
	
	@Test
	public void getStandardJobsTest(){
		assertNotNull(scheduler.getStandardJobs());
	}
	
	@Test
	public void advanceInternalClockTest(){
		scheduler.advanceInternalClock(new ImmutableClock(0, 50));
		assertEquals(650, scheduler.getTotalMinutesOfInternalClock());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void advanceInternalClockTestError(){
		scheduler.advanceInternalClock(null);
	}
	
	@Test
	public void addWorkBenchTypeTest(){
		scheduler.addWorkBenchType(WorkBenchType.CARGO);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void addWorkBenchTypeTestError1(){
		scheduler.addWorkBenchType(null);
	}
	
	@Test (expected = IllegalStateException.class)
	public void addWorkBenchTypeTestError2(){
		ClockObserver observer = new ClockObserver();
		Scheduler schedule = new Scheduler(observer, new ImmutableClock(0, 400));
		schedule.addWorkBenchType(WorkBenchType.CARGO);
	}
	
	
	
} 
