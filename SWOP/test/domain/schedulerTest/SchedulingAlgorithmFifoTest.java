package domain.schedulerTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;

import domain.assembly.AssemblyLine;
import domain.car.CarModel;
import domain.car.CarModelSpecification;
import domain.car.CarOption;
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
import domain.scheduler.SchedulingAlgorithmFifo;


public class SchedulingAlgorithmFifoTest {

	SchedulingAlgorithmFifo algorithm;
	private CarModel model;
	private CarModelSpecification template;

	@Before
	public void testConstructor() {
		int amountOfWorkBenches = 3;
		algorithm = new SchedulingAlgorithmFifo(amountOfWorkBenches);	
		assertNotNull(algorithm.getCustomJobs());
		assertNotNull(algorithm.getHistory());
		assertNotNull(algorithm.getStandardJobs());
	}

	@Test
	public void testAddCustomOrder1() throws AlreadyInMapException{
		CustomCarModel customModel = new CustomCarModel();
		UnmodifiableClock ordertime = new UnmodifiableClock(2, 30);
		UnmodifiableClock deadline = new UnmodifiableClock(5, 30);
		CustomOrder order = new CustomOrder("Mario", customModel, 5, ordertime, deadline);
		IJob job = new Job(order);
		algorithm.AddCustomJob(job);
		assertEquals(1, algorithm.getCustomJobs().size());
		assertEquals(0, algorithm.getStandardJobs().size());

	}

	@Test (expected = IllegalArgumentException.class)
	public void testAddCustomOrder2(){
		algorithm.AddCustomJob(null);
	}

	@Test
	public void testAddStandardJob1(){
		Set<CarOption> parts = new HashSet<>();
		template = new CarModelSpecification("model", parts, 60);
		model = new CarModel(template);
		UnmodifiableClock ordertime = new UnmodifiableClock(2, 30);
		IOrder order = new StandardOrder("mario", model, 3, ordertime);
		IJob job = new Job(order);
		algorithm.AddStandardJob(job);
		assertEquals(1,algorithm.getStandardJobs().size());
	}

	@Test (expected = IllegalArgumentException.class)
	public void testAddStandardJob2(){
		algorithm.AddStandardJob(null);
	}

	@Test
	public void testGetEstimatedTimeInMinutes1() throws ImmutableException{
		ClockObserver obs = new ClockObserver();
		AssemblyLine ass = new AssemblyLine(obs, new UnmodifiableClock(2, 360));
		ass.switchToFifo();
		Set<CarOption> parts = new HashSet<>();
		template = new CarModelSpecification("model", parts, 60);
		model = new CarModel(template);
		UnmodifiableClock ordertime1 = new UnmodifiableClock(2, 360); // om 6 uur op dag 2
		//UnmodifiableClock ordertime2 = new UnmodifiableClock(2, 420); // om 7 uur op dag 2
		int quantity =5;
		StandardOrder order1 = new StandardOrder("Luigi", model, quantity, ordertime1); // 420 minuten op de band
		OrderBook orderbook = new OrderBook(ass);
		try {
			orderbook.addOrder(order1, ordertime1);
		} catch (NotImplementedException e1) {}

		CustomCarModel customModel = new CustomCarModel();
		UnmodifiableClock ordertime = new UnmodifiableClock(0, 0);
		UnmodifiableClock deadline = new UnmodifiableClock(10, 800);
		CustomOrder customOrder = new CustomOrder("Mario", customModel, 5, ordertime, deadline);
		try {
			orderbook.addOrder(customOrder, ordertime);
		} catch (NotImplementedException e) {}

		//assertEquals(new UnmodifiableClock(2, 780), order1.getEstimatedTime());
		assertEquals(new UnmodifiableClock(8, 440), customOrder.getEstimatedTime());
		assertEquals(new UnmodifiableClock(2,780), order1.getEstimatedTime());
	}

	@Test (expected = IllegalArgumentException.class)
	public void testGetEstimatedTimeInMinutes2(){
		UnmodifiableClock clock = new UnmodifiableClock(0,500);
		try {
			algorithm.getEstimatedTimeInMinutes(null, clock);
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
			algorithm.getEstimatedTimeInMinutes(customJob, null);
		} catch (NotImplementedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void startNewDayTest(){
		ClockObserver obs = new ClockObserver();
		AssemblyLine ass = new AssemblyLine(obs, new UnmodifiableClock(2, 360));
		ass.switchToFifo();
		CustomCarModel customModel = new CustomCarModel();
		UnmodifiableClock ordertime = new UnmodifiableClock(0, 0);
		UnmodifiableClock deadline = new UnmodifiableClock(10, 800);
		CustomOrder customOrder = new CustomOrder("Mario", customModel, 5, ordertime, deadline);
		OrderBook orderbook = new OrderBook(ass);
		try {
			orderbook.addOrder(customOrder, ordertime);
		} catch (ImmutableException e) {}
		catch (NotImplementedException e) {}
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
