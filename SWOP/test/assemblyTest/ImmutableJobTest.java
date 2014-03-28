package assemblyTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import order.Order;

import org.junit.BeforeClass;
import org.junit.Test;

import car.Airco;
import car.Body;
import car.CarModel;
import car.Color;
import car.Engine;
import car.Gearbox;
import car.Seat;
import car.Wheel;
import assembly.IJob;
import assembly.ITask;
import assembly.ImmutableJob;
import assembly.Job;
import assembly.Task;

public class ImmutableJobTest {

	@Test
	public void test() {
		CarModel model = new CarModel("Volkswagen", new Airco("manual"), new Body("sedan"), new Color("blue"), 
				new Engine("standard 2l 4 cilinders"), new Gearbox("6 speed manual"), new Seat("leather black"), new Wheel("comfort"));

		Job job = new Job(new Order("Stef", model, 1));
		IJob immutable = new ImmutableJob(job);
		assertEquals(immutable.getOrder().getDescription(), model);
		List<ITask> tasks = new ArrayList<>();
		tasks.add(new Task("Test"));
		job.setTasks(tasks);
		assertEquals(tasks, immutable.getTasks());
		
		assertEquals(job.toString(), immutable.toString());
		assertEquals(job.hashCode(), immutable.hashCode());
		assertTrue(immutable.equals(job));;
		assertTrue(immutable.isCompleted());
	}

	@Test(expected=IllegalArgumentException.class)
	public void illegalConstructorTest(){
		new ImmutableJob(null);
	}
}
