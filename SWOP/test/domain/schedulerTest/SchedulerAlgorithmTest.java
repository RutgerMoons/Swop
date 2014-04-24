package domain.schedulerTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ui.ClientCommunication;
import ui.IClientCommunication;

import com.google.common.base.Optional;

import domain.assembly.AssemblyLine;
import domain.car.CarModel;
import domain.car.CarModelSpecification;
import domain.car.CarOption;
import domain.car.CarOptionCategory;
import domain.car.CustomCarModel;
import domain.clock.Clock;
import domain.clock.UnmodifiableClock;
import domain.exception.AlreadyInMapException;
import domain.exception.ImmutableException;
import domain.exception.NoSuitableJobFoundException;
import domain.exception.NotImplementedException;
import domain.facade.Facade;
import domain.job.IJob;
import domain.job.Job;
import domain.observer.ClockObserver;
import domain.order.CustomOrder;
import domain.order.StandardOrder;
import domain.restriction.BindingRestriction;
import domain.restriction.OptionalRestriction;
import domain.restriction.PartPicker;
import domain.scheduler.Scheduler;

public class SchedulerAlgorithmTest {

	private Facade facade;
	private IClientCommunication clientCommunication;
	private Scheduler scheduler;
	private ClockObserver clock;
	private CarModel model;
	private CarModelSpecification template;
	private Set<BindingRestriction> bindingRestrictions;
	private Set<OptionalRestriction> optionalRestrictions;
	private PartPicker picker;
	
	@Before
	public void initialize() {
		facade = new Facade(bindingRestrictions, optionalRestrictions);
		clientCommunication = new ClientCommunication();
		int amount = 3;
		clock = new ClockObserver();
		scheduler = new Scheduler(amount,clock, new UnmodifiableClock(0,600));
	}
	
	public void initializeRestrictions(){
		bindingRestrictions = new HashSet<>();
		optionalRestrictions = new HashSet<>();
		optionalRestrictions.add(new OptionalRestriction(new CarOption("sport", CarOptionCategory.BODY), CarOptionCategory.SPOILER, false));
		
		bindingRestrictions.add(new BindingRestriction(new CarOption("sport", CarOptionCategory.BODY), new CarOption("performance 2.5l V6", CarOptionCategory.ENGINE)));
		bindingRestrictions.add(new BindingRestriction(new CarOption("sport", CarOptionCategory.BODY), new CarOption("ultra 3l V8", CarOptionCategory.ENGINE)));
		
		bindingRestrictions.add(new BindingRestriction(new CarOption("ultra 3l V8", CarOptionCategory.ENGINE), new CarOption("manual", CarOptionCategory.AIRCO)));
		
		picker = new PartPicker(bindingRestrictions, optionalRestrictions);
		
		Set<CarOption> parts = new HashSet<>();
		parts.add(new CarOption("sport", CarOptionCategory.BODY));
		
		parts.add(new CarOption("black", CarOptionCategory.COLOR));
		parts.add(new CarOption("white", CarOptionCategory.COLOR));
		
		parts.add(new CarOption("performance 2.5l V6", CarOptionCategory.ENGINE));
		parts.add(new CarOption("ultra 3l V8", CarOptionCategory.ENGINE));
	
		parts.add(new CarOption("6 speed manual", CarOptionCategory.GEARBOX));
		
		parts.add(new CarOption("leather white", CarOptionCategory.SEATS));
		parts.add(new CarOption("leather black", CarOptionCategory.SEATS));
		
		parts.add(new CarOption("manual", CarOptionCategory.AIRCO));
		parts.add(new CarOption("automatic", CarOptionCategory.AIRCO));
		
		parts.add(new CarOption("winter", CarOptionCategory.WHEEL));
		parts.add(new CarOption("sports", CarOptionCategory.WHEEL));
		
		parts.add(new CarOption("high", CarOptionCategory.SPOILER));
		parts.add(new CarOption("low", CarOptionCategory.SPOILER));
		CarModelSpecification template = new CarModelSpecification("model", parts, 60);
		picker.setNewModel(template);
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
	
	@Test
	public void getAllCarOptionsInPendingOrdersTest() {
		Set<CarOption> parts = new HashSet<>();
		parts.add(new CarOption("sport", CarOptionCategory.BODY));
		CarModelSpecification template = new CarModelSpecification("model", parts, 60);
		model = new CarModel(template);

		try {
			model.addCarPart(new CarOption("manual", CarOptionCategory.AIRCO));
			model.addCarPart(new CarOption("sedan",  CarOptionCategory.BODY));
			model.addCarPart(new CarOption("red",  CarOptionCategory.COLOR));
			model.addCarPart(new CarOption("standard 2l 4 cilinders",  CarOptionCategory.ENGINE));
			model.addCarPart(new CarOption("6 speed manual",  CarOptionCategory.GEARBOX));
			model.addCarPart(new CarOption("leather black", CarOptionCategory.SEATS));
			model.addCarPart(new CarOption("comfort", CarOptionCategory.WHEEL));
		} catch (AlreadyInMapException e) {}
		Clock c = new Clock();
		AssemblyLine line = new AssemblyLine(clock, c.getUnmodifiableClock());
		StandardOrder order = new StandardOrder("Luigi", this.model, 5, c.getUnmodifiableClock());
		try {
			line.convertStandardOrderToJob(order);
		} catch (ImmutableException e) { }
		
		Set<Set<CarOption>> powerSet = line.getAllCarOptionsInPendingOrders();
		assertEquals(127, powerSet.size());
	}
	
	@Test
	public void getCurrentSchedulingAlgorithmAsStringTest() {
		assertTrue(facade.getCurrentSchedulingAlgorithmAsString().equalsIgnoreCase("fifo"));
		this.facade.switchToBatch(Collections.EMPTY_LIST);
		assertTrue(facade.getCurrentSchedulingAlgorithmAsString().equalsIgnoreCase("batch"));
		assertEquals(2, facade.getPossibleSchedulingAlgorithms().size());
		assertTrue(facade.getPossibleSchedulingAlgorithms().contains("Fifo"));
		assertTrue(facade.getPossibleSchedulingAlgorithms().contains("Batch"));
	}
} 
