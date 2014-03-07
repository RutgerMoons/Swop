package carTest;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;

import car.Airco;
import car.CarPart;
import car.CarPartCatalogue;

public class CarPartCatalogueTest {

	@Test
	public void constructorTest() {
		CarPartCatalogue c = new CarPartCatalogue();
		assertNotNull(c);
		assertNotNull(c.getMap());
	}
	
	@Test
	public void AddTest() {
		CarPartCatalogue c = new CarPartCatalogue();
		assertNotNull(c);
		ArrayList<CarPart> a = new ArrayList<>();
		Airco airco1 = new Airco("manual");
		a.add(airco1);
		assertNotNull(airco1);
		a.add(new Airco("automatic climate control"));
		c.addListForCarPart(Airco.class, a);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void AddNullKeyTest() {
		CarPartCatalogue c = new CarPartCatalogue();
		assertNotNull(c);
		ArrayList<CarPart> a = new ArrayList<>();
		Airco airco1 = new Airco("manual");
		a.add(airco1);
		assertNotNull(airco1);
		a.add(new Airco("automatic climate control"));
		c.addListForCarPart(null, a);
	} 
	
	@Test
	public void isValidCarPart() {
		//TODO
	}
	 
	

}
