package domain.schedulerTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;

import domain.car.CarModel;
import domain.car.CarModelSpecification;
import domain.car.CarOption;
import domain.car.CarOptionCategory;
import domain.car.CustomCarModel;
import domain.clock.UnmodifiableClock;
import domain.exception.NoSuitableJobFoundException;
import domain.exception.NotImplementedException;
import domain.job.IJob;
import domain.job.Job;
import domain.observer.ClockObserver;
import domain.order.CustomOrder;
import domain.order.StandardOrder;
import domain.scheduler.Scheduler;

public class SchedulerAlgorithmTest {

	private Scheduler scheduler;
	private ClockObserver clock;
	private CarModel model;
	private CarModelSpecification template;
	
	@Before
	public void initialize() {
		int amount = 3;
		clock = new ClockObserver();
		scheduler = new Scheduler(amount,clock, new UnmodifiableClock(0,600));
	}
	
	@Test
	public void constructorTest(){
		assertNotNull(scheduler);	
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void constructorTest2(){
		scheduler = new Scheduler(3,null, new UnmodifiableClock(0,100));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void constructorTest3(){
		scheduler = new Scheduler(3, clock,null);
	}
	
	@Test
	public void addCustomJobTest(){
		CustomCarModel customModel = new CustomCarModel();
		UnmodifiableClock ordertime = new UnmodifiableClock(0, 0);
		UnmodifiableClock deadline = new UnmodifiableClock(10, 800);
		CustomOrder customOrder = new CustomOrder("Mario", customModel, 5, ordertime, deadline);
		IJob job = new Job(customOrder);
		scheduler.addCustomJob(job);
	}
	
	@Test
	public void addStandardJobTest(){
		Set<CarOption> parts = new HashSet<>();
		template = new CarModelSpecification("model", parts, 60);
		model = new CarModel(template);
		UnmodifiableClock ordertime1 = new UnmodifiableClock(2, 360); 
		int quantity =5;
		StandardOrder order1 = new StandardOrder("Luigi", model, quantity, ordertime1);
		IJob job = new Job(order1);
		scheduler.addStandardJob(job);
	}
	
	@Test
	public void switchToFifoTest(){
		scheduler.switchToFifo();
	}
	
	@Test
	public void switchToBatchTest(){
		List<CarOption> options = new ArrayList<CarOption>();
		options.add(new CarOption("manual", CarOptionCategory.AIRCO));
		scheduler.switchToBatch(options);
	}
	
	@Test
	public void advanceTimeTest(){
		scheduler.advanceTime(new UnmodifiableClock(0,540));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void advanceTimeTest2(){
		scheduler.advanceTime(null);
	}
	
	@Test
	public void startNewDayTest(){
		scheduler.startNewDay(new UnmodifiableClock(1,420));
	}

	@Test (expected = IllegalArgumentException.class)
	public void startNewDayTest2(){
		scheduler.startNewDay(null);
	}
	
	@Test
	public void getEstimatedTimeInMinutes() throws NotImplementedException{
		Set<CarOption> parts = new HashSet<>();
		template = new CarModelSpecification("model", parts, 60);
		model = new CarModel(template);
		UnmodifiableClock ordertime1 = new UnmodifiableClock(2, 360); 
		int quantity =5;
		StandardOrder order1 = new StandardOrder("Luigi", model, quantity, ordertime1);
		IJob job = new Job(order1);
		scheduler.addStandardJob(job);
		int time = scheduler.getEstimatedTimeInMinutes(job);
		assertEquals(180, time);
	}
	
	@Test
	public void retrieveNextJobTest() throws NotImplementedException{
		// Standard job not containing necessary parts of list.
		Set<CarOption> parts = new HashSet<>();
		template = new CarModelSpecification("model", parts, 60);
		model = new CarModel(template);
		UnmodifiableClock ordertime1 = new UnmodifiableClock(0, 660); // om 6 uur op dag 2
		int quantity =5;
		StandardOrder order1 = new StandardOrder("Luigi", model, quantity, ordertime1); // 420 minuten op de band
		IJob job1 = new Job(order1);
		IJob job2 = new Job(order1);
		try {
			scheduler.addStandardJob(job1);
			scheduler.addStandardJob(job2);
			Optional<IJob> job = scheduler.retrieveNextJob();
			assertEquals(job1, job.get());
		} catch (NoSuitableJobFoundException e1) {}
	}
}
