package domain.scheduler;

import org.junit.Test;

public class SchedulingAlgorithmFifoTest {

	SchedulingAlgorithmFifo algorithm;
	@Test
	public void test() {
		int amountOfWorkBenches = 3;
		algorithm = new SchedulingAlgorithmFifo(amountOfWorkBenches);
		
	}

}
