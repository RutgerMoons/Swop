package carTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import car.Gearbox;

public class GearboxTest {

	
private Gearbox gearbox;
	
	@Before
	public void setup() {
		this.gearbox = new Gearbox("6 speed manual");
	}
	
	@Test
	public void testConstructor() {
		assertNotNull(gearbox);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testNullConstructor() {
		Gearbox aBom = new Gearbox(null);
	}
	
//	@Test
//	public void test2Add() {
//		assertNotNull(gearbox);
//		Gearbox.addPossibleGearbox("testing");
//		Iterator<String> i = Gearbox.getPossibleGearbox();
//		assertTrue(i.hasNext());
//		assertEquals("6 speed manual", i.next());
//		assertTrue(i.hasNext());
//		assertEquals("5 speed automatic", i.next());
//		assertTrue(i.hasNext());
//		assertEquals("testing", i.next());
//	}
	  
//	@Test (expected = IllegalArgumentException.class)
//	public void testAddNullToTypes() {
//		Gearbox.addPossibleGearbox(null);
//	}
	
//	@Test (expected = IllegalArgumentException.class)
//	public void test3Description() {
//		gearbox.setType("not a possible type of gearbox");
//		assertNull(gearbox.getDescription());
//	}
	
	@Test
	public void test4Description() {
		gearbox.setType("6 speed manual");
		assertEquals("6 speed manual", gearbox.getType());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void test5Description() {
		gearbox.setType(null);
		assertNull(gearbox.getType());
	}

}
