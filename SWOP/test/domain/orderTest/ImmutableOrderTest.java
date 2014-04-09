package domain.orderTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import domain.car.CarModel;
import domain.car.CarModelSpecification;
import domain.car.CarOption;
import domain.car.CarOptionCategory;
import domain.clock.UnmodifiableClock;
import domain.exception.AlreadyInMapException;
import domain.exception.ImmutableException;
import domain.order.IOrder;
import domain.order.ImmutableOrder;
import domain.order.StandardOrder;

public class ImmutableOrderTest {
	CarModel model;
	IOrder order;
	IOrder immutable;
	CarModelSpecification template;
	@Before
	public void initialize() throws AlreadyInMapException{
		Set<CarOption> parts = new HashSet<>();
		parts.add(new CarOption("sport", CarOptionCategory.BODY));
		
		parts.add(new CarOption("black", CarOptionCategory.COLOR));
		parts.add(new CarOption("white", CarOptionCategory.COLOR));
		
		parts.add(new CarOption("performance 2.5l V6", CarOptionCategory.ENGINE));
		parts.add(new CarOption("ultra 3l V8", CarOptionCategory.ENGINE));
	
		parts.add(new CarOption("6 Speed Manual", CarOptionCategory.GEARBOX));
		
		parts.add(new CarOption("Leather White", CarOptionCategory.SEATS));
		parts.add(new CarOption("Leather Black", CarOptionCategory.SEATS));
		
		parts.add(new CarOption("Manual", CarOptionCategory.AIRCO));
		parts.add(new CarOption("Automatic", CarOptionCategory.AIRCO));
		
		parts.add(new CarOption("Winter", CarOptionCategory.WHEEL));
		parts.add(new CarOption("Sports", CarOptionCategory.WHEEL));
		
		parts.add(new CarOption("high", CarOptionCategory.SPOILER));
		parts.add(new CarOption("low", CarOptionCategory.SPOILER));
		template = new CarModelSpecification("model", parts, 60);
		model = new CarModel(template);
		model.addCarPart(new CarOption("manual", CarOptionCategory.AIRCO));
		model.addCarPart(new CarOption("sedan", CarOptionCategory.BODY));
		model.addCarPart(new CarOption("red", CarOptionCategory.COLOR));
		model.addCarPart(new CarOption("standard 2l 4 cilinders", CarOptionCategory.ENGINE));
		model.addCarPart(new CarOption("6 speed manual", CarOptionCategory.GEARBOX));
		model.addCarPart(new CarOption("leather black", CarOptionCategory.SEATS));
		model.addCarPart(new CarOption("comfort", CarOptionCategory.WHEEL));
		order = new StandardOrder("Mario",model,3);
		immutable = new ImmutableOrder(order);
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
		new ImmutableOrder(null);
	}
	
	@Test(expected = ImmutableException.class)
	public void testImmutable1() throws ImmutableException{
		immutable.setEstimatedTime(new UnmodifiableClock(0, 0));
	}
	
	@Test(expected = ImmutableException.class)
	public void testImmutable2() throws ImmutableException{
		immutable.completeCar();
	}
}
