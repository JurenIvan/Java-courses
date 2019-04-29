package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ValueWrapperTest {

	@Test
	void testIntIntAdd() {
		ValueWrapper v1 = new ValueWrapper(3);
		ValueWrapper v2 = new ValueWrapper(2);
		v1.add(v2.getValue());
		assertEquals(5, v1.getValue());
		assertEquals(2, v2.getValue());

	}

	@Test
	void testIntIntSub() {
		ValueWrapper v1 = new ValueWrapper(3);
		ValueWrapper v2 = new ValueWrapper(2);
		v1.subtract(v2.getValue());
		assertEquals(1, v1.getValue());
		assertEquals(2, v2.getValue());

	}

	@Test
	void testIntIntMul() {
		ValueWrapper v1 = new ValueWrapper(3);
		ValueWrapper v2 = new ValueWrapper(2);
		v1.multiply(v2.getValue());
		assertEquals(6, v1.getValue());
		assertEquals(2, v2.getValue());

	}

	@Test
	void testIntIntDiv() {
		ValueWrapper v1 = new ValueWrapper(3);
		ValueWrapper v2 = new ValueWrapper(2);
		v1.divide(v2.getValue());
		assertEquals(1, v1.getValue());
		assertEquals(2, v2.getValue());

	}

	// ------------------------------------------

	@Test
	void testIntDoubleAdd() {
		ValueWrapper v1 = new ValueWrapper(3);
		ValueWrapper v2 = new ValueWrapper(2.0);
		v1.add(v2.getValue());
		assertEquals(5.0, v1.getValue());
		assertEquals(2.0, v2.getValue());

	}

	@Test
	void testIntDoubleSub() {
		ValueWrapper v1 = new ValueWrapper(3);
		ValueWrapper v2 = new ValueWrapper(2.0);
		v1.subtract(v2.getValue());
		assertEquals(1.0, v1.getValue());
		assertEquals(2.0, v2.getValue());

	}

	@Test
	void testIntDoubleMul() {
		ValueWrapper v1 = new ValueWrapper(3);
		ValueWrapper v2 = new ValueWrapper(2.0);
		v1.multiply(v2.getValue());
		assertEquals(6.0, v1.getValue());
		assertEquals(2.0, v2.getValue());

	}

	@Test
	void testIntDoubleDiv() {
		ValueWrapper v1 = new ValueWrapper(5);
		ValueWrapper v2 = new ValueWrapper(2.0);
		v1.divide(v2.getValue());
		assertEquals(2.5, v1.getValue());
		assertEquals(2.0, v2.getValue());
	}

	// ------------------------------------------

	@Test
	void testDoubleDoubleAdd() {
		ValueWrapper v1 = new ValueWrapper(3.0);
		ValueWrapper v2 = new ValueWrapper(2.0);
		v1.add(v2.getValue());
		assertEquals(5.0, v1.getValue());
		assertEquals(2.0, v2.getValue());

	}

	@Test
	void testDoubleDoubleSub() {
		ValueWrapper v1 = new ValueWrapper(3.0);
		ValueWrapper v2 = new ValueWrapper(2.0);
		v1.subtract(v2.getValue());
		assertEquals(1.0, v1.getValue());
		assertEquals(2.0, v2.getValue());

	}

	@Test
	void testDoubleDoubleMul() {
		ValueWrapper v1 = new ValueWrapper(3.0);
		ValueWrapper v2 = new ValueWrapper(2.0);
		v1.multiply(v2.getValue());
		assertEquals(6.0, v1.getValue());
		assertEquals(2.0, v2.getValue());

	}

	@Test
	void testDoubleDoubleDiv() {
		ValueWrapper v1 = new ValueWrapper(3.0);
		ValueWrapper v2 = new ValueWrapper(2.0);
		v1.divide(v2.getValue());
		assertEquals(1.5, v1.getValue());
		assertEquals(2.0, v2.getValue());
	}

	// ----------------------------------------------

	@Test
	void testIntNullAdd() {
		ValueWrapper v1 = new ValueWrapper(3);
		ValueWrapper v2 = new ValueWrapper(null);
		v1.add(v2.getValue());
		assertEquals(3, v1.getValue());
		assertEquals(null, v2.getValue());

	}

	@Test
	void testIntNullSub() {
		ValueWrapper v1 = new ValueWrapper(3);
		ValueWrapper v2 = new ValueWrapper(null);
		v1.subtract(v2.getValue());
		assertEquals(3, v1.getValue());
		assertEquals(null, v2.getValue());

	}

	@Test
	void testIntNullMul() {
		ValueWrapper v1 = new ValueWrapper(3);
		ValueWrapper v2 = new ValueWrapper(null);
		v1.multiply(v2.getValue());
		assertEquals(0, v1.getValue());
		assertEquals(null, v2.getValue());

	}

	@Test
	void testIntNullDiv() {
		ValueWrapper v1 = new ValueWrapper(3);
		ValueWrapper v2 = new ValueWrapper(null);
		assertThrows(IllegalArgumentException.class, () -> v1.divide(v2.getValue()));
	}

	@Test
	void testDoubleNullDiv() {
		ValueWrapper v1 = new ValueWrapper(3.0);
		ValueWrapper v2 = new ValueWrapper(null);
		assertThrows(IllegalArgumentException.class, () -> v1.divide(v2.getValue()));
	}

	// -------------------------------------

	@Test
	void testDoubleIntAdd() {
		ValueWrapper v1 = new ValueWrapper(3.0);
		ValueWrapper v2 = new ValueWrapper(2);
		v1.add(v2.getValue());
		assertEquals(5.0, v1.getValue());
		assertEquals(2, v2.getValue());

	}

	@Test
	void testDoubleIntSub() {
		ValueWrapper v1 = new ValueWrapper(3.0);
		ValueWrapper v2 = new ValueWrapper(2);
		v1.subtract(v2.getValue());
		assertEquals(1.0, v1.getValue());
		assertEquals(2, v2.getValue());

	}

	@Test
	void testDoubleIntMul() {
		ValueWrapper v1 = new ValueWrapper(3.0);
		ValueWrapper v2 = new ValueWrapper(2);
		v1.multiply(v2.getValue());
		assertEquals(6.0, v1.getValue());
		assertEquals(2, v2.getValue());

	}

	@Test
	void testDoubleIntDiv() {
		ValueWrapper v1 = new ValueWrapper(3.0);
		ValueWrapper v2 = new ValueWrapper(2);
		v1.divide(v2.getValue());
		assertEquals(1.5, v1.getValue());
		assertEquals(2, v2.getValue());
	}

	// ---------------------------

	@Test
	void testStringIntAdd() {
		ValueWrapper v1 = new ValueWrapper("3");
		ValueWrapper v2 = new ValueWrapper(2);
		v1.add(v2.getValue());
		assertEquals(5, v1.getValue());
		assertEquals(2, v2.getValue());

	}

	@Test
	void testStringIntSub() {
		ValueWrapper v1 = new ValueWrapper("3");
		ValueWrapper v2 = new ValueWrapper(2);
		v1.subtract(v2.getValue());
		assertEquals(1, v1.getValue());
		assertEquals(2, v2.getValue());

	}

	@Test
	void testStringIntMul() {
		ValueWrapper v1 = new ValueWrapper("3");
		ValueWrapper v2 = new ValueWrapper(2);
		v1.multiply(v2.getValue());
		assertEquals(6, v1.getValue());
		assertEquals(2, v2.getValue());

	}

	@Test
	void testStringIntDiv() {
		ValueWrapper v1 = new ValueWrapper("3");
		ValueWrapper v2 = new ValueWrapper(2);
		v1.divide(v2.getValue());
		assertEquals(1, v1.getValue());
		assertEquals(2, v2.getValue());
	}

	// -------------------------------------

	@Test
	void testIntStringAdd() {
		ValueWrapper v1 = new ValueWrapper(3);
		ValueWrapper v2 = new ValueWrapper("2");
		v1.add(v2.getValue());
		assertEquals(5, v1.getValue());
		assertEquals("2", v2.getValue());

	}

	@Test
	void testIntStringSub() {
		ValueWrapper v1 = new ValueWrapper(3);
		ValueWrapper v2 = new ValueWrapper("2");
		v1.subtract(v2.getValue());
		assertEquals(1, v1.getValue());
		assertEquals("2", v2.getValue());

	}

	@Test
	void testIntStringMul() {
		ValueWrapper v1 = new ValueWrapper(3);
		ValueWrapper v2 = new ValueWrapper("2");
		v1.multiply(v2.getValue());
		assertEquals(6, v1.getValue());
		assertEquals("2", v2.getValue());

	}

	@Test
	void testIntStringDiv() {
		ValueWrapper v1 = new ValueWrapper(3);
		ValueWrapper v2 = new ValueWrapper("2");
		v1.divide(v2.getValue());
		assertEquals(1, v1.getValue());
		assertEquals("2", v2.getValue());
	}

	// ------------------------------

	@Test
	void testDoubleNullAdd() {
		ValueWrapper v1 = new ValueWrapper(3.0);
		ValueWrapper v2 = new ValueWrapper(null);
		v1.add(v2.getValue());
		assertEquals(3.0, v1.getValue());
		assertEquals(null, v2.getValue());

	}

	@Test
	void testDoubleNullSub() {
		ValueWrapper v1 = new ValueWrapper(3.0);
		ValueWrapper v2 = new ValueWrapper(null);
		v1.subtract(v2.getValue());
		assertEquals(3.0, v1.getValue());
		assertEquals(null, v2.getValue());

	}

	@Test
	void testDoubleNullMul() {
		ValueWrapper v1 = new ValueWrapper(3.0);
		ValueWrapper v2 = new ValueWrapper(null);
		v1.multiply(v2.getValue());
		assertEquals(0.0, v1.getValue());
		assertEquals(null, v2.getValue());
	}

//------------------------------------------

	@Test
	void testStringStringAdd() {
		ValueWrapper v1 = new ValueWrapper("3");
		ValueWrapper v2 = new ValueWrapper("2");
		v1.add(v2.getValue());
		assertEquals(5, v1.getValue());
		assertEquals("2", v2.getValue());

	}

	@Test
	void testStringStringSub() {
		ValueWrapper v1 = new ValueWrapper("3");
		ValueWrapper v2 = new ValueWrapper("2");
		v1.subtract(v2.getValue());
		assertEquals(1, v1.getValue());
		assertEquals("2", v2.getValue());

	}

	@Test
	void testStringStringMul() {
		ValueWrapper v1 = new ValueWrapper("3");
		ValueWrapper v2 = new ValueWrapper("2");
		v1.multiply(v2.getValue());
		assertEquals(6, v1.getValue());
		assertEquals("2", v2.getValue());

	}

	@Test
	void testStringStringDiv() {
		ValueWrapper v1 = new ValueWrapper("3");
		ValueWrapper v2 = new ValueWrapper("2");
		v1.divide(v2.getValue());
		assertEquals(1, v1.getValue());
		assertEquals("2", v2.getValue());
	}

	// ------------------------------------------

	@Test
	void testStringStringAddDouble() {
		ValueWrapper v1 = new ValueWrapper("3.0");
		ValueWrapper v2 = new ValueWrapper("2");
		v1.add(v2.getValue());
		assertEquals(5.0, v1.getValue());
		assertEquals("2", v2.getValue());

	}

	@Test
	void testStringStringSubDouble() {
		ValueWrapper v1 = new ValueWrapper("3.0");
		ValueWrapper v2 = new ValueWrapper("2");
		v1.subtract(v2.getValue());
		assertEquals(1.0, v1.getValue());
		assertEquals("2", v2.getValue());

	}

	@Test
	void testStringStringMulDouble() {
		ValueWrapper v1 = new ValueWrapper("3.0");
		ValueWrapper v2 = new ValueWrapper("2");
		v1.multiply(v2.getValue());
		assertEquals(6.0, v1.getValue());
		assertEquals("2", v2.getValue());

	}

	@Test
	void testStringStringDivDouble() {
		ValueWrapper v1 = new ValueWrapper("3.0");
		ValueWrapper v2 = new ValueWrapper("2");
		v1.divide(v2.getValue());
		assertEquals(1.5, v1.getValue());
		assertEquals("2", v2.getValue());
	}

	// ------------------------------

	@Test
	void numCompare1IntInt() {
		ValueWrapper v1 = new ValueWrapper("3");
		ValueWrapper v2 = new ValueWrapper("2");
		assertTrue(v1.numCompare(v2.getValue()) > 0);
	}

	@Test
	void numCompare1IntIntReversed() {
		ValueWrapper v1 = new ValueWrapper("3.0");
		ValueWrapper v2 = new ValueWrapper("200");
		assertTrue(v1.numCompare(v2.getValue()) < 0);
	}

	@Test
	void numCompare1IntIntEqual() {
		ValueWrapper v1 = new ValueWrapper("200");
		ValueWrapper v2 = new ValueWrapper("200");
		assertTrue(v1.numCompare(v2.getValue()) == 0);
	}

	@Test
	void numCompare1IntNullbig() {
		ValueWrapper v1 = new ValueWrapper("200");
		ValueWrapper v2 = new ValueWrapper(null);
		assertTrue(v1.numCompare(v2.getValue()) > 0);
	}

	@Test
	void numCompare1IntNullsmall() {
		ValueWrapper v1 = new ValueWrapper("-200");
		ValueWrapper v2 = new ValueWrapper(null);
		assertTrue(v1.numCompare(v2.getValue()) > 0);
	}

	@Test
	void numCompareNullNull() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper(null);
		assertTrue(v1.numCompare(v2.getValue()) == 0);
	}

}
