package domain.assemblyTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import domain.car.Vehicle;
import domain.car.VehicleSpecification;
import domain.car.VehicleOption;
import domain.car.VehicleOptionCategory;
import domain.clock.ImmutableClock;
import domain.exception.AlreadyInMapException;
import domain.exception.ImmutableException;
import domain.job.IJob;
import domain.job.ITask;
import domain.job.UnmodifiableJob;
import domain.job.Job;
import domain.job.Task;
import domain.order.StandardOrder;

public class ImmutableJobTest {
	IJob job;
	IJob immutable;
	Vehicle model;
	@Before
	public void initialize() throws AlreadyInMapException{
		Set<VehicleOption> parts = new HashSet<>();
		parts.add(new VehicleOption("sport", VehicleOptionCategory.BODY));
		VehicleSpecification template = new VehicleSpecification("model", parts, 60);
		model = new Vehicle(template);
		model.addCarPart(new VehicleOption("manual", VehicleOptionCategory.AIRCO));
		model.addCarPart(new VehicleOption("sedan",  VehicleOptionCategory.BODY));
		model.addCarPart(new VehicleOption("red",  VehicleOptionCategory.COLOR));
		model.addCarPart(new VehicleOption("standard 2l 4 cilinders",  VehicleOptionCategory.ENGINE));
		model.addCarPart(new VehicleOption("6 speed manual",  VehicleOptionCategory.GEARBOX));
		model.addCarPart(new VehicleOption("leather black", VehicleOptionCategory.SEATS));
		model.addCarPart(new VehicleOption("comfort", VehicleOptionCategory.WHEEL));
		
		job = new Job(new StandardOrder("Stef", model, 1, new ImmutableClock(0,240)));
		immutable = new UnmodifiableJob(job);
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
		new UnmodifiableJob(null);
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
