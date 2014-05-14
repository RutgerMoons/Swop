package domain.assemblyTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import domain.assembly.workBench.WorkbenchType;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

public class WorkBenchTypeTest {


	@Test
	public void test() {
		assertNotEquals(WorkbenchType.ACCESSORIES, WorkbenchType.BODY);
		assertEquals(WorkbenchType.BODY, WorkbenchType.valueOf("BODY"));
		
	}

}
