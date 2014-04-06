package assemblyTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import order.Order;

import org.junit.Before;
import org.junit.Test;

import assembly.IJob;
import assembly.ITask;
import assembly.ImmutableJob;
import assembly.Job;
import assembly.Task;
import car.CarModel;
import car.CarPart;
import car.CarPartType;
import exception.AlreadyInMapException;
import exception.ImmutableException;

public class ImmutableJobTest {
	IJob job;
	IJob immutable;
	CarModel model;
	@Before
	public void initialize() throws AlreadyInMapException{
		model = new CarModel("Volkswagen");
		model.addCarPart(new CarPart("manual", true, CarPartType.AIRCO));
		model.addCarPart(new CarPart("sedan", false, CarPartType.BODY));
		model.addCarPart(new CarPart("red", false, CarPartType.COLOR));
		model.addCarPart(new CarPart("standard 2l 4 cilinders", false, CarPartType.ENGINE));
		model.addCarPart(new CarPart("6 speed manual", false, CarPartType.GEARBOX));
		model.addCarPart(new CarPart("leather black", false, CarPartType.SEATS));
		model.addCarPart(new CarPart("comfort", false, CarPartType.WHEEL));
		
		job = new Job(new Order("Stef", model, 1));
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
}
