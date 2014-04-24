package domain.orderTest;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import domain.clock.UnmodifiableClock;
import domain.order.Delay;

public class DelayTest {
	
	private UnmodifiableClock estimated, completed;
	private Delay delay;
	
	@Before
	public void initialize() {
		estimated = new UnmodifiableClock(2,400);
		completed = new UnmodifiableClock(2,850);
		delay = new Delay(estimated,completed);
		assertEquals(450, delay.getDelay());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void constructorTestFail1() {
		delay = new Delay(null, completed);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void constructorTestFail2() {
		delay = new Delay(null, null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void constructorTestFail3() {
		delay = new Delay(estimated, null);
	}
	
	@Test
	public void toStringTest(){
		assertEquals("2: 450", delay.toString());
	}

}
