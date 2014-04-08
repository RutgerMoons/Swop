package domain.orderTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import domain.car.CarModel;
import domain.car.CarPart;
import domain.car.CarPartType;
import domain.exception.AlreadyInMapException;
import domain.exception.ImmutableException;
import domain.order.IOrder;
import domain.order.ImmutableOrder;
import domain.order.StandardOrder;

public class ImmutableOrderTest {
	CarModel model;
	IOrder order;
	IOrder immutable;
	@Before
	public void initialize() throws AlreadyInMapException{
		model = new CarModel("Volkswagen");
		model.addCarPart(new CarPart("manual", true, CarPartType.AIRCO));
		model.addCarPart(new CarPart("sedan", false, CarPartType.BODY));
		model.addCarPart(new CarPart("red", false, CarPartType.COLOR));
		model.addCarPart(new CarPart("standard 2l 4 cilinders", false, CarPartType.ENGINE));
		model.addCarPart(new CarPart("6 speed manual", false, CarPartType.GEARBOX));
		model.addCarPart(new CarPart("leather black", false, CarPartType.SEATS));
		model.addCarPart(new CarPart("comfort", false, CarPartType.WHEEL));
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
		int[] array = {0,0};
		immutable.setEstimatedTime(array);
	}
	
	@Test(expected = ImmutableException.class)
	public void testImmutable2() throws ImmutableException{
		immutable.completeCar();
	}
}
