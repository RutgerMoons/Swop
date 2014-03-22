package usersTest;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import ui.AdvanceAssemblyLineHandler;
import ui.OrderHandler;
import ui.UseCaseHandler;
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
	
	@Test
	public void TestGetRightHandler(){
		assertNull(garageHolder.getRightHandler(new ArrayList<UseCaseHandler>()));
		OrderHandler rightHandler = new OrderHandler(null, null, null);
		AdvanceAssemblyLineHandler  wrongHandler = new AdvanceAssemblyLineHandler(null, null, null);
		ArrayList<UseCaseHandler> useCaseHandlers = new ArrayList<>();
		useCaseHandlers.add(wrongHandler);
		useCaseHandlers.add(rightHandler);
		UseCaseHandler returnHandler = garageHolder.getRightHandler(useCaseHandlers);
		assertEquals(rightHandler, returnHandler);
	}

	@Test (expected = IllegalArgumentException.class)
	public void TestSetIllegalName1(){
		new GarageHolder("");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void TestSetIllegalName2(){
		new GarageHolder(null);
	}
	
}
