package domain.vehicleTest;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import domain.assembly.workBench.WorkBenchType;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

public class VehicleSpecificationTest {

	VehicleSpecification template;
	@Before
	public void initialize(){
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
		
		HashMap<WorkBenchType, Integer> timeAtWorkBench = new HashMap<WorkBenchType, Integer>();
		timeAtWorkBench.put(WorkBenchType.ACCESSORIES, 60);
		timeAtWorkBench.put(WorkBenchType.BODY, 60);
		timeAtWorkBench.put(WorkBenchType.CARGO, 60);
		timeAtWorkBench.put(WorkBenchType.CERTIFICATION, 120);
		template = new VehicleSpecification("model", parts, timeAtWorkBench);
	}

	@Test
	public void testConstructor() {
		assertEquals("model", template.getDescription());
		assertEquals("model", template.toString());
		int time = template.getTimeAtWorkBench().get(WorkBenchType.BODY);
		assertEquals(60, time);
		
		assertEquals(8, template.getVehicleOptions().keySet().size());
		assertEquals(14, template.getVehicleOptions().values().size());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testInvalidConstructor(){
		new VehicleSpecification(null, null, null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testInvalidConstructor2(){
		new VehicleSpecification("", null, null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testInvalidConstructor3(){
		new VehicleSpecification("test", null, new HashMap<WorkBenchType, Integer>());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testInvalidConstructor4(){
		new VehicleSpecification("test", new HashSet<VehicleOption>(), null);
	}
	
	@Test
	public void getProductionTime() {
		assertEquals(60, this.template.getProductionTime(WorkBenchType.ACCESSORIES));
		assertEquals(120, this.template.getProductionTime(WorkBenchType.CERTIFICATION));
		assertEquals(0, this.template.getProductionTime(WorkBenchType.DRIVETRAIN));
	}

	
	@Test
	public void testEquals(){
		assertEquals(template, template);
		assertEquals(template.hashCode(), template.hashCode());
		assertNotEquals(template, null);
		assertNotEquals(template, VehicleOptionCategory.AIRCO);
		
		VehicleSpecification specification1 = new VehicleSpecification("test", new HashSet<VehicleOption>(), new HashMap<WorkBenchType, Integer>());
		assertNotEquals(template, specification1);
		assertNotEquals(template.hashCode(), specification1.hashCode());
		
		VehicleSpecification specification2 = new VehicleSpecification("model", new HashSet<VehicleOption>(), new HashMap<WorkBenchType, Integer>());
		assertNotEquals(template, specification2);
		assertNotEquals(template.hashCode(), specification2.hashCode());
		
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
		
		VehicleSpecification specification3 = new VehicleSpecification("model", parts, new HashMap<WorkBenchType, Integer>());
		assertNotEquals(template, specification3);
		assertNotEquals(template.hashCode(), specification3.hashCode());
		
		VehicleSpecification specification4 = new VehicleSpecification("model", parts, template.getTimeAtWorkBench());
		assertEquals(template, specification4);
		assertEquals(template.hashCode(), specification4.hashCode());
	}
}
