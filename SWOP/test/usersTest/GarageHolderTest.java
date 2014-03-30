package usersTest;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import users.GarageHolder;

public class GarageHolderTest {

	private GarageHolder garageHolder;
	@Before
	public void initialize(){
		garageHolder = new GarageHolder("Jos", new ArrayList<String>(Arrays.asList(new String[] {"Order"})));
	}
	
	@Test
	public void TestConstructor(){
		assertEquals("Jos", garageHolder.getName());
		assertEquals("Order", garageHolder.getAccessRights().get(0));
	}

	@Test (expected = IllegalArgumentException.class)
	public void TestSetIllegalName1(){
		new GarageHolder("", new ArrayList<String>(Arrays.asList(new String[] {"Order"})));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void TestSetIllegalName2(){
		new GarageHolder(null, new ArrayList<String>(Arrays.asList(new String[] {"Order"})));
	}
	
}
