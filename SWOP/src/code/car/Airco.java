package code.car;
/**
 * Class that represents the carpart of type Airco.
 *
 */
public class Airco extends CarPart {
	
	//private static final ArrayList<String> possibleAircos = new ArrayList<>(Arrays.asList("manual", "automatic climate control"));
	
//	public static void addPossibleAirco(String type) {
//		if (type == null) {
//			throw new IllegalArgumentException();
//		} 
//		possibleAircos.add(type); 
//	}
	
//	public static Iterator<String> getPossibleAirco() {
//		return possibleAircos.iterator();		
//	}
	
	/**
	 * Constructing an Airco and immediately choosing a certain type of Airco
	 * given the description.
	 */
	public Airco(String description) {
		this.setType(description);
	}
	
	/**
	 *  Checks if the given description is null. If so an
	 *  IllegalArgument exception is thrown. If not, the description
	 *  is set to the given string.
	 *  
	 */
	@Override
	public void setType(String description) {
		if (description == null) {
			throw new IllegalArgumentException();
		} 
		else {
			super.setType(description);
		}
	}
	   
}
  