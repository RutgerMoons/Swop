package uiTest;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import ui.UserInterface;

public class UserInterfaceTest {

	@Before
	public void init() {
		System.setIn(System.in);
		System.setOut(System.out);
	}
	
	@Test
	public void testGetNameAndAskQuestion() {
		try{
			
			String input = "naam";
			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
			System.setIn(in);
			
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			            
			UserInterface userInterface = new UserInterface();
			String name = userInterface.getName();
			
			String output = myout.toString();
			
			assertEquals("Hello user, what's your name?\n", output);
			assertEquals("naam", name);
			

		} catch(Throwable t) {}
	}
	
	@Test
	public void testGetQuantity() {
		try{
			
			String input = "5";
			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
			System.setIn(in);
			
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			            
			UserInterface userInterface = new UserInterface();
			int quantity = userInterface.getQuantity();
			
			String output = myout.toString();
			
			assertEquals("How many cars do you want to order?\n", output);
			assertEquals(5, quantity);
			

		} catch (Throwable t) {} 
	}
	
	@Test
	public void testGetQuantityWrongInput1() {
		try {
			String s = System.lineSeparator();
			String input = "-1" + s + "5" + s;
			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
			System.setIn(in);
			
			
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			            
			UserInterface userInterface = new UserInterface();
			int quantity = userInterface.getQuantity();
			
			String output = myout.toString();
			
			assertEquals("How many cars do you want to order?" + s + 
							"Sorry, that's not a valid response" + s + s +
							"How many cars do you want to order?" + s, output);
			assertEquals(5, quantity);
			

		} finally {}
	
	}
	
	@Test
	public void testGetQuantityWrongInput2() {
		try {
			String s = System.lineSeparator();
			String input = "derp" + s + "5" + s;
			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
			System.setIn(in);
			
			
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			            
			UserInterface userInterface = new UserInterface();
			int quantity = userInterface.getQuantity();
			
			String output = myout.toString();
			
			assertEquals("How many cars do you want to order?" + s + 
							"How many cars do you want to order?" + s, output);
			assertEquals(5, quantity);
			

		} finally {}
	}
	
	@Test
	public void testInvalidUserPrompt() {
		
		try {
			String s = System.lineSeparator();
			String input = "derp" + s + "5" + s;
			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
			System.setIn(in);
			
			
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			            
			UserInterface userInterface = new UserInterface();
			userInterface.invalidAnswerPrompt();
			
			String output = myout.toString();
			
			assertEquals("Sorry, that's not a valid response" + s + s, output);
			

		} finally {}
		
	}
	
	@Test
	public void testInvalidAnswerPrompt() {
		
		try {
			String s = System.lineSeparator();
			String input = "derp" + s + "5" + s;
			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
			System.setIn(in);
			
			
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			            
			UserInterface userInterface = new UserInterface();
			userInterface.invalidUserPrompt();
			
			String output = myout.toString();
			
			assertEquals("You don't have any rights" + s + s, output);
			

		} finally {}
		
	}
	
	@Test
	public void testAskQuestionLoop() {
			
		try {
			String s = System.lineSeparator();
			String input = "derp" + s + "5" + s + "Y" + s;
			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
			System.setIn(in);
			
			
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			            
			UserInterface userInterface = new UserInterface();
			boolean b = userInterface.askContinue();
			
			String output = myout.toString();
			
			assertEquals(true, b);
			assertEquals("Do you want to continue? Y/N" + s + 
					"Sorry, that's not a valid response" + s + s +
					"Do you want to continue? Y/N" + s+ 
					"Sorry, that's not a valid response" + s + s +
					"Do you want to continue? Y/N" + s, output);
			

		} finally {}
	}
	
	@Test
	public void testAskFinished() {
			
		try {
			String s = System.lineSeparator();
			String input = "testinput" + s;
			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
			System.setIn(in);
			
			
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			            
			UserInterface userInterface = new UserInterface();
			String test = userInterface.askFinished();
			
			String output = myout.toString();
			
			assertEquals("testinput", test);
			assertEquals("Press enter when you're finished" + s, output);
			
		} finally {}
	}
	
	@Test
	public void testAskAdvance() {
			
		try {
			String s = System.lineSeparator();
			String input = "derp" + s + "5" + s + "N" + s;
			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
			System.setIn(in);
			
			
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			            
			UserInterface userInterface = new UserInterface();
			boolean b = userInterface.askAdvance();
			
			String output = myout.toString();
			
			assertEquals(false, b);
			assertEquals("Do you want advance the assemblyLine? Y/N" + s + 
					"Sorry, that's not a valid response" + s + s +
					"Do you want advance the assemblyLine? Y/N" + s+ 
					"Sorry, that's not a valid response" + s + s +
					"Do you want advance the assemblyLine? Y/N" + s, output);
			
		} finally {}
	}
	
