package domain.orderTest;

import org.junit.Before;
import static org.junit.Assert.*;

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

}
