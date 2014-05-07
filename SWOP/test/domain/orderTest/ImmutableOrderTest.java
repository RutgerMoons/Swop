package domain.orderTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import domain.car.Vehicle;
import domain.car.VehicleSpecification;
import domain.car.VehicleOption;
import domain.car.VehicleOptionCategory;
import domain.clock.ImmutableClock;
import domain.exception.AlreadyInMapException;
import domain.exception.ImmutableException;
import domain.exception.NotImplementedException;
import domain.order.IOrder;
import domain.order.UnmodifiableOrder;
import domain.order.StandardOrder;

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
		template = new VehicleSpecification("model", parts, 60);
		model = new Vehicle(template);
		model.addCarPart(new VehicleOption("manual", VehicleOptionCategory.AIRCO));
		model.addCarPart(new VehicleOption("sedan", VehicleOptionCategory.BODY));
		model.addCarPart(new VehicleOption("red", VehicleOptionCategory.COLOR));
		model.addCarPart(new VehicleOption("standard 2l 4 cilinders", VehicleOptionCategory.ENGINE));
		model.addCarPart(new VehicleOption("6 speed manual", VehicleOptionCategory.GEARBOX));
		model.addCarPart(new VehicleOption("leather black", VehicleOptionCategory.SEATS));
		model.addCarPart(new VehicleOption("comfort", VehicleOptionCategory.WHEEL));
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
		assertEquals(order.hashCode(), immutable.hashCode());
		assertEquals(order.toString(), immutable.toString());
	}

	@Test (expected = IllegalArgumentException.class)
	public void invalidConstructorTest(){
		new UnmodifiableOrder(null);
	}
	
	@Test(expected = ImmutableException.class)
	public void testImmutable1() throws ImmutableException{
		immutable.setEstimatedTime(new ImmutableClock(0, 0));
	}
	
	@Test(expected = ImmutableException.class)
	public void testImmutable2() throws ImmutableException{
		immutable.completeCar();
	}
	
	@Test(expected = NotImplementedException.class)
	public void testGetDeadline() throws NotImplementedException{
		immutable.getDeadline();
	}
	
	@Test(expected = ImmutableException.class)
	public void testSetDeadline() throws NotImplementedException, ImmutableException{
		immutable.setDeadline(new ImmutableClock(5, 5));
	}
	
	@Test(expected=ImmutableException.class)
	public void testGetAndSetOrderTime() throws ImmutableException{
		order.setOrderTime(new ImmutableClock(0, 0));
		assertEquals(order.getOrderTime(), immutable.getOrderTime());
		
		
		immutable.setOrderTime(new ImmutableClock(3, 3));
	}

	@Test
	public void testGetProductionTime() throws NotImplementedException{
		assertEquals(order.getProductionTime(), immutable.getProductionTime());
	}
}
