package domain.usersTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import domain.users.AccessRight;


public class AccessRightTest {

	@Test
	public void test() {
		assertEquals(AccessRight.SHOWDETAILS, AccessRight.SHOWDETAILS);
		assertNotEquals(AccessRight.CHECKLINE, AccessRight.ASSEMBLE);
		
		assertEquals("Show order details", AccessRight.SHOWDETAILS.toString());
		assertEquals(AccessRight.SHOWDETAILS, AccessRight.valueOf("SHOWDETAILS"));
	}

}
