package carTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import car.Body;

public class BodyTest {

private Body body;
	
	@Before
	public void setup() {
		this.body = new Body("break");
	}
	
	@Test
	public void testConstructor() {
		assertNotNull(body);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testNullConstructor() {
		Body buddy = new Body(null);
	}
	
	@Test
	public void test2Add() {
		assertNotNull(body);
		Body.addPossibleBody("test the body");
		Iterator<String> i = Body.getPossibleBody();
		assertTrue(i.hasNext());
		assertEquals("break", i.next());
		assertTrue(i.hasNext());
		assertEquals("sedan", i.next());
		assertTrue(i.hasNext());
		assertEquals("test the body", i.next());
	}
	  
	@Test (expected = IllegalArgumentException.class)
	public void testAddNullToTypes() {
		Body.addPossibleBody(null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void test3Description() {
		body.setDescription("not a possible bodytype");
		assertNull(body.getDescription());
	}
	
	@Test
	public void test4Description() {
		body.setDescription("sedan");
		assertEquals("sedan", body.getDescription());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void test5Description() {
		body.setDescription(null);
		assertNull(body.getDescription());
	}
	
	 

}
