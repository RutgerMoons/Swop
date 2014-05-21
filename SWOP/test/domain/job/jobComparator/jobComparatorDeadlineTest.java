package domain.job.jobComparator;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import domain.assembly.workBench.WorkBenchType;
import domain.clock.ImmutableClock;
import domain.job.job.IJob;
import domain.job.job.Job;
import domain.order.order.CustomOrder;
import domain.order.order.StandardOrder;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicle.CustomVehicle;
import domain.vehicle.vehicle.Vehicle;
import domain.vehicle.vehicleOption.VehicleOption;

public class jobComparatorDeadlineTest {

	@Test
	public void compareToTest() {
		CustomVehicle customModel = new CustomVehicle();
		ImmutableClock ordertime = new ImmutableClock(2, 30);
		ImmutableClock deadline = new ImmutableClock(5, 30);
		CustomOrder order = new CustomOrder("Mario", customModel, 5, ordertime, deadline);
		IJob job = new Job(order);
		
		CustomVehicle customModel2 = new CustomVehicle();
		ImmutableClock ordertime2 = new ImmutableClock(2, 500);
		ImmutableClock deadline2 = new ImmutableClock(5, 500);
		CustomOrder order2 = new CustomOrder("Mario", customModel2, 5, ordertime2, deadline2);
		IJob job2 = new Job(order2);
		
		JobComparatorDeadLine d = new JobComparatorDeadLine();
		assertTrue(d.compare(job, job2) < 0);
	}
	
	@Test
	public void compareToTestException() {
		CustomVehicle customModel = new CustomVehicle();
		ImmutableClock ordertime = new ImmutableClock(2, 30);
		ImmutableClock deadline = new ImmutableClock(5, 30);
		CustomOrder order = new CustomOrder("Mario", customModel, 5, ordertime, deadline);
		IJob job = new Job(order);
		
		HashMap<WorkBenchType, Integer> timeAtWorkBench = new HashMap<WorkBenchType, Integer>();
		timeAtWorkBench.put(WorkBenchType.ACCESSORIES, 60);
		timeAtWorkBench.put(WorkBenchType.BODY, 60);
		timeAtWorkBench.put(WorkBenchType.CARGO, 0);
		timeAtWorkBench.put(WorkBenchType.CERTIFICATION, 0);
		timeAtWorkBench.put(WorkBenchType.DRIVETRAIN, 60);
		Set<VehicleOption> parts2 = new HashSet<>();
		VehicleSpecification template = new VehicleSpecification("model", parts2, timeAtWorkBench, new HashSet<VehicleOption>());
		Vehicle model = new Vehicle(template);
		ImmutableClock ordertime2 = new ImmutableClock(2, 30);
		StandardOrder order2 = new StandardOrder("mario", model, 3, ordertime2);
		IJob job2 = new Job(order2);
		
		JobComparatorDeadLine d = new JobComparatorDeadLine();
		assertEquals(0, d.compare(job, job2));
	}
}
