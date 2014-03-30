package usersTest;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import users.Manager;
import users.UserBook;
import users.Worker;

public class UserBookTest {


	@Test
	public void TestAddUser() {
		UserBook book = new UserBook();
		Manager mgr = new Manager("Stef", new ArrayList<String>(Arrays.asList(new String[] {"Advance assemblyline"})));
		book.addUser(mgr);
		assertEquals(1, book.getUserBook().size());
		assertEquals(mgr, book.getUserBook().get("Stef"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void TestAddSameUser(){
		UserBook book = new UserBook();
		book.addUser(new Manager("Stef", new ArrayList<String>(Arrays.asList(new String[] {"Advance assemblyline"}))));
		book.addUser(new Worker("Stef", new ArrayList<String>(Arrays.asList(new String[] {"Advance assemblyline"}))));
	}

	@Test(expected = IllegalArgumentException.class)
	public void TestAddIllegalUser(){
		UserBook book = new UserBook();
		book.addUser(null);
	}
}
