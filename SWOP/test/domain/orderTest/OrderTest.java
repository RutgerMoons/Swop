package domain.orderTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import domain.assembly.workBench.WorkBenchType;
import domain.clock.ImmutableClock;
import domain.exception.AlreadyInMapException;
import domain.exception.NotImplementedException;
import domain.job.action.Action;
import domain.order.order.StandardOrder;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicle.Vehicle;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

public class OrderTest {

	private Vehicle model;
	private VehicleSpecification template;
	private VehicleSpecification template2;
	private ImmutableClock clock;

	@Before
	public void initializeModel() throws AlreadyInMapException {
		clock = new ImmutableClock(0,20);
		Set<VehicleOption> parts = new HashSet<>();
		parts.add(new VehicleOption("sport", VehicleOptionCategory.BODY));
		
		parts.add(new VehicleOption("black", VehicleOptionCategory.COLOR));
		parts.add(new VehicleOption("white", VehicleOptionCategory.COLOR));
		
		parts.add(new VehicleOption("performance 2.5l V6", VehicleOptionCategory.ENGINE));
		parts.add(new VehicleOption("ultra 3l V8", VehicleOptionCategory.ENGINE));
	
		parts.add(new VehicleOption("6 Speed Manual", VehicleOptionCategory.GEARBOX));
		
		parts.add(new VehicleOption("Leather White", VehicleOptionCategory.SEATS));
		parts.add(new VehicleOption("Leather Black", VehicleOptionCategory.SEATS));
		
		parts.add(new VehicleOption("Manual", VehicleOptionCategory.AIRCO));
		parts.add(new VehicleOption("Automatic", VehicleOptionCategory.AIRCO));
		
		parts.add(new VehicleOption("Winter", VehicleOptionCategory.WHEEL));
		parts.add(new VehicleOption("Sports", VehicleOptionCategory.WHEEL));
		
		parts.add(new VehicleOption("high", VehicleOptionCategory.SPOILER));
		parts.add(new VehicleOption("low", VehicleOptionCategory.SPOILER));
		template = new VehicleSpecification("model", parts, new HashMap<WorkBenchType, Integer>());
		HashMap<WorkBenchType,Integer> timeAtWorkbench = new HashMap<WorkBenchType,Integer>();
		timeAtWorkbench.put(WorkBenchType.BODY, 50);
		template2 = new VehicleSpecification("model2", parts, timeAtWorkbench);
		model = new Vehicle(template);
		model.addVehicleOption(new VehicleOption("manual", VehicleOptionCategory.AIRCO));
		model.addVehicleOption(new VehicleOption("sedan", VehicleOptionCategory.BODY));
		model.addVehicleOption(new VehicleOption("red", VehicleOptionCategory.COLOR));
		model.addVehicleOption(new VehicleOption("standard 2l 4 cilinders",
				VehicleOptionCategory.ENGINE));
		model.addVehicleOption(new VehicleOption("6 speed manual",
				VehicleOptionCategory.GEARBOX));
		model.addVehicleOption(new VehicleOption("leather black", VehicleOptionCategory.SEATS));
		model.addVehicleOption(new VehicleOption("comfort", VehicleOptionCategory.WHEEL));
	}

	@Test
	public void test1Constructor() {
		StandardOrder order = new StandardOrder("Mario", model, 3, clock);
		assertNotNull(order);
		assertEquals(order.getDescription(), model);
		assertEquals("Mario", order.getGarageHolder());
		assertEquals(3, order.getQuantity());
		assertEquals(3, order.getPendingCars());
		
		ImmutableClock clock = new ImmutableClock(0, 5);
		order.setEstimatedTime(clock);
		assertEquals(0, order.getEstimatedTime().getDays());
		assertEquals(5, order.getEstimatedTime().getMinutes());

	}

