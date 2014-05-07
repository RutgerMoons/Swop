package controllerTest;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import view.ClientCommunication;
import view.IClientCommunication;
import controller.OrderFlowController;
import domain.car.VehicleSpecification;
import domain.car.VehicleOption;
import domain.car.VehicleOptionCategory;
import domain.facade.Facade;
import domain.restriction.BindingRestriction;
import domain.restriction.OptionalRestriction;
import domain.restriction.PartPicker;
import domain.users.AccessRight;

public class AdaptSchedulingAlgorithmScenario {

	private Facade facade;
	private IClientCommunication ui;
	private OrderFlowController controller;
	private PartPicker picker;
	private Set<BindingRestriction> bindingRestrictions;
	private Set<OptionalRestriction> optionalRestrictions;
	
	@Before
	public void initialize(){
		this.initializeRestrictions();
		facade = new Facade(bindingRestrictions, optionalRestrictions);
		AccessRight accessRight = AccessRight.SWITCH_SCHEDULING_ALGORITHM;
		ui = new ClientCommunication();
		controller = new OrderFlowController(accessRight, ui, facade);
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
	public void testUseCaseToFifo() {
		assertTrue(this.facade.getCurrentSchedulingAlgorithmAsString().equalsIgnoreCase("fifo"));
		
		facade.switchToBatch(Collections.EMPTY_LIST);
		assertNotNull(facade);
		assertTrue(this.facade.getCurrentSchedulingAlgorithmAsString().equalsIgnoreCase("batch"));
		
		facade.switchToFifo();
		assertTrue(this.facade.getCurrentSchedulingAlgorithmAsString().equalsIgnoreCase("fifo"));
		assertNotNull(facade);
	}
}
