package domain.scheduling.schedulerTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;

import domain.assembly.assemblyLine.AssemblyLine;
import domain.assembly.workBench.WorkBenchType;
import domain.clock.ImmutableClock;
import domain.job.job.IJob;
import domain.job.job.Job;
import domain.job.jobComparator.JobComparatorDeadLine;
import domain.observer.observers.ClockObserver;
import domain.order.order.CustomOrder;
import domain.order.order.IOrder;
import domain.order.order.StandardOrder;
import domain.scheduling.Scheduler;
import domain.scheduling.schedulingAlgorithm.SchedulingAlgorithmFifo;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreatorFifo;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicle.CustomVehicle;
import domain.vehicle.vehicle.Vehicle;
import domain.vehicle.vehicleOption.VehicleOption;


public class SchedulingAlgorithmFifoTest {

	SchedulingAlgorithmFifo algorithm;
	private Vehicle model;
	private VehicleSpecification template;
	private HashMap<WorkBenchType, Integer> timeAtWorkBench;
	private List<WorkBenchType> workBenchTypes;
	private Scheduler scheduler;
	
	@Before
	public void initialise(){
		ClockObserver clock = new ClockObserver();
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
		timeAtWorkBench.put(WorkBenchType.CARGO, 0);
		timeAtWorkBench.put(WorkBenchType.CERTIFICATION, 0);
		timeAtWorkBench.put(WorkBenchType.DRIVETRAIN, 60);
	}

	@Test
	public void addCustomOrderTest1() {
		CustomVehicle customModel = new CustomVehicle();
		ImmutableClock ordertime = new ImmutableClock(2, 30);
		ImmutableClock deadline = new ImmutableClock(5, 30);
		CustomOrder order = new CustomOrder("Mario", customModel, 5, ordertime, deadline);
		IJob job = new Job(order);
		algorithm.addCustomJob(job);
		assertEquals(1, algorithm.getCustomJobs().size());
		assertEquals(0, algorithm.getStandardJobs().size());

	}

	@Test (expected = IllegalArgumentException.class)
	public void addCustomOrderTest2(){
		algorithm.addCustomJob(null);
	}

	@Test
	public void addStandardJobTest1(){
		Set<VehicleOption> parts = new HashSet<>();
		template = new VehicleSpecification("model", parts, timeAtWorkBench, new HashSet<VehicleOption>());
		model = new Vehicle(template);
		ImmutableClock ordertime = new ImmutableClock(2, 30);
		IOrder order = new StandardOrder("mario", model, 3, ordertime);
		IJob job = new Job(order);
		algorithm.addStandardJob(job);
		assertEquals(1,algorithm.getStandardJobs().size());
	}

	@Test (expected = IllegalArgumentException.class)
	public void addStandardJobTest2(){
		algorithm.addStandardJob(null);
	}

	@Before
	public void constructorTest() {
		algorithm = new SchedulingAlgorithmFifo(this.workBenchTypes);
		assertNotNull(algorithm.getCustomJobs());
		assertEquals(0,algorithm.getCustomJobs().size());
		//assertNotNull(algorithm.getHistory());
		assertNotNull(algorithm.getStandardJobs());
		assertEquals(0, algorithm.getStandardJobs().size());
	}

	@Test
	public void getEstimatedTimeInMinutesTest1() {
		Set<VehicleOption> parts = new HashSet<>();
		template = new VehicleSpecification("model", parts, timeAtWorkBench, new HashSet<VehicleOption>());
		model = new Vehicle(template);
		ImmutableClock ordertime1 = new ImmutableClock(0, 610); // om 10.10 uur op dag 0
		StandardOrder order1 = new StandardOrder("Luigi", model, 1, ordertime1); // 420 minuten op de band
		IJob job = new Job(order1);
		ArrayList<Optional<IJob>> list = new ArrayList<Optional<IJob>>();
		Optional<IJob> job3 = Optional.absent();
		list.add(job3);
		scheduler.addJobToAlgorithm(job,list);
		
		CustomVehicle customModel = new CustomVehicle();
		ImmutableClock ordertime = new ImmutableClock(0, 0);
		ImmutableClock deadline = new ImmutableClock(10, 800);
		CustomOrder customOrder = new CustomOrder("Mario", customModel, 1, ordertime, deadline);
		IJob job2 = new Job(customOrder);
		scheduler.addJobToAlgorithm(job2,list);
		
		//assertEquals(new UnmodifiableClock(2, 780), order1.getEstimatedTime());
		assertEquals(new ImmutableClock(8, 440), job2.getOrder().getEstimatedTime());
		assertEquals(new ImmutableClock(2,780), job.getOrder().getEstimatedTime());
	}

	@Test (expected = IllegalArgumentException.class)
	public void getEstimatedTimeInMinutesTest2(){
		ImmutableClock clock = new ImmutableClock(0,500);
		algorithm.setEstimatedTime(null, clock);
	}

	@Test (expected = IllegalArgumentException.class)
	public void getEstimatedTimeInMinutesTest3(){
		CustomVehicle customModel = new CustomVehicle();
		ImmutableClock ordertime = new ImmutableClock(0, 30);
		ImmutableClock deadline = new ImmutableClock(5, 30);
		CustomOrder customOrder = new CustomOrder("Mario", customModel, 5, ordertime, deadline);
		IJob customJob = new Job(customOrder);
		algorithm.setEstimatedTime(customJob, null);
	}
	
