package ui;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;

import order.Order;
import users.*;


public class UserInterface implements UIFacade{
	
	private Scanner inputReader;

	private String askQuestion(String question){
		System.out.println(question);
		inputReader = new Scanner(System.in);
		String answer = inputReader.nextLine();
		return answer;
	}
	
	private void show(ArrayList<String> message){
		for (int i = 0; i < message.size(); i++) {
			System.out.println(message.get(i));
		}
		System.out.println("");
	}
	
	public String getName(){
		return askQuestion("Hello user, what's your name?");
	}
	
	public String getRole(){
		return askQuestion("What's your role: manager, garageholder or worker?");
	}
	
	@Override
	public String getModel(Set<String> catalogue) {
		ArrayList<String> catalogueInString = new ArrayList<String>();
		
		catalogueInString.add("Possible models:");
		for(String key:catalogue){
			catalogueInString.add(key);
		}
		show(catalogueInString);
		
		return askQuestion("Which model do you want to order?");
	}

	@Override
	public int getQuantity() {
		return Integer.parseInt(askQuestion("How many cars do you want to order?"));
	}

	public void invalidAnswerPrompt(){
		show(new ArrayList<String>(Arrays.asList("Sorry, that's not a valid response")));
	}
	
	public void invalidUserPrompt(){
		show(new ArrayList<String>(Arrays.asList("You don't have any rights")));
	}
	
	@Override
	public void showPendingOrders(ArrayList<Order> pendingOrders) {
		ArrayList<String> ordersInString = new ArrayList<String>();
		String orderInString;
		
		ordersInString.add("Your pending orders:");
		
		for (int i = 0; i < pendingOrders.size(); i++) {
			orderInString = pendingOrders.get(i).getQuantity() + " " + pendingOrders.get(i).getDescription() + " Estimated completion time:" + pendingOrders.get(i).getEstimatedTime();
			ordersInString.add(orderInString);
		}
		
		show(ordersInString);
		
	}

	@Override
	public void showCompletedOrders(ArrayList<Order> completedOrders) {
		ArrayList<String> ordersInString = new ArrayList<String>();
		String orderInString;
		
		ordersInString.add("Your completed orders:");
		
		for (int i = 0; i < completedOrders.size(); i++) {
			orderInString = completedOrders.get(i).getQuantity() + " " + completedOrders.get(i).getDescription();
			ordersInString.add(orderInString);
		}
		
		show(ordersInString);
		
	}

	@Override
	public void showOrder(int quantity, String model, int estimatedTime) {
		if(estimatedTime == -1)
			show(new ArrayList<String>(Arrays.asList("Your order:",quantity + " " + model)));
		else show(new ArrayList<String>(Arrays.asList("Your order:",quantity + " " + model + " Estimated completion time:" + estimatedTime)));
		
	}
	
	@Override
	public boolean askContinue() {
		// TODO Auto-generated method stub
		return false;
	}


}