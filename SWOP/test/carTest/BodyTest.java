package carTest;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;

import car.Body;

public class BodyTest {

	@Test
	public void test() {
		Body body = new Body("sedan");
		assertNotNull(body);
		
		
	} 

}
