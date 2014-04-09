package domain.carTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import domain.car.CarOption;
import domain.car.CarOptionCategory;
import domain.job.Action;

public class CarPartTest {

	@Test
	public void testConstructor(){
		CarOption part = new CarOption("manual", CarOptionCategory.AIRCO);
		assertNotNull(part.getType());
		assertNotNull(part.getDescription());
		assertEquals("manual", part.getDescription());
		assertEquals(CarOptionCategory.AIRCO, part.getType());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidArgument() {
		new CarOption("manual", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidArgument2() {
		new CarOption("", CarOptionCategory.AIRCO);
	}
	
	@Test
	public void testEqualsAndHashCode(){
		CarOption part1 = new CarOption("manual", CarOptionCategory.AIRCO);
		CarOption part2 = new CarOption("automatic", CarOptionCategory.AIRCO);
		CarOption part4 = new CarOption("manual", CarOptionCategory.COLOR);
		CarOption part5 = new CarOption("manual", CarOptionCategory.AIRCO);
		
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
		CarOption part = new CarOption("manual", CarOptionCategory.AIRCO);
		assertEquals("AIRCO: manual", part.toString());
	}
	
	@Test
	public void testTaskDescription(){
		assertEquals("Airco", new CarOption("manual", CarOptionCategory.AIRCO).getTaskDescription());
		assertEquals("Assembly", new CarOption("manual", CarOptionCategory.BODY).getTaskDescription());
		assertEquals("Paint", new CarOption("manual", CarOptionCategory.COLOR).getTaskDescription());
		assertEquals("Engine", new CarOption("manual", CarOptionCategory.ENGINE).getTaskDescription());
		assertEquals("Gearbox", new CarOption("manual", CarOptionCategory.GEARBOX).getTaskDescription());
		assertEquals("Seats", new CarOption("manual", CarOptionCategory.SEATS).getTaskDescription());
		assertEquals("Spoiler", new CarOption("manual", CarOptionCategory.SPOILER).getTaskDescription());
		assertEquals("Wheels", new CarOption("manual", CarOptionCategory.WHEEL).getTaskDescription());
	}
	
	@Test
	public void testActionDescription(){
		assertEquals("Put on manual airco", new CarOption("manual", CarOptionCategory.AIRCO).getActionDescription());
	}
	
}
