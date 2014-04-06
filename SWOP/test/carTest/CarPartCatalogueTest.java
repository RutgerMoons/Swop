package carTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import car.CarPart;
import car.CarPartCatalogue;
import car.CarPartCatalogueFiller;
import car.CarPartType;

import com.google.common.collect.Multimap;


public class CarPartCatalogueTest {

	@Test
	public void test1() {
		CarPartCatalogue catalogue = new CarPartCatalogue();
		CarPartCatalogueFiller filler = new CarPartCatalogueFiller(catalogue);
		filler.initializeCarParts();
		CarPart airco = new CarPart("manual", true, CarPartType.AIRCO);
		assertTrue(catalogue.isValidCarPart(airco));
	}
	
	@Test 
	public void test2() {
		CarPartCatalogue catalogue = new CarPartCatalogue();
		CarPartCatalogueFiller filler = new CarPartCatalogueFiller(catalogue);
		filler.initializeCarParts();
		CarPart airco = new CarPart("bla", true, CarPartType.AIRCO);
		assertFalse(catalogue.isValidCarPart(airco));
		CarPart body = null;
		assertFalse(catalogue.isValidCarPart(body));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void test3(){
		CarPartCatalogue cat = new CarPartCatalogue();
		CarPartCatalogueFiller filler = new CarPartCatalogueFiller(cat);
		filler.initializeCarParts();
		cat.addCarPart(null);
	}
	
	@Test
	public void test4(){
		CarPartCatalogue cat = new CarPartCatalogue();
		CarPartCatalogueFiller filler = new CarPartCatalogueFiller(cat);
		filler.initializeCarParts();
		Multimap<CarPartType, CarPart> map = cat.getMap();
		//System.out.println(map.toString());
		assertEquals(7,map.keySet().size());
		
		assertEquals(7,map.keySet().size());
	}
	
	

}
