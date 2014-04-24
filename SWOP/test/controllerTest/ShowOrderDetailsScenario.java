package controllerTest;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
import controller.ShowOrderDetailsFlowController;
import domain.car.CarModelSpecification;
import domain.car.CarOption;
import domain.car.CarOptionCategory;
import domain.exception.ImmutableException;
import domain.facade.Facade;
import domain.restriction.BindingRestriction;
import domain.restriction.OptionalRestriction;
import domain.restriction.PartPicker;
import domain.users.AccessRight;

@RunWith(Parameterized.class)
public class ShowOrderDetailsScenario {

	private Facade facade;
	private PartPicker picker;
	private Set<BindingRestriction> bindingRestrictions;
	private Set<OptionalRestriction> optionalRestrictions;
	private IClientCommunication clientCommunication;
	private OrderFlowController order;
	private ShowOrderDetailsFlowController controller;
	
	public ShowOrderDetailsScenario(IClientCommunication ui) {
		this.clientCommunication = ui;
	}

	@Before
	public void initialize(){
		this.initializeRestrictions();

		facade = new Facade(bindingRestrictions, optionalRestrictions);
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
	public void showOrderDetails() throws IllegalArgumentException, ImmutableException{
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
				+ "5" + s
				+ "Y" + s // approvement of order step 6
				+ "N" + s; // end of flow
		InputStream in1 = new ByteArrayInputStream(input1.getBytes());
		System.setIn(in1);
		clientCommunication = new ClientCommunication();
		AccessRight accessRight = AccessRight.ORDER;
		order = new OrderFlowController(accessRight, clientCommunication, facade);
		order.placeNewOrder();
		controller = new ShowOrderDetailsFlowController(AccessRight.SHOWDETAILS, clientCommunication, facade);
		controller.checkOrderDetails();
		System.out.println("details");
		String detail = "5 model A Estimated completion time: 0 days and 5 hours and 50 minutes";
		assertTrue(facade.getOrderDetails(detail).contains("Orderdetails:"));
		assertTrue(facade.getOrderDetails(detail).contains("5 model A"));
		assertTrue(facade.getOrderDetails(detail).contains("Order Time: day 0, 0 hours, 0 minutes."));
		assertTrue(facade.getOrderDetails(detail).contains("(Expected) Completion Time: day 0, 5 hours, 50 minutes."));

	}

	@Parameterized.Parameters
	public static Collection<Object[]> primeNumbers() {
		return Arrays.asList(new Object[][] { { new ClientCommunication() } });
	}

}
