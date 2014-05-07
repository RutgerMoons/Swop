package domain.orderTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import domain.clock.ImmutableClock;
import domain.exception.AlreadyInMapException;
import domain.exception.ImmutableException;
import domain.exception.NotImplementedException;
import domain.job.Action;
import domain.order.CustomOrder;
import domain.vehicle.CustomVehicle;
import domain.vehicle.VehicleOption;
import domain.vehicle.VehicleOptionCategory;

public class CustomOrderTest {
	private CustomVehicle model;
	private ImmutableClock orderTime;
	private ImmutableClock deadline;
	@Before
	public void initializeModel() throws AlreadyInMapException, ImmutableException {
		orderTime = new ImmutableClock(1, 10);
		deadline = new ImmutableClock(5, 20);
		Set<VehicleOption> parts = new HashSet<>();

		model = new CustomVehicle();
		model.addCarPart(new VehicleOption("low", VehicleOptionCategory.SPOILER));
	}

	@Test
	public void test1Constructor() {
		CustomOrder order = new CustomOrder("Mario", model, 3, orderTime, deadline);
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
		ImmutableClock clock = new ImmutableClock(0, 5);
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
	public void TestEqualsAndHashcode() throws AlreadyInMapException, ImmutableException {
		CustomVehicle model2 = new CustomVehicle();
		model2.addCarPart(new VehicleOption("high", VehicleOptionCategory.SPOILER));

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
		
		order1.setEstimatedTime(new ImmutableClock(1, 100));
		assertEquals(
				"2 SPOILER: low"+line+ "Deadline: 5 days and 0 hours and 20 minutes" + line+" Estimated completion time: 1 days and 1 hours and 40 minutes",
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
		order1.setDeadline(new ImmutableClock(0, 0));
		assertEquals(0, order1.getDeadline().getDays());
		assertEquals(0, order1.getDeadline().getMinutes());
	}
	
	@Test(expected = NotImplementedException.class)
	public void testGetProductionTime() throws NotImplementedException{
		CustomOrder order1 = new CustomOrder("Jan", model, 2, orderTime, deadline);
		assertEquals(order1.getProductionTime(), model.getSpecification().getTimeAtWorkBench());
	}
	
	@Test
	public void testGetAndSetOrderTime(){
		ImmutableClock clock = new ImmutableClock(4, 45);
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
