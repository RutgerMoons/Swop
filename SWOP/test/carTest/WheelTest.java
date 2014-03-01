package carTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

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
	
	@Test
	public void test2Add() {
		assertNotNull(wheel);
		Wheel.addPossibleWheel("testing yo");
		Iterator<String> i = Wheel.getPossibleWheel();
		assertTrue(i.hasNext());
		assertEquals("comfort", i.next());
		assertTrue(i.hasNext());
		assertEquals("sports (low profile)", i.next());
		assertTrue(i.hasNext());
		assertEquals("testing yo", i.next());
	}
	  
	@Test (expected = IllegalArgumentException.class)
	public void testAddNullToTypes() {
		Wheel.addPossibleWheel(null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void test3Description() {
		wheel.setDescription("not a possible type of wheel");
		assertNull(wheel.getDescription());
	}
	
	@Test
	public void test4Description() {
		wheel.setDescription("comfort");
		assertEquals("comfort", wheel.getDescription());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void test5Description() {
		wheel.setDescription(null);
		assertNull(wheel.getDescription());
	}
	

}
