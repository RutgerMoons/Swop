package domain.log;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import domain.clock.UnmodifiableClock;
import domain.order.Delay;

public class LogHistoryDelaysTest {

	LogHistoryDelays history;
	@Before
	public void testConstructor() {
		int amountOfDays = 2;
		history = new LogHistoryDelays(amountOfDays);
	}

	@Test
	public void shiftTest1(){
		Delay delay = new Delay(new UnmodifiableClock(1,300), new UnmodifiableClock(1,400));
		for(int i = 0; i < 4;i++){
			history.addNewDelay(delay);
		}
		assertEquals(2,history.getHistory().size());
		assertEquals(4,history.getCompleteHistory().size());

	}

	@Test
	public void shiftTest2(){
		Delay delay = new Delay(new UnmodifiableClock(1,300), new UnmodifiableClock(1,400));
		for(int i = 0; i < 4;i++){
			history.addNewDelay(delay);
		}
		Delay delay1 = new Delay(new UnmodifiableClock(1,400), new UnmodifiableClock(1,550));
		for(int i = 0; i < 5;i++){
			history.addNewDelay(delay1);
		}
		Delay delay3 = new Delay(new UnmodifiableClock(2,300), new UnmodifiableClock(2,400));
		for(int i = 0; i < 3;i++){
			history.addNewDelay(delay3);
		}
		assertEquals(2,history.getHistory().size());
		assertEquals(12,history.getCompleteHistory().size());	
	}

	@Test (expected = IllegalArgumentException.class)
	public void addNewDelayTestThrow(){
		history.addNewDelay(null);
	}

	@Test
	public void addNewDelayNoDelayTest(){
		Delay delay = new Delay(new UnmodifiableClock(1,300), new UnmodifiableClock(1,300));
		history.addNewDelay(delay);
	}
}
