package domain.carTest;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import domain.car.CarModelSpecification;
import domain.car.CarOption;
import domain.car.CarOptionCategogry;

public class CarModelTemplateTest {

	CarModelSpecification template;
	@Before
	public void initialize(){
		Set<CarOption> parts = new HashSet<>();
		parts.add(new CarOption("sport", CarOptionCategogry.BODY));
		
		parts.add(new CarOption("black", CarOptionCategogry.COLOR));
		parts.add(new CarOption("white", CarOptionCategogry.COLOR));
		
		parts.add(new CarOption("performance 2.5l V6", CarOptionCategogry.ENGINE));
		parts.add(new CarOption("ultra 3l V8", CarOptionCategogry.ENGINE));
	
		parts.add(new CarOption("6 Speed Manual", CarOptionCategogry.GEARBOX));
		
		parts.add(new CarOption("Leather White", CarOptionCategogry.SEATS));
		parts.add(new CarOption("Leather Black", CarOptionCategogry.SEATS));
		
		parts.add(new CarOption("Manual", CarOptionCategogry.AIRCO));
		parts.add(new CarOption("Automatic", CarOptionCategogry.AIRCO));
		
		parts.add(new CarOption("Winter", CarOptionCategogry.WHEEL));
		parts.add(new CarOption("Sports", CarOptionCategogry.WHEEL));
		
		parts.add(new CarOption("high", CarOptionCategogry.SPOILER));
		parts.add(new CarOption("low", CarOptionCategogry.SPOILER));
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
