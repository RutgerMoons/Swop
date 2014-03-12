import java.util.LinkedList;



/**
 * @author Karen en co
 * 
 * Liefste Karen, hierbij je eigen project dat wij een beetje uitgebreid hebben.
 * 
 * *Lees dit letterlijk voor:*
 * Hallo iedereen,
 * Op mijn computer werd vandaag een klasse Stack het leven ingeblazen op een elegante manier.
 * Na enkele woelige rondjes en heftige discussies was dit best snel geimplementeerd dus is 
 * er ook nog wat op gestaard en gepruld, maar het was wel plezant. Goed gedaan iedereen, bedankt.
 * 
 * *Doe nu een (Y) emoticon in het echt en een dansje*
 * 
 * We hebben vandaag allemaal een belangrijke les geleerd, die ik even ga demonstreren:
 * *TWERK TWERK TWERK TWERK*
 * *Ongemakkelijke pauze*
 * while(Vincken.getState() == LAUGHING){
 * 	 *TWERK TWERK TWERK TWERK*
 * }
 * 
 * Nu ga ik even de code voorlezen.
 * 
 * *Lees hier de code voor en laat een test zien, vlug, en laat eens zien dat je een mooi groen balkje krijgt, indien dit niet zo is: TWERK TWERK TWERK TWERK*
 * 
 * *Bespreek hier de tijdscomplexiteiten: linkedlist vs arraylist en zo, de simpele dingen*
 * 
 * Bedankt voor uw aandacht.
 *
 */
public class Integer_Stack {

	// The stack is implemented as a linked list for efficiency. (Constant time get/add).
	private LinkedList<Integer> stack;

	/**
	 * Constructor for this class, makes a new linkedlist which will be used as container for the stack
	 */
	public Integer_Stack(){
		stack = new LinkedList<Integer>();
	}


	/**
	 * @return the size of the current stack
	 * 	| result == stack.size()
	 */
	public int count(){
		return stack.size();
	}

	// Returns and removes the last added element from the stack.
	// If the stack is empty, return null.
	public Integer pop(){
		if (count()==0) {
			return null;
		} else {
			return stack.remove(count() - 1);
		}

	}

	// Add a new element on top of the stack.
	public void push(Integer number){
		if(number!=null)
			stack.add(number);
	}
	
	public void pushMultiple(int[] nb) {
		for (int i : nb) {
			push(i);
		}
	}
	
	//peak at the integer at the top
	public Integer peek(){
		if(count()==0)
			return null;
		return stack.get(count()-1);
	}

}
