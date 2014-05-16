package domain.vehicleTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import domain.assembly.workBench.WorkBenchType;
import domain.exception.AlreadyInMapException;
import domain.job.action.Action;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicle.Vehicle;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

public class VehicleTest {
	VehicleSpecification template;

	@Before
	public void initialize() {
		Set<VehicleOption> parts = new HashSet<>();
		parts.add(new VehicleOption("sport", VehicleOptionCategory.BODY));

		parts.add(new VehicleOption("black", VehicleOptionCategory.COLOR));
		parts.add(new VehicleOption("white", VehicleOptionCategory.COLOR));

		parts.add(new VehicleOption("performance 2.5l V6", VehicleOptionCategory.ENGINE));
		parts.add(new VehicleOption("ultra 3l V8", VehicleOptionCategory.ENGINE));

		parts.add(new VehicleOption("6 Speed Manual", VehicleOptionCategory.GEARBOX));

		parts.add(new VehicleOption("Leather White", VehicleOptionCategory.SEATS));
		parts.add(new VehicleOption("Leather Black", VehicleOptionCategory.SEATS));

		parts.add(new VehicleOption("Manual", VehicleOptionCategory.AIRCO));
		parts.add(new VehicleOption("Automatic", VehicleOptionCategory.AIRCO));

		parts.add(new VehicleOption("Winter", VehicleOptionCategory.WHEEL));
		parts.add(new VehicleOption("Sports", VehicleOptionCategory.WHEEL));

		parts.add(new VehicleOption("high", VehicleOptionCategory.SPOILER));
		parts.add(new VehicleOption("low", VehicleOptionCategory.SPOILER));
		Map<WorkBenchType, Integer> map = new HashMap<WorkBenchType, Integer>();
		map.put(WorkBenchType.BODY, 50);
		template = new VehicleSpecification("model", parts, map);
	}

	@Test
	public void testConstructor() {
		Vehicle model = new Vehicle(template);
		assertEquals(template, model.getVehicleSpecification());
		assertNotNull(model.getVehicleOptions());
		assertNotNull(model.getForcedOptionalTypes());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidConstructor() {
		new Vehicle(null);
	}

	@Test
	public void testAdd() throws AlreadyInMapException {
		Vehicle car = new Vehicle(template);
		VehicleOption part = new VehicleOption("manual", VehicleOptionCategory.AIRCO);
		car.addVehicleOption(part);
		assertEquals(part, car.getVehicleOptions().get(part.getType()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalAdd1() throws AlreadyInMapException {
		Vehicle car = new Vehicle(template);

		car.addVehicleOption(null);

	}

	@Test(expected = AlreadyInMapException.class)
	public void testIllegalAdd2() throws AlreadyInMapException {
		Vehicle car = new Vehicle(template);
		VehicleOption part1 = new VehicleOption("manual", VehicleOptionCategory.AIRCO);
		VehicleOption part2 = new VehicleOption("automatic", VehicleOptionCategory.AIRCO);

		car.addVehicleOption(part1);
		car.addVehicleOption(part2);

	}

	@Test
	public void testToString() {
		Vehicle car1 = new Vehicle(template);
		assertEquals("model", car1.toString());
	}

	@Test
	public void testEquals() throws AlreadyInMapException {
		Vehicle car1 = new Vehicle(template);
		VehicleOption part1 = new VehicleOption("manual", VehicleOptionCategory.AIRCO);
		VehicleOption part2 = new VehicleOption("break", VehicleOptionCategory.BODY);
		car1.addVehicleOption(part1);
		car1.addVehicleOption(part2);

		Vehicle car2 = new Vehicle(template);
		assertNotEquals(car1, car2);

		Vehicle car3 = new Vehicle(template);
		assertNotEquals(car1, car3);
		assertNotEquals(car1.hashCode(), car3.hashCode());

		car3.addVehicleOption(part1);
		assertNotEquals(car1, car3);
		assertNotEquals(car1.hashCode(), car3.hashCode());

		car3.addVehicleOption(part2);
		assertEquals(car1, car3);
		assertEquals(car1.hashCode(), car3.hashCode());

		car3.addForcedOptionalType(new VehicleOption("test", VehicleOptionCategory.AIRCO), true);
		assertNotEquals(car1, car3);
		
		
		Set<VehicleOption> parts = new HashSet<>();
		parts.add(new VehicleOption("sport", VehicleOptionCategory.BODY));
		VehicleSpecification template2 = new VehicleSpecification("modelb",
				parts, new HashMap<WorkBenchType, Integer>());

		Vehicle car4 = new Vehicle(template2);
		car4.addVehicleOption(part1);
		car4.addVehicleOption(part2);
		assertNotEquals(car1, car4);
		assertNotEquals(part1.hashCode(), part2.hashCode());

		assertEquals(car1, car1);
		assertNotEquals(car1, null);
		assertNotEquals(car1, new Action("Paint"));
	}

	@Test
	public void testForcedOptions() {
		Vehicle model = new Vehicle(template);
		model.addForcedOptionalType(new VehicleOption("sport",
				VehicleOptionCategory.BODY), false);

		assertFalse(model.getForcedOptionalTypes().get(
				new VehicleOption("sport", VehicleOptionCategory.BODY)));

	}

	@Test
	public void testValidCar() throws AlreadyInMapException {
		Vehicle model = new Vehicle(template);
		assertFalse(model.isValid());

		model.addVehicleOption(new VehicleOption("sport", VehicleOptionCategory.BODY));

		model.addVehicleOption(new VehicleOption("white", VehicleOptionCategory.COLOR));

		model.addVehicleOption(new VehicleOption("ultra 3l V8", VehicleOptionCategory.ENGINE));

		model.addVehicleOption(new VehicleOption("6 Speed Manual", VehicleOptionCategory.GEARBOX));
		
		model.addVehicleOption(new VehicleOption("Leather Black", VehicleOptionCategory.SEATS));

		model.addVehicleOption(new VehicleOption("Automatic", VehicleOptionCategory.AIRCO));

		model.addVehicleOption(new VehicleOption("Sports", VehicleOptionCategory.WHEEL));
		assertTrue(model.isValid());
	}
	
	@Test
	public void testTimeAtWorkbench(){
		Vehicle model = new Vehicle(template);
		assertFalse(model.isValid());

		model.addVehicleOption(new VehicleOption("sport", VehicleOptionCategory.BODY));

		model.addVehicleOption(new VehicleOption("white", VehicleOptionCategory.COLOR));

		model.addVehicleOption(new VehicleOption("ultra 3l V8", VehicleOptionCategory.ENGINE));

		model.addVehicleOption(new VehicleOption("6 Speed Manual", VehicleOptionCategory.GEARBOX));
		
		model.addVehicleOption(new VehicleOption("Leather Black", VehicleOptionCategory.SEATS));

		model.addVehicleOption(new VehicleOption("Automatic", VehicleOptionCategory.AIRCO));

		model.addVehicleOption(new VehicleOption("Sports", VehicleOptionCategory.WHEEL));
		int time = model.getTimeAtWorkBench().get(WorkBenchType.BODY);
		assertEquals(50, time);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testAddIllegalForcedOptional(){
		Vehicle model = new Vehicle(template);
		model.addForcedOptionalType(null, false);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testSetIllegalVehicleSpecification(){
		Vehicle model = new Vehicle(template);
		model.setVehicleSpecification(null);
	}
	
}
