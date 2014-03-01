package usersTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import users.GarageHolder;

public class GarageHolderTest {

	private GarageHolder garageHolder;
	@Before
	public void initialize(){
		garageHolder = new GarageHolder("Jos");
	}
	
	@Test
	public void TestConstructor(){
		assertEquals("Jos", garageHolder.getName());
	}

}
