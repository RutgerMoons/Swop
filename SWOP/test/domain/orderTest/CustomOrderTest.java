package domain.orderTest;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import domain.car.CarModel;
import domain.car.CarModelSpecification;
import domain.car.CarOption;
import domain.car.CarOptionCategory;
import domain.clock.UnmodifiableClock;
import domain.exception.AlreadyInMapException;
import domain.exception.ImmutableException;
import domain.exception.NotImplementedException;
import domain.job.Action;
import domain.order.CustomOrder;
import domain.order.IOrder;
import domain.order.ImmutableOrder;
import domain.order.CustomOrder;

public class CustomOrderTest {
	private CarModel model;
	private CarModelSpecification template;
	private UnmodifiableClock orderTime;
	private UnmodifiableClock deadline;
	@Before
	public void initializeModel() throws AlreadyInMapException {
		orderTime = new UnmodifiableClock(1, 10);
		deadline = new UnmodifiableClock(5, 20);
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
		CustomOrder order = new CustomOrder("Mario", model, 3, orderTime, deadline);
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
		new CustomOrder(null, null, -1, orderTime, deadline);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetGarageHolder1() {
		new CustomOrder("", model, 3, orderTime, deadline);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetGarageHolder2() {
		CustomOrder order3 = new CustomOrder("Mario", model, 3, orderTime, deadline);
		assertNotNull(order3.getGarageHolder());
		new CustomOrder(null, model, 3, orderTime, deadline);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPendingCars1() {
		new CustomOrder("Mario", model, -1, orderTime, deadline);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPendingCars2() throws ImmutableException {
		CustomOrder order = new CustomOrder("Mario", model, 1, orderTime, deadline);
		order.completeCar();
		assertEquals(0, order.getPendingCars());
		order.completeCar();

	}

	@Test(expected = IllegalArgumentException.class)
	public void testQuantity() {
		new CustomOrder("Mario", model, 0, orderTime, deadline);
	}

	@Test
	public void testEstimatedTime1() {
		CustomOrder order = new CustomOrder("Mario", model, 3, orderTime, deadline);
		UnmodifiableClock clock = new UnmodifiableClock(0, 5);
		order.setEstimatedTime(clock);
		assertEquals(0, order.getEstimatedTime().getDays());
		assertEquals(5, order.getEstimatedTime().getMinutes());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEstimatedTime2() {
		CustomOrder order = new CustomOrder("Mario", model, 3, orderTime, deadline);
		order.setEstimatedTime(null);
	}

	@Test
	public void testCarCompleted() throws ImmutableException {
		CustomOrder order = new CustomOrder("Mario", model, 3, orderTime, deadline);
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

		CustomOrder order1 = new CustomOrder("Jan", model, 2, orderTime, deadline);
		assertFalse(order1.equals(null));
		assertFalse(order1.equals(new Action("Paint")));
		CustomOrder order2 = new CustomOrder("Jan", model2, 2, orderTime, deadline);
		assertFalse(order1.equals(order2));
		order2 = new CustomOrder("Jos", model, 2, orderTime, deadline);
		assertFalse(order1.equals(order2));
		order2 = new CustomOrder("Jan", model, 1, orderTime, deadline);
		assertFalse(order1.equals(order2));
		order2 = new CustomOrder("Jan", model, 2, orderTime, deadline);
		assertTrue(order1.equals(order2));
		assertTrue(order1.equals(order1));
		assertEquals(order1.hashCode(), order2.hashCode());
	}

	@Test
	public void testToString() {
		String line = System.lineSeparator();
		CustomOrder order1 = new CustomOrder("Jan", model, 2, orderTime, deadline);
		
		order1.setEstimatedTime(new UnmodifiableClock(1, 100));
		assertEquals(
				"2 model" + line+ "Deadline: 5 days and 0 hours and 20 minutes" + line+" Estimated completion time: 1 days and 1 hours and 40 minutes",
				order1.toString());
	}
	
	@Test
	public void testGetDeadline() throws NotImplementedException{
		CustomOrder order1 = new CustomOrder("Jan", model, 2, orderTime, deadline);
		assertNotNull(order1.getDeadline());
	}
	
	@Test
	public void testSetDeadline() throws NotImplementedException{
		CustomOrder order1 = new CustomOrder("Jan", model, 2, orderTime, deadline);
		order1.setDeadline(new UnmodifiableClock(0, 0));
		assertEquals(0, order1.getDeadline().getDays());
		assertEquals(0, order1.getDeadline().getMinutes());
	}
	
	@Test
	public void testGetProductionTime(){
		CustomOrder order1 = new CustomOrder("Jan", model, 2, orderTime, deadline);
		assertEquals(order1.getProductionTime(), model.getSpecification().getTimeAtWorkBench());
	}
	
	@Test
	public void testGetAndSetOrderTime(){
		UnmodifiableClock clock = new UnmodifiableClock(4, 45);
		CustomOrder order1 = new CustomOrder("Jan", model, 2, orderTime, deadline);
		order1.setOrderTime(clock);
		assertEquals(clock, order1.getOrderTime());
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetIllegalOrderTime(){
		CustomOrder order1 = new CustomOrder("Jan", model, 2, orderTime, deadline);
		order1.setOrderTime(null);
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetIllegalDeadline(){
		CustomOrder order1 = new CustomOrder("Jan", model, 2, orderTime, deadline);
		order1.setDeadline(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSetIllegalDescription(){
		CustomOrder order1 = new CustomOrder("Jan", null, 2, orderTime, deadline);
	}
}
