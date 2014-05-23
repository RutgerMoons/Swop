package domain.usersTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import domain.exception.RoleNotYetAssignedException;
import domain.users.AccessRight;
import domain.users.User;
import domain.users.UserBook;


public class UserBookTest {
	UserBook book;
	
	@Before
	public void initialize(){
		book = new UserBook();
		
	}

	@Test
	public void TestAddUser() {
		User user = new User("Stef", Arrays.asList(AccessRight.ORDER));
		book.addUser(user);
		assertEquals(user, book.getUserBook().get("Stef"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void TestAddSameUser(){
		UserBook book = new UserBook();
		book.addUser(new User("Stef", Arrays.asList(AccessRight.ORDER)));
		book.addUser(new User("Stef", Arrays.asList(AccessRight.ASSEMBLE)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void TestAddIllegalUser(){
		UserBook book = new UserBook();
		book.addUser(null);
	}
	
	@Test
	public void testLogin() throws RoleNotYetAssignedException{
		book.addUser(new User("Stef", Arrays.asList(AccessRight.ORDER)));
		book.login("Stef");
		assertEquals("Stef", book.getCurrentUser().getName());
	}
	
	@Test(expected = RoleNotYetAssignedException.class)
	public void testLoginFalse() throws RoleNotYetAssignedException{
		book.login("Stef");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testLoginFalse2() throws RoleNotYetAssignedException{
		book.login(null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testLogout(){
		book.addUser(new User("Stef", Arrays.asList(AccessRight.ORDER)));
		book.login("Stef");
		assertEquals("Stef", book.getCurrentUser().getName());
		book.logout();
		assertNull(book.getCurrentUser());
	}
	
	@Test
	public void createUser(){
		book.createUser("jos", "manager");
		assertTrue(book.getUserBook().containsKey("jos"));
	}
}
