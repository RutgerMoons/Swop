package uiTest;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import ui.ClientCommunication;
import ui.FlowControllerFactory;
import ui.IClientCommunication;
import ui.UseCaseFlowController;

public class FlowControllerFactoryTest {

	private IClientCommunication clientCommunication;
	private FlowControllerFactory factory;
	
	@Before
	public void setup() {
		clientCommunication = new ClientCommunication();
		factory = new FlowControllerFactory(clientCommunication);
		assertNotNull(factory);
	}
	
	
	
	@Test
	public void testCreateFlowControllers() {
		ArrayList<UseCaseFlowController> flowControllers = factory.createFlowControllers();
		assertNotNull(flowControllers);
		assertEquals(3, flowControllers.size());
		assertEquals("Advance assemblyline", flowControllers.get(0).getAccessRight());
	}

}
