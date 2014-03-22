package carTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import car.Wheel;

public class WheelTest {

	private Wheel wheel;
	
	@Before
	public void setup() {
		this.wheel = new Wheel("comfort");
	}
	
	@Test
	public void testConstructor() {
		assertNotNull(wheel);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testNullConstructor() {
		Wheel abracadabra = new Wheel(null);
	}
	
//	@Test
//	public void test2Add() {
//		assertNotNull(wheel);
//		Wheel.addPossibleWheel("testing yo");
//		Iterator<String> i = Wheel.getPossibleWheel();
//		assertTrue(i.hasNext());
//		assertEquals("comfort", i.next());
//		assertTrue(i.hasNext());
//		assertEquals("sports (low profile)", i.next());
//		assertTrue(i.hasNext());
//		assertEquals("testing yo", i.next());
//	}
	  
//	@Test (expected = IllegalArgumentException.class)
//	public void testAddNullToTypes() {
//		Wheel.addPossibleWheel(null);
//	}
	
//	@Test (expected = IllegalArgumentException.class)
//	public void test3Description() {
//		wheel.setType("not a possible type of wheel");
//		assertNull(wheel.getType());
//	}
	
	@Test
	public void test4Description() {
		wheel.setType("comfort");
		assertEquals("comfort", wheel.getType());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void test5Description() {
		wheel.setType(null);
		assertNull(wheel.getType());
	}
	

}
