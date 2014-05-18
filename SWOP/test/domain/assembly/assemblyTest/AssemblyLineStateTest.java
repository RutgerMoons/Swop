package domain.assembly.assemblyTest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import domain.assembly.assemblyLine.AssemblyLineState;

public class AssemblyLineStateTest {

	@Test
	public void test() {
		assertEquals("The assemblyLine is operational", AssemblyLineState.OPERATIONAL.toString());
		assertEquals("The assemblyLine is broken", AssemblyLineState.BROKEN.toString());
		assertEquals("The assemblyLine is under maintenance", AssemblyLineState.MAINTENANCE.toString());
		
		assertEquals(AssemblyLineState.OPERATIONAL, AssemblyLineState.valueOf("OPERATIONAL"));
	}

}
