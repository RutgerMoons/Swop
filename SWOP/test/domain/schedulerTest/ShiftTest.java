package domain.schedulerTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import domain.scheduler.Shift;

public class ShiftTest {

	private Shift shift;
	
	@Before
	public void initialize() {
		int start = 360;
		int end = 900;
		int overtime = 20;
		shift = new Shift(start, end, overtime);
	}
	
	@Test
	public void constructorTest(){
		assertEquals(360, this.shift.getStartOfShift());
		assertEquals(880,this.shift.getEndOfShift());
		assertNotNull(this.shift);
	}
	
	@Test
	public void setNewOvertimeTest(){
		shift.setNewOvertime(300);
		assertEquals(600, this.shift.getEndOfShift());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void setNewOvertimeTest2(){
		this.shift.setNewOvertime(-200);
	}

}
