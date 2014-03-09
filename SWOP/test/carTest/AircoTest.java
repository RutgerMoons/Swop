package carTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import car.Airco;
import car.Body;

public class AircoTest {
	
	private Airco airco;
	
	@Before
	public void setup() {
		this.airco = new Airco("manual");
	}
	
	@Test
	public void testConstructor() {
		assertNotNull(airco);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testNullConstructor() {
		Airco a = new Airco(null);
	}
	
//	@Test
//	public void test2Add() {
//		assertNotNull(airco);
//		Airco.addPossibleAirco("test");
//		Iterator<String> i = Airco.getPossibleAirco();
//		assertTrue(i.hasNext());
//		assertEquals("manual", i.next());
//		assertTrue(i.hasNext());
//		assertEquals("automatic climate control", i.next());
//		assertTrue(i.hasNext());
//		assertEquals("test", i.next());
//	}
	  
//	@Test (expected = IllegalArgumentException.class)
//	public void testAddNullToTypes() {
//		Airco.addPossibleAirco(null);
//	}
//	
//	@Test (expected = IllegalArgumentException.class)
//	public void test3Description() {
//		airco.setDescription("not a possible type");
//		assertNull(airco.getDescription());
//	}
//	
	@Test
	public void test4Description() {
		airco.setType("manual");
		assertEquals("manual", airco.getType());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void test5Description() {
		airco.setType(null);
		assertNull(airco.getType());
	}
	
	@Test
	public void testEquals(){
		assertTrue(airco.equals(airco));
		assertFalse(airco.equals(null));
		Body body = new Body("break");
		assertFalse(airco.equals(body));
		
	}
	
	 
}
