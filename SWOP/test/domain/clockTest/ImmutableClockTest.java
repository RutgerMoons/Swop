package domain.clockTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import domain.clock.Clock;
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
	
	@Test
	public void plusTest1(){
		ImmutableClock result = clock.getImmutableClockPlusExtraMinutes(300);
		assertEquals(1, result.getDays());
		assertEquals(800, result.getMinutes());
	}
	
	@Test
	public void compareTest() {
		ImmutableClock clock1 = new ImmutableClock(1, 1);
		ImmutableClock clock2 = new ImmutableClock(10, 10);
		assertEquals(-1, clock1.compareTo(clock2));
		assertEquals(1, clock2.compareTo(clock1));
		ImmutableClock clock3 = new ImmutableClock(1, 1);
		assertEquals(0, clock1.compareTo(clock3));
		
		clock2 = new ImmutableClock(1, 10);
		assertEquals(-1, clock1.compareTo(clock2));
		
		clock2 = new ImmutableClock(10, 1);
		assertEquals(-1, clock1.compareTo(clock2));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void compareTestException() {
		ImmutableClock clock1 = new ImmutableClock(1, 1);
		clock1.compareTo(null);
	}
	
	@Test
	public void hashcodeTest(){
		ImmutableClock clock = new ImmutableClock(1,1);
		assertEquals(993,clock.hashCode());
	}
	
	@Test
	public void equalsTest1(){
		ImmutableClock clock = new ImmutableClock(1,1);
		assertTrue(clock.equals(clock));
		ImmutableClock clock1 = new ImmutableClock(1,2);
		assertFalse(clock.equals(clock1));
		ImmutableClock clock3 = new ImmutableClock(1,1);
		assertTrue(clock.equals(clock3));
	}
	
	@Test
	public void minutesTest(){
		ImmutableClock clock = new ImmutableClock(2, 100);
		assertEquals(2980, clock.getTotalInMinutes());
			
	}

}
