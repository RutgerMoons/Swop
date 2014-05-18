package domain.assembly.assemblyTest;

import static org.junit.Assert.assertEquals;

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
import domain.observer.observers.ClockObserver;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreatorFifo;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicleOption.VehicleOption;

public class UnmodifiableAssemblyLineTest {

	AssemblyLine line;
	UnmodifiableAssemblyLine unmodifiable;
	@Before
	public void initialise(){
		Set<VehicleSpecification> responsibilities = new HashSet<VehicleSpecification>();
		responsibilities.add(new VehicleSpecification("test", new HashSet<VehicleOption>(), new HashMap<WorkBenchType, Integer>()));
		
		line = new AssemblyLine(new ClockObserver(), new ImmutableClock(0, 0), AssemblyLineState.OPERATIONAL, responsibilities);
		line.switchToSchedulingAlgorithm(new SchedulingAlgorithmCreatorFifo());
		Set<String> responsibilities2 = new HashSet<>();
		responsibilities2.add("test");
		responsibilities2.add("test2");
		line.addWorkBench(new WorkBench(responsibilities2, WorkBenchType.BODY));
		line.addWorkBench(new WorkBench(new HashSet<String>(), WorkBenchType.DRIVETRAIN));
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
