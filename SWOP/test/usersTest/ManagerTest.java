package usersTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import users.Manager;

public class ManagerTest {

	private Manager manager;
	
	@Before
	public void initialize(){
		manager = new Manager("Jos");
	}

	@Test
	public void TestConstructor(){
		assertEquals("Jos", manager.getName());
	}
}
