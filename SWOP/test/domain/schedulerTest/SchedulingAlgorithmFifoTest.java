package domain.schedulerTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

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
import domain.observer.ClockObserver;
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
		
		IOrder order1 = new StandardOrder("Luigi", model, 5, ordertime1); // 420 minuten op de band

		//IOrder order2 = new StandardOrder("mario", model, 3, ordertime2);
		//ass.convertOrderToJob(order2);
		
		IJob job = new Job(order1);
		
		
		CustomCarModel customModel = new CustomCarModel();
		UnmodifiableClock ordertime = new UnmodifiableClock(0, 30);
		UnmodifiableClock deadline = new UnmodifiableClock(10, 800);
		CustomOrder customOrder = new CustomOrder("Mario", customModel, 5, ordertime, deadline);
		try {
			ass.convertCustomOrderToJob(customOrder);
		} catch (NotImplementedException e) {}
		
		UnmodifiableClock currentTime = new UnmodifiableClock(2, 480); // 8 uur op dag 2
		//assertEquals(new UnmodifiableClock(2, 780), order1.getEstimatedTime());
		assertEquals(new UnmodifiableClock(10, 800), customOrder.getEstimatedTime());
		
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
}
