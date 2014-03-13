package assemblyTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import order.Order;

import org.junit.Before;
import org.junit.Test;

import assembly.Action;
import assembly.AssemblyLine;
import assembly.Job;
import assembly.Task;
import assembly.WorkBench;
import car.Airco;
import car.Body;
import car.CarModel;
import car.Color;
import car.Engine;
import car.Gearbox;
import car.Seat;
import car.Wheel;
import clock.Clock;

public class AssemblyLineTest{

	private AssemblyLine line;
	private CarModel model;
	private int beginTime = 6*60;
	@Before
	public void initialize(){
		line = new AssemblyLine(new Clock());
		line.setWorkbenches(new ArrayList<WorkBench>()); //DIT MOET GEBEUREN OMDAT ER ANDERS AL 3 WORKBENCHES AANWEZIG ZIJN!!

		model = new CarModel("Volkswagen", new Airco("manual"), new Body("sedan"), new Color("blue"), 
				new Engine("standard 2l 4 cilinders"), new Gearbox("6 speed manual"), new Seat("leather black"), new Wheel("comfort"));
	}

	@Test
	public void TestConstructor() {
		assertNotNull(line);
		assertNotNull(line.getClock());
		assertNotNull(line.getCurrentJobs());
		assertNotNull(line.getOvertime());
		assertNotNull(line.getWorkbenches());
	}

	@Test
	public void TestSetValidCurrentJobs(){
		List<Job> jobs = new ArrayList<>();
		Order order1 = new Order("Jef", model, 1);
		jobs.add(new Job(order1));
		jobs.add(new Job(order1));
		line.setCurrentJobs(jobs);
		assertEquals(2, line.getCurrentJobs().size());
		assertEquals(jobs, line.getCurrentJobs());
	}

	@Test (expected=IllegalArgumentException.class)
	public void TestSetInvalidCurrentJobs(){
		List<Job> jobs = null;
		line.setCurrentJobs(jobs);
		assertEquals(2, line.getCurrentJobs().size());
		assertEquals(jobs, line.getCurrentJobs());
	}

	@Test
	public void TestSetValidOvertime(){
		line.setOvertime(1);
		assertEquals(1, line.getOvertime());
	}

	@Test (expected=IllegalArgumentException.class)
	public void TestSetInvaldOvertime(){
		line.setOvertime(-1);
		assertEquals(-1, line.getOvertime());
	}

	@Test
	public void TestSetValidWorkBenches(){
		List<WorkBench> workbenches = new ArrayList<WorkBench>();
		workbenches.add(new WorkBench(new ArrayList<String>(), "test"));
		workbenches.add(new WorkBench(new ArrayList<String>(), "test2"));
		line.setWorkbenches(workbenches);
		assertEquals(2, line.getWorkbenches().size());
		assertEquals(workbenches, line.getWorkbenches());
	}

	@Test (expected = IllegalArgumentException.class)
	public void TestSetInvalidWorkBenches(){
		List<WorkBench> workbenches = null;
		line.setWorkbenches(workbenches);
		assertEquals(0, line.getWorkbenches().size());
		assertEquals(workbenches, line.getWorkbenches());
	}

	@Test
	public void TestAddOneValidJob(){
		Order order1 = new Order("Jef", model, 1);
		Job job = new Job(order1);
		line.addJob(job);
		assertEquals(1, line.getCurrentJobs().size());
		assertEquals(job, line.getCurrentJobs().get(0));
	}

	@Test (expected = IllegalArgumentException.class)
	public void TestAddOneInvalidJob(){
		line.addJob(null);
		assertEquals(1, line.getCurrentJobs().size());
	}

	@Test
	public void TestAddTwoValidJobs(){
		Order order1 = new Order("Jef", model, 1);
		Job job1 = new Job(order1);
		Job job2 = new Job(order1);
		line.addJob(job1);
		line.addJob(job2);
		assertEquals(2, line.getCurrentJobs().size());
		assertEquals(job1, line.getCurrentJobs().get(0));
		assertEquals(job2, line.getCurrentJobs().get(1));
	}

