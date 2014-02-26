package car;

public enum Color {
	RED(1),BLUE(2),BLACK(3),WHITE(4), GREY(5);
	
	private int code;
	
	private Color(int code){
		this.code = code;
	}
	
	public int getCode() {
		return this.code;
	}

	public String toString() {
		switch (this.getCode()) {
		case 1:
			return "red";
		case 2:
			return "blue";
		case 3:
			return "black";
		case 4:
			return "white";
		case 5:
			return "grey";
		default: return "no code found";
		}
	}
}
