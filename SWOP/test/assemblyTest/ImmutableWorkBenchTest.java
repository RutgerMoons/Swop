package assemblyTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import order.Order;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Optional;

import car.Airco;
import car.Body;
import car.CarModel;
import car.Color;
import car.Engine;
import car.Gearbox;
import car.Seat;
import car.Wheel;
import assembly.Action;
import assembly.IJob;
import assembly.ITask;
import assembly.IWorkBench;
import assembly.ImmutableJob;
import assembly.ImmutableWorkBench;
import assembly.Job;
import assembly.Task;
import assembly.WorkBench;

public class ImmutableWorkBenchTest {


	@Test
	public void test() {
		CarModel model = new CarModel("Volkswagen", new Airco("manual"), new Body("sedan"), new Color("blue"), 
				new Engine("standard 2l 4 cilinders"), new Gearbox("6 speed manual"), new Seat("leather black"), new Wheel("comfort"));

		
		
		Set<String> responsibilities = new HashSet<>();
		responsibilities.add("paint");
		WorkBench bench = new WorkBench(responsibilities, "paintbooth");
		IWorkBench immutable = new ImmutableWorkBench(bench);
		assertEquals("paintbooth", immutable.getWorkbenchName());
		assertTrue(immutable.getResponsibilities().contains("paint"));
		
		IJob job = new Job(new Order("Stef", model, 1));
		Task task= new Task("paint");
		task.addAction(new Action("paint blue"));
		((Job) job).addTask(task);
		Optional<IJob> jobOptional = Optional.fromNullable(job);
		bench.setCurrentJob(jobOptional);
		bench.chooseTasksOutOfJob();
		
		assertEquals(job, immutable.getCurrentJob().get());
		assertEquals(task, immutable.getCurrentTasks().get(0));
		assertEquals(bench.toString(), immutable.toString());
		assertFalse(immutable.isCompleted());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void illegalConstructorTest(){
		new ImmutableWorkBench(null);
	}
}
