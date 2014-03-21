package tests.carTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableMultimap;

import code.car.Airco;
import code.car.Body;
import code.car.CarPart;
import code.car.CarPartCatalogue;


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
