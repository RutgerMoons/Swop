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
	private int beginTime = 6*60;
	@Before
	public void initialize() throws AlreadyInMapException{
		line = new AssemblyLine(new ClockObserver());

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
		new AssemblyLine(null);
	}
	@Test
	public void TestSetValidCurrentJobs(){
		List<IJob> jobs = new ArrayList<>();
		StandardOrder order1 = new StandardOrder("Jef", model, 1);
		jobs.add(new Job(order1));
		jobs.add(new Job(order1));
		line.setCurrentJobs(jobs);
		assertEquals(2, line.getCurrentJobs().size());
		assertEquals(jobs, line.getCurrentJobs());
	}

	@Test (expected=IllegalArgumentException.class)
	public void TestSetInvalidCurrentJobs(){
		List<IJob> jobs = null;
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
		List<IWorkBench> workbenches = new ArrayList<IWorkBench>();
		workbenches.add(new WorkBench(new HashSet<String>(), "test"));
		workbenches.add(new WorkBench(new HashSet<String>(), "test2"));
		line.setWorkbenches(workbenches);
		assertEquals(2, line.getWorkbenches().size());
		assertEquals(workbenches, line.getWorkbenches());
	}

	@Test (expected = IllegalArgumentException.class)
	public void TestSetInvalidWorkBenches(){
		List<IWorkBench> workbenches = null;
		line.setWorkbenches(workbenches);
		assertEquals(0, line.getWorkbenches().size());
		assertEquals(workbenches, line.getWorkbenches());
	}

	@Test
	public void TestAddOneValidJob(){
		StandardOrder order1 = new StandardOrder("Jef", model, 1);
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
		StandardOrder order1 = new StandardOrder("Jef", model, 1);
		Job job1 = new Job(order1);
		Job job2 = new Job(order1);
		line.addJob(job1);
		line.addJob(job2);
		assertEquals(2, line.getCurrentJobs().size());
		assertEquals(job1, line.getCurrentJobs().get(0));
		assertEquals(job2, line.getCurrentJobs().get(1));
	}
	
	@Test
	public void TestAddMultipleJobs(){
		StandardOrder order1 = new StandardOrder("Jef", model, 1);
		Job job1 = new Job(order1);
		Job job2 = new Job(order1);
		List<IJob> jobs = new ArrayList<>();
		jobs.add(job1);
		jobs.add(job2);
		line.addMultipleJobs(jobs);
		assertEquals(2, line.getCurrentJobs().size());
		assertEquals(job1, line.getCurrentJobs().get(0));
		assertEquals(job2, line.getCurrentJobs().get(1));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void TestAddMultipleJobsNull(){
		line.addMultipleJobs(null);
	}

	@Test (expected = IllegalArgumentException.class)
	public void TestAddOneValidJobOneInvalidJob(){
		StandardOrder order1 = new StandardOrder("Jef", model, 1);
		Job job = new Job(order1);
		line.addJob(job);
		assertEquals(1, line.getCurrentJobs().size());
		assertEquals(job, line.getCurrentJobs().get(0));
		line.addJob(null);
		assertEquals(2, line.getCurrentJobs().size());
	}

	@Test
	public void TestAddOneValidWorkBench(){
		WorkBench workBench = new WorkBench(new HashSet<String>(), "test");
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
		WorkBench bench1 = new WorkBench(new HashSet<String>(), "test");
		WorkBench bench2 = new WorkBench(new HashSet<String>(), "test2");
		line.addWorkBench(bench1);
		line.addWorkBench(bench2);
		assertEquals(2, line.getWorkbenches().size());
		assertEquals(bench1, line.getWorkbenches().get(0));
		assertEquals(bench2, line.getWorkbenches().get(1));
	}

	@Test (expected = IllegalArgumentException.class)
	public void TestAddOneValidWorkBenchOneInvalidWorkBench(){
		WorkBench bench = new WorkBench(new HashSet<String>(), "test");
		line.addWorkBench(bench);
		assertEquals(1, line.getWorkbenches().size());
		assertEquals(bench, line.getWorkbenches().get(0));
		line.addWorkBench(null);
		assertEquals(2, line.getCurrentJobs().size());
	}




	@Test
	public void TestAdvanceOneWorkbench(){
		WorkBench bench = new WorkBench(new HashSet<String>(), "test");
		StandardOrder order1 = new StandardOrder("Jef", model, 1);
		Job job = new Job(order1);
		line.addJob(job);
		line.addWorkBench(bench);
		line.advance();
		assertEquals(bench.getCurrentJob().get(), job);
	}

	@Test
	public void TestAdvanceTwoWorkbenches(){
		WorkBench bench1 = new WorkBench(new HashSet<String>(), "test");
		WorkBench bench2 = new WorkBench(new HashSet<String>(), "test2");
		StandardOrder order1 = new StandardOrder("Jef", model, 1);
		Job job = new Job(order1);
		line.addJob(job);
		line.addWorkBench(bench1);
		line.addWorkBench(bench2);
		line.advance();
		assertEquals(bench1.getCurrentJob().get(), job);
		line.advance();
		assertEquals(bench2.getCurrentJob().get(), job);
	}


	@Test
	public void TestAdvanceTwoWorkbenchesTwoJobs(){
		WorkBench bench1 = new WorkBench(new HashSet<String>(), "test");
		WorkBench bench2 = new WorkBench(new HashSet<String>(), "test2");
		StandardOrder order1 = new StandardOrder("Jef", model, 1);
		Job job1 = new Job(order1);
		Job job2 = new Job(order1);
		line.addJob(job1);
		line.addJob(job2);
		line.addWorkBench(bench1);
		line.addWorkBench(bench2);
		line.advance();
		assertEquals(bench1.getCurrentJob().get(), job1);
		line.advance();
		assertEquals(bench1.getCurrentJob().get(), job2);
		assertEquals(bench2.getCurrentJob().get(), job1);
	}

	@Test
	public void TestAdvanceTwoWorkbenchesCompleteFullJob(){
		WorkBench bench1 = new WorkBench(new HashSet<String>(), "test");
		WorkBench bench2 = new WorkBench(new HashSet<String>(), "test2");
		bench2.addResponsibility("Body");
		StandardOrder order1 = new StandardOrder("Jef", model, 1);
		Job job = new Job(order1);
		Task task = new Task("Body");
		Action action = new Action("Spray Colour");
		task.addAction(action);
		job.addTask(task);
		line.addJob(job);
		line.addWorkBench(bench1);
		line.addWorkBench(bench2);
		line.advance();
		assertEquals(bench1.getCurrentJob().get(), job);
		line.advance();
		assertEquals(bench2.getCurrentJob().get(), job);
		action.setCompleted(true);
		line.advance();
		assertEquals(0, line.getCurrentJobs().size());
	}

	@Test
	public void TestAdvanceAfterTenOClock(){
		WorkBench bench = new WorkBench(new HashSet<String>(), "test");
		StandardOrder order1 = new StandardOrder("Jef", model, 1);
		IJob job = new Job(order1);
		line.addJob(job);
		line.addWorkBench(bench);
		bench.setCurrentJob(Optional.fromNullable(job));
		line.getClock().advanceTime(21*60 + 5);


		line.advance();
		assertEquals(Optional.absent(), bench.getCurrentJob());
	}

	@Test(expected=IllegalStateException.class)
	public void TestNoCurrentJobs(){
		WorkBench bench1 = new WorkBench(new HashSet<String>(), "test");
		line.addWorkBench(bench1);
		line.advance();
	}

	@Test
	public void TestConvertOrderToJobOneCar() throws AlreadyInMapException{
		StandardOrder order = new StandardOrder("Stef", model, 1);
		model.addCarPart(new CarPart("manual", true, CarPartType.AIRCO));
		model.addCarPart(new CarPart("sedan", false, CarPartType.BODY));
		model.addCarPart(new CarPart("red", false, CarPartType.COLOR));
		model.addCarPart(new CarPart("standard 2l 4 cilinders", false, CarPartType.ENGINE));
		model.addCarPart(new CarPart("6 speed manual", false, CarPartType.GEARBOX));
		model.addCarPart(new CarPart("leather black", false, CarPartType.SEATS));
		model.addCarPart(new CarPart("comfort", false, CarPartType.WHEEL));
		List<IJob> jobs = line.convertOrderToJob(order);
		assertEquals(1, jobs.size());
		assertEquals(7, jobs.get(0).getTasks().size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void TestConvertIllegalOrderToJobOneCar(){
		List<IJob> jobs = line.convertOrderToJob(null);
		assertEquals(1, jobs.size());
		assertEquals(7, jobs.get(0).getTasks().size());
	}

	@Test
	public void TestConvertOrderToJobTwoCars() throws AlreadyInMapException{
		StandardOrder order = new StandardOrder("Stef", model, 2);
		
		
		model.addCarPart(new CarPart("manual", true, CarPartType.AIRCO));
		model.addCarPart(new CarPart("sedan", false, CarPartType.BODY));
		model.addCarPart(new CarPart("red", false, CarPartType.COLOR));
		model.addCarPart(new CarPart("standard 2l 4 cilinders", false, CarPartType.ENGINE));
		model.addCarPart(new CarPart("6 speed manual", false, CarPartType.GEARBOX));
		model.addCarPart(new CarPart("leather black", false, CarPartType.SEATS));
		model.addCarPart(new CarPart("comfort", false, CarPartType.WHEEL));
		
		List<IJob> jobs = line.convertOrderToJob(order);
		assertEquals(2, jobs.size());
		assertEquals(7, jobs.get(0).getTasks().size());
	}

	
	@Test
	public void TestEstimatedTime1(){
		StandardOrder order = new StandardOrder("Stef", model, 2);
		line.addMultipleJobs(line.convertOrderToJob(order));
		WorkBench bench1 = new WorkBench(new HashSet<String>(), "test");
		WorkBench bench2 = new WorkBench(new HashSet<String>(), "test2");
		line.addWorkBench(bench1);
		line.addWorkBench(bench2);
		line.calculateEstimatedTime(order);
		assertEquals(0, order.getEstimatedTime()[0]);
		assertEquals(240, order.getEstimatedTime()[1]);
	}

	@Test
	public void TestEstimatedTime2(){
		StandardOrder order = new StandardOrder("Stef", model, 20);
		line.addMultipleJobs(line.convertOrderToJob(order));
		WorkBench bench1 = new WorkBench(new HashSet<String>(), "test");
		WorkBench bench2 = new WorkBench(new HashSet<String>(), "test2");
		line.addWorkBench(bench1);
		line.addWorkBench(bench2);
		line.calculateEstimatedTime(order);
		assertEquals(1, order.getEstimatedTime()[0]);
		assertEquals(360 + beginTime , order.getEstimatedTime()[1]); //+begintijd om 6u
	}

	@Test
	public void TestEstimatedTime3(){
		StandardOrder order = new StandardOrder("Stef", model, 40);
		line.addMultipleJobs(line.convertOrderToJob(order));
		WorkBench bench1 = new WorkBench(new HashSet<String>(), "test");
		WorkBench bench2 = new WorkBench(new HashSet<String>(), "test2");
		line.addWorkBench(bench1);
		line.addWorkBench(bench2);
		line.calculateEstimatedTime(order);
		assertEquals(2, order.getEstimatedTime()[0]);
		assertEquals(beginTime + 720, order.getEstimatedTime()[1]);
	}

	@Test(expected = IllegalStateException.class)
	public void TestEstimatedTime4(){
		StandardOrder order = new StandardOrder("Stef", model, 1);
		line.addMultipleJobs(line.convertOrderToJob(order));
		line.calculateEstimatedTime(order);
	}

	@Test
	public void TestEstimatedTime5(){

		StandardOrder order = new StandardOrder("Stef", model, 40);
		line.addJob(new Job(order));
		line.addMultipleJobs(line.convertOrderToJob(order));
		WorkBench bench1 = new WorkBench(new HashSet<String>(), "test");
		WorkBench bench2 = new WorkBench(new HashSet<String>(), "test2");
		line.addWorkBench(bench1);
		line.addWorkBench(bench2);
		line.advance();
		line.calculateEstimatedTime(order);
		assertEquals(2, order.getEstimatedTime()[0]);
		assertEquals(beginTime + 720, order.getEstimatedTime()[1]);
	}


	@Test(expected= IllegalStateException.class)
	public void TestEstimatedTime6(){

		StandardOrder order = new StandardOrder("Stef", model, 1);
		WorkBench bench1 = new WorkBench(new HashSet<String>(), "test");
		WorkBench bench2 = new WorkBench(new HashSet<String>(), "test2");
		line.addWorkBench(bench1);
		line.addWorkBench(bench2);
		line.calculateEstimatedTime(order);
		assertEquals(2, order.getEstimatedTime()[0]);
		assertEquals(720, order.getEstimatedTime()[1]);
	}


	@Test
	public void TestEstimatedTime7() {
		line.getClock().advanceTime(19*60);
		WorkBench bench1 = new WorkBench(new HashSet<String>(), "test");
		WorkBench bench2 = new WorkBench(new HashSet<String>(), "test2");
		line.addWorkBench(bench1);
		line.addWorkBench(bench2);
		StandardOrder order = new StandardOrder("Stef", model, 1);
		line.addJob(new Job(order));
		line.addMultipleJobs(line.convertOrderToJob(order));
		line.calculateEstimatedTime(order);
		assertEquals(0, order.getEstimatedTime()[0]);
		assertEquals(line.getClock().getMinutes() + 180, order.getEstimatedTime()[1]);
	}

	
	@Test (expected = IllegalArgumentException.class)
	public void TestEstimatedTimeInvalidOrder(){
		line.addWorkBench(new WorkBench(new HashSet<String>(), "test"));
		line.calculateEstimatedTime(null);
		}
	
	@Test
	public void TestFutureAssemblyLine(){
		StandardOrder order = new StandardOrder("Stef", model, 1);
		Job job = new Job(order);
		line.addJob(job);
		WorkBench bench1 = new WorkBench(new HashSet<String>(), "test");
		line.addWorkBench(bench1);
		AssemblyLine lineClone = line.getFutureAssemblyLine();
		assertFalse(lineClone.getWorkbenches().get(0).getCurrentJob().equals(line.getWorkbenches().get(0).getCurrentJob()));
		assertEquals(Optional.absent(), line.getWorkbenches().get(0).getCurrentJob());
	}

	@Test
	public void TestToString(){
		assertEquals("", line.toString());
		WorkBench bench1 = new WorkBench(new HashSet<String>(), "test");
		bench1.addResponsibility("Paint");
		line.addWorkBench(bench1);
		assertEquals("-workbench 1: test", line.toString());
		StandardOrder order = new StandardOrder("Stef", model, 1);
		IJob job = new Job(order);
		ITask task = new Task("Paint");
		IAction action = new Action("Paint car blue");
		((Task) task).addAction(action);
		((Job) job).addTask(task);
		bench1.setCurrentJob(Optional.fromNullable(job));
		bench1.chooseTasksOutOfJob();
		assertEquals("-workbench 1: test,  *Paint: not completed", line.toString());
		((Action) action).setCompleted(true);
		assertEquals("-workbench 1: test,  *Paint: completed", line.toString());
	}

	@Test
	public void TestOvertime() throws ImmutableException{
		WorkBench bench1 = new WorkBench(new HashSet<String>(), "test");
		bench1.addResponsibility("Paint");
		line.addWorkBench(bench1);
		StandardOrder order = new StandardOrder("Stef", model, 1);
		IJob job = new Job(order);
		ITask task = new Task("Paint");
		IAction action = new Action("Paint car blue");
		action.setCompleted(false);
		task.addAction(action);
		job.addTask(task);
		line.addJob(job);
		bench1.setCurrentJob(Optional.fromNullable(job));
		bench1.chooseTasksOutOfJob();		

		line.getClock().advanceTime(23*60);
		line.advance();
		assertEquals(60, line.getOvertime());
	}
	
	@Test
	public void TestIllegalAssemblyLine(){
		WorkBench bench1 = new WorkBench(new HashSet<String>(), "test");
		bench1.addResponsibility("Paint");
		line.addWorkBench(bench1);
		AssemblyLine future = line.getFutureAssemblyLine();
		assertEquals(line.getWorkbenches().get(0).getCurrentJob(), future.getWorkbenches().get(0).getCurrentJob());
	}
	
	@Test
	public void testCanAdvanceAndBlockingWorkBenches() throws ImmutableException{
		assertTrue(line.canAdvance());
		
		WorkBench bench1 = new WorkBench(new HashSet<String>(), "test");
		bench1.addResponsibility("Paint");
		line.addWorkBench(bench1);
		StandardOrder order = new StandardOrder("Stef", model, 1);
		IJob job = new Job(order);
		ITask task = new Task("Paint");
		IAction action = new Action("Paint car blue");
		action.setCompleted(false);
		task.addAction(action);
		job.addTask(task);
		line.addJob(job);
		bench1.setCurrentJob(Optional.fromNullable(job));
		bench1.chooseTasksOutOfJob();		
		assertFalse(line.canAdvance());
		
		assertEquals(1, (int)line.getBlockingWorkBenches().get(0));
	}
}
