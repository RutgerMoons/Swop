package domain.clockTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import domain.clock.ImmutableClock;

public class ImmutableClockTest {

	ImmutableClock clock;
	
	@Before
	public void testConstructor() {
		int days = 1;
		int minutes = 500;
		clock = new ImmutableClock(days, minutes);
		assertNotNull(clock);
		assertEquals(1, clock.getDays());
		assertEquals(500, clock.getMinutes());
	}
	
	@Test
	public void isEarlierThanTest1(){
		ImmutableClock otherClock = new ImmutableClock(5,200);
		assertTrue(clock.isEarlierThan(otherClock));
	}
	
	@Test
	public void isEarlierThanTest2(){
		ImmutableClock otherClock = new ImmutableClock(1,200);
		assertFalse(clock.isEarlierThan(otherClock));
	}
	
	@Test
	public void isEarlierThanTest3(){
		ImmutableClock otherClock = new ImmutableClock(1,500);
		assertTrue(clock.isEarlierThan(otherClock));
	}
	
	@Test
	public void isEarlierThanTest4(){
		ImmutableClock otherClock = new ImmutableClock(1,600);
		assertTrue(clock.isEarlierThan(otherClock));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void isEarlierThanTest5(){
		clock.isEarlierThan(null);
	}
	
	@Test
	public void minusTest1(){
		ImmutableClock otherClock = new ImmutableClock(1,600);
		int result = clock.minus(otherClock);
		assertEquals(0,result);
	}
	
	@Test
	public void minusTest2(){
		ImmutableClock otherClock = new ImmutableClock(1,200);
		int result = clock.minus(otherClock);
		assertEquals(300,result);
	}
	
	@Test
	public void toStringTest(){
		assertEquals("day 1, 8 hours, 20 minutes.", clock.toString());
	}

}
