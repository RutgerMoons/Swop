package carTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import car.Airco;
import car.Body;
import car.CarModel;
import car.CarModelCatalogue;
import car.Color;
import car.Engine;
import car.Gearbox;
import car.Seat;
import car.Wheel;

public class CatalogueTest {
	CarModel car1, car2;
	
	@Before
	public void setUp(){
		Airco airco = new Airco("manual");
		Body body = new Body("break");
		Color color = new Color("red");
		Engine engine = new Engine("standard 2l 4 cilinders");
		Gearbox gear = new Gearbox("6 speed manual");
		Seat seat = new Seat("leather black");
		Wheel wheel = new Wheel("comfort");
		car1 = new CarModel("car1",airco,body,color,engine,gear,seat,wheel);
		
		Airco airco1 = new Airco("manual");
		Body body1 = new Body("break");
		Color color1 = new Color("red");
		Engine engine1 = new Engine("standard 2l 4 cilinders");
		Gearbox gear1 = new Gearbox("6 speed manual");
		Seat seat1 = new Seat("leather black");
		Wheel wheel1 = new Wheel("comfort");
		car2 = new CarModel("car2",airco1,body1,color1,engine1,gear1,seat1,wheel1);
	}
	
	@Test
	public void Test(){
		
		CarModelCatalogue cat = new CarModelCatalogue();
		Set<CarModel> list = new HashSet<CarModel>();
		list.add(car2);
		list.add(car1);
		cat.initializeCatalogue(list);
		assertNotNull(cat.getCatalogue());
		assertEquals(2, cat.getCatalogue().entrySet().size());
		
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void Test2(){
		CarModelCatalogue cat = new CarModelCatalogue();
		Set<CarModel> list = new HashSet<CarModel>();
		Airco airco1 = new Airco("manual");
		Body body1 = new Body("break");
		Color color1 = new Color("red");
		Engine engine1 = new Engine("standard 2l 4 cilinders");
		Gearbox gear1 = new Gearbox("6 speed manual");
		Seat seat1 = new Seat("leather black");
		Wheel wheel1 = new Wheel(null);
		CarModel car3 = new CarModel("car3",airco1,body1,color1,engine1,gear1,seat1,wheel1);
		list.add(car3);
		cat.initializeCatalogue(list);
		
	}
	
}
