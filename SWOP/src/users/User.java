package users;

import java.util.ArrayList;

import ui.*;

public abstract class User {

	private String name;
	
	public User(String name){
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public UseCaseHandler getRightHandler(ArrayList<UseCaseHandler> handlers){
		for (int i = 0; i < handlers.size(); i++) {
			if (handlers.get(i).mayUseThisHandler(this)) return handlers.get(i);
		}
		return null;
	}
}
