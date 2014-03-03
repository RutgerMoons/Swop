package usersTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import users.User;

public class UserTest {

	private User user;
	@Before
	public void initialize(){
		user = new User("Jos");
	}
	@Test
	public void TestConstructor() {
		assertEquals("Jos", user.getName());
	}

}
