package orderTest;

import static org.junit.Assert.*;

import org.junit.Test;

import Order.Order;


public class OrderTest {

	@Test
	public void test1Constructor(){
		Order order = new Order("Mario","Volkswagen",3);
		assertNotNull(order);
		assertEquals("Volkswagen",order.getDescription());
		assertEquals("Mario",order.getGarageHolder());
		assertEquals(3,order.getQuantity());
		assertEquals(3, order.getPendingCars());
		order.setEstimatedTime(5);
		assertEquals(5,order.getEstimatedTime());
		
	}
	@Test (expected = IllegalArgumentException.class)
	public void test2Constructor(){
		Order order = new Order(null, null, -1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testSetGarageHolder1(){
		Order order = new Order(" ","volkswagen",3);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testSetGarageHolder2(){
		Order order3 = new Order("Mario","Volkswagen",3);
		assertNotNull(order3.getGarageHolder());
		Order order2 = new Order(null, "volkswagen",3);		
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testPendingCars1(){
		Order order = new Order("Mario","Luigi",-1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testPendingCars2(){
		Order order = new Order("Mario", "Luigi",1);
		order.completeCar();
		assertEquals(0, order.getPendingCars());
		order.completeCar();
		
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testQuantity(){
		Order order =  new Order("Mario", "Luigi",0);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testDescription(){
		Order order = new Order("Mario"," ",2);
		Order order2 = new Order("Mario",null,2);
	}
	
	@Test
	public void testEstimatedTime1(){
		Order order = new Order("Mario","Luigi",3);
		order.setEstimatedTime(5);
		assertEquals(5, order.getEstimatedTime());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testEstimatedTime2(){
		Order order = new Order("Mario","Luigi",3);
		order.setEstimatedTime(-3);
	}
	
	@Test
	public void testCarCompleted(){
		Order order = new Order("Mario","Volkswagen",3);
		assertEquals(3,order.getPendingCars());
		order.completeCar();
		assertEquals(2,order.getPendingCars());
		
	}

}
