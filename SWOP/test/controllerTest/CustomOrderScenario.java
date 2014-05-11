package controllerTest;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import domain.facade.Facade;
import domain.restriction.BindingRestriction;
import domain.restriction.OptionalRestriction;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

public class CustomOrderScenario {

	private Facade facade;
	
	@Before
	public void initialize(){
		Set<BindingRestriction> bindingRestrictions = new HashSet<>();
		Set<OptionalRestriction> optionalRestrictions = new HashSet<>();
		optionalRestrictions.add(new OptionalRestriction(new VehicleOption("sport", VehicleOptionCategory.BODY), VehicleOptionCategory.SPOILER, false));
		
		bindingRestrictions.add(new BindingRestriction(new VehicleOption("sport", VehicleOptionCategory.BODY), new VehicleOption("performance 2.5l V6", VehicleOptionCategory.ENGINE)));
		bindingRestrictions.add(new BindingRestriction(new VehicleOption("sport", VehicleOptionCategory.BODY), new VehicleOption("ultra 3l V8", VehicleOptionCategory.ENGINE)));
		
		bindingRestrictions.add(new BindingRestriction(new VehicleOption("ultra 3l V8", VehicleOptionCategory.ENGINE), new VehicleOption("manual", VehicleOptionCategory.AIRCO)));
		this.facade = new Facade(bindingRestrictions, optionalRestrictions);
		
		facade.createAndAddUser("c", "custom car shop manager");
	}
	
	@Test
	public void customOrderTest(){		
		List<String> allCustomTasks = facade.getCustomTasks();
		assertTrue(allCustomTasks.contains("installing custom seats"));
		assertTrue(allCustomTasks.contains("spraying car bodies"));
		
		String customTaskSpraying = "spraying car bodies";

		List<String> specificTasksSpraying= facade.getSpecificCustomTasks(customTaskSpraying);
		assertTrue(specificTasksSpraying.contains("COLOR: white"));
		assertTrue(specificTasksSpraying.contains("COLOR: black"));
		assertTrue(specificTasksSpraying.contains("COLOR: yellow"));
		assertTrue(specificTasksSpraying.contains("COLOR: red"));
		assertTrue(specificTasksSpraying.contains("COLOR: blue"));
		assertTrue(specificTasksSpraying.contains("COLOR: green"));
		
		
		String customTaskInstalling = "installing custom seats";
		List<String> specificTasksInstalling= facade.getSpecificCustomTasks(customTaskInstalling);
		assertTrue(specificTasksInstalling.contains("SEATS: custom"));
		
		String model = "SEATS: custom";
		String deadline = "1,2,3";
		
		String time = facade.processCustomOrder(model, deadline);
		assertEquals(time,"day 1, 2 hours, 3 minutes.");
	}
	
}
