package carTest;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import assembly.Action;
import car.CarPart;
import car.CarPartType;

public class CarPartTest {

	@Test
	public void testConstructor(){
		CarPart part = new CarPart("manual", false, CarPartType.AIRCO);
		assertNotNull(part.getType());
		assertNotNull(part.getDescription());
		assertEquals("manual", part.getDescription());
		assertFalse(part.isOptional());
		assertEquals(CarPartType.AIRCO, part.getType());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidArgument() {
		new CarPart("manual", false, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidArgument2() {
		new CarPart("", false, CarPartType.AIRCO);
	}
	
	@Test
	public void testEqualsAndHashCode(){
		CarPart part1 = new CarPart("manual", false, CarPartType.AIRCO);
		CarPart part2 = new CarPart("automatic", false, CarPartType.AIRCO);
		CarPart part3 = new CarPart("manual", true, CarPartType.AIRCO);
		CarPart part4 = new CarPart("manual", false, CarPartType.COLOR);
		CarPart part5 = new CarPart("manual", false, CarPartType.AIRCO);
		
		assertNotEquals(part1, part2);
		assertNotEquals(part1, part3);
		assertNotEquals(part1, part4);
		
		assertNotEquals(part1.hashCode(), part2.hashCode());
		assertNotEquals(part1.hashCode(), part3.hashCode());
		assertNotEquals(part1.hashCode(), part4.hashCode());
		
		assertEquals(part1, part5);
		assertEquals(part1.hashCode(), part5.hashCode());
		
		
		assertEquals(part1, part1);
		assertNotEquals(part1, null);
		assertNotEquals(part1, new Action("Paint"));
	}
	
	@Test
	public void testToString(){
		CarPart part = new CarPart("manual", false, CarPartType.AIRCO);
		assertEquals("AIRCO: manual", part.toString());
	}
}
