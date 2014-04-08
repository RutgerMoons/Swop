package domain.clockTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import domain.clock.UnmodifiableClock;

public class ImmutableClockTest {

	UnmodifiableClock clock;
	
	@Before
	public void testConstructor() {
		int days = 1;
		int minutes = 500;
		clock = new UnmodifiableClock(days, minutes);
		assertNotNull(clock);
		assertEquals(1, clock.getDays());
		assertEquals(500, clock.getMinutes());
	}
	
	@Test
	public void isEarlierThanTest1(){
		UnmodifiableClock otherClock = new UnmodifiableClock(5,200);
		assertTrue(clock.isEarlierThan(otherClock));
	}
	
	@Test
	public void isEarlierThanTest2(){
		UnmodifiableClock otherClock = new UnmodifiableClock(1,200);
		assertFalse(clock.isEarlierThan(otherClock));
	}
	
	@Test
	public void isEarlierThanTest3(){
		UnmodifiableClock otherClock = new UnmodifiableClock(1,500);
		assertTrue(clock.isEarlierThan(otherClock));
	}
	
	@Test
	public void isEarlierThanTest4(){
		UnmodifiableClock otherClock = new UnmodifiableClock(1,600);
		assertTrue(clock.isEarlierThan(otherClock));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void isEarlierThanTest5(){
		clock.isEarlierThan(null);
	}
	
	@Test
	public void minusTest1(){
		UnmodifiableClock otherClock = new UnmodifiableClock(1,600);
		int result = clock.minus(otherClock);
		assertEquals(0,result);
	}
	
	@Test
	public void minusTest2(){
		UnmodifiableClock otherClock = new UnmodifiableClock(1,200);
		int result = clock.minus(otherClock);
		assertEquals(300,result);
	}
	
	@Test
	public void toStringTest(){
		assertEquals("day 1, 8 hours, 20 minutes.", clock.toString());
	}

}
