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
	public void startNewDayTest(){
		ClockObserver obs = new ClockObserver();
		AssemblyLine ass = new AssemblyLine(obs, new UnmodifiableClock(2, 360));
		ass.switchToBatch(list);
		CustomCarModel customModel = new CustomCarModel();
		UnmodifiableClock ordertime = new UnmodifiableClock(0, 0);
		UnmodifiableClock deadline = new UnmodifiableClock(10, 800);
		CustomOrder customOrder = new CustomOrder("Mario", customModel, 5, ordertime, deadline);
		OrderBook orderbook = new OrderBook(ass);
		try {
			orderbook.addOrder(customOrder, ordertime);
		} catch (ImmutableException e) {}
		catch (NotImplementedException e) {}
		scheduling.startNewDay();
	}
	
	
	@Test
	public void transformTest1(){
		PriorityQueue<IJob> jobs = new PriorityQueue<IJob>(10,new JobComparatorDeadLine());
		ArrayList<IJob> standardJobs = new ArrayList<IJob>();
		ArrayList<Optional<IJob>> history = new ArrayList<Optional<IJob>>();
		scheduling.transform(jobs, standardJobs, history);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void transformTest2Error(){
		scheduling.transform(null, null, null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void transformTest3Error(){
		PriorityQueue<IJob> jobs = new PriorityQueue<IJob>(10,new JobComparatorDeadLine());
		scheduling.transform(jobs, null, null);
	}

	@Test (expected = IllegalArgumentException.class)
	public void transformTest4Error(){
		PriorityQueue<IJob> jobs = new PriorityQueue<IJob>(10,new JobComparatorDeadLine());
		ArrayList<IJob> standardJobs = new ArrayList<IJob>();
		scheduling.transform(jobs, standardJobs, null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void transformTest5Error(){
		PriorityQueue<IJob> jobs = new PriorityQueue<IJob>(10,new JobComparatorDeadLine());
		ArrayList<Optional<IJob>> history = new ArrayList<Optional<IJob>>();
		scheduling.transform(jobs, null, history);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void transformTest6Error(){
		ArrayList<IJob> standardJobs = new ArrayList<IJob>();
		ArrayList<Optional<IJob>> history = new ArrayList<Optional<IJob>>();
		scheduling.transform(null, standardJobs, history);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void transformTest7Error(){
		ArrayList<IJob> standardJobs = new ArrayList<IJob>();
		scheduling.transform(null, standardJobs, null);
	}
}
