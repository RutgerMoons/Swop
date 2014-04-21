package domain.schedulerTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import domain.clock.UnmodifiableClock;
import domain.observer.ClockObserver;
import domain.scheduler.Scheduler;

public class SchedulerAlgorithmTest {

	private Scheduler scheduler;
	private ClockObserver clock;

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
		
	}
	
	

}
