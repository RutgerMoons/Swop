package tests.carTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import code.car.Seat;

public class SeatTest {

private Seat seat;
	
	@Before
	public void setup() {
		this.seat = new Seat("leather black");
	}
	
	@Test
	public void testConstructor() {
		assertNotNull(seat);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testNullConstructor() {
		Seat alibaba = new Seat(null);
	}
	
//	@Test
//	public void test2Add() {
//		assertNotNull(seat);
//		Seat.addPossibleSeat("testshizzle");
//		Iterator<String> i = Seat.getPossibleSeats();
//		assertTrue(i.hasNext());
//		assertEquals("leather black", i.next());
//		assertTrue(i.hasNext());
//		assertEquals("leather white", i.next());
//		assertTrue(i.hasNext());
//		assertEquals("vinyl grey", i.next());
//		assertTrue(i.hasNext());
//		assertEquals("testshizzle", i.next());
//	}
	  
//	@Test (expected = IllegalArgumentException.class)
//	public void testAddNullToTypes() {
//		Seat.addPossibleSeat(null);
//	}
	
//	@Test (expected = IllegalArgumentException.class)
//	public void test3Description() {
//		seat.setType("not a possible type of seat");
//		assertNull(seat.getDescription());
//	}
	
	@Test
	public void test4Description() {
		seat.setType("leather black");
		assertEquals("leather black", seat.getType());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void test5Description() {
		seat.setType(null);
		assertNull(seat.getType());
	}
	

}
