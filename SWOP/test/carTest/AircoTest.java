package carTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import car.Airco;

public class AircoTest {
	
	private Airco airco;
	
	@Before
	public void setup() {
		this.airco = new Airco();
	}

	@Test
	public void test1Constructor() {
		assertNotNull(airco);
		Iterator<String> aircoTypes = airco.getTypes();
		assertTrue(aircoTypes.hasNext()); 
		assertEquals("manual", aircoTypes.next());
		assertEquals("automatic climate control", aircoTypes.next());
		assertFalse(aircoTypes.hasNext());
		ArrayList<String> types = new ArrayList<String>();
		types.add("manual");
		types.add("automatic climate control");
		Airco airco2 = new Airco(types);
		assertNotNull(airco2);
		Iterator<String> airco2types = airco2.getTypes();
		assertTrue(airco2types.hasNext()); 
		assertEquals("manual", airco2types.next());
		assertTrue(airco2types.hasNext()); 
		assertEquals("automatic climate control", airco2types.next());
		assertFalse(airco2types.hasNext()); // in totaal 2 elementen
	}
	
	@Test
	public void test2Add() {
		assertNotNull(airco);
		Airco.addPossibleAirco("test");
		Airco a = new Airco();
		Iterator<String> i = a.getTypes();
		assertTrue(i.hasNext());
		assertEquals("manual", i.next());
		assertTrue(i.hasNext());
		assertEquals("automatic climate control", i.next());
		assertTrue(i.hasNext());
		assertEquals("test", i.next());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void test3Description() {
		airco.setDescription("not a possible type");
		assertNull(airco.getDescription());
	}
	
	@Test
	public void test4Description() {
		airco.setDescription("manual");
		assertEquals("manual", airco.getDescription());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void test5Description() {
		airco.setDescription(null);
		assertNull(airco.getDescription());
	}
	
	 
}
