package tests.usersTest;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import code.assembly.WorkBench;
import code.ui.AdvanceAssemblyLineHandler;
import code.ui.AssembleHandler;
import code.ui.OrderHandler;
import code.ui.UseCaseHandler;
import code.users.Worker;

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
		assertNull(worker.getRightHandler(new ArrayList<UseCaseHandler>()));
		AssembleHandler rightHandler = new AssembleHandler(null, null);
		OrderHandler wrongHandler = new OrderHandler(null, null, null);
		ArrayList<UseCaseHandler> useCaseHandlers = new ArrayList<>();
		useCaseHandlers.add(wrongHandler);
		useCaseHandlers.add(rightHandler);
		UseCaseHandler returnHandler = worker.getRightHandler(useCaseHandlers);
		assertEquals(rightHandler, returnHandler);
	}

}
