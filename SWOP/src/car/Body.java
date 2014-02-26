package car;
public enum Body {

	SEDAN(1), BREAK(2);

	private int code;

	private Body(int body_code) {
		code = body_code;
	}

	public int getCode() {
		return this.code;
	}

	public String toString() {
		switch (this.getCode()) {
		case 1:
			return "sedan";
		case 2:
			return "break";

		default: return "no code found";
		}
	}

}
