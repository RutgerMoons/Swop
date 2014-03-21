package carTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import car.Engine;

public class EngineTest {

private Engine engine;
	
	@Before
	public void setup() {
		this.engine = new Engine("standard 2l 4 cilinders");
	}
	
	@Test
	public void testConstructor() {
		assertNotNull(engine);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testNullConstructor() {
		Engine alibaba = new Engine(null);
	}
	
//	@Test
//	public void test2Add() {
//		assertNotNull(engine);
//		Engine.addPossibleEngine("Fake engine");
//		Iterator<String> i = Engine.getPossibleEngines();
//		assertTrue(i.hasNext());
//		assertEquals("standard 2l 4 cilinders", i.next());
//		assertTrue(i.hasNext());
//		assertEquals("performance 2.5l 6 cilinders", i.next());
//		assertTrue(i.hasNext());
//		assertEquals("Fake engine", i.next());
//	}
//	  
//	@Test (expected = IllegalArgumentException.class)
//	public void testAddNullToTypes() {
//		Engine.addPossibleEngine(null);
//	}
	
//	@Test (expected = IllegalArgumentException.class)
//	public void test3Description() {
//		engine.setType("not a possible type of engine");
//		assertNull(engine.getDescription());
//	}
	
	@Test
	public void test4Description() {
		engine.setType("standard 2l 4 cilinders");
		assertEquals("standard 2l 4 cilinders", engine.getType());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void test5Description() {
		engine.setType(null);
		assertNull(engine.getType());
	}
}
