package ui;


import java.util.Scanner;

import users.GarageHolder;
import users.User;

public class UserInterface {

	public void login(){
		System.out.println("Hello user, what's your name?");
		Scanner inputReader = new Scanner(System.in);
		String name = inputReader.nextLine();
		// this is how you write to console
		System.out.println("What's your role: manager, garageholder or worker?");
		String role = inputReader.nextLine();
		if(role.equals("garageholder")){
			GarageHolder garageHolder = new GarageHolder(name);
			GarageHolderInterface inter = new GarageHolderInterface(garageHolder);
			inter.showOrderBook();
		}

		if(role.equals("manager")){
			System.out.println("bla2");
		}
		if(role.equals("worker")){
			System.out.println("bla3");
		}

	}
	
	public static void main(String[] args) {
		UserInterface inter = new UserInterface();
		inter.login();
		
	}

}