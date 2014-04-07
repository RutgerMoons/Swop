package carTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import domain.car.CarModel;
import domain.car.CarPart;
import domain.car.CarPartType;
import domain.car.ICarModel;
import domain.car.ImmutableCarModel;
import domain.exception.AlreadyInMapException;

public class ImmutableCarModelTest {

	@Test
	public void test() throws AlreadyInMapException {

		CarModel car = new CarModel("car1");
		car.addCarPart(new CarPart("manual", true, CarPartType.AIRCO));
		car.addCarPart(new CarPart("break", false, CarPartType.BODY));
		
		ICarModel immutable = new ImmutableCarModel(car);
		assertEquals(car.getCarParts(), immutable.getCarParts());
		assertEquals(car.getDescription(), immutable.getDescription());
		assertEquals(car.toString(), immutable.toString());
		assertTrue(immutable.equals(car));
		assertEquals(car.hashCode(), immutable.hashCode());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidConstructor(){
		new ImmutableCarModel(null);
	}
}
