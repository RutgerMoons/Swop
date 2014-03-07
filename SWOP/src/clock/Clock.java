package clock;

public class Clock {
	
	//public omdat AssemblyLine hier ook aan moet kunnen
	public final static int MINUTESINADAY = 1440;
	private int minute;
	
	public Clock(){
	}
	
	public int getTime(){
		return minute;
	}
	
	public void advanceTime(int elapsedTime){
		if (elapsedTime < 0) throw new IllegalArgumentException("argument can't be negative");
		else if (this.minute + elapsedTime > MINUTESINADAY) this.minute = (this.minute + elapsedTime) % MINUTESINADAY;
		else this.minute +=elapsedTime;
	}
	
	public void reset(){
		this.minute = 0;
	}

}
