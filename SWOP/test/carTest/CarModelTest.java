package carTest;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import car.Airco;
import car.Body;
import car.CarModel;
import car.Catalogue;
import car.Color;
import car.Engine;
import car.Gearbox;
import car.Seat;
import car.Wheel;

public class CarModelTest {

	@Test
	public void test1() {
		Airco airco = new Airco("manual");
		Body body = new Body("break");
		Color color = new Color("red");
		Engine engine = new Engine("standard 2l 4 cilinders");
		Gearbox gear = new Gearbox("6 speed manual");
		Seat seat = new Seat("leather black");
		Wheel wheel = new Wheel("comfort");
		CarModel car1 = new CarModel("car1",airco,body,color,engine,gear,seat,wheel);
		assertNotNull(car1.getCarParts());
		assertEquals(7, car1.getCarParts().size());

		Airco airco1 = new Airco("manual");
		Body body1 = new Body("break");
		Color color1 = new Color("red");
		Engine engine1 = new Engine("standard 2l 4 cilinders");
		Gearbox gear1 = new Gearbox("6 speed manual");
		Seat seat1 = new Seat("leather black");
		Wheel wheel1 = new Wheel("comfort");
		CarModel car2 = new CarModel("car2",airco1,body1,color1,engine1,gear1,seat1,wheel1);
		assertEquals("car2",car2.getDescription());
		
	}

	@Test (expected = IllegalArgumentException.class)
	public void test2(){
		Airco airco1 = new Airco(null);
		Body body1 = new Body(null);
		Color color1 = new Color(null);
		Engine engine1 = new Engine(null);
		Gearbox gear1 = new Gearbox(null);
		Seat seat1 = new Seat(null);
		Wheel wheel1 = new Wheel(null);
		CarModel car2 = new CarModel("car2",airco1,body1,color1,engine1,gear1,seat1,wheel1);

	}

	@Test (expected = IllegalArgumentException.class)
	public void test3(){
		Airco airco1 = new Airco("manual");
		Body body1 = new Body(null);
		Color color1 = new Color(null);
		Engine engine1 = new Engine(null);
		Gearbox gear1 = new Gearbox(null);
		Seat seat1 = new Seat(null);
		Wheel wheel1 = new Wheel(null);
		CarModel car2 = new CarModel("car2",airco1,body1,color1,engine1,gear1,seat1,wheel1);

	}
	// zo een 14 om alle mogelijkheden van de if in setCarParts te testen
	@Test (expected = IllegalArgumentException.class)
	public void test4(){
		Airco airco1 = new Airco("manual");
		Body body1 = new Body("break");
		Color color1 = new Color(null);
		Engine engine1 = new Engine(null);
		Gearbox gear1 = new Gearbox(null);
		Seat seat1 = new Seat(null);
		Wheel wheel1 = new Wheel(null);
		CarModel car2 = new CarModel("car2",airco1,body1,color1,engine1,gear1,seat1,wheel1);

	}

	@Test (expected = IllegalArgumentException.class)
	public void test5(){
		Airco airco1 = new Airco("manual");
		Body body1 = new Body("break");
		Color color1 = new Color(null);
		Engine engine1 = new Engine(null);
		Gearbox gear1 = new Gearbox(null);
		Seat seat1 = new Seat(null);
		Wheel wheel1 = new Wheel(null);
		CarModel car2 = new CarModel(null,airco1,body1,color1,engine1,gear1,seat1,wheel1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void test6(){
		Airco airco1 = new Airco("manual");
		Body body1 = new Body("break");
		Color color1 = new Color(null);
		Engine engine1 = new Engine(null);
		Gearbox gear1 = new Gearbox(null);
		Seat seat1 = new Seat(null);
		Wheel wheel1 = new Wheel(null);
		CarModel car2 = new CarModel("",airco1,body1,color1,engine1,gear1,seat1,wheel1);
	}
}
