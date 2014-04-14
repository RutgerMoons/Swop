package domain.schedulerTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import domain.car.CarModel;
import domain.car.CarModelSpecification;
import domain.car.CarOption;
import domain.car.CustomCarModel;
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
		assertNotNull(algorithm.getCustomJobs());
		assertNotNull(algorithm.getHistory());
		assertNotNull(algorithm.getStandardJobs());
	}

	@Test
	public void testAddCustomOrder1(){
		CustomCarModel customModel = new CustomCarModel();
		CarOption 
		customModel.addCarPart(part)
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testAddCustomOrder2(){
		algorithm.AddCustomJob(null);
	}
	
	@Test
	public void testAddStandardJob1(){
		Set<CarOption> parts = new HashSet<>();
		template = new CarModelSpecification("model", parts, 60);
		model = new CarModel(template);
		IOrder order = new StandardOrder("mario",model,3);
		IJob job = new Job(order);
		algorithm.AddStandardJob(job);
		assertEquals(1,algorithm.getStandardJobs().size());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testAddStandardJob2(){
		algorithm.AddStandardJob(null);
	}
}
