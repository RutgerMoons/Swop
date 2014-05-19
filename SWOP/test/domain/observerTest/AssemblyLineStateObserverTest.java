package domain.observerTest;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import domain.assembly.assemblyLine.AssemblyLine;
import domain.assembly.assemblyLine.AssemblyLineState;
import domain.observer.observers.AssemblyLineObserver;
import domain.observer.observers.AssemblyLineStateObserver;
import domain.observer.observers.OrderBookObserver;
import domain.scheduling.WorkloadDivider;

public class AssemblyLineStateObserverTest {

	private AssemblyLineStateObserver observer;
	private WorkloadDivider divider;
	@Before
	public void initialise(){
		observer = new AssemblyLineStateObserver();
		divider = new WorkloadDivider(new ArrayList<AssemblyLine>(), new OrderBookObserver(), new AssemblyLineObserver());
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
		observer.updateAssemblyLineState(AssemblyLineState.BROKEN, AssemblyLineState.MAINTENANCE);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testUpdateIllegalOrder(){
		observer.updateAssemblyLineState(null, null);
	}

}
