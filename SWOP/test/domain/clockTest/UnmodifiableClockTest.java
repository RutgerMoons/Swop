package domain.clockTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import domain.clock.UnmodifiableClock;

public class UnmodifiableClockTest {

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
	
	@Test
	public void plusTest1(){
		UnmodifiableClock result = clock.getUnmodifiableClockPlusExtraMinutes(300);
		assertEquals(1, result.getDays());
		assertEquals(800, result.getMinutes());
	}
	
	@Test
	public void compareTest() {
		UnmodifiableClock clock1 = new UnmodifiableClock(1, 1);
		UnmodifiableClock clock2 = new UnmodifiableClock(10, 10);
		assertEquals(-1, clock1.compareTo(clock2));
		assertEquals(1, clock2.compareTo(clock1));
		UnmodifiableClock clock3 = new UnmodifiableClock(1, 1);
		assertEquals(0, clock1.compareTo(clock3));
		
		clock2 = new UnmodifiableClock(1, 10);
		assertEquals(-1, clock1.compareTo(clock2));
		
		clock2 = new UnmodifiableClock(10, 1);
		assertEquals(-1, clock1.compareTo(clock2));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void compareTestException() {
		UnmodifiableClock clock1 = new UnmodifiableClock(1, 1);
		clock1.compareTo(null);
	}

}
