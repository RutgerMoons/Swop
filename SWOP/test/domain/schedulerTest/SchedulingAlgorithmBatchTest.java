package domain.schedulerTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;

import domain.assembly.AssemblyLine;
import domain.car.CarModel;
import domain.car.CarModelSpecification;
import domain.car.CarOption;
import domain.car.CarOptionCategory;
import domain.car.CustomCarModel;
import domain.clock.UnmodifiableClock;
import domain.exception.AlreadyInMapException;
import domain.exception.ImmutableException;
import domain.exception.NotImplementedException;
import domain.job.IJob;
import domain.job.Job;
import domain.job.JobComparatorDeadLine;
import domain.observer.ClockObserver;
import domain.order.CustomOrder;
import domain.order.IOrder;
import domain.order.OrderBook;
import domain.order.StandardOrder;
import domain.scheduler.SchedulingAlgorithmBatch;

public class SchedulingAlgorithmBatchTest {

	private SchedulingAlgorithmBatch scheduling;
	private CarModel model;
	private CarModelSpecification template;
	private List<CarOption> list;

	@Before
	public void initialize() {
		int amount = 3;
		list = new ArrayList<CarOption>();
		list.add(new CarOption("manual", CarOptionCategory.AIRCO));
		scheduling = new SchedulingAlgorithmBatch(list, amount);
	}

	@Test
	public void constructorTest(){
		assertNotNull(scheduling);
		assertNotNull(scheduling.getCustomJobs());
		assertNotNull(scheduling.getHistory());
		assertNotNull(scheduling.getStandardJobs());
	}

	@Test (expected = IllegalArgumentException.class)
	public void constructorTestError(){
		scheduling = new SchedulingAlgorithmBatch(null, 3);
	}

	@Test
	public void testAddCustomOrder1() throws AlreadyInMapException{
		CustomCarModel customModel = new CustomCarModel();
		UnmodifiableClock ordertime = new UnmodifiableClock(2, 30);
		UnmodifiableClock deadline = new UnmodifiableClock(5, 30);
		CustomOrder order = new CustomOrder("Mario", customModel, 5, ordertime, deadline);
		IJob job = new Job(order);
		scheduling.AddCustomJob(job);
		assertEquals(1, scheduling.getCustomJobs().size());
		assertEquals(0, scheduling.getStandardJobs().size());

	}

	@Test (expected = IllegalArgumentException.class)
	public void testAddCustomOrder2(){
		scheduling.AddCustomJob(null);
	}

	@Test
	public void testAddStandardJob1() throws NotImplementedException{
		Set<CarOption> parts = new HashSet<>();
		parts.add(new CarOption("manual", CarOptionCategory.AIRCO));
		assertEquals(1, parts.size());
		template = new CarModelSpecification("model", parts, 60);
		assertEquals(1, template.getCarParts().size());
		model = new CarModel(template);
		assertEquals(1, model.getSpecification().getCarParts().size());
		assertEquals(0, model.getCarParts().size());
		UnmodifiableClock ordertime = new UnmodifiableClock(2, 30);
		IOrder order = new StandardOrder("mario", model, 3, ordertime);
		IJob job = new Job(order);
		scheduling.AddStandardJob(job);
		assertEquals(1,scheduling.getStandardJobs().size());

		Set<CarOption> parts2 = new HashSet<>();
		template = new CarModelSpecification("model", parts2, 60);
		model = new CarModel(template);
		UnmodifiableClock ordertime2 = new UnmodifiableClock(2, 30);
		IOrder order2 = new StandardOrder("mario", model, 3, ordertime2);
		IJob job2 = new Job(order2);
		scheduling.AddStandardJob(job2);
		assertEquals(2,scheduling.getStandardJobs().size());
	}

	@Test (expected = IllegalArgumentException.class)
	public void testAddStandardJob2() throws NotImplementedException{
		scheduling.AddStandardJob(null);
	}

	@Test
	public void testGetEstimatedTimeInMinutes1() throws ImmutableException, NotImplementedException{
		ClockObserver obs = new ClockObserver();
		AssemblyLine ass = new AssemblyLine(obs, new UnmodifiableClock(2, 360));
		ass.switchToBatch(list);
		// Standard job not containing necessary parts of list.
		Set<CarOption> parts = new HashSet<>();
		template = new CarModelSpecification("model", parts, 60);
		model = new CarModel(template);
		UnmodifiableClock ordertime1 = new UnmodifiableClock(2, 360); // om 6 uur op dag 2
		int quantity =5;
		StandardOrder order1 = new StandardOrder("Luigi", model, quantity, ordertime1); // 420 minuten op de band
		OrderBook orderbook = new OrderBook(ass);
		try {
			orderbook.addOrder(order1, ordertime1);
		} catch (NotImplementedException e1) {}
		// Custom job
		CustomCarModel customModel = new CustomCarModel();
		UnmodifiableClock ordertime = new UnmodifiableClock(0, 0);
		UnmodifiableClock deadline = new UnmodifiableClock(10, 800);
		CustomOrder customOrder = new CustomOrder("Mario", customModel, 5, ordertime, deadline);
		try {
			orderbook.addOrder(customOrder, ordertime);
		} catch (NotImplementedException e) {}
		// batch job
		Set<CarOption> parts2 = new HashSet<>();
		parts2.add(new CarOption("manual", CarOptionCategory.AIRCO));
		template = new CarModelSpecification("model", parts2, 60);
		model = new CarModel(template);
		UnmodifiableClock ordertime2 = new UnmodifiableClock(2, 420); // om 7 uur op dag 2
		int quantity2 =5;
		StandardOrder order2 = new StandardOrder("Luigi", model, quantity2, ordertime2); // 420 minuten op de band
		try {
			orderbook.addOrder(order2, ordertime2);
		} catch (NotImplementedException e1) {}
		assertEquals(new UnmodifiableClock(8, 440), customOrder.getEstimatedTime());
		assertEquals(new UnmodifiableClock(2,780), order1.getEstimatedTime());
		assertEquals(new UnmodifiableClock(2, 840), order2.getEstimatedTime());
	}

