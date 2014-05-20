package domain.order.orderTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import domain.assembly.workBench.WorkBenchType;
import domain.clock.ImmutableClock;
import domain.exception.AlreadyInMapException;
import domain.exception.UnmodifiableException;
import domain.exception.NotImplementedException;
import domain.order.order.CustomOrder;
import domain.order.order.IOrder;
import domain.order.order.StandardOrder;
import domain.order.order.UnmodifiableOrder;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicle.CustomVehicle;
import domain.vehicle.vehicle.Vehicle;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

public class ImmutableOrderTest {
	Vehicle model;
	IOrder order;
	IOrder immutable;
	VehicleSpecification template;

	@Before
	public void initialize() throws AlreadyInMapException{
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
		template = new VehicleSpecification("model", parts, new HashMap<WorkBenchType, Integer>(), new HashSet<VehicleOption>());
		model = new Vehicle(template);
		model.addVehicleOption(new VehicleOption("manual", VehicleOptionCategory.AIRCO));
		model.addVehicleOption(new VehicleOption("sedan", VehicleOptionCategory.BODY));
		model.addVehicleOption(new VehicleOption("red", VehicleOptionCategory.COLOR));
		model.addVehicleOption(new VehicleOption("standard 2l 4 cilinders", VehicleOptionCategory.ENGINE));
		model.addVehicleOption(new VehicleOption("6 speed manual", VehicleOptionCategory.GEARBOX));
		model.addVehicleOption(new VehicleOption("leather black", VehicleOptionCategory.SEATS));
		model.addVehicleOption(new VehicleOption("comfort", VehicleOptionCategory.WHEEL));
		order = new StandardOrder("Mario",model,3, new ImmutableClock(1, 1));
		immutable = new UnmodifiableOrder(order);
	}

	@Test
	public void test() {
		
		assertEquals(immutable.getDescription(), model);
		assertEquals(order.getEstimatedTime(), immutable.getEstimatedTime());
		assertEquals(order.getGarageHolder(), immutable.getGarageHolder());
		assertEquals(order.getPendingCars(), immutable.getPendingCars());
		assertEquals(order.getQuantity(), immutable.getQuantity());
		assertTrue(immutable.equals(order));
		assertTrue(order.equals(immutable));
		
		assertEquals(order.hashCode(), immutable.hashCode());
		assertEquals(order.toString(), immutable.toString());
	}

	@Test (expected = IllegalArgumentException.class)
	public void invalidConstructorTest(){
		new UnmodifiableOrder(null);
	}
	
	@Test(expected = UnmodifiableException.class)
	public void testImmutable1() throws UnmodifiableException{
		immutable.setEstimatedTime(new ImmutableClock(0, 0));
	}
	
	@Test(expected = UnmodifiableException.class)
	public void testImmutable2() throws UnmodifiableException{
		immutable.completeCar();
	}
	
	@Test(expected = NotImplementedException.class)
	public void testGetDeadline() throws NotImplementedException{
		immutable.getDeadline();
	}
	
	@Test(expected = UnmodifiableException.class)
	public void testSetDeadline() throws NotImplementedException, UnmodifiableException{
		immutable.setDeadline(new ImmutableClock(5, 5));
	}
	
	@Test(expected=UnmodifiableException.class)
	public void testGetAndSetOrderTime() throws UnmodifiableException{
		order.setOrderTime(new ImmutableClock(0, 0));
		assertEquals(order.getOrderTime(), immutable.getOrderTime());
		
		
		immutable.setOrderTime(new ImmutableClock(3, 3));
	}

	@Test
	public void testGetProductionTime() throws NotImplementedException{
		assertEquals(order.getProductionTime(), immutable.getProductionTime());
	}
	
	@Test(expected = NotImplementedException.class)
	public void testGetStandardDeadine(){
		order.setDeadline(new ImmutableClock(0, 0));
		assertEquals(order.getDeadline(), immutable.getDeadline());
	}
	
	@Test
	public void testGetCustomDeadine(){
		CustomOrder order1 = new CustomOrder("garageholder", new CustomVehicle(), 1, order.getOrderTime(), new ImmutableClock(0, 0));
		UnmodifiableOrder immutable = new UnmodifiableOrder(order1);
		assertEquals(order1.getDeadline(), immutable.getDeadline());
	}
	
	@Test
	public void testGetVehicleOptions(){
		assertEquals(order.getVehicleOptions().size(), immutable.getVehicleOptions().size());
		assertTrue(immutable.getVehicleOptions().containsAll(order.getVehicleOptions()));
		
	}
	
	@Test
	public void testGetTimeAtWorkBench(){
		assertEquals(order.getTimeAtWorkBench(), immutable.getTimeAtWorkBench());
	}
	
	@Test
	public void testGetVehicleSpecification(){
		assertEquals(order.getVehicleSpecification(), immutable.getVehicleSpecification());
	}
}
