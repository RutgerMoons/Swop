package usersTest;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import ui.AdvanceAssemblyLineFlowController;
import ui.OrderFlowController;
import ui.UseCaseFlowController;
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
	
	@Test
	public void TestGetRightHandler(){
		assertNull(manager.getRightHandler(new ArrayList<UseCaseFlowController>()));
		AdvanceAssemblyLineFlowController rightHandler = new AdvanceAssemblyLineFlowController(null, null, null);
		OrderFlowController wrongHandler = new OrderFlowController(null, null, null);
		ArrayList<UseCaseFlowController> useCaseHandlers = new ArrayList<>();
		useCaseHandlers.add(wrongHandler);
		useCaseHandlers.add(rightHandler);
		UseCaseFlowController returnHandler = manager.getRightHandler(useCaseHandlers);
		assertEquals(rightHandler, returnHandler);
	}
}
