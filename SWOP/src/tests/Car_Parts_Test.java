package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import car.Airco;
import car.Body;
import car.Color;
import car.Engine;

public class Car_Parts_Test {

	@Test
	public void test_body() {
		assertTrue(Body.SEDAN.getCode() == 1);
		assertTrue(Body.SEDAN.toString().equals("sedan"));
		assertTrue(Body.BREAK.getCode() == 2);
		assertTrue(Body.BREAK.toString().equals("break"));
	}
	
	@Test
	public void test_airco() {
		assertTrue(Airco.MANUAL.getCode() == 1);
		assertTrue(Airco.MANUAL.toString().equals("manual"));
		assertTrue(Airco.AUTOMATIC.getCode() == 2);
		assertTrue(Airco.AUTOMATIC.toString().equals("automatic climate control"));
	}
	
	@Test
	public void test_color() {
		assertEquals(1, Color.RED.getCode());
		assertEquals(2, Color.BLUE.getCode());
		assertEquals(3, Color.BLACK.getCode());
		assertEquals(4, Color.WHITE.getCode());
		assertEquals(5, Color.GREY.getCode());
		assertEquals("red", Color.RED.toString());
		assertEquals("blue", Color.BLUE.toString());
		assertEquals("black", Color.BLACK.toString());
		assertEquals("white", Color.WHITE.toString());
		assertEquals("grey", Color.GREY.toString());
	}
	
	@Test
	public void test_engine() {
		assertEquals(1, Engine.STANDARD.getCode());
	}

}
