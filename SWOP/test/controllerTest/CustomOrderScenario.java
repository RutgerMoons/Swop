package controllerTest;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import domain.car.CarOption;
import domain.car.CarOptionCategory;
import domain.facade.Facade;
import domain.restriction.BindingRestriction;
import domain.restriction.OptionalRestriction;

public class CustomOrderScenario {

	private Facade facade;
	
	@Before
	public void initialize(){
		Set<BindingRestriction> bindingRestrictions = new HashSet<>();
		Set<OptionalRestriction> optionalRestrictions = new HashSet<>();
		optionalRestrictions.add(new OptionalRestriction(new CarOption("sport", CarOptionCategory.BODY), CarOptionCategory.SPOILER, false));
		
		bindingRestrictions.add(new BindingRestriction(new CarOption("sport", CarOptionCategory.BODY), new CarOption("performance 2.5l V6", CarOptionCategory.ENGINE)));
		bindingRestrictions.add(new BindingRestriction(new CarOption("sport", CarOptionCategory.BODY), new CarOption("ultra 3l V8", CarOptionCategory.ENGINE)));
		
		bindingRestrictions.add(new BindingRestriction(new CarOption("ultra 3l V8", CarOptionCategory.ENGINE), new CarOption("manual", CarOptionCategory.AIRCO)));
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
