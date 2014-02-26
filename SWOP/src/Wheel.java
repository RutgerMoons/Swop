
public enum Wheel {
	COMFORT(1),SPORTS(2);

	private int code;
	private Wheel(int code){
		this.code=code;
	}

	public int getCode(){
		return this.code;
	}

	public String toString(){
		switch (this.getCode()){
		case 1: return "comfort";
		case 2: return "sports (low profile)";
		default : return "No such code";
		}
	}
}
