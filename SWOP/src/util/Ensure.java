package util;

public class Ensure {

	
	public static void isNotNull(Object object){
		if(object==null)
			throw new NullPointerException();
	}
	
	public static void isNonEmptyString(String object){
		if(object.isEmpty())
			throw new NullPointerException();
	}
}
