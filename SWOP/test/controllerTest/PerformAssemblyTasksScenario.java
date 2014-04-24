package controllerTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ui.ClientCommunication;
import ui.IClientCommunication;
import controller.AssembleFlowController;
import controller.OrderFlowController;
import domain.car.CarModelSpecification;
import domain.car.CarOption;
import domain.car.CarOptionCategory;
import domain.exception.ImmutableException;
import domain.exception.NoSuitableJobFoundException;
import domain.facade.Facade;
import domain.restriction.BindingRestriction;
import domain.restriction.OptionalRestriction;
import domain.restriction.PartPicker;
import domain.users.AccessRight;
/**
 * Scenario test for checking use case 4.3
 *
 */
@RunWith(Parameterized.class)
public class PerformAssemblyTasksScenario {
	
	private IClientCommunication clientCommunication;
	private AssembleFlowController controller;
	private OrderFlowController order;
	private PartPicker picker;
	private Set<BindingRestriction> bindingRestrictions;
	private Set<OptionalRestriction> optionalRestrictions;
	private Facade facade;

	public PerformAssemblyTasksScenario(IClientCommunication ui) {
		this.clientCommunication = ui;
	}

	@Before
	public void initialize() {
		this.initializeRestrictions();
		facade = new Facade(bindingRestrictions, optionalRestrictions);
		AccessRight accessRight = AccessRight.ASSEMBLE;
		controller = new AssembleFlowController(accessRight, clientCommunication, facade);
		order = new OrderFlowController(AccessRight.ORDER, clientCommunication, facade);
	}
	
	@Test
	public void PerformUseCase() throws ImmutableException, NoSuitableJobFoundException{
		//placement of an order
		facade.createAndAddUser("Mario", "garageholder");
		String s = System.lineSeparator();
		String input1 = "Y" + s // step 2, user wants to place an order
						+ "model A" + s // step 4, user chooses a car model
						+ "1" + s // step 6 completing of ordering form
						+ "1" + s // step 6 completing of ordering form
						+ "1" + s // step 6 completing of ordering form
						+ "1" + s // step 6 completing of ordering form
						+ "1" + s // step 6 completing of ordering form
						+ "1" + s // step 6 completing of ordering form
						+ "1" + s
						+ "1" + s
						+ "Y" + s // approvement of order step 6
						+ "N" + s; // end of flow
		InputStream in1 = new ByteArrayInputStream(input1.getBytes());
		System.setIn(in1);
		clientCommunication = new ClientCommunication();
		AccessRight accessRight = AccessRight.ORDER;
		order = new OrderFlowController(accessRight, clientCommunication, facade);
		order.placeNewOrder();
		
		// working at a workbench
		facade.createAndAddUser("Luigi", "worker");
		
		assertTrue("car body".equalsIgnoreCase(facade.getWorkBenchNames().get(0)));
		assertTrue("drivetrain".equalsIgnoreCase(facade.getWorkBenchNames().get(1)));
		assertTrue("accessories".equalsIgnoreCase(facade.getWorkBenchNames().get(2)));
		// Worker wants to work at the first workbench
		assertTrue(facade.getTasksOfChosenWorkBench(0).contains(
				"Paint,Required actions: 1.Put on black color"));
		assertTrue(facade.getTasksOfChosenWorkBench(0).
				contains("Assembly,Required actions: 1.Put on sedan body"));
		
		// Worker has completed the first task at his workbench. 40 minutes has elapsed
		int time = 40;
		facade.completeChosenTaskAtChosenWorkBench(0, 0, time);
		// assemblyLine will advance if needed		
	}
	
	
	
	@Test
	public void PerformUseCaseAlternateFlow(){
		String s = System.lineSeparator();
		ByteArrayOutputStream myout = new ByteArrayOutputStream();
		System.setOut(new PrintStream(myout));
		String input = "1" // The workbench the worker is residing at, step 2
						+ s 
						+ "N" // Worker does not choose to continue, step 8a
						+ s;
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		clientCommunication = new ClientCommunication();
		controller = new AssembleFlowController(AccessRight.ASSEMBLE, clientCommunication, facade);
		controller.executeUseCase();
		String output = myout.toString();
		assertEquals(
				"Workbenches:"+ s //Status of the workbenches, step 1
				+ "1: car body" + s 
				+ "2: drivetrain" + s 
				+ "3: accessories" + s + s 
				+ "What's the number of the workbench you're currently residing at?" + s //Step 1
				+ "All the tasks at this workbench are completed" + s + s // step 7, updated vieuw of the workbench
				+ "Do you want to continue? Y/N" + s, // step 8
				output);
		// flow stops here
	}
	

	public void initializeRestrictions(){
		bindingRestrictions = new HashSet<>();
		optionalRestrictions = new HashSet<>();
		optionalRestrictions.add(new OptionalRestriction(new CarOption("sport", CarOptionCategory.BODY), CarOptionCategory.SPOILER, false));
		
		bindingRestrictions.add(new BindingRestriction(new CarOption("sport", CarOptionCategory.BODY), new CarOption("performance 2.5l V6", CarOptionCategory.ENGINE)));
		bindingRestrictions.add(new BindingRestriction(new CarOption("sport", CarOptionCategory.BODY), new CarOption("ultra 3l V8", CarOptionCategory.ENGINE)));
		
		bindingRestrictions.add(new BindingRestriction(new CarOption("ultra 3l V8", CarOptionCategory.ENGINE), new CarOption("manual", CarOptionCategory.AIRCO)));
		
		picker = new PartPicker(bindingRestrictions, optionalRestrictions);
		
		Set<CarOption> parts = new HashSet<>();
		parts.add(new CarOption("sport", CarOptionCategory.BODY));
		
		parts.add(new CarOption("black", CarOptionCategory.COLOR));
		parts.add(new CarOption("white", CarOptionCategory.COLOR));
		
		parts.add(new CarOption("performance 2.5l V6", CarOptionCategory.ENGINE));
		parts.add(new CarOption("ultra 3l V8", CarOptionCategory.ENGINE));
	
		parts.add(new CarOption("6 speed manual", CarOptionCategory.GEARBOX));
		
		parts.add(new CarOption("leather white", CarOptionCategory.SEATS));
		parts.add(new CarOption("leather black", CarOptionCategory.SEATS));
		
		parts.add(new CarOption("manual", CarOptionCategory.AIRCO));
		parts.add(new CarOption("automatic", CarOptionCategory.AIRCO));
		
		parts.add(new CarOption("winter", CarOptionCategory.WHEEL));
		parts.add(new CarOption("sports", CarOptionCategory.WHEEL));
		
		parts.add(new CarOption("high", CarOptionCategory.SPOILER));
		parts.add(new CarOption("low", CarOptionCategory.SPOILER));
		CarModelSpecification template = new CarModelSpecification("model", parts, 60);
		picker.setNewModel(template);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> primeNumbers() {
		return Arrays.asList(new Object[][] { { new ClientCommunication() } });
	}
}
