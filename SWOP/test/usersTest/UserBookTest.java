package usersTest;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import users.Manager;
import users.UserBook;
import users.Worker;

public class UserBookTest {


	@Test
	public void TestAddUser() {
		UserBook book = new UserBook();
		Manager mgr = new Manager("Stef");
		book.addUser(mgr);
		assertEquals(1, book.getUserBook().size());
		assertEquals(mgr, book.getUserBook().get("Stef"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void TestAddSameUser(){
		UserBook book = new UserBook();
		book.addUser(new Manager("Stef"));
		book.addUser(new Worker("Stef"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void TestAddIllegalUser(){
		UserBook book = new UserBook();
		book.addUser(null);
	}
}
