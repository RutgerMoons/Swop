package carTest;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import car.Airco;
import car.Body;
import car.CarModel;
import car.Color;
import car.Engine;
import car.Gearbox;
import car.ICarModel;
import car.ImmutableCarModel;
import car.Seat;
import car.Wheel;

public class ImmutableCarModelTest {

	@Test
	public void test() {
		Airco airco = new Airco("manual");
		Body body = new Body("break");
		Color color = new Color("red");
		Engine engine = new Engine("standard 2l 4 cilinders");
		Gearbox gear = new Gearbox("6 speed manual");
		Seat seat = new Seat("leather black");
		Wheel wheel = new Wheel("comfort");
		CarModel car = new CarModel("car1",airco,body,color,engine,gear,seat,wheel);
	
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
