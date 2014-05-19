package domain.assembly.workBench;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class WorkBenchTypeTest {


	@Test
	public void test() {
		assertNotEquals(WorkBenchType.ACCESSORIES, WorkBenchType.BODY);
		assertEquals(WorkBenchType.BODY, WorkBenchType.valueOf("BODY"));
		
	}

}
