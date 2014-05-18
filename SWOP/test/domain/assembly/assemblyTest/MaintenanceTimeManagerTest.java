package domain.assembly.assemblyTest;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import domain.assembly.assemblyLine.AssemblyLine;
import domain.assembly.assemblyLine.AssemblyLineState;
import domain.assembly.assemblyLine.MaintenanceTimeManager;
import domain.clock.ImmutableClock;
import domain.observer.observers.ClockObserver;
import domain.vehicle.VehicleSpecification;

public class MaintenanceTimeManagerTest {

	private AssemblyLine line;
	private MaintenanceTimeManager manager;
	@Before
	public void initialise() {
		line = new AssemblyLine(new ClockObserver(), new ImmutableClock(0, 0), AssemblyLineState.BROKEN, new HashSet<VehicleSpecification>());
		manager = new MaintenanceTimeManager(line, new ImmutableClock(0, 0));
	}

	@Test
	public void test() {
		assertEquals(AssemblyLineState.BROKEN, line.getState());
	}
	
	@Test
	public void testAdvanceTime(){
		manager.advanceTime(new ImmutableClock(0, 230));
		assertEquals(AssemblyLineState.BROKEN, line.getState());
		
		manager.advanceTime(new ImmutableClock(0, 240));
		assertEquals(AssemblyLineState.OPERATIONAL, line.getState());
	}

	@Test
	public void testStartNewDay(){
		manager.startNewDay(new ImmutableClock(0, 230));
		assertEquals(AssemblyLineState.BROKEN, line.getState());
		manager.startNewDay(new ImmutableClock(1, 230));
		assertEquals(AssemblyLineState.OPERATIONAL, line.getState());
	}
}
