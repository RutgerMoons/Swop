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

import view.ClientCommunication;
import view.IClientCommunication;
import controller.AssembleFlowController;
import controller.OrderFlowController;
import domain.exception.UnmodifiableException;
import domain.exception.NoSuitableJobFoundException;
import domain.facade.Facade;
import domain.restriction.BindingRestriction;
import domain.restriction.OptionalRestriction;
import domain.restriction.PartPicker;
import domain.users.AccessRight;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;
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
	public void PerformUseCase() throws UnmodifiableException, NoSuitableJobFoundException{
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
		
		assertTrue("car body".equalsIgnoreCase(facade.getWorkbenches().get(0)));
		assertTrue("drivetrain".equalsIgnoreCase(facade.getWorkbenches().get(1)));
		assertTrue("accessories".equalsIgnoreCase(facade.getWorkbenches().get(2)));
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
		optionalRestrictions.add(new OptionalRestriction(new VehicleOption("sport", VehicleOptionCategory.BODY), VehicleOptionCategory.SPOILER, false));
		
		bindingRestrictions.add(new BindingRestriction(new VehicleOption("sport", VehicleOptionCategory.BODY), new VehicleOption("performance 2.5l V6", VehicleOptionCategory.ENGINE)));
		bindingRestrictions.add(new BindingRestriction(new VehicleOption("sport", VehicleOptionCategory.BODY), new VehicleOption("ultra 3l V8", VehicleOptionCategory.ENGINE)));
		
		bindingRestrictions.add(new BindingRestriction(new VehicleOption("ultra 3l V8", VehicleOptionCategory.ENGINE), new VehicleOption("manual", VehicleOptionCategory.AIRCO)));
		
		picker = new PartPicker(bindingRestrictions, optionalRestrictions);
		
		Set<VehicleOption> parts = new HashSet<>();
		parts.add(new VehicleOption("sport", VehicleOptionCategory.BODY));
		
		parts.add(new VehicleOption("black", VehicleOptionCategory.COLOR));
		parts.add(new VehicleOption("white", VehicleOptionCategory.COLOR));
		
		parts.add(new VehicleOption("performance 2.5l V6", VehicleOptionCategory.ENGINE));
		parts.add(new VehicleOption("ultra 3l V8", VehicleOptionCategory.ENGINE));
	
		parts.add(new VehicleOption("6 speed manual", VehicleOptionCategory.GEARBOX));
		
		parts.add(new VehicleOption("leather white", VehicleOptionCategory.SEATS));
		parts.add(new VehicleOption("leather black", VehicleOptionCategory.SEATS));
		
		parts.add(new VehicleOption("manual", VehicleOptionCategory.AIRCO));
		parts.add(new VehicleOption("automatic", VehicleOptionCategory.AIRCO));
		
		parts.add(new VehicleOption("winter", VehicleOptionCategory.WHEEL));
		parts.add(new VehicleOption("sports", VehicleOptionCategory.WHEEL));
		
		parts.add(new VehicleOption("high", VehicleOptionCategory.SPOILER));
		parts.add(new VehicleOption("low", VehicleOptionCategory.SPOILER));
		VehicleSpecification template = new VehicleSpecification("model", parts, 60);
		picker.setNewModel(template);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> primeNumbers() {
		return Arrays.asList(new Object[][] { { new ClientCommunication() } });
	}
}
