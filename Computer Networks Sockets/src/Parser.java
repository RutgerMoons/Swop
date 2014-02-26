
public class Parser {
		
	private String toParse;
	
	public Parser(String toParse) {
		this.toParse = toParse;
	}
	
	public String[] splitInParts() {
		return this.toParse.split(" ");
	}
	
	
	
	
}
