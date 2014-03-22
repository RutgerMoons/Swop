package carTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import car.Color;

public class ColorTest {

private Color color;
	
	@Before
	public void setup() {
		this.color = new Color("red");
	}
	
	@Test
	public void testConstructor() {
		assertNotNull(color);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testNullConstructor() {
		Color aWholeNewColor = new Color(null);
	}
	
//	@Test
//	public void test2Add() {
//		assertNotNull(color);
//		Color.addPossibleColor("testing for pixels");
//		Iterator<String> i = Color.getPossibleColors();
//		assertTrue(i.hasNext());
//		assertEquals("red", i.next());
//		assertTrue(i.hasNext());
//		assertEquals("blue", i.next());
//		assertTrue(i.hasNext());
//		assertEquals("black", i.next());
//		assertTrue(i.hasNext());
//		assertEquals("white", i.next());
//		assertTrue(i.hasNext());
//		assertEquals("testing for pixels", i.next());
//	}
	  
//	@Test (expected = IllegalArgumentException.class)
//	public void testAddNullToTypes() {
//		Color.addPossibleColor(null);
//	}
//	
//	@Test (expected = IllegalArgumentException.class)
//	public void test3Description() {
//		color.setType("not a possible type of color");
//		assertNull(color.getDescription());
//	}
	
	@Test
	public void test4Description() {
		color.setType("red");
		assertEquals("red", color.getType());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void test5Description() {
		color.setType(null);
		assertNull(color.getType());
	}
	
	 

}
