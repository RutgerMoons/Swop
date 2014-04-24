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

public class CheckStatisticsScenario {

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
	}
	
	@Test
	public void noStatisticsAvailableTest(){
		List<String> statistics = facade.getStatistics();
		assertEquals(statistics.get(0),"0");
		assertEquals(statistics.get(1),"0");
		assertEquals(statistics.get(2),"0");
		assertEquals(statistics.get(3),"0");
		assertEquals(statistics.get(4),"0");
		assertEquals(statistics.get(5),"0");
		assertEquals(statistics.get(6),"none");
		assertEquals(statistics.get(7),"none");
		
	}
}
