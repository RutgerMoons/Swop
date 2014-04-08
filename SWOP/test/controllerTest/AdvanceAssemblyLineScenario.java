package controllerTest;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ui.IClientCommunication;
import ui.ClientCommunication;

import com.google.common.base.Optional;

import controller.AdvanceAssemblyLineFlowController;
import domain.assembly.AssemblyLine;
import domain.assembly.WorkBench;
import domain.car.Airco;
import domain.car.Body;
import domain.car.CarModel;
import domain.car.Color;
import domain.car.Engine;
import domain.car.Gearbox;
import domain.car.Seat;
import domain.car.Wheel;
import domain.clock.Clock;
<<<<<<< HEAD
import domain.job.Action;
import domain.job.IJob;
import domain.job.Job;
import domain.job.Task;
=======
>>>>>>> origin/stef
import domain.order.StandardOrder;
import domain.users.Manager;
import domain.users.Worker;

@RunWith(Parameterized.class)
public class AdvanceAssemblyLineScenario {

	public IClientCommunication uiFacade;
	public AdvanceAssemblyLineFlowController advAss;
	public AssemblyLine assembly;
	public Job job;

	public AdvanceAssemblyLineScenario(IClientCommunication ui){
		uiFacade = ui;
	}

	@Before
	public void setup(){
		Clock clock = new Clock();
		assembly = new AssemblyLine(clock);
		advAss = new AdvanceAssemblyLineFlowController(uiFacade, assembly, clock);
		CarModel model = new CarModel("it's me", new Airco("manual"), new Body("break"), new Color("red"), new Engine("bla"), new Gearbox("manual"), new Seat("vinyl grey"), new Wheel("comfort"));
		StandardOrder order = new StandardOrder("Luigi", model, 5);
		job = new Job(order);
	}
	
	/*
	 * assumption: user is logged in as a manager 
	 */
	
	@Test
	public void useCaseTest(){
		/*
		 * assemblyLine will be advanced correctly
		 * sometimes with wrong input, 
		 * this is caught as expected
		 * 
		 * Normal flow
		 */
		try{
			String s = System.lineSeparator(); 
			String input = "200" + s // incorrect input 		| step 1
							+ "Y" + s 
							+ "200" + s // incorrect input 		| step 3
							+ "Y" + s 
							+ "Y" + s  // incorrect input
							+ "200" + s 
							+ "Y" + s; // too much input 		| step 5
			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
			System.setIn(in);
			
			
			ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			
			uiFacade = new ClientCommunication();
			Clock clock = new Clock();
			assembly = new AssemblyLine(clock);
			advAss = new AdvanceAssemblyLineFlowController(uiFacade, assembly, clock);
			
			assembly.addJob(job);
			Manager manager = new Manager("name");
			assertTrue(advAss.mayUseThisHandler(manager));
			advAss.executeUseCase(manager);
			String output = myout.toString();

			assertEquals("Do you want advance the assemblyLine? Y/N" + s 		//step 1
							+ "Sorry, that's not a valid response" + s + s
							+ "Do you want advance the assemblyLine? Y/N" + s
							+ "current assemblyline:" + s  						//step 2
							+ "-workbench 1: car body" + s 
							+ "-workbench 2: drivetrain" + s 
							+ "-workbench 3: accessories" + s + s
							+ "future assemblyline:" + s 
							+ "-workbench 1: car body" + s 
							+ "-workbench 2: drivetrain" + s 
							+ "-workbench 3: accessories" + s + s
							+ "Do you want to continue? Y/N" + s				//step 3
							+ "Sorry, that's not a valid response" + s + s
							+ "Do you want to continue? Y/N" + s
							+ "How much time has passed? (minutes, type a negative number if this is the start of the day)" + s
							+ "How much time has passed? (minutes, type a negative number if this is the start of the day)" + s
							+ "current assemblyline:" + s 						//step 5
							+ "-workbench 1: car body" + s 
							+ "-workbench 2: drivetrain" + s 
							+ "-workbench 3: accessories" + s + s
							+ "Press enter when you're finished" + s, output);	//step 6: use case ends
		} finally {
		} 
	}
	
