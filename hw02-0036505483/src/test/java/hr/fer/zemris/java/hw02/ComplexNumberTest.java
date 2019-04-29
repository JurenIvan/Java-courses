package hr.fer.zemris.java.hw02;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ComplexNumberTest {

	/**
	 * Used to negate the imprecision caused by mathematical operations and storing
	 * techniques. If difference between two real or two imaginary parts of two
	 * numbers is smaller then they are considered the same.
	 */
	private static final double TOLERANCE = 0.01;

	@Test
	void constructorTest() {
		ComplexNumber complexNumber = new ComplexNumber(5.6, 6.3);
		assertTrue(complexNumber.getReal() == 5.6 && complexNumber.getImaginary() == 6.3);
	}

	@Test
	void fromRealTest() {
		ComplexNumber complexNumber = new ComplexNumber(5.6, 0);
		assertEquals(complexNumber, ComplexNumber.fromReal(5.6));
	}

	@Test
	void fromImaginaryTest() {
		ComplexNumber complexNumber = new ComplexNumber(0, 4.5);
		assertTrue(complexNumber.equals(ComplexNumber.fromImaginary(4.5)));
	}

	@Test
	void fromMagnitudeAndAngleTestFirstQuandrant() {
		ComplexNumber complexNumber1 = new ComplexNumber(1, 2);
		ComplexNumber complexNumber2 = ComplexNumber.fromMagnitudeAndAngle(2.24, 1.10715);
		assertTrue(complexNumber1.equals(complexNumber2));
		;
	}

	@Test
	void fromMagnitudeAndAngleTestSecondQuandrant() {
		ComplexNumber complexNumber1 = new ComplexNumber(-1, 2);
		ComplexNumber complexNumber2 = ComplexNumber.fromMagnitudeAndAngle(2.24, 2.03444);
		assertTrue(complexNumber1.equals(complexNumber2));
	}

	@Test
	void fromMagnitudeAndAngleTestThirdQuandrant() {
		ComplexNumber complexNumber1 = new ComplexNumber(-2, -1);
		ComplexNumber complexNumber2 = ComplexNumber.fromMagnitudeAndAngle(2.24, 3.60524);
		assertTrue(complexNumber1.equals(complexNumber2));
	}

	@Test
	void fromMagnitudeAndAngleTestFourthQuandrant() {
		ComplexNumber complexNumber1 = new ComplexNumber(1, -2);
		ComplexNumber complexNumber2 = ComplexNumber.fromMagnitudeAndAngle(2.24, 5.17603);
		assertTrue(complexNumber1.equals(complexNumber2));
	}

	@Test
	void getRealTest() {
		ComplexNumber complexNumber = new ComplexNumber(1, -2);
		assertEquals(1, complexNumber.getReal());
	}

	@Test
	void getImaginaryTest() {
		ComplexNumber complexNumber = new ComplexNumber(1, -2);
		assertEquals(-2, complexNumber.getImaginary());
	}

	@Test
	void getMagnitudeTest() {
		ComplexNumber complexNumber = new ComplexNumber(1, -2);
		assertEquals(Math.sqrt(5), complexNumber.getMagnitude());
	}

	@Test
	void getAngleTestFirstQuadrant() {
		ComplexNumber complexNumber = new ComplexNumber(1, 2);
		assertTrue(Math.abs(complexNumber.getAngle() - 1.10715) < TOLERANCE);
	}

	@Test
	void getAngleTestSecondQuadrant() {
		ComplexNumber complexNumber = new ComplexNumber(-1, 2);
		assertTrue(Math.abs(complexNumber.getAngle() - 2.03444) < TOLERANCE);
	}

	@Test
	void getAngleTestThirdQuadrant() {
		ComplexNumber complexNumber = new ComplexNumber(-2, -1);
		assertTrue(Math.abs(complexNumber.getAngle() - 3.60524) < TOLERANCE);
	}

	@Test
	void getAngleTestFourthQuadrant() {
		ComplexNumber complexNumber = new ComplexNumber(1, -2);
		assertTrue(Math.abs(complexNumber.getAngle() - 5.17603) < TOLERANCE);
	}

	@Test
	void addTest() {
		ComplexNumber complexNumber1 = new ComplexNumber(7.0, 4.0);
		ComplexNumber complexNumber2 = new ComplexNumber(5.0, 2.0);
		assertEquals(new ComplexNumber(12, 6), complexNumber1.add(complexNumber2));
	}

	@Test
	void subTest() {
		ComplexNumber complexNumber1 = new ComplexNumber(7.0, 4.0);
		ComplexNumber complexNumber2 = new ComplexNumber(5.0, 2.0);
		assertEquals(new ComplexNumber(2.0, 2.0), complexNumber1.sub(complexNumber2));
	}

	@Test
	void mulTest() {
		ComplexNumber complexNumber1 = new ComplexNumber(7.0, 4.0);
		ComplexNumber complexNumber2 = new ComplexNumber(5.0, 2.0);
		assertEquals(new ComplexNumber(27.0, 34.0), complexNumber1.mul(complexNumber2));
	}

	@Test
	void divTest() {
		ComplexNumber complexNumber1 = new ComplexNumber(7.0, 4.0);
		ComplexNumber complexNumber2 = new ComplexNumber(5.0, 2.0);
		assertEquals(new ComplexNumber(43.0 / 29.0, 6.0 / 29.0), complexNumber1.div(complexNumber2));
	}

	@Test
	void addTestNullReference() {
		ComplexNumber complexNumber = new ComplexNumber(7.0, 4.0);
		assertThrows(NullPointerException.class, () -> complexNumber.add(null));
	}

	@Test
	void subTestNullReference() {
		ComplexNumber complexNumber = new ComplexNumber(7.0, 4.0);
		assertThrows(NullPointerException.class, () -> complexNumber.sub(null));
	}

	@Test
	void mulTestNullReference() {
		ComplexNumber complexNumber = new ComplexNumber(7.0, 4.0);
		assertThrows(NullPointerException.class, () -> complexNumber.mul(null));
	}

	@Test
	void divTestNullReference() {
		ComplexNumber complexNumber = new ComplexNumber(7.0, 4.0);
		assertThrows(NullPointerException.class, () -> complexNumber.div(null));
	}

	@Test
	void powerTest1() {
		ComplexNumber complexNumber1 = new ComplexNumber(7.0, -4.0);
		ComplexNumber complexNumber2 = new ComplexNumber(-274527, -7336);

		assertTrue(Math.abs(complexNumber1.power(6).sub(complexNumber2).getMagnitude()) < TOLERANCE);
	}

	@Test
	void powerTest2() {
		ComplexNumber complexNumber1 = new ComplexNumber(7.0, -4.0);
		ComplexNumber complexNumber2 = new ComplexNumber(7, -524);

		assertTrue(Math.abs(complexNumber1.power(3).sub(complexNumber2).getMagnitude()) < TOLERANCE);
	}

	@Test
	void powerTestIllegalArgument() {
		ComplexNumber complexNumber = new ComplexNumber(7.0, 4.0);
		assertThrows(IllegalArgumentException.class, () -> complexNumber.power(-3));
	}

	@Test
	void rootTest() {
		ComplexNumber complexNumber = new ComplexNumber(9.2, 4.7);

		ComplexNumber root1 = new ComplexNumber(1.58813779158645, 0.150465005697441);
		ComplexNumber root2 = new ComplexNumber(0.347660842866373, 1.55690503928212);
		ComplexNumber root3 = new ComplexNumber(-1.3732715741376, 0.8117552258349);
		ComplexNumber root4 = new ComplexNumber(-1.19638935146748, -1.05521271917081);
		ComplexNumber root5 = new ComplexNumber(0.633862291152251, -1.46391255164366);

		ComplexNumber[] calculatedRoots = complexNumber.root(5);

		assertTrue(
				root1.equals(calculatedRoots[0]) && root2.equals(calculatedRoots[1]) && root3.equals(calculatedRoots[2])
						&& root4.equals(calculatedRoots[3]) && root5.equals(calculatedRoots[4]));
	}

	@Test
	void rootTestIllegalArgumentNegative() {
		ComplexNumber complexNumber = new ComplexNumber(7.0, 4.0);
		assertThrows(IllegalArgumentException.class, () -> complexNumber.root(-3));
	}

	@Test
	void rootTestIllegalArgumentZero() {
		ComplexNumber complexNumber = new ComplexNumber(7.0, 4.0);
		assertThrows(IllegalArgumentException.class, () -> complexNumber.root(0));
	}

	@Test
	public void toStringTest() {
		ComplexNumber complexNumber = new ComplexNumber(34, 35.6);
		assertEquals("34.0 + 35.6i", complexNumber.toString());
	}
	
	@Test
	public void toStringThenParse(){
		ComplexNumber complexNumber1=ComplexNumber.parse("23-3i");
		ComplexNumber complexNumber2=ComplexNumber.parse(complexNumber1.toString());
		assertTrue(complexNumber1.equals(complexNumber2));
	}

	@Test
	void parseTestJustRealDecimal() {
		ComplexNumber cn = new ComplexNumber(3.51, 0);
		String inputString = "3.51";
		assertTrue(cn.equals(ComplexNumber.parse(inputString)));
	}

	@Test
	void parseTestJustNegativeReal() {
		ComplexNumber cn = new ComplexNumber(-3.17, 0);
		String inputString = "-3.17";
		assertTrue(cn.equals(ComplexNumber.parse(inputString)));
	}

	@Test
	void JustNegativeImaginaryDecimal() {
		ComplexNumber cn = new ComplexNumber(0, -2.71);
		String inputString = "-2.71i";
		assertTrue(cn.equals(ComplexNumber.parse(inputString)));
	}

	@Test
	void parseTestJustINoNumber() {
		ComplexNumber cn = new ComplexNumber(0, 1);
		String inputString = "i";
		assertTrue(cn.equals(ComplexNumber.parse(inputString)));
	}

	@Test
	void parseTestJustReal() {
		ComplexNumber cn = new ComplexNumber(1, 0);
		String inputString = "1";
		assertTrue(cn.equals(ComplexNumber.parse(inputString)));
	}

	@Test
	void parseTestFullExpresion() {
		ComplexNumber cn = new ComplexNumber(-2.71, -3.15);
		String inputString = "-2.71-3.15i";
		assertTrue(cn.equals(ComplexNumber.parse(inputString)));
	}

	@Test
	void parseTestJustNegativeImaginaryNegativeWhole() {
		ComplexNumber cn = new ComplexNumber(0, -317);
		String inputString = "-317i";
		assertTrue(cn.equals(ComplexNumber.parse(inputString)));
	}

	@Test
	void parseTestJustImaginaryPositiveWhole() {
		ComplexNumber cn = new ComplexNumber(0, 351);
		String inputString = "351i";
		assertTrue(cn.equals(ComplexNumber.parse(inputString)));
	}

	@Test
	void parseTestJustPositiveImaginaryDecimal() {
		ComplexNumber cn = new ComplexNumber(0, 3.51);
		String inputString = "3.51i";
		assertTrue(cn.equals(ComplexNumber.parse(inputString)));
	}

	@Test
	void parseTestJustNegativeImaginaryNegativeNoNumber() {
		ComplexNumber cn = new ComplexNumber(0, -1);
		String inputString = "-i";
		assertTrue(cn.equals(ComplexNumber.parse(inputString)));
	}

	@Test
	void parseTestFullExpresionWhole() {
		ComplexNumber cn = new ComplexNumber(31, 24);
		String inputString = "31+24i";
		assertTrue(cn.equals(ComplexNumber.parse(inputString)));
	}

	@Test
	void parseTestFullExpresionNegative() {
		ComplexNumber cn = new ComplexNumber(-1, -1);
		String inputString = "-1-i";
		assertTrue(cn.equals(ComplexNumber.parse(inputString)));
	}

	@Test
	void parseTestPlusJustRealDecimal() {
		ComplexNumber cn = new ComplexNumber(2.71, 0);
		String inputString = "+2.71";
		assertTrue(cn.equals(ComplexNumber.parse(inputString)));
	}

	@Test
	void parseTestPlusFullExpresion2() {
		ComplexNumber cn = new ComplexNumber(2.71, 3.15);
		String inputString = "+2.71+3.15i";
		assertTrue(cn.equals(ComplexNumber.parse(inputString)));
	}

	@Test
	void parseTestPlusImaginary() {
		ComplexNumber cn = new ComplexNumber(0, 1);
		String inputString = "+i";
		assertTrue(cn.equals(ComplexNumber.parse(inputString)));
	}

	@Test
	void parseTestMinusFulExpression() {
		ComplexNumber cn = new ComplexNumber(-231, -2.4);
		String inputString = "-231-2.4i";
		assertTrue(cn.equals(ComplexNumber.parse(inputString)));
	}

	@Test
	void parseTestNumbersAfterI() {
		assertThrows(NumberFormatException.class, () -> ComplexNumber.parse("i351"));
	}

	@Test
	void parseTestNumbersAfterI2() {
		assertThrows(NumberFormatException.class, () -> ComplexNumber.parse("-i317"));
	}

	@Test
	void parseTestNotOkMinusPlus() {
		assertThrows(NumberFormatException.class, () -> ComplexNumber.parse("-+2.71"));
	}

	@Test
	void parseTestNotOkMinusMinus() {
		assertThrows(NumberFormatException.class, () -> ComplexNumber.parse("--2.71"));
	}

	@Test
	void parseTestNotOkFullExpression() {
		assertThrows(NumberFormatException.class, () -> ComplexNumber.parse("-2.71+-3.15i"));
	}

	@Test
	void parseTestNotOkReal() {
		assertThrows(NumberFormatException.class, () -> ComplexNumber.parse("-+2.71"));
	}
}
