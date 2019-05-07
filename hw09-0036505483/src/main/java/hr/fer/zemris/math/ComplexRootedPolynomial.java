package hr.fer.zemris.math;

import java.util.Arrays;

public class ComplexRootedPolynomial {

	private Complex[] coeficients;
	private Complex factor;

	public ComplexRootedPolynomial(Complex constant, Complex... roots) {
		factor=constant;
		coeficients=Arrays.copyOf(roots, roots.length);
		
	}

	public Complex apply(Complex z) {
		Complex result = factor;
		for (int i = 0; i < coeficients.length; i++) {
			result = result.multiply((z.sub(coeficients[i])));
		}
		return result;
	}

	public int indexOfClosestRootFor(Complex z, double treshold) {
		double minDistance=Double.MAX_VALUE;
		int indexOfResult=-1;
		
		for(int i=0;i<coeficients.length;i++) {
			
			double distance=coeficients[i].sub(z).module();
			if(distance<minDistance && distance<treshold) {
				minDistance=distance;
				indexOfResult=i;
			}
		}
		return indexOfResult+1;
	}

	public ComplexPolynomial toComplexPolynom() {
		ComplexPolynomial cp=new ComplexPolynomial(factor);
		for(int i=0;i<coeficients.length;i++) {
			cp=cp.multiply(new ComplexPolynomial(Complex.ONE, coeficients[i].negate()));
		}
		return cp;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(factor + "*");
		for (int i = 0; i < coeficients.length; i++) {
			sb.append("(z-" + coeficients[i].toString() + ")*");
		}
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}
}
