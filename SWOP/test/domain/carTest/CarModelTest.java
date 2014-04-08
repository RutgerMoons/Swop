package domain.carTest;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import domain.car.CarModel;
import domain.car.CarModelSpecification;
import domain.car.CarOption;
import domain.car.CarOptionCategogry;
import domain.exception.AlreadyInMapException;
import domain.job.Action;

public class CarModelTest {
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
		CarModel model = new CarModel(template);
		assertEquals(template, model.getSpecification());
		assertNotNull(model.getCarParts());
		assertNotNull(model.getForcedOptionalTypes());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidConstructor() {
		new CarModel(null);
	}

	@Test
	public void testAdd() throws AlreadyInMapException {
		CarModel car = new CarModel(template);
		CarOption part = new CarOption("manual", CarOptionCategogry.AIRCO);
		car.addCarPart(part);
		assertEquals(part, car.getCarParts().get(part.getType()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalAdd1() throws AlreadyInMapException {
		CarModel car = new CarModel(template);

		car.addCarPart(null);

	}

	@Test(expected = AlreadyInMapException.class)
	public void testIllegalAdd2() throws AlreadyInMapException {
		CarModel car = new CarModel(template);
		CarOption part1 = new CarOption("manual", CarOptionCategogry.AIRCO);
		CarOption part2 = new CarOption("automatic", CarOptionCategogry.AIRCO);

		car.addCarPart(part1);
		car.addCarPart(part2);

	}
	
	@Test
	public void testToString(){
		CarModel car1 = new CarModel(template);
		assertEquals("model", car1.toString());
	}

	@Test
	public void testEquals() throws AlreadyInMapException {
		CarModel car1 = new CarModel(template);
		CarOption part1 = new CarOption("manual", CarOptionCategogry.AIRCO);
		CarOption part2 = new CarOption("break", CarOptionCategogry.BODY);
		car1.addCarPart(part1);
		car1.addCarPart(part2);
		
		CarModel car2 = new CarModel(template);
		assertNotEquals(car1, car2);
		
		CarModel car3 = new CarModel(template);
		assertNotEquals(car1, car3);
		assertNotEquals(car1.hashCode(), car3.hashCode());
		
		car3.addCarPart(part1);
		assertNotEquals(car1, car3);
		assertNotEquals(car1.hashCode(), car3.hashCode());
		
		car3.addCarPart(part2);
		assertEquals(car1, car3);
		assertEquals(car1.hashCode(), car3.hashCode());
		
		Set<CarOption> parts = new HashSet<>();
		parts.add(new CarOption("sport", CarOptionCategogry.BODY));
		CarModelSpecification template2 = new CarModelSpecification("modelb", parts, 50);
		template2.getCarParts().put(CarOptionCategogry.AIRCO, new CarOption("abl", CarOptionCategogry.AIRCO));
		CarModel car4 = new CarModel(template2);
		car4.addCarPart(part1);
		car4.addCarPart(part2);
		assertNotEquals(car1, car4);
		assertNotEquals(part1.hashCode(), part2.hashCode());
		
		
		assertEquals(car1, car1);
		assertNotEquals(car1, null);
		assertNotEquals(car1, new Action("Paint"));
	}
	
	@Test
	public void testForcedOptions(){
		CarModel model = new CarModel(template);
		model.addForcedOptionalType(CarOptionCategogry.AIRCO, false);
		
		assertFalse(model.getForcedOptionalTypes().get(CarOptionCategogry.AIRCO));
		
		
	}
}
