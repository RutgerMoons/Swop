package domain.usersTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import domain.users.AccessRight;
import domain.users.User;
import domain.users.UserBook;


public class UserTest {
	User user;
	@Before
	public void initialize() {
		user = new User("Stef", Arrays.asList(AccessRight.ADVANCE));
	}

	@Test
	public void testConstructor() {
		assertEquals("Stef", user.getName());
		assertEquals(AccessRight.ADVANCE, user.getAccessRights().get(0));
	}
	
	@Test(expected= IllegalArgumentException.class)
	public void testIllegalConstructor1(){
		new User("", Arrays.asList(AccessRight.ADVANCE));
	}

	@Test(expected= IllegalArgumentException.class)
	public void testIllegalConstructor2(){
		new User(null, Arrays.asList(AccessRight.ADVANCE));
	}
	
	@Test(expected= IllegalArgumentException.class)
	public void testIllegalConstructor3(){
		new User("Stef", null);
	}
	
	@Test
	public void testEqualsAndHashCode(){
		User user2 = new User("Stef", Arrays.asList(AccessRight.ASSEMBLE));
		assertNotEquals(user, user2);
		assertNotEquals(user.hashCode(), user2.hashCode());
		
		User user3 = new User("bla", Arrays.asList(AccessRight.ADVANCE));
		assertNotEquals(user, user3);
		assertNotEquals(user.hashCode(), user3.hashCode());
		
		User user4 = new User("Stef", Arrays.asList(AccessRight.ADVANCE));
		assertEquals(user, user4);
		assertEquals(user.hashCode(), user4.hashCode());
		
		assertEquals(user, user);
		assertNotEquals(user, null);
		assertNotEquals(user, new UserBook());
		
	}
}
