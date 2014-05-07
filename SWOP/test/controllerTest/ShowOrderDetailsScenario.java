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

import view.ClientCommunication;
import view.IClientCommunication;
import controller.OrderFlowController;
import controller.ShowOrderDetailsFlowController;
import domain.exception.UnmodifiableException;
import domain.facade.Facade;
import domain.restriction.BindingRestriction;
import domain.restriction.OptionalRestriction;
import domain.restriction.PartPicker;
import domain.users.AccessRight;
import domain.vehicle.VehicleOption;
import domain.vehicle.VehicleOptionCategory;
import domain.vehicle.VehicleSpecification;

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

	@Test
	public void showOrderDetails() throws IllegalArgumentException, UnmodifiableException{
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
