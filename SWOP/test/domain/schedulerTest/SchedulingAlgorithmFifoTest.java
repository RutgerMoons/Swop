package domain.schedulerTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import domain.car.CarModel;
import domain.car.CarModelSpecification;
import domain.car.CarOption;
import domain.car.CustomCarModel;
import domain.clock.UnmodifiableClock;
import domain.exception.AlreadyInMapException;
import domain.exception.NotImplementedException;
import domain.job.IJob;
import domain.job.Job;
import domain.order.CustomOrder;
import domain.order.IOrder;
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
		IOrder order = new StandardOrder("mario", model, 3);
		IJob job = new Job(order);
		algorithm.AddStandardJob(job);
		assertEquals(1,algorithm.getStandardJobs().size());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testAddStandardJob2(){
		algorithm.AddStandardJob(null);
	}
	
	@Test
	public void testGetEstimatedTimeInMinutes1(){
		Set<CarOption> parts = new HashSet<>();
		template = new CarModelSpecification("model", parts, 60);
		model = new CarModel(template);
		IOrder order1 = new StandardOrder("Luigi", model, 5);
		IJob job1 = new Job(order1);
		algorithm.AddStandardJob(job1);
		IOrder order2 = new StandardOrder("mario", model, 3);
		IJob job2 = new Job(order2);
		algorithm.AddStandardJob(job2);
		CustomCarModel customModel = new CustomCarModel();
		UnmodifiableClock ordertime = new UnmodifiableClock(0, 30);
		UnmodifiableClock deadline = new UnmodifiableClock(0, 800);
		CustomOrder customOrder = new CustomOrder("Mario", customModel, 5, ordertime, deadline);
		IJob customJob = new Job(customOrder);
		algorithm.AddCustomJob(customJob);
		UnmodifiableClock currentTime = new UnmodifiableClock(0, 200);
		try {
			assertEquals(0, algorithm.getEstimatedTimeInMinutes(job1, currentTime));
			int time1 = algorithm.getEstimatedTimeInMinutes(job2, currentTime);
			System.out.println(time1);
			assertEquals(600, algorithm.getEstimatedTimeInMinutes(customJob, currentTime));
		} catch (NotImplementedException e) {
			System.out.println("error");
			e.printStackTrace();
		}
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testGetEstimatedTimeInMinutes2(){
		UnmodifiableClock clock = new UnmodifiableClock(500);
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
}
