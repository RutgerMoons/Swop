package usersTest;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import ui.AdvanceAssemblyLineFlowController;
import ui.OrderFlowController;
import ui.UseCaseFlowController;
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
		assertNull(garageHolder.getRightHandler(new ArrayList<UseCaseFlowController>()));
		OrderFlowController rightHandler = new OrderFlowController(null, null, null);
		AdvanceAssemblyLineFlowController  wrongHandler = new AdvanceAssemblyLineFlowController(null, null, null);
		ArrayList<UseCaseFlowController> useCaseHandlers = new ArrayList<>();
		useCaseHandlers.add(wrongHandler);
		useCaseHandlers.add(rightHandler);
		UseCaseFlowController returnHandler = garageHolder.getRightHandler(useCaseHandlers);
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
