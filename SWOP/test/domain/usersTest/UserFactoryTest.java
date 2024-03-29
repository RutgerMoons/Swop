package domain.usersTest;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import domain.users.AccessRight;
import domain.users.User;
import domain.users.UserFactory;


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
		assertEquals(AccessRight.SHOWDETAILS, garageholder.getAccessRights().get(1));
		
		User manager = factory.createUser("Karen", "manager");
		assertEquals("Karen", manager.getName());
		assertEquals(AccessRight.STATISTICS, manager.getAccessRights().get(0));
		
		User worker = factory.createUser("Rutger", "worker");
		assertEquals("Rutger", worker.getName());
		assertEquals(AccessRight.ASSEMBLE, worker.getAccessRights().get(0));
		
		User customManager = factory.createUser("Faeshaas", "custom car shop manager");
		assertEquals("Faeshaas", customManager.getName());
		assertEquals(AccessRight.CUSTOMORDER, customManager.getAccessRights().get(0));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void invalidRoleTest1() {
		factory.createUser("bla", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void invalidRoleTest2() {
		factory.createUser("bla", "bla");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void invalidRoleTest3(){
		factory.createUser(null, "worker");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void invalidRoleTest4(){
		factory.createUser(null, "");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void invalidRoleTest5(){
		factory.createUser(null, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void invalidRoleTest7(){
		factory.createUser("bla", "");
	}
	

}
