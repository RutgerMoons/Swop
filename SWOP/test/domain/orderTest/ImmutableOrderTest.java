package domain.orderTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import domain.car.CarModel;
import domain.car.CarOption;
import domain.car.CarOptionCategogry;
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
		model.addCarPart(new CarOption("manual", true, CarOptionCategogry.AIRCO));
		model.addCarPart(new CarOption("sedan", false, CarOptionCategogry.BODY));
		model.addCarPart(new CarOption("red", false, CarOptionCategogry.COLOR));
		model.addCarPart(new CarOption("standard 2l 4 cilinders", false, CarOptionCategogry.ENGINE));
		model.addCarPart(new CarOption("6 speed manual", false, CarOptionCategogry.GEARBOX));
		model.addCarPart(new CarOption("leather black", false, CarOptionCategogry.SEATS));
		model.addCarPart(new CarOption("comfort", false, CarOptionCategogry.WHEEL));
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
