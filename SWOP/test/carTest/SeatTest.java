package carTest;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;

import car.Seat;

public class SeatTest {

	@Test
	public void test() {
		Seat s1 = new Seat();
		assertNotNull(s1);
		
		ArrayList<String> types = new ArrayList<>();
		Seat s2 = new Seat(types);
		assertNotNull(s2);
	}

}
