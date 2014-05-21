package domain.vehicle.vehicleOptionTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import domain.vehicle.vehicleOption.VehicleOptionCategory;

public class VehicleOptionCategoryTest {

	@Test
	public void test() {
		assertNotEquals(VehicleOptionCategory.AIRCO, VehicleOptionCategory.BODY);
		assertEquals(VehicleOptionCategory.AIRCO, VehicleOptionCategory.valueOf("AIRCO"));
		assertFalse(VehicleOptionCategory.BODY.isOptional());
		assertTrue(VehicleOptionCategory.AIRCO.isOptional());
		
		assertEquals("Airco", VehicleOptionCategory.AIRCO.toString());
		assertEquals("Body", VehicleOptionCategory.BODY.toString());
		assertEquals("Cargo", VehicleOptionCategory.CARGO.toString());
		assertEquals("Certification", VehicleOptionCategory.CERTIFICATION.toString());
		assertEquals("Gearbox", VehicleOptionCategory.GEARBOX.toString());
		assertEquals("Seats", VehicleOptionCategory.SEATS.toString());
		assertEquals("Color", VehicleOptionCategory.COLOR.toString());
		assertEquals("Engine", VehicleOptionCategory.ENGINE.toString());
		assertEquals("Wheel", VehicleOptionCategory.WHEEL.toString());
		assertEquals("Spoiler", VehicleOptionCategory.SPOILER.toString());
	}

}
