
public enum Seat {
	LEATHER_BLACK(1,Color.BLACK), VINYL(2,Color.GREY), LEATHER_WHITE(3,Color.WHITE);
	
	private int code;
	private Color color;
	private Seat(int code, Color color){
		this.code=code;
		this.color=color;
	}
	
	public int getCode(){
		return this.code;
	}
	
	public Color getColor(){
		return this.color;
	}

	public String toString() {
		String type = "";
		switch (this.getCode()) {
		case 1:
			type = "leather ";
			break;
		case 2:
			type = "Vinyl ";
			break;
		case 3:
			type = "leather ";
		default: return "no code found";
		}
		return type + this.getColor().toString() ;
	} 
}
