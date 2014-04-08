package domain.carTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import domain.car.CarOption;
import domain.car.CarOptionCategogry;
import domain.job.Action;

public class CarPartTest {

	@Test
	public void testConstructor(){
		CarOption part = new CarOption("manual", CarOptionCategogry.AIRCO);
		assertNotNull(part.getType());
		assertNotNull(part.getDescription());
		assertEquals("manual", part.getDescription());
		assertEquals(CarOptionCategogry.AIRCO, part.getType());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidArgument() {
		new CarOption("manual", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidArgument2() {
		new CarOption("", CarOptionCategogry.AIRCO);
	}
	
	@Test
	public void testEqualsAndHashCode(){
		CarOption part1 = new CarOption("manual", CarOptionCategogry.AIRCO);
		CarOption part2 = new CarOption("automatic", CarOptionCategogry.AIRCO);
		CarOption part4 = new CarOption("manual", CarOptionCategogry.COLOR);
		CarOption part5 = new CarOption("manual", CarOptionCategogry.AIRCO);
		
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
		CarOption part = new CarOption("manual", CarOptionCategogry.AIRCO);
		assertEquals("AIRCO: manual", part.toString());
	}
	
	@Test
	public void testTaskDescription(){
		assertEquals("Airco", new CarOption("manual", CarOptionCategogry.AIRCO).getTaskDescription());
		assertEquals("Assembly", new CarOption("manual", CarOptionCategogry.BODY).getTaskDescription());
		assertEquals("Paint", new CarOption("manual", CarOptionCategogry.COLOR).getTaskDescription());
		assertEquals("Engine", new CarOption("manual", CarOptionCategogry.ENGINE).getTaskDescription());
		assertEquals("Gearbox", new CarOption("manual", CarOptionCategogry.GEARBOX).getTaskDescription());
		assertEquals("Seats", new CarOption("manual", CarOptionCategogry.SEATS).getTaskDescription());
		assertEquals("Spoiler", new CarOption("manual", CarOptionCategogry.SPOILER).getTaskDescription());
		assertEquals("Wheels", new CarOption("manual", CarOptionCategogry.WHEEL).getTaskDescription());
	}
	
	@Test
	public void testActionDescription(){
		assertEquals("Put on manual airco", new CarOption("manual", CarOptionCategogry.AIRCO).getActionDescription());
	}
	
}
