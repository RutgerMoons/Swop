package domain.assemblyTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import domain.assembly.workBench.WorkBenchType;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

public class WorkBenchTypeTest {


	@Test
	public void test() {
		assertNotEquals(WorkBenchType.ACCESSORIES, WorkBenchType.BODY);
		assertEquals(WorkBenchType.BODY, WorkBenchType.valueOf("BODY"));
		
	}

}
