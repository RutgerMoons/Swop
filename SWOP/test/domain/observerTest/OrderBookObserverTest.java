package domain.observerTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import domain.assembly.assemblyLine.AssemblyLine;
import domain.assembly.workBench.WorkBenchType;
import domain.clock.ImmutableClock;
import domain.observer.observers.AssemblyLineObserver;
import domain.observer.observers.OrderBookObserver;
import domain.order.order.IOrder;
import domain.order.order.StandardOrder;
import domain.scheduling.WorkloadDivider;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicle.Vehicle;
import domain.vehicle.vehicleOption.VehicleOption;

public class OrderBookObserverTest {

	private OrderBookObserver observer;
	private WorkloadDivider divider;
	@Before
	public void initialise(){
		observer = new OrderBookObserver();
		divider = new WorkloadDivider(new ArrayList<AssemblyLine>(), observer, new AssemblyLineObserver());
	}
	
	
	@Test 
	public void attachLoggerTest1(){
		observer.attachLogger(divider);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void attachLoggerTest2(){
		observer.attachLogger(null);
	}
	
	@Test 
	public void detachLoggerTest1(){
		observer.attachLogger(divider);
		observer.detachLogger(divider);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void detachLoggerTest2(){
		observer.detachLogger(null);
	}

	@Test
	public void testUpdateOrder(){
		observer.attachLogger(divider);
		VehicleSpecification template = new VehicleSpecification("jos", new HashSet<VehicleOption>(), new HashMap<WorkBenchType, Integer>(), new HashSet<VehicleOption>());
		Vehicle description = new Vehicle(template);
		IOrder order = new StandardOrder("jef", description, 1, new ImmutableClock(0, 0));
		observer.notifyNewOrder(order);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testUpdateIllegalOrder(){
		observer.notifyNewOrder(null);
	}
}
