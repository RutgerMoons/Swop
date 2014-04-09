package domain.schedulerTest;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import domain.car.CarModel;
import domain.car.CarModelSpecification;
import domain.car.CarOption;
import domain.job.IJob;
import domain.job.Job;
import domain.order.IOrder;
import domain.order.StandardOrder;
import domain.scheduler.SchedulingAlgorithmFifo;

public class SchedulingAlgorithmFifoTest {

	SchedulingAlgorithmFifo algorithm;
	private CarModel model;
	private CarModelSpecification template;
	@Before
	public void testConstructor() {
		int amountOfWorkBenches = 3;
		algorithm = new SchedulingAlgorithmFifo(amountOfWorkBenches);	
	}
	
	
	@Test
	public void testAddToHistory(){
		Set<CarOption> parts = new HashSet<>();
		template = new CarModelSpecification("model", parts, 60);
		model = new CarModel(template);
		IOrder order = new StandardOrder("mario",model,3);
		IJob job = new Job(order);
	}

}
