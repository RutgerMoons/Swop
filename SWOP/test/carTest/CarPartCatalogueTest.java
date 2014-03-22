package carTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import car.Airco;
import car.Body;
import car.CarPart;
import car.CarPartCatalogue;

import com.google.common.collect.ImmutableMultimap;


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
		cat.addCarPart(null, new Airco("manual"));
	}
	
	@Test
	public void test4(){
		CarPartCatalogue cat = new CarPartCatalogue();
		cat.addCarPart(Airco.class, new Airco("manual"));
		ImmutableMultimap<Class<?>, CarPart> map = cat.getMap();
		//System.out.println(map.toString());
		assertEquals(7,map.keySet().size());
		
		cat.addCarPart(CarPart.class, new Body("break"));
		assertEquals(7,map.keySet().size());
	}
	
	

}
