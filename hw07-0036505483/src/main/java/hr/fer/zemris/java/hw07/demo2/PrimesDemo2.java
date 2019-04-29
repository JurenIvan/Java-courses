package hr.fer.zemris.java.hw07.demo2;

/**
 * Short demo that shows functionality of created collection
 * 
 * @author juren
 *
 */
public class PrimesDemo2 {

	/**
	 * Main method used to start program.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		PrimesCollection primesCollection = new PrimesCollection(5); // 5: how many of them
		for (Integer prime : primesCollection) {
			System.out.println("Got prime: " + prime);
		}
		
	}

}