	@Test
	public void testGetElapsedTimePositive() {
			
		try {
			String s = System.lineSeparator();
			String input = "derp" + s + "5" + s;
			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
			System.setIn(in);
			
			
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			            
			UserInterface userInterface = new UserInterface();
			int time = userInterface.getElapsedTime();
			
			String output = myout.toString();
			
			assertEquals(5, time);
			assertEquals("How much time has passed? (minutes, type a negative number if this is the start of the day)" + s + 
					"How much time has passed? (minutes, type a negative number if this is the start of the day)" + s, output);
			

		} finally {}
	}
	
	@Test
	public void testGetElapsedTimeNegative() {
			
		try {
			String s = System.lineSeparator();
			String input = "derp" + s + "-5" + s;
			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
			System.setIn(in);
			
			
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			            
			UserInterface userInterface = new UserInterface();
			int time = userInterface.getElapsedTime();
			
			String output = myout.toString();
			
			assertEquals(-5, time);
			assertEquals("How much time has passed? (minutes, type a negative number if this is the start of the day)" + s + 
					"How much time has passed? (minutes, type a negative number if this is the start of the day)" + s, output);
			

		} finally {}
	}
	
	/**
	 * 
	 * Alle Show** functies
	 * 
	 */
	
	@Test 
	public void testShowPendingOrders() {
		try {
			String s = System.lineSeparator();
			
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			            
			UserInterface userInterface = new UserInterface();
			ArrayList<String> messages = new ArrayList<>();
			messages.add("5 Lada");
			messages.add("10 Polo");
			userInterface.showPendingOrders(messages);
			
			String output = myout.toString();
			
			assertEquals("Your pending orders:" + s + 
					"5 Lada" + s +
					"10 Polo" + s + s, output);
			
		} finally {}
	}
	
	@Test 
	public void testShowPendingOrders2() {
		try {
			String s = System.lineSeparator();
			
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			            
			UserInterface userInterface = new UserInterface();
			userInterface.showPendingOrders(null);
			
			String output = myout.toString();
			
			assertEquals("You have no pending Orders" + s + s, output);
			
		} finally {}
	}
	
	
	
	@Test 
	public void testShowCompletedOrders1() {
		try {
			String s = System.lineSeparator();
			
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			            
			UserInterface userInterface = new UserInterface();
			ArrayList<String> messages = new ArrayList<>();
			messages.add("5 Lada");
			messages.add("10 Polo");
			userInterface.showCompletedOrders(messages);
			
			String output = myout.toString();
			
			assertEquals("Your completed orders:" + s + 
					"5 Lada" + s +
					"10 Polo" + s + s, output);
			
		} finally {}
	}
	
	@Test 
	public void testShowCompletedOrders2() {
		try {
			String s = System.lineSeparator();
			
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			            
			UserInterface userInterface = new UserInterface();
			userInterface.showCompletedOrders(null);
			
			String output = myout.toString();
			
			assertEquals("You have no completed Orders" + s + s, output);
			
		} finally {}
	}
	
	@Test 
	public void testShowOrder1() {
		try {
			String s = System.lineSeparator();
			
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			            
			UserInterface userInterface = new UserInterface();
			userInterface.showOrder(5, "Lada", new int[] {0, 125});
			
			String output = myout.toString();
			
			assertEquals("Your order:" + s + 5 + " " + 
				"Lada" + " Estimated completion time: day " + 
				0 + " " + 
				2 + "h" + 
				5 + s + s, output);
			
		} finally {}
	}
	
	@Test 
	public void testShowOrder2() {
		try {
			String s = System.lineSeparator();
			
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			            
			UserInterface userInterface = new UserInterface();
			userInterface.showOrder(5, "Lada", new int[] {-1, 125});
			
			String output = myout.toString();
			
			assertEquals("Your order:" + s + 5 + " " + "Lada" + s + s, output);
			
		} finally {}
	}
	
	@Test 
	public void testShowWorkBenchCompleted() {
		try {
			String s = System.lineSeparator();
			
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			            
			UserInterface userInterface = new UserInterface();
			userInterface.showWorkBenchCompleted();
			
			String output = myout.toString();
			
			assertEquals("All the tasks at this workbench are completed" + s + s, output);
			
		} finally {}
	}
	
	@Test 
	public void testShowAssemblyLine() {
		try {
			String s = System.lineSeparator();
			String tense = "current";
			String assemblyline = "car body,drive train,accessories";
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			            
			UserInterface userInterface = new UserInterface();
			userInterface.showAssemblyLine(assemblyline, tense);;
			
			String output = myout.toString();
			
			assertEquals("current assemblyline:" + s + 
							"car body" + s +
							"drive train" + s +
							"accessories" + s + s, output);
			
		} finally {}
	}
	
	@Test 
	public void testShowTask() {
		try {
			String s = System.lineSeparator();
			String task = "action 1,action 2,action 3";
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			            
			UserInterface userInterface = new UserInterface();
			userInterface.showChosenTask(task);
			
			String output = myout.toString();
			
			assertEquals("Your task: " + s + 
							"action 1" + s +
							"action 2" + s +
							"action 3" + s + s, output);
			
		} finally {}
	}
	
