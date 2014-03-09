package carTest;

import static org.junit.Assert.*;

import org.junit.Test;

import car.Airco;
import car.Body;
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

}
