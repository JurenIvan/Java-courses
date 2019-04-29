package hr.fer.zemris.java.hw07.demo2;

/**
 * Short demo that shows that multiple iterators are mutually independant
 * 
 * @author juren
 *
 */
public class PrimesDemo1 {

	/**
	 * Main method used to start program.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		PrimesCollection primesCollection = new PrimesCollection(2);
		for (Integer prime : primesCollection) {
			for (Integer prime2 : primesCollection) {
				System.out.println("Got prime pair: " + prime + ", " + prime2);
			}
		}
	}

}
