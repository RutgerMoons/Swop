package usersTest;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import users.AccessRight;
import users.User;
import users.UserFactory;

public class UserFactoryTest {

	UserFactory factory;
	
	@Before
	public void initialize(){
		factory = new UserFactory();
	}

	@Test
	public void testCreate() {
		User garageholder = factory.createUser("Stef", "garageholder");
		assertEquals("Stef", garageholder.getName());
		assertEquals(AccessRight.ORDER, garageholder.getAccessRights().get(0));
		
		User manager = factory.createUser("Karen", "manager");
		assertEquals("Karen", manager.getName());
		assertEquals(AccessRight.ADVANCE, manager.getAccessRights().get(0));
		
		User worker = factory.createUser("Rutger", "worker");
		assertEquals("Rutger", worker.getName());
		assertEquals(AccessRight.ASSEMBLE, worker.getAccessRights().get(0));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidRole1(){
		factory.createUser("bla", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidRole2(){
		factory.createUser("bla", "bla");
	}

}
