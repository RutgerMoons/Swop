package uiTest;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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

}
 
