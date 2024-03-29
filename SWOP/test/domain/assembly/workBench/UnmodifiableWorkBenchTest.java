package domain.assembly.workBench;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;

import domain.clock.ImmutableClock;
import domain.exception.AlreadyInMapException;
import domain.exception.UnmodifiableException;
import domain.job.action.Action;
import domain.job.job.IJob;
import domain.job.job.Job;
import domain.job.task.Task;
import domain.order.order.StandardOrder;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicle.Vehicle;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;


public class UnmodifiableWorkBenchTest {
	IWorkBench bench;
	IWorkBench immutable;
	@Before
	public void initialize(){
		bench = new WorkBench(WorkBenchType.BODY);
		immutable = new UnmodifiableWorkBench(bench);
		
	}
	@Test
	public void test() throws AlreadyInMapException, UnmodifiableException {
		Set<VehicleOption> parts = new HashSet<>();
		parts.add(new VehicleOption("sport", VehicleOptionCategory.BODY));
		VehicleSpecification template = new VehicleSpecification("model", parts, new HashMap<WorkBenchType, Integer>(), new HashSet<VehicleOption>());
		Vehicle model = new Vehicle(template);
		model.addVehicleOption(new VehicleOption("manual", VehicleOptionCategory.AIRCO));
		model.addVehicleOption(new VehicleOption("sedan",  VehicleOptionCategory.BODY));
		model.addVehicleOption(new VehicleOption("red",  VehicleOptionCategory.COLOR));
		model.addVehicleOption(new VehicleOption("standard 2l 4 cilinders",  VehicleOptionCategory.ENGINE));
		model.addVehicleOption(new VehicleOption("6 speed manual",  VehicleOptionCategory.GEARBOX));
		model.addVehicleOption(new VehicleOption("leather black", VehicleOptionCategory.SEATS));
		model.addVehicleOption(new VehicleOption("comfort", VehicleOptionCategory.WHEEL));
		
		assertEquals(WorkBenchType.BODY, immutable.getWorkbenchType());
		
		IJob job = new Job(new StandardOrder("Stef", model, 1, new ImmutableClock(0,240)));
		Task task= new Task(VehicleOptionCategory.COLOR.toString());
		task.addAction(new Action("paint blue"));
		((Job) job).addTask(task);
		Optional<IJob> jobOptional = Optional.fromNullable(job);
		bench.setCurrentJob(jobOptional);
		bench.chooseTasksOutOfJob();
		
		assertEquals(immutable.getCurrentJob().get(), job );
		assertEquals(task, immutable.getCurrentTasks().get(0));
		assertEquals(bench.toString(), immutable.toString());
		assertFalse(immutable.isCompleted());
		
		assertEquals(bench.getResponsibilities(), immutable.getResponsibilities());
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
	public void testImmutable5() throws UnmodifiableException{
		immutable.chooseTasksOutOfJob();
	}
	
	@Test
	public void testNoOptional() throws UnmodifiableException{
		Optional<IJob> optional = Optional.absent();
		bench.setCurrentJob(optional);
		assertFalse(immutable.getCurrentJob().isPresent());
	}
	
	@Test (expected=UnmodifiableException.class)
	public void testCompleteChosenTaskAtWorkBench(){
		immutable.completeChosenTaskAtChosenWorkBench(null);
	}
	
	@Test
	public void testEquals(){
		assertEquals(bench, immutable);
		assertEquals(immutable, bench);
		assertEquals(bench.hashCode(), immutable.hashCode());
	}
}
