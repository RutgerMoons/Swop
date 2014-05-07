package domain.assemblyTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;

import domain.assembly.IWorkBench;
import domain.assembly.UnmodifiableWorkBench;
import domain.assembly.WorkBench;
import domain.clock.ImmutableClock;
import domain.exception.AlreadyInMapException;
import domain.exception.UnmodifiableException;
import domain.job.Action;
import domain.job.IJob;
import domain.job.Job;
import domain.job.Task;
import domain.order.StandardOrder;
import domain.vehicle.Vehicle;
import domain.vehicle.VehicleOption;
import domain.vehicle.VehicleOptionCategory;
import domain.vehicle.VehicleSpecification;


public class ImmutableWorkBenchTest {
	IWorkBench bench;
	IWorkBench immutable;
	@Before
	public void initialize(){
		Set<String> responsibilities = new HashSet<>();
		responsibilities.add("paint");

		bench = new WorkBench(responsibilities, "paintbooth");
		immutable = new UnmodifiableWorkBench(bench);
		
	}
	@Test
	public void test() throws AlreadyInMapException, UnmodifiableException {
		Set<VehicleOption> parts = new HashSet<>();
		parts.add(new VehicleOption("sport", VehicleOptionCategory.BODY));
		VehicleSpecification template = new VehicleSpecification("model", parts, 60);
		Vehicle model = new Vehicle(template);
		model.addCarPart(new VehicleOption("manual", VehicleOptionCategory.AIRCO));
		model.addCarPart(new VehicleOption("sedan",  VehicleOptionCategory.BODY));
		model.addCarPart(new VehicleOption("red",  VehicleOptionCategory.COLOR));
		model.addCarPart(new VehicleOption("standard 2l 4 cilinders",  VehicleOptionCategory.ENGINE));
		model.addCarPart(new VehicleOption("6 speed manual",  VehicleOptionCategory.GEARBOX));
		model.addCarPart(new VehicleOption("leather black", VehicleOptionCategory.SEATS));
		model.addCarPart(new VehicleOption("comfort", VehicleOptionCategory.WHEEL));
		
		assertEquals("paintbooth", immutable.getWorkbenchName());
		assertTrue(immutable.getResponsibilities().contains("paint"));
		
		IJob job = new Job(new StandardOrder("Stef", model, 1, new ImmutableClock(0,240)));
		Task task= new Task("paint");
		task.addAction(new Action("paint blue"));
		((Job) job).addTask(task);
		Optional<IJob> jobOptional = Optional.fromNullable(job);
		bench.setCurrentJob(jobOptional);
		bench.chooseTasksOutOfJob();
		
		assertEquals(immutable.getCurrentJob().get(), job );
		assertEquals(task, immutable.getCurrentTasks().get(0));
		assertEquals(bench.toString(), immutable.toString());
		assertFalse(immutable.isCompleted());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void illegalConstructorTest(){
		new UnmodifiableWorkBench(null);
	}
	
	@Test(expected=UnmodifiableException.class)
	public void testImmutable1() throws UnmodifiableException{
		immutable.setCurrentJob(null);
	}
	
	@Test(expected=UnmodifiableException.class)
	public void testImmutable2() throws UnmodifiableException{
		immutable.setResponsibilities(null);
	}
	
	@Test(expected=UnmodifiableException.class)
	public void testImmutable3() throws UnmodifiableException{
		immutable.addResponsibility(null);
	}
	
	@Test(expected=UnmodifiableException.class)
	public void testImmutable4() throws UnmodifiableException{
		immutable.setCurrentTasks(null);
	}
	
	@Test(expected=UnmodifiableException.class)
	public void testImmutable5() throws UnmodifiableException{
		immutable.chooseTasksOutOfJob();
	}
	
	@Test
	public void testNoOptional() throws UnmodifiableException{
		Optional<IJob> optional = Optional.absent();
		bench.setCurrentJob(optional);
		assertFalse(immutable.getCurrentJob().isPresent());
	}
}
