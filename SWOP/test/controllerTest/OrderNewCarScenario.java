package controllerTest;

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
import controller.OrderFlowController;
import domain.car.CarModelSpecification;
import domain.car.CarOption;
import domain.car.CarOptionCategory;
import domain.exception.ImmutableException;
import domain.facade.Facade;
import domain.restriction.BindingRestriction;
import domain.restriction.OptionalRestriction;
import domain.restriction.PartPicker;
import domain.users.AccessRight;

/**
 * Scenario test for checking use case 4.1.
 * 
 */
@RunWith(Parameterized.class)
public class OrderNewCarScenario {

	private Facade facade;
	private IClientCommunication ui;
	private OrderFlowController controller;
	private PartPicker picker;
	private Set<BindingRestriction> bindingRestrictions;
	private Set<OptionalRestriction> optionalRestrictions;
	
	public OrderNewCarScenario(IClientCommunication ui) {
		this.ui = ui;
	}
	
	@Before
	public void initialize(){
		this.initializeRestrictions();
		facade = new Facade(bindingRestrictions, optionalRestrictions);
		AccessRight accessRight = AccessRight.ORDER;
		controller = new OrderFlowController(accessRight, ui, facade);
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

	@Test
	public void TestUseCaseAcceptOrder() {
		facade.createAndAddUser("Luigi", "garageholder");		
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
		ByteArrayOutputStream myout = new ByteArrayOutputStream();
		System.setOut(new PrintStream(myout));
		ui = new ClientCommunication();
		AccessRight accessRight = AccessRight.ORDER;
		controller = new OrderFlowController(accessRight, ui, facade);
		try {
			controller.executeUseCase();
		} catch (IllegalArgumentException e1) {
		} catch (ImmutableException e1) {
		}		
		String output = myout.toString();
		assertTrue(output.contains("You have no pending Orders"));
		assertTrue(output.contains("You have no completed Orders"));
		assertTrue(output.contains("Do you want to continue? Y/N"));
		assertTrue(output.contains("Possible models:"));
		assertTrue(output.contains("model A"));
		assertTrue(output.contains("model B"));
		assertTrue(output.contains("model C"));
		assertTrue(output.contains("Which model do you want to order?"));
		assertTrue(output.contains("Possible parts:"));
		assertTrue(output.contains("1.SEATS: vinyl grey"));
		assertTrue(output.contains("2.SEATS: leather white"));
		assertTrue(output.contains("3.SEATS: leather black"));
		assertTrue(output.contains("Which Part Number do you choose?"));
		assertTrue(output.contains("Possible parts:"));
		assertTrue(output.contains("1.BODY: sedan"));
		assertTrue(output.contains("2.BODY: break"));
		assertTrue(output.contains("Which Part Number do you choose?"));
		assertTrue(output.contains("Possible parts:"));
		assertTrue(output.contains("1.GEARBOX: 5 speed automatic"));
		assertTrue(output.contains("2.GEARBOX: 6 speed manual"));
		assertTrue(output.contains("3.GEARBOX: 5 speed manual"));
		assertTrue(output.contains("Which Part Number do you choose?"));
		assertTrue(output.contains("Possible parts:"));
		assertTrue(output.contains("1.COLOR: black"));
		assertTrue(output.contains("2.COLOR: blue"));
		assertTrue(output.contains(".COLOR: white"));
		assertTrue(output.contains(".COLOR: red"));
		assertTrue(output.contains("Which Part Number do you choose?"));
		assertTrue(output.contains("Possible parts:"));
		assertTrue(output.contains("1.ENGINE: standard 2l V4"));
		assertTrue(output.contains("2.ENGINE: performance 2.5l V6"));
		assertTrue(output.contains("Which Part Number do you choose?"));
		assertTrue(output.contains("Possible parts:"));
		assertTrue(output.contains("1.WHEEL: sports"));
		assertTrue(output.contains("2.WHEEL: winter"));
		assertTrue(output.contains("3.WHEEL: comfort"));
		assertTrue(output.contains("Which Part Number do you choose?" ));
		assertTrue(output.contains("Possible parts:"));
		assertTrue(output.contains("1.AIRCO: manual"));
		assertTrue(output.contains("2.Select nothing"));
		assertTrue(output.contains("Which Part Number do you choose?"));
		assertTrue(output.contains("How many cars do you want to order?"));
		assertTrue(output.contains("Your order:" +s+"1 model A" +s+s+"Your chosen parts:"+s+s+"SEATS: vinyl grey" +s+"BODY: sedan"
				+s+"GEARBOX: 5 speed automatic" +s+"COLOR: black" +s +"ENGINE: standard 2l V4"+s+"WHEEL: sports" 
				+ s + "AIRCO: manual" +s+s+"Do you want to continue? Y/N"+s+"Your order:" +s
				+ "1 model A Estimated completion time: day 0, 2 hours, 30 minutes."+s +s+ "Your chosen parts:" +s+
				s+ "SEATS: vinyl grey" +s+ "BODY: sedan" + s + "GEARBOX: 5 speed automatic"+s+"COLOR: black" + 
				s + "ENGINE: standard 2l V4" +s + "WHEEL: sports" +s + "AIRCO: manual"+s+s ));
	}

	@Test
	public void useCaseTestDeclineOrder() {
		facade.createAndAddUser("mario", "garageholder");		
		String s = System.lineSeparator();
		String input2 = "Y" + s // step 2, user wants to place an order
						+ "model A" + s // step 4, user chooses a car model
						+ "1" + s // step 6 completing of ordering form
						+ "1" + s // step 6 completing of ordering form
						+ "1" + s // step 6 completing of ordering form
						+ "1" + s // step 6 completing of ordering form
						+ "1" + s // step 6 completing of ordering form
						+ "1" + s // step 6 completing of ordering form
						+ "1" + s
						+ "1" + s
						+ "N" + s // approvement of order step 6
						+ "N" + s; // end of flow
		InputStream in2 = new ByteArrayInputStream(input2.getBytes());
		System.setIn(in2);
		ByteArrayOutputStream myout = new ByteArrayOutputStream();
		System.setOut(new PrintStream(myout));
		ui = new ClientCommunication();
		AccessRight accessRight = AccessRight.ORDER;
		controller = new OrderFlowController(accessRight, ui, facade);
		try {
			controller.executeUseCase();
		} catch (IllegalArgumentException e1) {
			System.out.println("here");
		} catch (ImmutableException e1) {
			System.out.println("here");
		}		
		String output = myout.toString();
		assertTrue(output.contains("You have no pending Orders"));
		assertTrue(output.contains("You have no pending Orders"));
		assertTrue(output.contains("You have no completed Orders"));
		assertTrue(output.contains("Do you want to continue? Y/N"));
		assertTrue(output.contains("Possible models:"));
		assertTrue(output.contains("model A"));
		assertTrue(output.contains("model B"));
		assertTrue(output.contains("model C"));
		assertTrue(output.contains("Which model do you want to order?"));
		assertTrue(output.contains("Possible parts:"));
		assertTrue(output.contains("1.SEATS: vinyl grey"));
		assertTrue(output.contains("2.SEATS: leather white"));
		assertTrue(output.contains("3.SEATS: leather black"));
		assertTrue(output.contains("Which Part Number do you choose?"));
		assertTrue(output.contains("Possible parts:"));
		assertTrue(output.contains("1.BODY: sedan"));
		assertTrue(output.contains("2.BODY: break"));
		assertTrue(output.contains("Which Part Number do you choose?"));
		assertTrue(output.contains("Possible parts:"));
		assertTrue(output.contains("1.GEARBOX: 5 speed automatic"));
		assertTrue(output.contains("2.GEARBOX: 6 speed manual"));
		assertTrue(output.contains("3.GEARBOX: 5 speed manual"));
		assertTrue(output.contains("Which Part Number do you choose?"));
		assertTrue(output.contains("Possible parts:"));
		assertTrue(output.contains("1.COLOR: black"));
		assertTrue(output.contains("2.COLOR: blue"));
		assertTrue(output.contains(".COLOR: white"));
		assertTrue(output.contains(".COLOR: red"));
		assertTrue(output.contains("Which Part Number do you choose?"));
		assertTrue(output.contains("Possible parts:"));
		assertTrue(output.contains("1.ENGINE: standard 2l V4"));
		assertTrue(output.contains("2.ENGINE: performance 2.5l V6"));
		assertTrue(output.contains("Which Part Number do you choose?"));
		assertTrue(output.contains("Possible parts:"));
		assertTrue(output.contains("1.WHEEL: sports"));
		assertTrue(output.contains("2.WHEEL: winter"));
		assertTrue(output.contains("3.WHEEL: comfort"));
		assertTrue(output.contains("Which Part Number do you choose?" ));
		assertTrue(output.contains("Possible parts:"));
		assertTrue(output.contains("1.AIRCO: manual"));
		assertTrue(output.contains("2.Select nothing"));
		assertTrue(output.contains("Which Part Number do you choose?"));
		assertTrue(output.contains("How many cars do you want to order?"));
		assertTrue(output.contains("Your order:" +s+"1 model A" +s+s+"Your chosen parts:"+s+s+"SEATS: vinyl grey" +s+"BODY: sedan"
				+s+"GEARBOX: 5 speed automatic" +s+"COLOR: black" +s +"ENGINE: standard 2l V4"+s+"WHEEL: sports" 
				+ s + "AIRCO: manual" +s+s+"Do you want to continue? Y/N"+s));
	}

	@Parameterized.Parameters
	public static Collection<Object[]> primeNumbers() {
		return Arrays.asList(new Object[][] { { new ClientCommunication() } });
	}

}
