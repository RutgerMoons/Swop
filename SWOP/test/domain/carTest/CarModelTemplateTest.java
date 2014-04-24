package domain.carTest;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import domain.car.CarModelSpecification;
import domain.car.CarOption;
import domain.car.CarOptionCategory;

public class CarModelTemplateTest {

	CarModelSpecification template;
	@Before
	public void initialize(){
		Set<CarOption> parts = new HashSet<>();
		parts.add(new CarOption("sport", CarOptionCategory.BODY));
		
		parts.add(new CarOption("black", CarOptionCategory.COLOR));
		parts.add(new CarOption("white", CarOptionCategory.COLOR));
		
		parts.add(new CarOption("performance 2.5l V6", CarOptionCategory.ENGINE));
		parts.add(new CarOption("ultra 3l V8", CarOptionCategory.ENGINE));
	
		parts.add(new CarOption("6 Speed Manual", CarOptionCategory.GEARBOX));
		
		parts.add(new CarOption("Leather White", CarOptionCategory.SEATS));
		parts.add(new CarOption("Leather Black", CarOptionCategory.SEATS));
		
		parts.add(new CarOption("Manual", CarOptionCategory.AIRCO));
		parts.add(new CarOption("Automatic", CarOptionCategory.AIRCO));
		
		parts.add(new CarOption("Winter", CarOptionCategory.WHEEL));
		parts.add(new CarOption("Sports", CarOptionCategory.WHEEL));
		
		parts.add(new CarOption("high", CarOptionCategory.SPOILER));
		parts.add(new CarOption("low", CarOptionCategory.SPOILER));
		template = new CarModelSpecification("model", parts, 60);
	}

	@Test
	public void testConstructor() {
		assertEquals("model", template.getDescription());
		assertEquals("model", template.toString());
		assertEquals(60, template.getTimeAtWorkBench());
		
		assertEquals(8, template.getCarParts().keySet().size());
		assertEquals(14, template.getCarParts().values().size());
	}

}
