
public enum Gearbox {
	MANUAL(1, 6), AUTOMATIC(2, 5);

	private int code, amount;
	private Gearbox(int code, int amount) {
		this.code = code;
		this.amount = amount;
	}

	public int getCode(){
		return this.code;
	}

	public int getAmount(){
		return this.amount;
	}

	public String toString() {
		String type = "";
		switch (this.getCode()) {
		case 1:
			type = "manual ";
			break;
		case 2:
			type = "automatic ";
			break;
		default: return "no code found";
		}
		return (this.getAmount() + "speed ") + type;
	}
}