	@Test(expected = IllegalArgumentException.class)
	public void test2Constructor() {
		new StandardOrder(null, null, -1, clock);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetGarageHolder1() {
		new StandardOrder("", model, 3, clock);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetGarageHolder2() {
		StandardOrder order3 = new StandardOrder("Mario", model, 3, clock);
		assertNotNull(order3.getGarageHolder());
		new StandardOrder(null, model, 3, clock);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPendingCars1() {
		new StandardOrder("Mario", model, -1, clock);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPendingCars2() {
		StandardOrder order = new StandardOrder("Mario", model, 1, clock);
		order.completeCar();
		assertEquals(0, order.getPendingCars());
		order.completeCar();

	}

	@Test(expected = IllegalArgumentException.class)
	public void testQuantity() {
		new StandardOrder("Mario", model, 0, clock);
	}

	@Test
	public void testEstimatedTime1() {
		StandardOrder order = new StandardOrder("Mario", model, 3, clock);
		ImmutableClock clock = new ImmutableClock(0, 5);
		order.setEstimatedTime(clock);
		assertEquals(0, order.getEstimatedTime().getDays());
		assertEquals(5, order.getEstimatedTime().getMinutes());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEstimatedTime2() {
		StandardOrder order = new StandardOrder("Mario", model, 3, clock);
		order.setEstimatedTime(null);
	}

	@Test
	public void testCarCompleted() {
		StandardOrder order = new StandardOrder("Mario", model, 3, clock);
		assertEquals(3, order.getPendingCars());
		order.completeCar();
		assertEquals(2, order.getPendingCars());

	}

	@Test
	public void TestEqualsAndHashcode() {
		VehicleSpecification template2 = new VehicleSpecification("abc", new HashSet<VehicleOption>(), new HashMap<WorkBenchType, Integer>());
		Vehicle model2 = new Vehicle(template2);
		model2.addVehicleOption(new VehicleOption("manual", VehicleOptionCategory.AIRCO));
		model2.addVehicleOption(new VehicleOption("sedan", VehicleOptionCategory.BODY));
		model2.addVehicleOption(new VehicleOption("red", VehicleOptionCategory.COLOR));
		model2.addVehicleOption(new VehicleOption("standard 2l 4 cilinders",
				VehicleOptionCategory.ENGINE));
		model2.addVehicleOption(new VehicleOption("6 speed manual",
				VehicleOptionCategory.GEARBOX));
		model2.addVehicleOption(new VehicleOption("leather black", VehicleOptionCategory.SEATS));
		model2.addVehicleOption(new VehicleOption("comfort", VehicleOptionCategory.WHEEL));

		StandardOrder order1 = new StandardOrder("Jan", model, 2, clock);
		assertFalse(order1.equals(null));
		assertFalse(order1.equals(new Action("Paint")));
		StandardOrder order2 = new StandardOrder("Jan", model2, 2, clock);
		assertFalse(order1.equals(order2));
		order2 = new StandardOrder("Jos", model, 2, clock);
		assertFalse(order1.equals(order2));
		order2 = new StandardOrder("Jan", model, 1, clock);
		assertFalse(order1.equals(order2));
		order2 = new StandardOrder("Jan", model, 2, clock);
		assertTrue(order1.equals(order2));
		assertTrue(order1.equals(order1));
		assertEquals(order1.hashCode(), order2.hashCode());
	}

	@Test
	public void testToString() {
		StandardOrder order1 = new StandardOrder("Jan", model, 2, clock);
		
		order1.setEstimatedTime(new ImmutableClock(1, 100));
		assertEquals(
				"2 model",
				order1.toString());
	}
	
	@Test(expected=NotImplementedException.class)
	public void testGetDeadline() {
		StandardOrder order1 = new StandardOrder("Jan", model, 2, clock);
		order1.getDeadline();
	}
	
	@Test(expected=NotImplementedException.class)
	public void testSetDeadline(){
		StandardOrder order1 = new StandardOrder("Jan", model, 2, clock);
		order1.setDeadline(new ImmutableClock(0, 0));
	}
	
	@Test
	public void testGetProductionTime() {
		Vehicle model1 = new Vehicle(template2);
		StandardOrder order1 = new StandardOrder("Jan", model1, 2, clock);
		assertEquals(50, order1.getProductionTime());
	}
	
	@Test
	public void testGetAndSetOrderTime(){
		ImmutableClock clock = new ImmutableClock(4, 45);
		StandardOrder order1 = new StandardOrder("Jan", model, 2, clock);
		order1.setOrderTime(clock);
		assertEquals(clock, order1.getOrderTime());
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetIllegalOrderTime(){
		StandardOrder order1 = new StandardOrder("Jan", model, 2, clock);
		order1.setOrderTime(null);
	}
	
	@Test
	public void testGetVehicleOptions(){
		StandardOrder order1 = new StandardOrder("Jan", model, 2, clock);
		assertTrue(order1.getVehicleOptions().containsAll(model.getVehicleOptions().values()));
		assertTrue(model.getVehicleOptions().values().containsAll(order1.getVehicleOptions()));
		
	}
	
	@Test
	public void testGetVehicleSpecification(){
		StandardOrder order1 = new StandardOrder("Jan", model, 2, clock);
		assertEquals(model.getVehicleSpecification(), order1.getVehicleSpecification());
	}
	
}
