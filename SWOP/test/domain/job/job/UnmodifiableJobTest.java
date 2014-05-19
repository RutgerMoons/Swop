package domain.job.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import domain.assembly.workBench.WorkBenchType;
import domain.clock.ImmutableClock;
import domain.exception.AlreadyInMapException;
import domain.exception.UnmodifiableException;
import domain.job.job.IJob;
import domain.job.job.Job;
import domain.job.job.UnmodifiableJob;
import domain.job.task.ITask;
import domain.job.task.Task;
import domain.order.order.StandardOrder;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicle.Vehicle;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

public class UnmodifiableJobTest {
	IJob job;
	IJob immutable;
	Vehicle model;
	@Before
	public void initialize() throws AlreadyInMapException{
		Set<VehicleOption> parts = new HashSet<>();
		parts.add(new VehicleOption("sport", VehicleOptionCategory.BODY));
		VehicleSpecification template = new VehicleSpecification("model", parts, new HashMap<WorkBenchType, Integer>(), new HashSet<VehicleOption>());
		model = new Vehicle(template);
		model.addVehicleOption(new VehicleOption("manual", VehicleOptionCategory.AIRCO));
		model.addVehicleOption(new VehicleOption("sedan",  VehicleOptionCategory.BODY));
		model.addVehicleOption(new VehicleOption("red",  VehicleOptionCategory.COLOR));
		model.addVehicleOption(new VehicleOption("standard 2l 4 cilinders",  VehicleOptionCategory.ENGINE));
		model.addVehicleOption(new VehicleOption("6 speed manual",  VehicleOptionCategory.GEARBOX));
		model.addVehicleOption(new VehicleOption("leather black", VehicleOptionCategory.SEATS));
		model.addVehicleOption(new VehicleOption("comfort", VehicleOptionCategory.WHEEL));
		
		job = new Job(new StandardOrder("Stef", model, 1, new ImmutableClock(0,240)));
		immutable = new UnmodifiableJob(job);
	}
	
	@Test
	public void test() throws UnmodifiableException {
		
		assertEquals(immutable.getOrder().getDescription(), model);
		List<ITask> tasks = new ArrayList<>();
		tasks.add(new Task("Test"));
		job.setTasks(tasks);
		assertEquals(tasks, immutable.getTasks());
		
		assertEquals(job.toString(), immutable.toString());
		assertEquals(job.hashCode(), immutable.hashCode());
		assertTrue(immutable.equals(job));
		assertTrue(job.equals(immutable));
		assertTrue(immutable.isCompleted());
	}

	@Test(expected=IllegalArgumentException.class)
	public void illegalConstructorTest(){
		new UnmodifiableJob(null);
	}
	
	@Test(expected=UnmodifiableException.class)
	public void testImmutable1() throws UnmodifiableException{
		immutable.setOrder(null);
	}
	
	@Test(expected=UnmodifiableException.class)
	public void testImmutable2() throws UnmodifiableException{
		immutable.setTasks(null);
	}
	
	@Test(expected=UnmodifiableException.class)
	public void testImmutable3() throws UnmodifiableException{
		immutable.addTask(null);
	}
	
	@Test
	public void testGetIndex() throws UnmodifiableException{
		job.setMinimalIndex(10);
		assertEquals(10, immutable.getMinimalIndex());
	}
	
	@Test(expected = UnmodifiableException.class)
	public void testSetIndex() throws UnmodifiableException{
		immutable.setMinimalIndex(10);
	}
	
	 @Test
	 public void testGetVehicleOptions(){
		 assertEquals(job.getVehicleOptions().size(), immutable.getVehicleOptions().size());
	 }
	
	 @Test
	 public void testGetTimeAtWorkBench(){
		 assertEquals(job.getTimeAtWorkBench(), immutable.getTimeAtWorkBench());
	 }
	 
	 @Test
	 public void testGetVehicleSpecification(){
		 assertEquals(job.getVehicleSpecification(), immutable.getVehicleSpecification());
	 }
	 
	 @Test
	 public void testGetProductionTime(){
		 assertEquals(job.getProductionTime(WorkBenchType.ACCESSORIES), immutable.getProductionTime(WorkBenchType.ACCESSORIES));
	 }

	 @Test
	 public void testGetTotalProductionTime(){
		 assertEquals(job.getTotalProductionTime(), immutable.getTotalProductionTime());
	 }
}
