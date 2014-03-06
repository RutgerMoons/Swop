
public class Parser {
		
	private String toParse;
	
	public Parser(String toParse) {
		this.toParse = toParse;
	}
	
	public String[] splitInParts() {
		return this.toParse.split(" ");
	}
	
	public static String[] splitUri(String uri) throws NullPointerException {
		if (uri == null) {
			throw new NullPointerException();
		}
		else {
			int idx = uri.indexOf("/");
			String uri0 = uri.substring(0,idx);
			String uri1 = uri.substring(idx);
			return new String[] {uri0, uri1};
		}
	}
	
	
	
}
