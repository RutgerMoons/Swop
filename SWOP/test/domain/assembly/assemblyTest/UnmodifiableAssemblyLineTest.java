package domain.assembly.assemblyTest;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import domain.assembly.assemblyLine.AssemblyLine;
import domain.assembly.assemblyLine.AssemblyLineState;
import domain.assembly.assemblyLine.UnmodifiableAssemblyLine;
import domain.assembly.workBench.WorkBench;
import domain.assembly.workBench.WorkBenchType;
import domain.clock.ImmutableClock;
import domain.exception.UnmodifiableException;
import domain.job.action.Action;
import domain.job.job.IJob;
import domain.job.job.Job;
import domain.job.task.ITask;
import domain.job.task.Task;
import domain.observer.observers.ClockObserver;
import domain.order.order.IOrder;
import domain.order.order.StandardOrder;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreatorFifo;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicle.Vehicle;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

public class UnmodifiableAssemblyLineTest {

	AssemblyLine line;
	UnmodifiableAssemblyLine unmodifiable;
	@Before
	public void initialise(){
		Set<VehicleSpecification> responsibilities = new HashSet<VehicleSpecification>();
		responsibilities.add(new VehicleSpecification("test", new HashSet<VehicleOption>(), new HashMap<WorkBenchType, Integer>(), new HashSet<VehicleOption>()));
		
		line = new AssemblyLine(new ClockObserver(), new ImmutableClock(0, 0), AssemblyLineState.OPERATIONAL, responsibilities);
		line.switchToSchedulingAlgorithm(new SchedulingAlgorithmCreatorFifo());
		Set<String> responsibilities2 = new HashSet<>();
		responsibilities2.add("Color");
		responsibilities2.add("test2");
		line.addWorkBench(new WorkBench(WorkBenchType.BODY));
		line.addWorkBench(new WorkBench(WorkBenchType.DRIVETRAIN));
		unmodifiable = new UnmodifiableAssemblyLine(line);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testInvalidConstructor() {
		new UnmodifiableAssemblyLine(null);
	}
	
	@Test
	public void testCanAdvance(){
		assertEquals(line.canAdvance(), unmodifiable.canAdvance());
	}
	
	@Test
	public void testGetBlockingWorkBenches(){
		assertEquals(line.getBlockingWorkBenches(), unmodifiable.getBlockingWorkBenches());
		
		
		VehicleSpecification template = new VehicleSpecification("test", new HashSet<VehicleOption>(), new HashMap<WorkBenchType, Integer>(), new HashSet<VehicleOption>());
		Vehicle model = new Vehicle(template);
		model.addVehicleOption(new VehicleOption("manual", VehicleOptionCategory.AIRCO));
		model.addVehicleOption(new VehicleOption("sedan",  VehicleOptionCategory.BODY));
		model.addVehicleOption(new VehicleOption("red",  VehicleOptionCategory.COLOR));
		model.addVehicleOption(new VehicleOption("standard 2l 4 cilinders",  VehicleOptionCategory.ENGINE));
		model.addVehicleOption(new VehicleOption("6 speed manual",  VehicleOptionCategory.GEARBOX));
		model.addVehicleOption(new VehicleOption("leather black", VehicleOptionCategory.SEATS));
		model.addVehicleOption(new VehicleOption("comfort", VehicleOptionCategory.WHEEL));
		
		WorkBench body1 = new WorkBench(WorkBenchType.BODY);

		WorkBench drivetrain1 = new WorkBench(WorkBenchType.DRIVETRAIN);

		WorkBench accessories1 = new WorkBench(WorkBenchType.ACCESSORIES);

		line.addWorkBench(body1);
		line.addWorkBench(drivetrain1);
		line.addWorkBench(accessories1);
		
		line.switchToSchedulingAlgorithm(new SchedulingAlgorithmCreatorFifo());
		
		
		
		
		Action action1 = new Action("paint");
		action1.setCompleted(false);
		Task task1 = new Task("Color");
		task1.addAction(action1);
		ArrayList<ITask> list = new ArrayList<ITask>();
		list.add(task1);
		IOrder order = new StandardOrder("jos", model, 1, new ImmutableClock(0, 0));
		IJob job = new Job(order);
		job.setTasks(list);
		
		line.schedule(job);
		line.advance();
		assertEquals(line.getBlockingWorkBenches(), unmodifiable.getBlockingWorkBenches());
	}

	@Test
	public void testGetWorkBenches(){
		assertEquals(line.getWorkBenches(), unmodifiable.getWorkBenches());
	}
	
	@Test
	public void getAllVehicleOptionsInPendingOrders(){
		assertEquals(line.getAllVehicleOptionsInPendingOrders(), unmodifiable.getAllVehicleOptionsInPendingOrders());
	}
	
	@Test
	public void testGetState(){
		assertEquals(line.getState(), unmodifiable.getState());
	}
	
	@Test (expected = UnmodifiableException.class)
	public void testSetState(){
		unmodifiable.setState(AssemblyLineState.BROKEN);
	}
	
	@Test
	public void testToString(){
		assertEquals(line.toString(), unmodifiable.toString());
	}
	
	@Test
	public void testGetResponsibilities(){
		assertEquals(line.getResponsibilities(), unmodifiable.getResponsibilities());
	}
	
	@Test
	public void testEquals(){
		assertEquals(line, unmodifiable);
		assertEquals(unmodifiable, line);
	}
}
