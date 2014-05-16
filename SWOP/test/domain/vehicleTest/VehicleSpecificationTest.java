package domain.vehicleTest;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import domain.assembly.workBench.WorkbenchType;
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
		
		HashMap<WorkbenchType, Integer> timeAtWorkBench = new HashMap<WorkbenchType, Integer>();
		timeAtWorkBench.put(WorkbenchType.ACCESSORIES, 60);
		timeAtWorkBench.put(WorkbenchType.BODY, 60);
		timeAtWorkBench.put(WorkbenchType.CARGO, 60);
		timeAtWorkBench.put(WorkbenchType.CERTIFICATION, 60);
		timeAtWorkBench.put(WorkbenchType.DRIVETRAIN, 60);
		template = new VehicleSpecification("model", parts, timeAtWorkBench);
	}

	@Test
	public void testConstructor() {
		assertEquals("model", template.getDescription());
		assertEquals("model", template.toString());
		int time = template.getTimeAtWorkBench().get(WorkbenchType.BODY);
		assertEquals(60, time);
		
		assertEquals(8, template.getVehicleOptions().keySet().size());
		assertEquals(14, template.getVehicleOptions().values().size());
	}

}
