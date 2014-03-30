package usersTest;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import users.Manager;

public class ManagerTest {

	private Manager manager;
	
	@Before
	public void initialize(){
		manager = new Manager("Jos", new ArrayList<String>(Arrays.asList(new String[] {"Advance assemblyline"})));
	}

	@Test
	public void TestConstructor(){
		assertEquals("Jos", manager.getName());
		assertEquals("Advance assemblyline", manager.getAccessRights().get(0));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void TestSetIllegalName1(){
		new Manager("", new ArrayList<String>(Arrays.asList(new String[] {"Advance assemblyline"})));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void TestSetIllegalName2(){
		new Manager(null, new ArrayList<String>(Arrays.asList(new String[] {"Advance assemblyline"})));
	}
}
