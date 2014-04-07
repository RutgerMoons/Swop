package usersTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import domain.users.AccessRight;


public class AccessRightTest {

	@Test
	public void test() {
		assertEquals(AccessRight.ADVANCE, AccessRight.ADVANCE);
		assertNotEquals(AccessRight.ADVANCE, AccessRight.ASSEMBLE);
		
		assertEquals("Advance the assemblyline", AccessRight.ADVANCE.toString());
		assertEquals(AccessRight.ADVANCE, AccessRight.valueOf("ADVANCE"));
	}

}
