package domain.scheduler;

public class Shift {
	
	private final int start;
	private final int end;
	private int overtime;
	
	public Shift(int start, int end, int overtime) {
		this.start = start;
		this.end = end;
		this.overtime = overtime;
	}
	
	public int getEndOfShift() {
		return this.end - this.overtime;
	}
	
	public void setNewOvertime(int newOvertime) {
		if(newOvertime < 0){
			throw new IllegalArgumentException();
		}
		this.overtime = newOvertime;
	}
	
	public int getStartOfShift() {
		return this.start;
	}

}