	@Test (expected = IllegalArgumentException.class)
	public void testGetEstimatedTimeInMinutes2(){
		UnmodifiableClock clock = new UnmodifiableClock(0,500);
		try {
			scheduling.getEstimatedTimeInMinutes(null, clock);
		} catch (NotImplementedException e) {
			e.printStackTrace();
		}
	}

	@Test (expected = IllegalArgumentException.class)
	public void testGetEstimatedTimeInMinutes3(){
		CustomCarModel customModel = new CustomCarModel();
		UnmodifiableClock ordertime = new UnmodifiableClock(0, 30);
		UnmodifiableClock deadline = new UnmodifiableClock(5, 30);
		CustomOrder customOrder = new CustomOrder("Mario", customModel, 5, ordertime, deadline);
		IJob customJob = new Job(customOrder);
		try {
			scheduling.getEstimatedTimeInMinutes(customJob, null);
		} catch (NotImplementedException e) {
			e.printStackTrace();
		}
	}


	@Test
	public void startNewDayTest() throws NotImplementedException, ImmutableException{
		ClockObserver obs = new ClockObserver();
		AssemblyLine ass = new AssemblyLine(obs, new UnmodifiableClock(2, 360));
		ass.switchToBatch(list);
		CustomCarModel customModel = new CustomCarModel();
		UnmodifiableClock ordertime = new UnmodifiableClock(0, 0);
		UnmodifiableClock deadline = new UnmodifiableClock(10, 800);
		CustomOrder customOrder = new CustomOrder("Mario", customModel, 5, ordertime, deadline);
		UnmodifiableClock ordertime2 = new UnmodifiableClock(0, 0);
		UnmodifiableClock deadline2 = new UnmodifiableClock(10, 810);
		CustomOrder customOrder2 = new CustomOrder("Mario", customModel, 5, ordertime2, deadline2);
		IJob job1 = new Job(customOrder);
		IJob job2 = new Job(customOrder2);
		job2.setMinimalIndex(2);
		scheduling.AddCustomJob(job1);
		scheduling.AddCustomJob(job2);
		assertEquals(2,scheduling.getCustomJobs().size());
		scheduling.startNewDay();
	}


	@Test
	public void transformTest1() throws NotImplementedException{
		PriorityQueue<IJob> jobs = new PriorityQueue<IJob>(10,new JobComparatorDeadLine());
		ArrayList<IJob> standardJobs = new ArrayList<IJob>();
		ArrayList<Optional<IJob>> history = new ArrayList<Optional<IJob>>();
		// Standard job not containing necessary parts of list.
		Set<CarOption> parts = new HashSet<>();
		template = new CarModelSpecification("model", parts, 60);
		model = new CarModel(template);
		UnmodifiableClock ordertime1 = new UnmodifiableClock(2, 360); // om 6 uur op dag 2
		int quantity =5;
		StandardOrder order1 = new StandardOrder("Luigi", model, quantity, ordertime1); // 420 minuten op de band
		IJob job1 = new Job(order1);
		// batch job
		Set<CarOption> parts2 = new HashSet<>();
		parts2.add(new CarOption("manual", CarOptionCategory.AIRCO));
		template = new CarModelSpecification("model", parts2, 60);
		model = new CarModel(template);
		UnmodifiableClock ordertime2 = new UnmodifiableClock(2, 420); // om 7 uur op dag 2
		int quantity2 =5;
		StandardOrder order2 = new StandardOrder("Luigi", model, quantity2, ordertime2); // 420 minuten op de band
		IJob job2 = new Job(order2);
		standardJobs.add(job1);
		standardJobs.add(job2);
		scheduling.transform(jobs, standardJobs, history);
	}

	@Test (expected = IllegalArgumentException.class)
	public void transformTest2Error() throws NotImplementedException{
		scheduling.transform(null, null, null);
	}

	@Test (expected = IllegalArgumentException.class)
	public void transformTest3Error() throws NotImplementedException{
		PriorityQueue<IJob> jobs = new PriorityQueue<IJob>(10,new JobComparatorDeadLine());
		scheduling.transform(jobs, null, null);
	}

	@Test (expected = IllegalArgumentException.class)
	public void transformTest4Error() throws NotImplementedException{
		PriorityQueue<IJob> jobs = new PriorityQueue<IJob>(10,new JobComparatorDeadLine());
		ArrayList<IJob> standardJobs = new ArrayList<IJob>();
		scheduling.transform(jobs, standardJobs, null);
	}

	@Test (expected = IllegalArgumentException.class)
	public void transformTest5Error() throws NotImplementedException{
		PriorityQueue<IJob> jobs = new PriorityQueue<IJob>(10,new JobComparatorDeadLine());
		ArrayList<Optional<IJob>> history = new ArrayList<Optional<IJob>>();
		scheduling.transform(jobs, null, history);
	}

	@Test (expected = IllegalArgumentException.class)
	public void transformTest6Error() throws NotImplementedException{
		ArrayList<IJob> standardJobs = new ArrayList<IJob>();
		ArrayList<Optional<IJob>> history = new ArrayList<Optional<IJob>>();
		scheduling.transform(null, standardJobs, history);
	}

	@Test (expected = IllegalArgumentException.class)
	public void transformTest7Error() throws NotImplementedException{
		ArrayList<IJob> standardJobs = new ArrayList<IJob>();
		scheduling.transform(null, standardJobs, null);
	}
}
