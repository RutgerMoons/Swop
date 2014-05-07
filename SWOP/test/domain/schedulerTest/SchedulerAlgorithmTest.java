package domain.schedulerTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import view.ClientCommunication;
import view.IClientCommunication;

import com.google.common.base.Optional;

import domain.assembly.AssemblyLine;
import domain.clock.Clock;
import domain.clock.ImmutableClock;
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
import domain.scheduling.Scheduler;
import domain.vehicle.CustomVehicle;
import domain.vehicle.Vehicle;
import domain.vehicle.VehicleOption;
import domain.vehicle.VehicleOptionCategory;
import domain.vehicle.VehicleSpecification;

public class SchedulerAlgorithmTest {

	private Facade facade;
	private IClientCommunication clientCommunication;
	private Scheduler scheduler;
	private ClockObserver clock;
	private Vehicle model;
	private VehicleSpecification template;
	private Set<BindingRestriction> bindingRestrictions;
	private Set<OptionalRestriction> optionalRestrictions;
	private PartPicker picker;
	
	@Before
	public void initialize() {
		facade = new Facade(bindingRestrictions, optionalRestrictions);
		clientCommunication = new ClientCommunication();
		int amount = 3;
		clock = new ClockObserver();
		scheduler = new Scheduler(amount,clock, new ImmutableClock(0,600));
	}
	
	public void initializeRestrictions(){
		bindingRestrictions = new HashSet<>();
		optionalRestrictions = new HashSet<>();
		optionalRestrictions.add(new OptionalRestriction(new VehicleOption("sport", VehicleOptionCategory.BODY), VehicleOptionCategory.SPOILER, false));
		
		bindingRestrictions.add(new BindingRestriction(new VehicleOption("sport", VehicleOptionCategory.BODY), new VehicleOption("performance 2.5l V6", VehicleOptionCategory.ENGINE)));
		bindingRestrictions.add(new BindingRestriction(new VehicleOption("sport", VehicleOptionCategory.BODY), new VehicleOption("ultra 3l V8", VehicleOptionCategory.ENGINE)));
		
		bindingRestrictions.add(new BindingRestriction(new VehicleOption("ultra 3l V8", VehicleOptionCategory.ENGINE), new VehicleOption("manual", VehicleOptionCategory.AIRCO)));
		
		picker = new PartPicker(bindingRestrictions, optionalRestrictions);
		
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
		VehicleSpecification template = new VehicleSpecification("model", parts, 60);
		picker.setNewModel(template);
	}
	
	@Test
	public void constructorTest(){
		assertNotNull(scheduler);	
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void constructorTest2(){
		scheduler = new Scheduler(3,null, new ImmutableClock(0,100));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void constructorTest3(){
		scheduler = new Scheduler(3, clock,null);
	}
	
	@Test
	public void addCustomJobTest(){
		CustomVehicle customModel = new CustomVehicle();
		ImmutableClock ordertime = new ImmutableClock(0, 0);
		ImmutableClock deadline = new ImmutableClock(10, 800);
		CustomOrder customOrder = new CustomOrder("Mario", customModel, 5, ordertime, deadline);
		IJob job = new Job(customOrder);
		scheduler.addCustomJob(job);
	}
	
	@Test
	public void addStandardJobTest(){
		Set<VehicleOption> parts = new HashSet<>();
		template = new VehicleSpecification("model", parts, 60);
		model = new Vehicle(template);
		ImmutableClock ordertime1 = new ImmutableClock(2, 360); 
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
		List<VehicleOption> options = new ArrayList<VehicleOption>();
		options.add(new VehicleOption("manual", VehicleOptionCategory.AIRCO));
		scheduler.switchToBatch(options);
	}
	
	@Test
	public void advanceTimeTest(){
		scheduler.advanceTime(new ImmutableClock(0,540));
	}
	
	@Test (expected = IllegalArgumentException.class)
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
	public void getEstimatedTimeInMinutes() throws NotImplementedException{
		Set<VehicleOption> parts = new HashSet<>();
		template = new VehicleSpecification("model", parts, 60);
		model = new Vehicle(template);
		ImmutableClock ordertime1 = new ImmutableClock(2, 360); 
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
		Set<VehicleOption> parts = new HashSet<>();
		template = new VehicleSpecification("model", parts, 60);
		model = new Vehicle(template);
		ImmutableClock ordertime1 = new ImmutableClock(0, 660); // om 6 uur op dag 2
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
		Set<VehicleOption> parts = new HashSet<>();
		parts.add(new VehicleOption("sport", VehicleOptionCategory.BODY));
		VehicleSpecification template = new VehicleSpecification("model", parts, 60);
		model = new Vehicle(template);

		try {
			model.addCarPart(new VehicleOption("manual", VehicleOptionCategory.AIRCO));
			model.addCarPart(new VehicleOption("sedan",  VehicleOptionCategory.BODY));
			model.addCarPart(new VehicleOption("red",  VehicleOptionCategory.COLOR));
			model.addCarPart(new VehicleOption("standard 2l 4 cilinders",  VehicleOptionCategory.ENGINE));
			model.addCarPart(new VehicleOption("6 speed manual",  VehicleOptionCategory.GEARBOX));
			model.addCarPart(new VehicleOption("leather black", VehicleOptionCategory.SEATS));
			model.addCarPart(new VehicleOption("comfort", VehicleOptionCategory.WHEEL));
		} catch (AlreadyInMapException e) {}
		Clock c = new Clock();
		AssemblyLine line = new AssemblyLine(clock, c.getUnmodifiableClock());
		StandardOrder order = new StandardOrder("Luigi", this.model, 5, c.getUnmodifiableClock());
		try {
			line.convertStandardOrderToJob(order);
		} catch (ImmutableException e) { }
		
		Set<Set<VehicleOption>> powerSet = line.getAllCarOptionsInPendingOrders();
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
