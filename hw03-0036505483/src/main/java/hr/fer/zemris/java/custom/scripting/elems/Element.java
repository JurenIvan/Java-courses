package hr.fer.zemris.java.custom.scripting.elems;
/**
 * Element is base class(should have been abstract) which has only a single public function: String asText();
 * All other Element classes inherit this one.
 * 
 * @author juren
 *
 */
public class Element {
	
	/**
	 * Method that returns empty string.
	 * @return empty string
	 */
	public String asText() {
		return new String();
	}
	
}
