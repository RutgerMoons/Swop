package carTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import car.Airco;
import car.Body;
import car.CarPart;
import car.CarPartCatalogue;


public class CarPartCatalogueTest {

	@Test
	public void test1() {
		CarPartCatalogue catalogue = new CarPartCatalogue();
		Airco airco = new Airco("manual");
		assertTrue(catalogue.isValidCarPart(airco));
		Airco airco2 = new Airco("manual");
		assertTrue(airco.equals(airco2));
		
	}
	
	@Test 
	public void test2() {
		CarPartCatalogue catalogue = new CarPartCatalogue();
		Airco airco = new Airco("bla");
		assertFalse(catalogue.isValidCarPart(airco));
		Body body = null;
		assertFalse(catalogue.isValidCarPart(body));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void test3(){
		CarPartCatalogue cat = new CarPartCatalogue();
		ArrayList<CarPart> array = new ArrayList<CarPart>();
		array.add(new Airco("manual"));
		cat.addListForCarPart(null, array);
	}
	
	@Test
	public void test4(){
		CarPartCatalogue cat = new CarPartCatalogue();
		ArrayList<CarPart> array = new ArrayList<CarPart>();
		array.add(new Airco("manual"));
		cat.addListForCarPart(Airco.class, array);
		HashMap<Class<?>, List<CarPart>> map = cat.getMap();
		//System.out.println(map.toString());
		assertEquals(7,map.entrySet().size());
		ArrayList<CarPart> arr = new ArrayList<CarPart>();
		arr.add(new Body("break"));
		cat.addListForCarPart(CarPart.class, arr);
		assertEquals(7,map.entrySet().size());
	}
	
	

}
