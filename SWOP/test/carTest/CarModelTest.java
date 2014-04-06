package carTest;

import static org.junit.Assert.*;

import org.junit.Test;

import assembly.Action;
import car.CarModel;
import car.CarPart;
import car.CarPartType;
import exception.AlreadyInMapException;

public class CarModelTest {

	@Test
	public void test1() {
		CarModel car1 = new CarModel("car1");
		assertNotNull(car1.getCarParts());
		assertEquals("car1", car1.getDescription());

		CarModel car2 = new CarModel("car2");
		assertEquals("car2", car2.getDescription());

	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidConstructor() {
		new CarModel("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidConstructor2() {
		new CarModel(null);
	}

	@Test
	public void testAdd() throws AlreadyInMapException {
		CarModel car = new CarModel("model");
		CarPart part = new CarPart("manual", true, CarPartType.AIRCO);

		car.addCarPart(part);

		assertEquals(part, car.getCarParts().get(part.getType()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalAdd1() throws AlreadyInMapException {
		CarModel car = new CarModel("model");

		car.addCarPart(null);

	}

	@Test(expected = AlreadyInMapException.class)
	public void testIllegalAdd2() throws AlreadyInMapException {
		CarModel car = new CarModel("model");
		CarPart part1 = new CarPart("manual", true, CarPartType.AIRCO);
		CarPart part2 = new CarPart("automatic", true, CarPartType.AIRCO);

		car.addCarPart(part1);
		car.addCarPart(part2);

	}
	
	@Test
	public void testToString(){
		CarModel car1 = new CarModel("model");
		assertEquals("model", car1.toString());
	}

	@Test
	public void testEquals() throws AlreadyInMapException {
		CarModel car1 = new CarModel("model");
		CarPart part1 = new CarPart("manual", true, CarPartType.AIRCO);
		CarPart part2 = new CarPart("break", true, CarPartType.BODY);
		car1.addCarPart(part1);
		car1.addCarPart(part2);
		
		CarModel car2 = new CarModel("otherModel");
		assertNotEquals(car1, car2);
		
		CarModel car3 = new CarModel("model");
		assertNotEquals(car1, car3);
		assertNotEquals(car1.hashCode(), car3.hashCode());
		
		car3.addCarPart(part1);
		assertNotEquals(car1, car3);
		assertNotEquals(car1.hashCode(), car3.hashCode());
		
		car3.addCarPart(part2);
		assertEquals(car1, car3);
		assertEquals(car1.hashCode(), car3.hashCode());
		
		assertEquals(car1, car1);
		assertNotEquals(car1, null);
		assertNotEquals(car1, new Action("Paint"));
	}
}
