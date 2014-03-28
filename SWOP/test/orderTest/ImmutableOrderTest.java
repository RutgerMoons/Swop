package orderTest;

import static org.junit.Assert.*;
import order.IOrder;
import order.ImmutableOrder;
import order.Order;

import org.junit.BeforeClass;
import org.junit.Test;

import car.Airco;
import car.Body;
import car.CarModel;
import car.Color;
import car.Engine;
import car.Gearbox;
import car.Seat;
import car.Wheel;

public class ImmutableOrderTest {


	@Test
	public void test() {
		CarModel model = new CarModel("Volkswagen", new Airco("manual"), new Body("sedan"), new Color("blue"), 
				new Engine("standard 2l 4 cilinders"), new Gearbox("6 speed manual"), new Seat("leather black"), new Wheel("comfort"));
		Order order = new Order("Mario",model,3);
		IOrder immutable = new ImmutableOrder(order);
		assertEquals(order.getDescription(), immutable.getDescription());
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
}
