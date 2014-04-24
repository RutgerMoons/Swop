package controllerTest;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ui.ClientCommunication;
import ui.IClientCommunication;
import controller.OrderFlowController;
import domain.car.CarModelSpecification;
import domain.car.CarOption;
import domain.car.CarOptionCategory;
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