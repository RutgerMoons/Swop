package domain.orderTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
import domain.exception.NotImplementedException;
import domain.job.Action;
import domain.order.StandardOrder;

public class OrderTest {

	private CarModel model;
	private CarModelSpecification template;

	@Before
	public void initializeModel() throws AlreadyInMapException {
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
		model.addCarPart(new CarOption("standard 2l 4 cilinders",
				CarOptionCategory.ENGINE));
		model.addCarPart(new CarOption("6 speed manual",
				CarOptionCategory.GEARBOX));
		model.addCarPart(new CarOption("leather black", CarOptionCategory.SEATS));
		model.addCarPart(new CarOption("comfort", CarOptionCategory.WHEEL));
	}

	@Test
	public void test1Constructor() {
		StandardOrder order = new StandardOrder("Mario", model, 3, new UnmodifiableClock(20));
		assertNotNull(order);
		assertEquals(order.getDescription(), model);
		assertEquals("Mario", order.getGarageHolder());
		assertEquals(3, order.getQuantity());
		assertEquals(3, order.getPendingCars());
		
		UnmodifiableClock clock = new UnmodifiableClock(0, 5);
		order.setEstimatedTime(clock);
		assertEquals(0, order.getEstimatedTime().getDays());
		assertEquals(5, order.getEstimatedTime().getMinutes());

	}

	@Test(expected = IllegalArgumentException.class)
	public void test2Constructor() {
		new StandardOrder(null, null, -1, new UnmodifiableClock(20));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetGarageHolder1() {
		new StandardOrder(" ", model, 3, new UnmodifiableClock(20));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetGarageHolder2() {
		StandardOrder order3 = new StandardOrder("Mario", model, 3, new UnmodifiableClock(20));
		assertNotNull(order3.getGarageHolder());
		new StandardOrder(null, model, 3, new UnmodifiableClock(20));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPendingCars1() {
		new StandardOrder("Mario", model, -1, new UnmodifiableClock(20));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPendingCars2() {
		StandardOrder order = new StandardOrder("Mario", model, 1, new UnmodifiableClock(20));
		order.completeCar();
		assertEquals(0, order.getPendingCars());
		order.completeCar();

	}

	@Test(expected = IllegalArgumentException.class)
	public void testQuantity() {
		new StandardOrder("Mario", model, 0, new UnmodifiableClock(20));
	}

	@Test
	public void testEstimatedTime1() {
		StandardOrder order = new StandardOrder("Mario", model, 3, new UnmodifiableClock(20));
		UnmodifiableClock clock = new UnmodifiableClock(0, 5);
		order.setEstimatedTime(clock);
		assertEquals(0, order.getEstimatedTime().getDays());
		assertEquals(5, order.getEstimatedTime().getMinutes());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEstimatedTime2() {
		StandardOrder order = new StandardOrder("Mario", model, 3, new UnmodifiableClock(20));
		order.setEstimatedTime(null);
	}

	@Test
	public void testCarCompleted() {
		StandardOrder order = new StandardOrder("Mario", model, 3, new UnmodifiableClock(20));
		assertEquals(3, order.getPendingCars());
		order.completeCar();
		assertEquals(2, order.getPendingCars());

	}

	@Test
	public void TestEqualsAndHashcode() throws AlreadyInMapException {
		CarModelSpecification template2 = new CarModelSpecification("abc", new HashSet<CarOption>(), 50);
		CarModel model2 = new CarModel(template2);
		model2.addCarPart(new CarOption("manual", CarOptionCategory.AIRCO));
		model2.addCarPart(new CarOption("sedan", CarOptionCategory.BODY));
		model2.addCarPart(new CarOption("red", CarOptionCategory.COLOR));
		model2.addCarPart(new CarOption("standard 2l 4 cilinders",
				CarOptionCategory.ENGINE));
		model2.addCarPart(new CarOption("6 speed manual",
				CarOptionCategory.GEARBOX));
		model2.addCarPart(new CarOption("leather black", CarOptionCategory.SEATS));
		model2.addCarPart(new CarOption("comfort", CarOptionCategory.WHEEL));

		StandardOrder order1 = new StandardOrder("Jan", model, 2, new UnmodifiableClock(20));
		assertFalse(order1.equals(null));
		assertFalse(order1.equals(new Action("Paint")));
		StandardOrder order2 = new StandardOrder("Jan", model2, 2, new UnmodifiableClock(20));
		assertFalse(order1.equals(order2));
		order2 = new StandardOrder("Jos", model, 2, new UnmodifiableClock(20));
		assertFalse(order1.equals(order2));
		order2 = new StandardOrder("Jan", model, 1, new UnmodifiableClock(20));
		assertFalse(order1.equals(order2));
		order2 = new StandardOrder("Jan", model, 2, new UnmodifiableClock(20));
		assertTrue(order1.equals(order2));
		assertTrue(order1.equals(order1));
		assertEquals(order1.hashCode(), order2.hashCode());
	}

	@Test
	public void testToString() {
		StandardOrder order1 = new StandardOrder("Jan", model, 2, new UnmodifiableClock(20));
		
		order1.setEstimatedTime(new UnmodifiableClock(1, 100));
		assertEquals(
				"2 model Estimated completion time: 1 days and 1 hours and 40 minutes",
				order1.toString());
	}
	
	@Test(expected=NotImplementedException.class)
	public void testGetDeadline() throws NotImplementedException{
		StandardOrder order1 = new StandardOrder("Jan", model, 2, new UnmodifiableClock(20));
		order1.getDeadline();
	}
	
	@Test(expected=NotImplementedException.class)
	public void testSetDeadline() throws NotImplementedException{
		StandardOrder order1 = new StandardOrder("Jan", model, 2, new UnmodifiableClock(20));
		order1.setDeadline(new UnmodifiableClock(0, 0));
	}
	
	@Test
	public void testGetProductionTime() throws NotImplementedException{
		StandardOrder order1 = new StandardOrder("Jan", model, 2, new UnmodifiableClock(20));
		assertEquals(order1.getProductionTime(), model.getSpecification().getTimeAtWorkBench());
	}
	
	@Test
	public void testGetAndSetOrderTime(){
		UnmodifiableClock clock = new UnmodifiableClock(4, 45);
		StandardOrder order1 = new StandardOrder("Jan", model, 2, new UnmodifiableClock(20));
		order1.setOrderTime(clock);
		assertEquals(clock, order1.getOrderTime());
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetIllegalOrderTime(){
		StandardOrder order1 = new StandardOrder("Jan", model, 2, new UnmodifiableClock(20));
		order1.setOrderTime(null);
	}
}
