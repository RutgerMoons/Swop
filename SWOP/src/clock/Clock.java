package clock;

public class Clock {
	
	private final int minutesInADay = 1440;
	private int minute;
	
	public Clock(){
	}
	
	public int getTime(){
		return minute;
	}
	
	public void advanceTime(int elapsedTime){
		if (elapsedTime < 0) throw new IllegalArgumentException("argument can't be negative");
		else if (this.minute + elapsedTime > minutesInADay) this.minute = (this.minute + elapsedTime) % minutesInADay;
		else this.minute +=elapsedTime;
	}
	
	public void reset(){
		this.minute = 0;
	}

}