	@Test (expected = IllegalArgumentException.class)
	public void TestAddOneValidJobOneInvalidJob(){
		Order order1 = new Order("Jef", model, 1);
		Job job = new Job(order1);
		line.addJob(job);
		assertEquals(1, line.getCurrentJobs().size());
		assertEquals(job, line.getCurrentJobs().get(0));
		line.addJob(null);
		assertEquals(2, line.getCurrentJobs().size());
	}

	@Test
	public void TestAddOneValidWorkBench(){
		WorkBench workBench = new WorkBench(new ArrayList<String>(), "test");
		line.addWorkBench(workBench);
		assertEquals(1, line.getWorkbenches().size());
		assertEquals(workBench, line.getWorkbenches().get(0));
	}
	@Test (expected = IllegalArgumentException.class)
	public void TestAddOneInvalidWorkBench(){
		line.addWorkBench(null);
		assertEquals(1, line.getCurrentJobs().size());
	}

	@Test
	public void TestAddTwoValidWorkBenchs(){
		WorkBench bench1 = new WorkBench(new ArrayList<String>(), "test");
		WorkBench bench2 = new WorkBench(new ArrayList<String>(), "test2");
		line.addWorkBench(bench1);
		line.addWorkBench(bench2);
		assertEquals(2, line.getWorkbenches().size());
		assertEquals(bench1, line.getWorkbenches().get(0));
		assertEquals(bench2, line.getWorkbenches().get(1));
	}

	@Test (expected = IllegalArgumentException.class)
	public void TestAddOneValidWorkBenchOneInvalidWorkBench(){
		WorkBench bench = new WorkBench(new ArrayList<String>(), "test");
		line.addWorkBench(bench);
		assertEquals(1, line.getWorkbenches().size());
		assertEquals(bench, line.getWorkbenches().get(0));
		line.addWorkBench(null);
		assertEquals(2, line.getCurrentJobs().size());
	}




	@Test
	public void TestAdvanceOneWorkbench(){
		WorkBench bench = new WorkBench(new ArrayList<String>(), "test");
		Order order1 = new Order("Jef", model, 1);
		Job job = new Job(order1);
		line.addJob(job);
		line.addWorkBench(bench);
		line.advance();
		assertEquals(job, bench.getCurrentJob());
	}

	@Test
	public void TestAdvanceTwoWorkbenches(){
		WorkBench bench1 = new WorkBench(new ArrayList<String>(), "test");
		WorkBench bench2 = new WorkBench(new ArrayList<String>(), "test2");
		Order order1 = new Order("Jef", model, 1);
		Job job = new Job(order1);
		line.addJob(job);
		line.addWorkBench(bench1);
		line.addWorkBench(bench2);
		line.advance();
		assertEquals(job, bench1.getCurrentJob());
		line.advance();
		assertEquals(job, bench2.getCurrentJob());
	}


	@Test
	public void TestAdvanceTwoWorkbenchesTwoJobs(){
		WorkBench bench1 = new WorkBench(new ArrayList<String>(), "test");
		WorkBench bench2 = new WorkBench(new ArrayList<String>(), "test2");
		Order order1 = new Order("Jef", model, 1);
		Job job1 = new Job(order1);
		Job job2 = new Job(order1);
		line.addJob(job1);
		line.addJob(job2);
		line.addWorkBench(bench1);
		line.addWorkBench(bench2);
		line.advance();
		assertEquals(job1, bench1.getCurrentJob());
		line.advance();
		assertEquals(job2, bench1.getCurrentJob());
		assertEquals(job1, bench2.getCurrentJob());
	}

	@Test
	public void TestAdvanceTwoWorkbenchesCompleteFullJob(){
		WorkBench bench1 = new WorkBench(new ArrayList<String>(), "test");
		WorkBench bench2 = new WorkBench(new ArrayList<String>(), "test2");
		bench2.addResponsibility("Body");
		Order order1 = new Order("Jef", model, 1);
		Job job = new Job(order1);
		Task task = new Task("Body");
		Action action = new Action("Spray Colour");
		task.addAction(action);
		job.addTask(task);
		line.addJob(job);
		line.addWorkBench(bench1);
		line.addWorkBench(bench2);
		line.advance();
		assertEquals(job, bench1.getCurrentJob());
		line.advance();
		assertEquals(job, bench2.getCurrentJob());
		action.setCompleted(true);
		line.advance();
		assertEquals(0, line.getCurrentJobs().size());
	}

