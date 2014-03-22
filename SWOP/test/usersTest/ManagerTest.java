package usersTest;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import ui.AdvanceAssemblyLineHandler;
import ui.OrderHandler;
import ui.UseCaseHandler;
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
		assertNull(manager.getRightHandler(new ArrayList<UseCaseHandler>()));
		AdvanceAssemblyLineHandler rightHandler = new AdvanceAssemblyLineHandler(null, null, null);
		OrderHandler wrongHandler = new OrderHandler(null, null, null);
		ArrayList<UseCaseHandler> useCaseHandlers = new ArrayList<>();
		useCaseHandlers.add(wrongHandler);
		useCaseHandlers.add(rightHandler);
		UseCaseHandler returnHandler = manager.getRightHandler(useCaseHandlers);
		assertEquals(rightHandler, returnHandler);
	}
}