	@Test
	public void retrieveNextJobTest() {
		Set<VehicleOption> parts = new HashSet<>();
		template = new VehicleSpecification("model", parts, timeAtWorkBench, new HashSet<VehicleOption>());
		model = new Vehicle(template);
		ImmutableClock ordertime1 = new ImmutableClock(0, 420); 
		int quantity =5;
		StandardOrder order1 = new StandardOrder("Luigi", model, quantity, ordertime1); 
		IJob sJob1 = new Job(order1);
		IJob sJob2 = new Job(order1);
		algorithm.addStandardJob(sJob1);
		algorithm.addStandardJob(sJob2);
		CustomVehicle customModel = new CustomVehicle();
		ImmutableClock ordertime = new ImmutableClock(0, 360);
		ImmutableClock deadline = new ImmutableClock(10, 800);
		CustomOrder customOrder = new CustomOrder("Mario", customModel, 5, ordertime, deadline);
		IJob job2 = new Job(customOrder);
		algorithm.addCustomJob(job2);
		// Stel algoritme zit op tijdstip dag 0 360 minuten
		algorithm.startNewDay();
		int minTillEndOfDay = 1320;
		Optional<IJob> job = algorithm.retrieveNext(minTillEndOfDay, new ImmutableClock(1,360));
		assertEquals(job2, job.get());
		Optional<IJob> newJob = algorithm.retrieveNext(1280, new ImmutableClock(1,420));
		assertEquals(sJob1, newJob.get());
		CustomVehicle customModel2 = new CustomVehicle();
		ImmutableClock ordertime2 = new ImmutableClock(1, 430);
		ImmutableClock deadline2 = new ImmutableClock(1, 540);
		CustomOrder customOrder2 = new CustomOrder("Mario", customModel2, 1, ordertime2, deadline2);
		IJob job4 = new Job(customOrder2);
		algorithm.addCustomJob(job4);
		Optional<IJob> newJob2 = algorithm.retrieveNext(1220, new ImmutableClock(1,480));
		assertEquals(job4, newJob2.get());
		Optional<IJob> newJob3 = algorithm.retrieveNext(1160, new ImmutableClock(1,520));
		assertEquals(sJob2, newJob3.get());
		CustomVehicle customModel3 = new CustomVehicle();
		ImmutableClock ordertime3 = new ImmutableClock(1, 530);
		ImmutableClock deadline3 = new ImmutableClock(2, 540);
		CustomOrder customOrder3 = new CustomOrder("Mario", customModel3, 3, ordertime3, deadline3);
		IJob job5= new Job(customOrder3);
		algorithm.addCustomJob(job5);
		Optional<IJob> newJob4 = algorithm.retrieveNext(1080, new ImmutableClock(1,580));
		assertEquals(job5, newJob4.get());
	}
	
//	@Test (expected = NoSuitableJobFoundException.class)
//	public void retrieveNextJobTest2() throws NoSuitableJobFoundException, NotImplementedException{
//		algorithm.retrieveNext(4545, new ImmutableClock(2,3));
//	}
	
	@Test
	public void startNewDayTest() {
		ClockObserver obs = new ClockObserver();
		AssemblyLine ass = new AssemblyLine(obs, new ImmutableClock(2, 360));
		ass.switchToFifo();
		CustomVehicle customModel = new CustomVehicle();
		ImmutableClock ordertime = new ImmutableClock(0, 0);
		ImmutableClock deadline = new ImmutableClock(10, 800);
		CustomOrder customOrder = new CustomOrder("Mario", customModel, 5, ordertime, deadline);
		ImmutableClock ordertime2 = new ImmutableClock(0, 0);
		ImmutableClock deadline2 = new ImmutableClock(10, 810);
		CustomOrder customOrder2 = new CustomOrder("Mario", customModel, 5, ordertime2, deadline2);
		IJob job1 = new Job(customOrder);
		IJob job2 = new Job(customOrder2);
		job2.setMinimalIndex(2);
		algorithm.addCustomJob(job1);
		algorithm.addCustomJob(job2);
		assertEquals(2,algorithm.getCustomJobs().size());
		algorithm.startNewDay();
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void transformTest2Error(){
		algorithm.transform(null, null, null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void transformTest3Error(){
		PriorityQueue<IJob> jobs = new PriorityQueue<IJob>(10,new JobComparatorDeadLine());
		algorithm.transform(jobs, null, null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void transformTest4Error(){
		PriorityQueue<IJob> jobs = new PriorityQueue<IJob>(10,new JobComparatorDeadLine());
		ArrayList<IJob> standardJobs = new ArrayList<IJob>();
		algorithm.transform(jobs, standardJobs, null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void transformTest5Error(){
		PriorityQueue<IJob> jobs = new PriorityQueue<IJob>(10,new JobComparatorDeadLine());
		ArrayList<Optional<IJob>> history = new ArrayList<Optional<IJob>>();
		algorithm.transform(jobs, null, history);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void transformTest6Error(){
		ArrayList<IJob> standardJobs = new ArrayList<IJob>();
		ArrayList<Optional<IJob>> history = new ArrayList<Optional<IJob>>();
		algorithm.transform(null, standardJobs, history);
	}

}
