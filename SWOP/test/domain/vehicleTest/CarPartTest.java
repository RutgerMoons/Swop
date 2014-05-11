package domain.vehicleTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import domain.job.action.Action;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

public class CarPartTest {

	@Test
	public void testConstructor(){
		VehicleOption part = new VehicleOption("manual", VehicleOptionCategory.AIRCO);
		assertNotNull(part.getType());
		assertNotNull(part.getDescription());
		assertEquals("manual", part.getDescription());
		assertEquals(VehicleOptionCategory.AIRCO, part.getType());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidArgument() {
		new VehicleOption("manual", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidArgument2() {
		new VehicleOption("", VehicleOptionCategory.AIRCO);
	}
	
	@Test
	public void testEqualsAndHashCode(){
		VehicleOption part1 = new VehicleOption("manual", VehicleOptionCategory.AIRCO);
		VehicleOption part2 = new VehicleOption("automatic", VehicleOptionCategory.AIRCO);
		VehicleOption part4 = new VehicleOption("manual", VehicleOptionCategory.COLOR);
		VehicleOption part5 = new VehicleOption("manual", VehicleOptionCategory.AIRCO);
		
		assertNotEquals(part1, part2);
		assertNotEquals(part1, part4);
		
		assertNotEquals(part1.hashCode(), part2.hashCode());
		assertNotEquals(part1.hashCode(), part4.hashCode());
		
		assertEquals(part1, part5);
		assertEquals(part1.hashCode(), part5.hashCode());
		
		
		assertEquals(part1, part1);
		assertNotEquals(part1, null);
		assertNotEquals(part1, new Action("Paint"));
	}
	
	@Test
	public void testToString(){
		VehicleOption part = new VehicleOption("manual", VehicleOptionCategory.AIRCO);
		assertEquals("AIRCO: manual", part.toString());
	}
	
	@Test
	public void testTaskDescription(){
		assertEquals("Airco", new VehicleOption("manual", VehicleOptionCategory.AIRCO).getTaskDescription());
		assertEquals("Assembly", new VehicleOption("manual", VehicleOptionCategory.BODY).getTaskDescription());
		assertEquals("Paint", new VehicleOption("manual", VehicleOptionCategory.COLOR).getTaskDescription());
		assertEquals("Engine", new VehicleOption("manual", VehicleOptionCategory.ENGINE).getTaskDescription());
		assertEquals("Gearbox", new VehicleOption("manual", VehicleOptionCategory.GEARBOX).getTaskDescription());
		assertEquals("Seats", new VehicleOption("manual", VehicleOptionCategory.SEATS).getTaskDescription());
		assertEquals("Spoiler", new VehicleOption("manual", VehicleOptionCategory.SPOILER).getTaskDescription());
		assertEquals("Wheels", new VehicleOption("manual", VehicleOptionCategory.WHEEL).getTaskDescription());
	}
	
	@Test
	public void testActionDescription(){
		assertEquals("Put on manual airco", new VehicleOption("manual", VehicleOptionCategory.AIRCO).getActionDescription());
	}
	
}