	@Test
	public void useCaseTestAlternateFlow(){
		/*
		 * assemblyLine will be advanced correctly
		 * sometimes with wrong input, 
		 * this is caught as expected
		 * 
		 * Normal flow
		 */
		try{
			String s = System.lineSeparator(); 
			String input = "200" + s // incorrect input			|step 1
							+ "Y" + s 
							+ "200" + s // incorrect input		|step 3
							+ "Y" + s 
							+ "Y" + s  // incorrect input
							+ "200" + s 
							+ "Y" + s; // too much input		|step 5b
			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
			System.setIn(in);
			
			
			ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			
			uiFacade = new ClientCommunication();
			Clock clock = new Clock();
			assembly = new AssemblyLine(clock);
			advAss = new AdvanceAssemblyLineFlowController(uiFacade, assembly, clock);
			
			CarModel model = new CarModel("it's me", new Airco("manual"), new Body("break"), new Color("red"), new Engine("bla"), new Gearbox("manual"), new Seat("vinyl grey"), new Wheel("comfort"));
			StandardOrder order = new StandardOrder("Luigi", model, 5);
			IJob job = new Job(order);
			Action action = new Action("action");
			action.setCompleted(false);
			Task task = new Task("Paint");
			task.addAction(action);
			((Job) job).addTask(task);
			
			assembly.addJob(job);
			((WorkBench) assembly.getWorkbenches().get(0)).setCurrentJob(Optional.fromNullable(job));
			((WorkBench) assembly.getWorkbenches().get(0)).chooseTasksOutOfJob();
			advAss = new AdvanceAssemblyLineFlowController(uiFacade, assembly, clock);
			
			Manager manager = new Manager("name");
			assertTrue(advAss.mayUseThisHandler(manager));
			advAss.executeUseCase(manager);
			String output = myout.toString();

			assertEquals("Do you want advance the assemblyLine? Y/N" + s							//step 1
							+ "Sorry, that's not a valid response" + s + s
							+ "Do you want advance the assemblyLine? Y/N" + s
							+ "current assemblyline:" + s 											//Step 2
							+ "-workbench 1: car body" + s 
							+ "  *Paint: not completed" + s
							+ "-workbench 2: drivetrain" + s 
							+ "-workbench 3: accessories" + s + s
							+ "future assemblyline:" + s 
							+ "-workbench 1: car body" + s 
							+ "  *Paint: not completed" + s
							+ "-workbench 2: drivetrain" + s 
							+ "-workbench 3: accessories" + s + s
							+ "Do you want to continue? Y/N" + s									//step 3
							+ "Sorry, that's not a valid response" + s + s
							+ "Do you want to continue? Y/N" + s
							+ "AssemblyLine can't be advanced because of workbench [1]" + s + s		//step 5b
							+ "current assemblyline:" + s 
							+ "-workbench 1: car body" + s 
							+ "  *Paint: not completed" + s
							+ "-workbench 2: drivetrain" + s + "-workbench 3: accessories" + s + s
							+ "Press enter when you're finished" + s, output);						//step 6
		} finally {
		} 
	}
	
	@Test
	public void noIndicationToAdvance(){
		try{
			// indicates he doesn't want to advance
			// use case ends
			String s = System.lineSeparator(); 
			String input = "N" + s;		//step 1
			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
			System.setIn(in);
			
			
			ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			
			uiFacade = new ClientCommunication();
			Clock clock = new Clock();
			assembly = new AssemblyLine(clock);
			advAss = new AdvanceAssemblyLineFlowController(uiFacade, assembly, clock);
			
			assembly.addJob(job);
			Manager manager = new Manager("name");
			assertTrue(advAss.mayUseThisHandler(manager));
			advAss.executeUseCase(manager);
			String output = myout.toString();

			assertEquals("Do you want advance the assemblyLine? Y/N" + s, output); //Step 1
			
			//use case ends here

		} finally {
		} 
	}
	
	@Test
	public void wrongUser() {
		try{
			// indicates he doesn't want to advance
			// use case ends
			
			uiFacade = new ClientCommunication();
			Clock clock = new Clock();
			assembly = new AssemblyLine(clock);
			advAss = new AdvanceAssemblyLineFlowController(uiFacade, assembly, clock);
			
			Worker worker = new Worker("name");
			assertFalse(advAss.mayUseThisHandler(worker));
			//flow stops here

		} finally {
		} 
	}
	
	@Parameterized.Parameters
	public static Collection<Object[]> instancesToTest() { 
		return Arrays.asList(
				new Object[][]{{new ClientCommunication()}});
	}

}
