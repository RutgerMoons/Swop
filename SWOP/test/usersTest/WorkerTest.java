package usersTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import ui.AssembleFlowController;
import ui.OrderFlowController;
import ui.UseCaseFlowController;
import users.Worker;

public class WorkerTest {

	private Worker worker;
	@Before
	public void initialize(){
		worker = new Worker("Jos");
	}
	
	@Test
	public void TestConstructor(){
		assertEquals("Jos", worker.getName());
	}
	
	@Test
	public void TestGetRightHandler(){
		assertNull(worker.getRightHandler(new ArrayList<UseCaseFlowController>()));
		AssembleFlowController rightHandler = new AssembleFlowController(null, null);
		OrderFlowController wrongHandler = new OrderFlowController(null, null, null);
		ArrayList<UseCaseFlowController> useCaseHandlers = new ArrayList<>();
		useCaseHandlers.add(wrongHandler);
		useCaseHandlers.add(rightHandler);
		UseCaseFlowController returnHandler = worker.getRightHandler(useCaseHandlers);
		assertEquals(rightHandler, returnHandler);
	}

}