	@Test 
	public void testShowBlockingBenches() {
		try {
			String s = System.lineSeparator();
			ArrayList<Integer> blocking = new ArrayList<>(Arrays.asList(1,2,3));
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			            
			UserInterface userInterface = new UserInterface();
			userInterface.showBlockingBenches(blocking);
			
			String output = myout.toString();
			
			assertEquals("AssemblyLine can't be advanced because of workbench " + "[1, 2, 3]" + s + s, output);
			
		} finally {}
	}
	
	/*
	 * 
	 * ALLE Choose** functies
	 * 
	 */
	
	@Test
	public void testChooseRoleManager() {
		try {
			String s = System.lineSeparator();
			String input = "ManAgER" + s + "5" + s + "manager" + s;
			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
			System.setIn(in);
			
			
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			            
			UserInterface userInterface = new UserInterface();
			String role = userInterface.chooseRole();
			
			String output = myout.toString();
			
			assertEquals("manager",  role);
			assertEquals("What's your role: manager, garageholder or worker?" + s + 
					"Sorry, that's not a valid response" + s + s +
					"What's your role: manager, garageholder or worker?" + s+ 
					"Sorry, that's not a valid response" + s + s +
					"What's your role: manager, garageholder or worker?" + s, output);
			
		} finally {}
	}
	
	@Test
	public void testChooseRoleWorker() {
		try {
			String s = System.lineSeparator();
			String input = "ManAgER" + s + "5" + s + "worker" + s;
			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
			System.setIn(in);
			
			
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			            
			UserInterface userInterface = new UserInterface();
			String role = userInterface.chooseRole();
			
			String output = myout.toString();
			
			assertEquals("worker",  role);
			assertEquals("What's your role: manager, garageholder or worker?" + s + 
					"Sorry, that's not a valid response" + s + s +
					"What's your role: manager, garageholder or worker?" + s+ 
					"Sorry, that's not a valid response" + s + s +
					"What's your role: manager, garageholder or worker?" + s, output);
			
		} finally {}
	}
	
	@Test
	public void testChooseRoleGarageholder() {
		try {
			String s = System.lineSeparator();
			String input = "ManAgER" + s + "5" + s + "garageholder" + s;
			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
			System.setIn(in);
			
			
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			            
			UserInterface userInterface = new UserInterface();
			String role = userInterface.chooseRole();
			
			String output = myout.toString();
			
			assertEquals("garageholder",  role);
			assertEquals("What's your role: manager, garageholder or worker?" + s + 
					"Sorry, that's not a valid response" + s + s +
					"What's your role: manager, garageholder or worker?" + s+ 
					"Sorry, that's not a valid response" + s + s +
					"What's your role: manager, garageholder or worker?" + s, output);
			
		} finally {}
	}
	
	@Test
	public void testChooseWorkBench() {
		try {
			// ik kan alternatieve input (foute input) niet testen omdat de inputreader steeds opniuew wordt geïntialiseerd..
			String s = System.lineSeparator();
			String input = "3" + s;
			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
			System.setIn(in);
			
			
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			            
			UserInterface userInterface = new UserInterface();
			int quantity = userInterface.chooseWorkBench(3, new ArrayList<String>(Arrays.asList("w1","w2","w3")));
			
			String output = myout.toString();
			
			assertEquals("Workbenches:" + s + 
							"1: w1" + s +
							"2: w2" + s +
							"3: w3" + s + s +
							"What's the number of the workbench you're currently residing at?" + s, output);
			assertEquals(3, quantity);
			

		} finally {}
	
	}
	
	@Test
	public void testChooseTask() {
		try {
			// ik kan alternatieve input (foute input) niet testen omdat de inputreader steeds opniuew wordt geïntialiseerd..
			String s = System.lineSeparator();
			String input = "1" + s;
			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
			System.setIn(in);
			
			
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			            
			UserInterface userInterface = new UserInterface();
			int quantity = userInterface.chooseTask(new ArrayList<String>(Arrays.asList("t1","t2","t3")));
			
			String output = myout.toString();
			
			assertEquals("Tasks:" + s + 
							"1.t1" + s +
							"2.t2" + s + 
							"3.t3" + s + s +
							"Which taskNumber do you choose?" + s, output);
			assertEquals(1, quantity);
			

		} finally {}
	
	}
	
	@Test
	public void testChooseModel() {
		try {
			// ik kan alternatieve input (foute input) niet testen omdat de inputreader steeds opniuew wordt geïntialiseerd..
			String s = System.lineSeparator();
			String input = "LaDA" + s + "Lada" + s;
			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
			System.setIn(in);
			
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			            
			UserInterface userInterface = new UserInterface();
			HashSet<String> set = new HashSet<String>(new ArrayList<String>(Arrays.asList("Lada","Polo")));
			assertEquals(2, set.size());
			String model = userInterface.chooseModel(set);
			
			String output = myout.toString();
			
			assertEquals("Possible models:" + s + 
							"Lada" + s +
							"Polo" + s + s + 
							"Which model do you want to order?" + s +
							"Sorry, that's not a valid response" + s + s +
							"Which model do you want to order?" + s, output);
			assertEquals("Lada", model);
			

		} finally {}
	
	}
	
}
 