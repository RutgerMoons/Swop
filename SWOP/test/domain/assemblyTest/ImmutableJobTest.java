package domain.assemblyTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import domain.car.CarModel;
import domain.car.CarModelSpecification;
import domain.car.CarOption;
import domain.car.CarOptionCategory;
import domain.clock.UnmodifiableClock;
import domain.exception.AlreadyInMapException;
import domain.exception.ImmutableException;
import domain.job.IJob;
import domain.job.ITask;
import domain.job.ImmutableJob;
import domain.job.Job;
import domain.job.Task;
import domain.order.StandardOrder;

public class ImmutableJobTest {
	IJob job;
	IJob immutable;
	CarModel model;
	@Before
	public void initialize() throws AlreadyInMapException{
		Set<CarOption> parts = new HashSet<>();
		parts.add(new CarOption("sport", CarOptionCategory.BODY));
		CarModelSpecification template = new CarModelSpecification("model", parts, 60);
		model = new CarModel(template);
		model.addCarPart(new CarOption("manual", CarOptionCategory.AIRCO));
		model.addCarPart(new CarOption("sedan",  CarOptionCategory.BODY));
		model.addCarPart(new CarOption("red",  CarOptionCategory.COLOR));
		model.addCarPart(new CarOption("standard 2l 4 cilinders",  CarOptionCategory.ENGINE));
		model.addCarPart(new CarOption("6 speed manual",  CarOptionCategory.GEARBOX));
		model.addCarPart(new CarOption("leather black", CarOptionCategory.SEATS));
		model.addCarPart(new CarOption("comfort", CarOptionCategory.WHEEL));
		
		job = new Job(new StandardOrder("Stef", model, 1, new UnmodifiableClock(0,240)));
		immutable = new ImmutableJob(job);
	}
	
	@Test
	public void test() throws ImmutableException {
		
		assertEquals(immutable.getOrder().getDescription(), model);
		List<ITask> tasks = new ArrayList<>();
		tasks.add(new Task("Test"));
		job.setTasks(tasks);
		assertEquals(tasks, immutable.getTasks());
		
		assertEquals(job.toString(), immutable.toString());
		assertEquals(job.hashCode(), immutable.hashCode());
		assertTrue(immutable.equals(job));;
		assertTrue(immutable.isCompleted());
	}

	@Test(expected=IllegalArgumentException.class)
	public void illegalConstructorTest(){
		new ImmutableJob(null);
	}
	
	@Test(expected=ImmutableException.class)
	public void testImmutable1() throws ImmutableException{
		immutable.setOrder(null);
	}
	
	@Test(expected=ImmutableException.class)
	public void testImmutable2() throws ImmutableException{
		immutable.setTasks(null);
	}
	
	@Test(expected=ImmutableException.class)
	public void testImmutable3() throws ImmutableException{
		immutable.addTask(null);
	}
	
	@Test
	public void testGetIndex() throws ImmutableException{
		job.setMinimalIndex(10);
		assertEquals(10, immutable.getMinimalIndex());
	}
	
	@Test(expected = ImmutableException.class)
	public void testSetIndex() throws ImmutableException{
		immutable.setMinimalIndex(10);
	}
}
