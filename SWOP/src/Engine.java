
public enum Engine {
	STANDARD(1,2,4), PERFORMANCE(2,2.5,6);
	
	private int code;
	private double liter;
	private int cilinder;
	
	private Engine(int code, double liter, int cilinders){
		this.code = code;
		this.liter= liter;
		this.cilinder=cilinders;
	}
	
	public int getCode() {
		return this.code;
	}
	
	public double getLiter(){
		return this.liter;
	}
	
	public int getCilinder(){
		return this.cilinder;
	}

	public String toString() {
		String type = "";
		switch (this.getCode()) {
		case 1:
			type = "standard ";
			break;
		case 2:
			type = "performance ";
			break;
		default: return "no code found";
		}
		return type + Double.toString(getLiter()) + "l " + Integer.toString(getCilinder()) + "cilinder";
	}
}
