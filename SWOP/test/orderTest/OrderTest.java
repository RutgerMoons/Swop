package orderTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;

import order.Order;

import org.junit.Before;
import org.junit.Test;

import assembly.Action;
import car.CarModel;
import car.CarPart;
import car.CarPartType;
import exception.AlreadyInMapException;



public class OrderTest {

	private CarModel model;
	@Before
	public void initializeModel() throws AlreadyInMapException{
		model = new CarModel("Volkswagen");
		model.addCarPart(new CarPart("manual", true, CarPartType.AIRCO));
		model.addCarPart(new CarPart("sedan", false, CarPartType.BODY));
		model.addCarPart(new CarPart("red", false, CarPartType.COLOR));
		model.addCarPart(new CarPart("standard 2l 4 cilinders", false, CarPartType.ENGINE));
		model.addCarPart(new CarPart("6 speed manual", false, CarPartType.GEARBOX));
		model.addCarPart(new CarPart("leather black", false, CarPartType.SEATS));
		model.addCarPart(new CarPart("comfort", false, CarPartType.WHEEL));
	}
	@Test
	public void test1Constructor(){
		Order order = new Order("Mario",model,3);
		assertNotNull(order);
		assertEquals(order.getDescription(), model);
		assertEquals("Mario",order.getGarageHolder());
		assertEquals(3,order.getQuantity());
		assertEquals(3, order.getPendingCars());
		int[] array = {0,5};
		order.setEstimatedTime(array);
		assertEquals(0,order.getEstimatedTime()[0]);
		assertEquals(5,order.getEstimatedTime()[1]);
		
	}
	@Test (expected = IllegalArgumentException.class)
	public void test2Constructor(){
		Order order = new Order(null, null, -1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testSetGarageHolder1(){
		Order order = new Order(" ",model,3);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testSetGarageHolder2(){
		Order order3 = new Order("Mario",model,3);
		assertNotNull(order3.getGarageHolder());
		Order order2 = new Order(null, model,3);		
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testPendingCars1(){
		Order order = new Order("Mario",model,-1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testPendingCars2(){
		Order order = new Order("Mario", model,1);
		order.completeCar();
		assertEquals(0, order.getPendingCars());
		order.completeCar();
		
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testQuantity(){
		Order order =  new Order("Mario", model,0);
	}
	
	@Test
	public void testEstimatedTime1(){
		Order order = new Order("Mario",model,3);
		int[] array = {0,5};
		order.setEstimatedTime(array);
		assertEquals(0,order.getEstimatedTime()[0]);
		assertEquals(5,order.getEstimatedTime()[1]);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testEstimatedTime2(){
		Order order = new Order("Mario",model,3);
		int[] array = {-1,5};
		order.setEstimatedTime(array);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testEstimatedTime3(){
		Order order = new Order("Mario",model,3);
		int[] array = {0,-3};
		order.setEstimatedTime(array);
	}
	
	@Test
	public void testCarCompleted(){
		Order order = new Order("Mario",model,3);
		assertEquals(3,order.getPendingCars());
		order.completeCar();
		assertEquals(2,order.getPendingCars());
		
	}

	@Test
	public void TestEqualsAndHashcode() throws AlreadyInMapException{
		CarModel model2 = new CarModel("BMW");
		model2.addCarPart(new CarPart("manual", true, CarPartType.AIRCO));
		model2.addCarPart(new CarPart("sedan", false, CarPartType.BODY));
		model2.addCarPart(new CarPart("red", false, CarPartType.COLOR));
		model2.addCarPart(new CarPart("standard 2l 4 cilinders", false, CarPartType.ENGINE));
		model2.addCarPart(new CarPart("6 speed manual", false, CarPartType.GEARBOX));
		model2.addCarPart(new CarPart("leather black", false, CarPartType.SEATS));
		model2.addCarPart(new CarPart("comfort", false, CarPartType.WHEEL));
		
		Order order1 = new Order("Jan", model, 2);
		assertFalse(order1.equals(null));
		assertFalse(order1.equals(new Action("Paint")));
		Order order2 = new Order("Jan", model2, 2);
		assertFalse(order1.equals(order2));
		order2 = new Order("Jos", model, 2);
		assertFalse(order1.equals(order2));
		order2 = new Order("Jan", model, 1);
		assertFalse(order1.equals(order2));
		order2 = new Order("Jan", model, 2);
		assertTrue(order1.equals(order2));
		assertTrue(order1.equals(order1));
		assertEquals(order1.hashCode(), order2.hashCode());
	}
	
	@Test
	public void testToString(){
		Order order1 = new Order("Jan", model, 2);
		int[] array = {1,100};
		order1.setEstimatedTime(array);
		assertEquals("2 Volkswagen Estimated completion time: 1 days and 1 hours and 40 minutes",order1.toString());
	}
}

