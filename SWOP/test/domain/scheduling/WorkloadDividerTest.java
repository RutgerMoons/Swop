package domain.scheduling;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import domain.assembly.assemblyLine.AssemblyLine;
import domain.assembly.assemblyLine.AssemblyLineState;
import domain.assembly.assemblyLine.UnmodifiableAssemblyLine;
import domain.clock.Clock;
import domain.clock.ImmutableClock;
import domain.observer.observers.AssemblyLineObserver;
import domain.observer.observers.ClockObserver;
import domain.observer.observers.OrderBookObserver;
import domain.scheduling.WorkloadDivider;
import domain.scheduling.schedulingAlgorithm.SchedulingAlgorithm;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreatorFifo;

public class WorkloadDividerTest {

	private WorkloadDivider workloadDivider;
	private OrderBookObserver orderBookObserver;
	private AssemblyLineObserver assemblyLineObserver;
	
	@Before
	public void testConstructor2() {
		Clock clock = new Clock();
		ClockObserver clockObserver = new ClockObserver();
		clock.attachObserver(clockObserver);
		ImmutableClock currentTime = clock.getImmutableClock();
		AssemblyLine line1 = new AssemblyLine(clockObserver, currentTime, AssemblyLineState.OPERATIONAL);
		ArrayList<AssemblyLine> lines = new ArrayList<>();
		lines.add(line1);
		orderBookObserver = new OrderBookObserver();
		assemblyLineObserver = new AssemblyLineObserver();
		
		workloadDivider = new WorkloadDivider(lines, orderBookObserver, assemblyLineObserver);
		assertNotNull(workloadDivider);
	}
	
	@Test
	public void testConstructor1() {
		OrderBookObserver orderBookObserver = new OrderBookObserver();
		AssemblyLineObserver assemblyLineObserver = new AssemblyLineObserver();
		WorkloadDivider w = new WorkloadDivider(new ArrayList<AssemblyLine>(), orderBookObserver, assemblyLineObserver);
		assertNotNull(w);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorException1() {
		WorkloadDivider w = new WorkloadDivider(null, null, null);
	}
	
	@Test
	public void testGetCurrentSchedulingAlgorithmNoAssemblyLines() {
		WorkloadDivider w = new WorkloadDivider(new ArrayList<AssemblyLine>(), orderBookObserver, assemblyLineObserver);
		assertNotNull(w);
		assertEquals("There are no assemblyLines at the moment.", w.getCurrentSchedulingAlgorithm());
	} 
	
	@Test
	public void testGetCurrentSchedulingAlgorithmNoSchedulingAlgorithm() {
		assertEquals("No scheduling algorithm used at the moment.", workloadDivider.getCurrentSchedulingAlgorithm());
	}
	
	@Test
	public void testGetCurrentSchedulingAlgorithmWithSchedulingAlgorithm() {
		SchedulingAlgorithmCreatorFifo creator = new SchedulingAlgorithmCreatorFifo();
		workloadDivider.switchToSchedulingAlgorithm(creator);
		assertNotNull(workloadDivider);
		assertEquals("Fifo", workloadDivider.getCurrentSchedulingAlgorithm());
	}
	
	@Test
	public void testGetAssemblyLines() {
		assertEquals(1, workloadDivider.getAssemblyLines().size());
		
		Clock clock = new Clock();
		ClockObserver clockObserver = new ClockObserver();
		clock.attachObserver(clockObserver);
		ImmutableClock currentTime = clock.getImmutableClock();
		AssemblyLine line1 = new AssemblyLine(clockObserver, currentTime, AssemblyLineState.OPERATIONAL);
		AssemblyLine line2 = new AssemblyLine(clockObserver, currentTime, AssemblyLineState.BROKEN);
		ArrayList<AssemblyLine> lines = new ArrayList<>();
		lines.add(line1);
		lines.add(line2);
		
		WorkloadDivider w = new WorkloadDivider(lines, orderBookObserver, assemblyLineObserver);
		assertEquals(2, w.getAssemblyLines().size());
	}
	
	@Test
	public void testgetOperationalUnmodifiableAssemblyLines() {
		Clock clock = new Clock();
		ClockObserver clockObserver = new ClockObserver();
		clock.attachObserver(clockObserver);
		ImmutableClock currentTime = clock.getImmutableClock();
		AssemblyLine line1 = new AssemblyLine(clockObserver, currentTime, AssemblyLineState.OPERATIONAL);
		AssemblyLine line2 = new AssemblyLine(clockObserver, currentTime, AssemblyLineState.BROKEN);
		ArrayList<AssemblyLine> lines = new ArrayList<>();
		lines.add(line1);
		lines.add(line2);
		
		WorkloadDivider w = new WorkloadDivider(lines, orderBookObserver, assemblyLineObserver);
		assertEquals(2, w.getAssemblyLines().size());
		assertEquals(1, w.getOperationalUnmodifiableAssemblyLines().size());
		assertTrue(w.getOperationalUnmodifiableAssemblyLines().get(0).getState().equals(AssemblyLineState.OPERATIONAL));
	}
	
	@Test
	public void testgetBrokenUnmodifiableAssemblyLines() {
		Clock clock = new Clock();
		ClockObserver clockObserver = new ClockObserver();
		clock.attachObserver(clockObserver);
		ImmutableClock currentTime = clock.getImmutableClock();
		AssemblyLine line1 = new AssemblyLine(clockObserver, currentTime, AssemblyLineState.OPERATIONAL);
		AssemblyLine line2 = new AssemblyLine(clockObserver, currentTime, AssemblyLineState.BROKEN);
		ArrayList<AssemblyLine> lines = new ArrayList<>();
		lines.add(line1);
		lines.add(line2);
		
		WorkloadDivider w = new WorkloadDivider(lines, orderBookObserver, assemblyLineObserver);
		assertEquals(2, w.getAssemblyLines().size());
		assertEquals(1, w.getBrokenUnmodifiableAssemblyLines().size());
		assertTrue(w.getBrokenUnmodifiableAssemblyLines().get(0).getState().equals(AssemblyLineState.BROKEN));
	}
	
	@Test
	public void testgetMaintenanceUnmodifiableAssemblyLines() {
		Clock clock = new Clock();
		ClockObserver clockObserver = new ClockObserver();
		clock.attachObserver(clockObserver);
		ImmutableClock currentTime = clock.getImmutableClock();
		AssemblyLine line1 = new AssemblyLine(clockObserver, currentTime, AssemblyLineState.MAINTENANCE);
		AssemblyLine line2 = new AssemblyLine(clockObserver, currentTime, AssemblyLineState.BROKEN);
		ArrayList<AssemblyLine> lines = new ArrayList<>();
		lines.add(line1);
		lines.add(line2);
		
		WorkloadDivider w = new WorkloadDivider(lines, orderBookObserver, assemblyLineObserver);
		assertEquals(2, w.getAssemblyLines().size());
		assertEquals(1, w.getMaintenanceUnmodifiableAssemblyLines().size());
		assertTrue(w.getMaintenanceUnmodifiableAssemblyLines().get(0).getState().equals(AssemblyLineState.MAINTENANCE));
	}
	
	
	

}
