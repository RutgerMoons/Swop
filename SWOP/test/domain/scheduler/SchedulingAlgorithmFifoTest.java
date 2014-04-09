package domain.scheduler;

import static org.junit.Assert.fail;

import org.junit.Before;

public class SchedulingAlgorithmFifoTest {

	SchedulingAlgorithmFifo algorithm;
	@Before
	public void test() {
		int amountOfWorkBenches = 3;
		algorithm = new SchedulingAlgorithmFifo(amountOfWorkBenches);
		
	}

}
