
public enum Airco {

	MANUAL(1),AUTOMATIC(2);
	
	private int code;
	
	private Airco(int code){
		this.code=code;
	}
	
	public int getCode(){
		return this.code;
	}
	
	public String toString(){
		switch(this.getCode()){
		case 1: 
			return "manual";
		case 2:
			return "automatic climate control";
		default: return "No such code";
		}
	}
}
