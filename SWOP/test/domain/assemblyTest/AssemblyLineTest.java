package domain.assemblyTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;

import domain.assembly.AssemblyLine;
import domain.assembly.IWorkBench;
import domain.assembly.WorkBench;
import domain.car.CarModel;
import domain.car.CarModelSpecification;
import domain.car.CarOption;
import domain.car.CarOptionCategory;
import domain.clock.UnmodifiableClock;
import domain.exception.AlreadyInMapException;
import domain.exception.ImmutableException;
import domain.job.Action;
import domain.job.IAction;
import domain.job.IJob;
import domain.job.ITask;
import domain.job.Job;
import domain.job.Task;
import domain.observer.ClockObserver;
import domain.order.StandardOrder;


public class AssemblyLineTest{

	private AssemblyLine line;
	private CarModel model;
	
	@Before
	public void initialize() throws AlreadyInMapException{
		line = new AssemblyLine(new ClockObserver(), new UnmodifiableClock(0));

		Set<CarOption> parts = new HashSet<>();
		parts.add(new CarOption("sport", CarOptionCategory.BODY));
		CarModelSpecification template = new CarModelSpecification("model", parts, 60);
		model = new CarModel(template);
		model.addCarPart(new CarOption("manual", CarOptionCategory.AIRCO));
		model.addCarPart(new CarOption("sedan",  CarOptionCategory.BODY));
		model.addCarPart(new CarOption("red",  CarOptionCategory.COLOR));
		model.addCarPart(new CarOption("standard 2l 4 cilinders",  CarOptionCategory.ENGINE));
		model.addCarPart(new CarOption("6 speed manual",  CarOptionCategory.GEARBOX));
		model.addCarPart(new CarOption("leather black", CarOptionCategory.SEATS));
		model.addCarPart(new CarOption("comfort", CarOptionCategory.WHEEL));
	}

	@Test
	public void TestConstructor() {
		assertNotNull(line);
		assertNotNull(line.getCurrentJobs());
		assertNotNull(line.getWorkbenches());
	}

	@Test(expected = IllegalArgumentException.class)
	public void TestInvalidConstructor(){
		new AssemblyLine(null, new UnmodifiableClock(0));
	}
	
	
}
