package carTest;

import static org.junit.Assert.*;

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

public class CatalogueTest {
	@Test
	public void Test(){
		Airco airco = new Airco("manual");
		Body body = new Body("break");
		Color color = new Color("red");
		Engine engine = new Engine("standard 2l 4 cilinders");
		Gearbox gear = new Gearbox("6 speed manual");
		Seat seat = new Seat("leather black");
		Wheel wheel = new Wheel("comfort");
		CarModel car1 = new CarModel("car1",airco,body,color,engine,gear,seat,wheel);
		
		Airco airco1 = new Airco("manual");
		Body body1 = new Body("break");
		Color color1 = new Color("red");
		Engine engine1 = new Engine("standard 2l 4 cilinders");
		Gearbox gear1 = new Gearbox("6 speed manual");
		Seat seat1 = new Seat("leather black");
		Wheel wheel1 = new Wheel("comfort");
		CarModel car2 = new CarModel("car2",airco1,body1,color1,engine1,gear1,seat1,wheel1);
		Catalogue cat = new Catalogue();
		ArrayList<CarModel> list = new ArrayList<CarModel>();
		list.add(car2);
		list.add(car1);
		cat.initializeCatalogue(list);
		assertNotNull(cat.getCatalogue());
		assertEquals(2, cat.getCatalogue().entrySet().size());
		
	}

}
