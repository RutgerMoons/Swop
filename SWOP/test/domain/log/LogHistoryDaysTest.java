package domain.log;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LogHistoryDaysTest {

	LogHistoryDays history;
	
	@Before
	public void testConstructor() {
		int amountOfDays = 2;
		history = new LogHistoryDays(amountOfDays);
	}
	
	@Test
	public void shiftTest1(){
		for(int i = 0; i < 4;i++){
			history.incrementAmountOfCarsProducedToday();
		}
		history.shift();
		assertEquals(1,history.getHistory().size());
		assertEquals(1,history.getCompleteHistory().size());
		
	}
	
	@Test
	public void shiftTest2(){
		for(int i = 0; i < 4;i++){
			history.incrementAmountOfCarsProducedToday();
		}
		history.shift();
		for(int i = 0; i < 5;i++){
			history.incrementAmountOfCarsProducedToday();
		}
		history.shift();
		for(int i = 0; i < 3;i++){
			history.incrementAmountOfCarsProducedToday();
		}
		history.shift();
		assertEquals(2,history.getHistory().size());
		assertEquals(3,history.getCompleteHistory().size());
		
	}

}
