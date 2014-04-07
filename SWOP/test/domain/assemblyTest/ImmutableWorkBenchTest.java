package domain.assemblyTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;


import org.junit.Before;
import org.junit.Test;


import com.google.common.base.Optional;

import domain.assembly.Action;
import domain.assembly.IJob;
import domain.assembly.IWorkBench;
import domain.assembly.ImmutableWorkBench;
import domain.assembly.Job;
import domain.assembly.Task;
import domain.assembly.WorkBench;
import domain.car.CarModel;
import domain.car.CarPart;
import domain.car.CarPartType;
import domain.exception.AlreadyInMapException;
import domain.exception.ImmutableException;
import domain.order.Order;


public class ImmutableWorkBenchTest {
	IWorkBench bench;
	IWorkBench immutable;
	@Before
	public void initialize(){
		Set<String> responsibilities = new HashSet<>();
		responsibilities.add("paint");

		bench = new WorkBench(responsibilities, "paintbooth");
		immutable = new ImmutableWorkBench(bench);
		
	}
	@Test
	public void test() throws AlreadyInMapException, ImmutableException {
		CarModel model = new CarModel("Volkswagen");
		
		model.addCarPart(new CarPart("manual", true, CarPartType.AIRCO));
		model.addCarPart(new CarPart("sedan", false, CarPartType.BODY));
		model.addCarPart(new CarPart("red", false, CarPartType.COLOR));
		model.addCarPart(new CarPart("standard 2l 4 cilinders", false, CarPartType.ENGINE));
		model.addCarPart(new CarPart("6 speed manual", false, CarPartType.GEARBOX));
		model.addCarPart(new CarPart("leather black", false, CarPartType.SEATS));
		model.addCarPart(new CarPart("comfort", false, CarPartType.WHEEL));
		
		assertEquals("paintbooth", immutable.getWorkbenchName());
		assertTrue(immutable.getResponsibilities().contains("paint"));
		
		IJob job = new Job(new Order("Stef", model, 1));
		Task task= new Task("paint");
		task.addAction(new Action("paint blue"));
		((Job) job).addTask(task);
		Optional<IJob> jobOptional = Optional.fromNullable(job);
		bench.setCurrentJob(jobOptional);
		bench.chooseTasksOutOfJob();
		
		assertEquals(immutable.getCurrentJob().get(), job );
		assertEquals(task, immutable.getCurrentTasks().get(0));
		assertEquals(bench.toString(), immutable.toString());
		assertFalse(immutable.isCompleted());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void illegalConstructorTest(){
		new ImmutableWorkBench(null);
	}
	
	@Test(expected=ImmutableException.class)
	public void testImmutable1() throws ImmutableException{
		immutable.setCurrentJob(null);
	}
	
	@Test(expected=ImmutableException.class)
	public void testImmutable2() throws ImmutableException{
		immutable.setResponsibilities(null);
	}
	
	@Test(expected=ImmutableException.class)
	public void testImmutable3() throws ImmutableException{
		immutable.addResponsibility(null);
	}
	
	@Test(expected=ImmutableException.class)
	public void testImmutable4() throws ImmutableException{
		immutable.setCurrentTasks(null);
	}
	
	@Test(expected=ImmutableException.class)
	public void testImmutable5() throws ImmutableException{
		immutable.chooseTasksOutOfJob();
	}
}
