package domain.order.orderTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import domain.clock.ImmutableClock;
import domain.job.action.Action;
import domain.order.order.CustomOrder;
import domain.vehicle.vehicle.CustomVehicle;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

public class CustomOrderTest {
	private CustomVehicle model;
	private ImmutableClock orderTime;
	private ImmutableClock deadline;
	
	@Before
	public void initializeModel() {
		orderTime = new ImmutableClock(1, 10);
		deadline = new ImmutableClock(5, 20);

		model = new CustomVehicle();
		model.addVehicleOption(new VehicleOption("low", VehicleOptionCategory.SPOILER));
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
	public void testPendingCars2(){
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
	public void testCarCompleted() {
		CustomOrder order = new CustomOrder("Mario", model, 3, orderTime, deadline);
		assertEquals(3, order.getPendingCars());
		order.completeCar();
		assertEquals(2, order.getPendingCars());

	}

	@Test
	public void TestEqualsAndHashcode(){
		CustomVehicle model2 = new CustomVehicle();
		model2.addVehicleOption(new VehicleOption("high", VehicleOptionCategory.SPOILER));

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
		
		assertEquals("2 Spoiler: low"+line+ "\tDeadline: 5 days and 0 hours and 20 minutes", order1.toString());
		
		order1.setEstimatedTime(new ImmutableClock(1, 100));
		assertEquals(
				"2 Spoiler: low"+line+ "\tDeadline: 5 days and 0 hours and 20 minutes",
				order1.toString());
		order1.completeCar();
		order1.completeCar();
		assertEquals("2 Spoiler: low"+line, order1.toString());
	}
	
	@Test
	public void testGetDeadline(){
		CustomOrder order1 = new CustomOrder("Jan", model, 2, orderTime, deadline);
		assertNotNull(order1.getDeadline());
	}
	
	@Test
	public void testSetDeadline() {
		CustomOrder order1 = new CustomOrder("Jan", model, 2, orderTime, deadline);
		order1.setDeadline(new ImmutableClock(0, 0));
		assertEquals(0, order1.getDeadline().getDays());
		assertEquals(0, order1.getDeadline().getMinutes());
	}
	
	@Test
	public void testGetProductionTime(){
		CustomOrder order1 = new CustomOrder("Jan", model, 2, orderTime, deadline);
		int time = 0;
		for(Integer integer: model.getTimeAtWorkBench().values()){
			time+=integer;
		}
		assertEquals(order1.getProductionTime(), time);
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
		new CustomOrder("Jan", null, 2, orderTime, deadline);
	}
	
	@Test
	public void testVehicleOptions(){
		CustomOrder order = new CustomOrder("jos", model, 1, orderTime, deadline);
		assertEquals(order.getVehicleOptions().size(), model.getVehicleOptions().values().size());
		assertTrue(order.getVehicleOptions().containsAll(model.getVehicleOptions().values()));
	}
	
	@Test
	public void testGetTimeAtWorkbench(){
		CustomOrder order = new CustomOrder("jos", model, 1, orderTime, deadline);
		assertEquals(model.getTimeAtWorkBench(), order.getTimeAtWorkBench());
	}
	
	@Test
	public void testGetVehicleSpecification(){
		CustomOrder order = new CustomOrder("jos", model, 1, orderTime, deadline);
		assertEquals("custom", order.getVehicleSpecification().getDescription());
	}
	
	@Test
	public void testEquals(){
		CustomOrder order = new CustomOrder("jos", model, 1, orderTime, deadline);
		assertEquals(order, order);
		assertEquals(order.hashCode(), order.hashCode());
		
		assertNotEquals(order, VehicleOptionCategory.AIRCO);
		assertNotEquals(order, null);
		
		
		CustomOrder order2 = new CustomOrder("jos", model, 1, orderTime, new ImmutableClock(10, 10));
		assertNotEquals(order, order2);
		assertNotEquals(order.hashCode(), order2.hashCode());
		
		order2.setDeadline(order.getDeadline());
		order2.setEstimatedTime(new ImmutableClock(10, 10));
		assertNotEquals(order, order2);
		assertNotEquals(order.hashCode(), order2.hashCode());
		
		order.setEstimatedTime(new ImmutableClock(10, 9));
		assertNotEquals(order, order2);
		assertNotEquals(order.hashCode(), order2.hashCode());
		
		order.setEstimatedTime(new ImmutableClock(10, 10));
		
		CustomOrder order3 = new CustomOrder("jos", model, 2, new ImmutableClock(20, 20), new ImmutableClock(10, 10));
		order3.setDeadline(order2.getDeadline());
		order3.setEstimatedTime(order2.getEstimatedTime());
		assertNotEquals(order, order3);
		assertNotEquals(order.hashCode(), order3.hashCode());
		
		order3 = new CustomOrder("jos", model, 2, orderTime, deadline);
		order3.completeCar();
		order3.setDeadline(order2.getDeadline());
		order3.setEstimatedTime(order2.getEstimatedTime());
		assertNotEquals(order, order3);
		assertNotEquals(order.hashCode(), order3.hashCode());
		
		
		order3 = new CustomOrder("jos", model, 1, orderTime, deadline);
		order3.setDeadline(order.getDeadline());
		order3.setEstimatedTime(order.getEstimatedTime());
		assertEquals(order, order3);
		assertEquals(order.hashCode(), order3.hashCode());
		
	}
}
