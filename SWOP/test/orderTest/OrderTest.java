package orderTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import order.Order;

import org.junit.Before;
import org.junit.Test;

import assembly.Action;
import car.CarModel;
import car.CarPart;
import car.CarPartType;
import exception.AlreadyInMapException;

public class OrderTest {

	private CarModel model;

	@Before
	public void initializeModel() throws AlreadyInMapException {
		model = new CarModel("Volkswagen");
		model.addCarPart(new CarOption("manual", true, CarOptionCategogry.AIRCO));
		model.addCarPart(new CarOption("sedan", false, CarOptionCategogry.BODY));
		model.addCarPart(new CarOption("red", false, CarOptionCategogry.COLOR));
		model.addCarPart(new CarOption("standard 2l 4 cilinders", false,
				CarOptionCategogry.ENGINE));
		model.addCarPart(new CarOption("6 speed manual", false,
				CarOptionCategogry.GEARBOX));
		model.addCarPart(new CarOption("leather black", false, CarOptionCategogry.SEATS));
		model.addCarPart(new CarOption("comfort", false, CarOptionCategogry.WHEEL));
	}

	@Test
	public void test1Constructor() {
		Order order = new Order("Mario", model, 3);
		assertNotNull(order);
		assertEquals(order.getDescription(), model);
		assertEquals("Mario", order.getGarageHolder());
		assertEquals(3, order.getQuantity());
		assertEquals(3, order.getPendingCars());
		int[] array = { 0, 5 };
		order.setEstimatedTime(array);
		assertEquals(0, order.getEstimatedTime()[0]);
		assertEquals(5, order.getEstimatedTime()[1]);

	}

	@Test(expected = IllegalArgumentException.class)
	public void test2Constructor() {
		new Order(null, null, -1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetGarageHolder1() {
		new Order(" ", model, 3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetGarageHolder2() {
		Order order3 = new Order("Mario", model, 3);
		assertNotNull(order3.getGarageHolder());
		new Order(null, model, 3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPendingCars1() {
		new Order("Mario", model, -1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPendingCars2() {
		Order order = new Order("Mario", model, 1);
		order.completeCar();
		assertEquals(0, order.getPendingCars());
		order.completeCar();

	}

	@Test(expected = IllegalArgumentException.class)
	public void testQuantity() {
		new Order("Mario", model, 0);
	}

	@Test
	public void testEstimatedTime1() {
		Order order = new Order("Mario", model, 3);
		int[] array = { 0, 5 };
		order.setEstimatedTime(array);
		assertEquals(0, order.getEstimatedTime()[0]);
		assertEquals(5, order.getEstimatedTime()[1]);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEstimatedTime2() {
		Order order = new Order("Mario", model, 3);
		int[] array = { -1, 5 };
		order.setEstimatedTime(array);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEstimatedTime3() {
		Order order = new Order("Mario", model, 3);
		int[] array = { 0, -3 };
		order.setEstimatedTime(array);
	}

	@Test
	public void testCarCompleted() {
		Order order = new Order("Mario", model, 3);
		assertEquals(3, order.getPendingCars());
		order.completeCar();
		assertEquals(2, order.getPendingCars());

	}

	@Test
	public void TestEqualsAndHashcode() throws AlreadyInMapException {
		CarModel model2 = new CarModel("BMW");
		model2.addCarPart(new CarOption("manual", true, CarOptionCategogry.AIRCO));
		model2.addCarPart(new CarOption("sedan", false, CarOptionCategogry.BODY));
		model2.addCarPart(new CarOption("red", false, CarOptionCategogry.COLOR));
		model2.addCarPart(new CarOption("standard 2l 4 cilinders", false,
				CarOptionCategogry.ENGINE));
		model2.addCarPart(new CarOption("6 speed manual", false,
				CarOptionCategogry.GEARBOX));
		model2.addCarPart(new CarOption("leather black", false, CarOptionCategogry.SEATS));
		model2.addCarPart(new CarOption("comfort", false, CarOptionCategogry.WHEEL));

		Order order1 = new Order("Jan", model, 2);
		assertFalse(order1.equals(null));
		assertFalse(order1.equals(new Action("Paint")));
		Order order2 = new Order("Jan", model2, 2);
		assertFalse(order1.equals(order2));
		order2 = new Order("Jos", model, 2);
		assertFalse(order1.equals(order2));
		order2 = new Order("Jan", model, 1);
		assertFalse(order1.equals(order2));
		order2 = new Order("Jan", model, 2);
		assertTrue(order1.equals(order2));
		assertTrue(order1.equals(order1));
		assertEquals(order1.hashCode(), order2.hashCode());
	}

	@Test
	public void testToString() {
		Order order1 = new Order("Jan", model, 2);
		int[] array = { 1, 100 };
		order1.setEstimatedTime(array);
		assertEquals(
				"2 Volkswagen Estimated completion time: 1 days and 1 hours and 40 minutes",
				order1.toString());
	}
}
