package hr.fer.zemris.math;

/**
 * Class containing main method used to demonstrate basic functionalities with
 * {@link ComplexRootedPolynomial} and {@link ComplexPolynomial}} classes
 * 
 * @author juren
 *
 */
public class TestPolynomes {
	/**
	 * Main method used to start the main program
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		ComplexRootedPolynomial crp = new ComplexRootedPolynomial(new Complex(2, 0), Complex.ONE, Complex.ONE_NEG,
				Complex.IM, Complex.IM_NEG);
		ComplexPolynomial cp = crp.toComplexPolynom();
		System.out.println(crp);
		System.out.println(cp);
		System.out.println(cp.derive());

//		System.out.println(crp.indexOfClosestRootFor(new Complex(1,0),0.1));
//		System.out.println(crp.indexOfClosestRootFor(new Complex(-1,0),0.1));
//		System.out.println(crp.indexOfClosestRootFor(new Complex(0,1),0.1));
//		System.out.println(crp.indexOfClosestRootFor(new Complex(0,-1),0.1));

	}

}
