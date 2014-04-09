package domain.schedulerTest;

import org.junit.Test;

import domain.scheduler.SchedulingAlgorithmFifo;

public class SchedulingAlgorithmFifoTest {

	SchedulingAlgorithmFifo algorithm;
	@Test
	public void test() {
		int amountOfWorkBenches = 3;
		algorithm = new SchedulingAlgorithmFifo(amountOfWorkBenches);
		
	}

}