	@Test
	public void TestAdvanceAfterTenOClock(){
		WorkBench bench = new WorkBench(new ArrayList<String>(), "test");
		Order order1 = new Order("Jef", model, 1);
		Job job = new Job(order1);
		line.addJob(job);
		line.addWorkBench(bench);
		bench.setCurrentJob(job);
		line.getClock().advanceTime(21*60 + 5);


		line.advance();
		assertNull(bench.getCurrentJob());
	}

	@Test(expected=IllegalStateException.class)
	public void TestNoCurrentJobs(){
		WorkBench bench1 = new WorkBench(new ArrayList<String>(), "test");
		line.addWorkBench(bench1);
		line.advance();
	}

	@Test
	public void TestConvertOrderToJobOneCar(){
		Order order = new Order("Stef", model, 1);
		List<Job> jobs = line.convertOrderToJob(order);
		assertEquals(1, jobs.size());
		assertEquals(7, jobs.get(0).getTasks().size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void TestConvertIllegalOrderToJobOneCar(){
		List<Job> jobs = line.convertOrderToJob(null);
		assertEquals(1, jobs.size());
		assertEquals(7, jobs.get(0).getTasks().size());
	}

	@Test
	public void TestConvertOrderToJobTwoCars(){
		Order order = new Order("Stef", model, 2);
		List<Job> jobs = line.convertOrderToJob(order);
		assertEquals(2, jobs.size());
		assertEquals(7, jobs.get(0).getTasks().size());
	}

	@Test
	public void TestEstimatedTime1(){
		Order order = new Order("Stef", model, 2);
		line.getCurrentJobs().addAll(line.convertOrderToJob(order));
		WorkBench bench1 = new WorkBench(new ArrayList<String>(), "test");
		WorkBench bench2 = new WorkBench(new ArrayList<String>(), "test2");
		line.addWorkBench(bench1);
		line.addWorkBench(bench2);
		line.calculateEstimatedTime(order);
		assertEquals(0, order.getEstimatedTime()[0]);
		assertEquals(240, order.getEstimatedTime()[1]);
	}

	@Test
	public void TestEstimatedTime2(){
		Order order = new Order("Stef", model, 20);
		line.getCurrentJobs().addAll(line.convertOrderToJob(order));
		WorkBench bench1 = new WorkBench(new ArrayList<String>(), "test");
		WorkBench bench2 = new WorkBench(new ArrayList<String>(), "test2");
		line.addWorkBench(bench1);
		line.addWorkBench(bench2);
		line.calculateEstimatedTime(order);
		assertEquals(1, order.getEstimatedTime()[0]);
		assertEquals(360 + beginTime , order.getEstimatedTime()[1]); //+begintijd om 6u
	}

	@Test
	public void TestEstimatedTime3(){
		Order order = new Order("Stef", model, 40);
		line.getCurrentJobs().addAll(line.convertOrderToJob(order));
		WorkBench bench1 = new WorkBench(new ArrayList<String>(), "test");
		WorkBench bench2 = new WorkBench(new ArrayList<String>(), "test2");
		line.addWorkBench(bench1);
		line.addWorkBench(bench2);
		line.calculateEstimatedTime(order);
		assertEquals(2, order.getEstimatedTime()[0]);
		assertEquals(beginTime + 720, order.getEstimatedTime()[1]);
	}

	@Test(expected = IllegalStateException.class)
	public void TestEstimatedTime4(){
		Order order = new Order("Stef", model, 1);
		line.getCurrentJobs().addAll(line.convertOrderToJob(order));
		line.calculateEstimatedTime(order);
	}

	@Test
	public void TestEstimatedTime5(){

		Order order = new Order("Stef", model, 40);
		line.addJob(new Job(order));
		line.getCurrentJobs().addAll(line.convertOrderToJob(order));
		WorkBench bench1 = new WorkBench(new ArrayList<String>(), "test");
		WorkBench bench2 = new WorkBench(new ArrayList<String>(), "test2");
		line.addWorkBench(bench1);
		line.addWorkBench(bench2);
		line.advance();
		line.calculateEstimatedTime(order);
		assertEquals(2, order.getEstimatedTime()[0]);
		assertEquals(beginTime + 720, order.getEstimatedTime()[1]);
	}


	@Test(expected= IllegalStateException.class)
	public void TestEstimatedTime6(){

		Order order = new Order("Stef", model, 1);
		WorkBench bench1 = new WorkBench(new ArrayList<String>(), "test");
		WorkBench bench2 = new WorkBench(new ArrayList<String>(), "test2");
		line.addWorkBench(bench1);
		line.addWorkBench(bench2);
		line.calculateEstimatedTime(order);
		assertEquals(2, order.getEstimatedTime()[0]);
		assertEquals(720, order.getEstimatedTime()[1]);
	}


	@Test
	public void TestEstimatedTime7() {
		line.getClock().advanceTime(19*60);
		WorkBench bench1 = new WorkBench(new ArrayList<String>(), "test");
		WorkBench bench2 = new WorkBench(new ArrayList<String>(), "test2");
		line.addWorkBench(bench1);
		line.addWorkBench(bench2);
		Order order = new Order("Stef", model, 1);
		line.addJob(new Job(order));
		line.getCurrentJobs().addAll(line.convertOrderToJob(order));
		line.calculateEstimatedTime(order);
		assertEquals(0, order.getEstimatedTime()[0]);
		assertEquals(line.getClock().getMinutes() + 180, order.getEstimatedTime()[1]);
	}

	@Test
	public void TestFutureAssemblyLine(){
		Order order = new Order("Stef", model, 1);
		Job job = new Job(order);
		line.addJob(job);
		WorkBench bench1 = new WorkBench(new ArrayList<String>(), "test");
		line.addWorkBench(bench1);
		AssemblyLine lineClone = line.getFutureAssemblyLine();
		assertFalse(lineClone.getWorkbenches().get(0).getCurrentJob().equals(line.getWorkbenches().get(0).getCurrentJob()));
		assertNull(line.getWorkbenches().get(0).getCurrentJob());
	}

	@Test
	public void TestToString(){
		assertEquals("", line.toString());
		WorkBench bench1 = new WorkBench(new ArrayList<String>(), "test");
		bench1.addResponsibility("Paint");
		line.addWorkBench(bench1);
		assertEquals("-workbench 1: test", line.toString());
		Order order = new Order("Stef", model, 1);
		Job job = new Job(order);
		Task task = new Task("Paint");
		Action action = new Action("Paint car blue");
		task.addAction(action);
		job.addTask(task);
		bench1.setCurrentJob(job);
		bench1.chooseTasksOutOfJob();
		assertEquals("-workbench 1: test,  *Paint: not completed", line.toString());
		action.setCompleted(true);
		assertEquals("-workbench 1: test,  *Paint: completed", line.toString());
	}

	@Test
	public void TestOvertime(){
		WorkBench bench1 = new WorkBench(new ArrayList<String>(), "test");
		bench1.addResponsibility("Paint");
		line.addWorkBench(bench1);
		Order order = new Order("Stef", model, 1);
		Job job = new Job(order);
		Task task = new Task("Paint");
		Action action = new Action("Paint car blue");
		action.setCompleted(false);
		task.addAction(action);
		job.addTask(task);
		line.addJob(job);
		bench1.setCurrentJob(job);
		bench1.chooseTasksOutOfJob();		

		line.getClock().advanceTime(23*60);
		line.advance();
		assertEquals(60, line.getOvertime());
	}
	
	@Test
	public void TestIllegalAssemblyLine(){
		WorkBench bench1 = new WorkBench(new ArrayList<String>(), "test");
		bench1.addResponsibility("Paint");
		line.addWorkBench(bench1);
		AssemblyLine future = line.getFutureAssemblyLine();
		assertEquals(line.getWorkbenches().get(0).getCurrentJob(), future.getWorkbenches().get(0).getCurrentJob());
	}
}
