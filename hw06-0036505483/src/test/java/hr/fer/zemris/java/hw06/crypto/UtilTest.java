package hr.fer.zemris.java.hw06.crypto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


class UtilTest {
	
	
	@Test
	void hextobyteTestOneValue() {
		byte[] result = { 27};
		byte[] calculated = Util.hextobyte("1B");
		for (int i = 0; i < result.length; i++) {
			assertTrue(result[i] == calculated[i]);
		}
	}

	@Test
	void hextobyteTestMultipleValuesBigAndSmallLetters() {
		byte[] result = { 27, 42 };
		byte[] calculated = Util.hextobyte("1B2a");
		for (int i = 0; i < result.length; i++) {
			assertTrue(result[i] == calculated[i]);
		}
	}

	@Test
	void hextobyteTestPositive() {
		byte[] result = { 27, 42,43,127 };
		byte[] calculated = Util.hextobyte("1B2a2b7f");
		for (int i = 0; i < result.length; i++) {
			assertTrue(result[i] == calculated[i]);
		}
	}

	@Test
	void hextobyteTestNegative() {
		byte[] result = { -25, -42,-43,-127,-128 };
		byte[] calculated = Util.hextobyte("E7D6D58180");
		
		for (int i = 0; i < result.length; i++) {
			assertTrue(result[i] == calculated[i]);
		}
	}

	@Test
	void hextobyteTestNullPointer() {
		assertThrows(NullPointerException.class, ()->Util.hextobyte(null));
	}

	@Test
	void hextobyteTestOddShaped() {
		assertThrows(IllegalArgumentException.class, ()->Util.hextobyte("abc"));
	}
	
	@Test
	void hextobyteTestIllegalLegal() {
		assertThrows(IllegalArgumentException.class, ()->Util.hextobyte("ayyy"));
	}
	
	@Test
	void bytetohexTestOneValue() {
		String result = "1b";
		byte[] input = { 27};
		String calculated= Util.bytetohex(input);
		assertEquals(calculated, result);
	}

	@Test
	void bytetohexTestMultipleValues() {
		
		String result = "1b2a";
		byte[] input = { 27, 42 };
		String calculated= Util.bytetohex(input);
		assertEquals(calculated, result);
		
	}

	@Test
	void bytetohexTestPositive() {
		
		String result = "1b2a2b7f";
		byte[] input = { 27, 42,43,127 };
		String calculated= Util.bytetohex(input);
		assertEquals(calculated, result);
	
	}

	@Test
	void bytetohexTestNegative() {
		String result = "e7d6d58180";
		byte[] input = { -25, -42,-43,-127,-128 };
		String calculated= Util.bytetohex(input);
		assertEquals(calculated, result);
	
	}

	@Test
	void transformToStringTestZero() {
		byte input=0;
		String output=Util.transformToString(input);
		
		assertEquals("00", output);
	}

	@Test
	void transformToStringTestPositive() {
		byte input=74;
		String output=Util.transformToString(input);
		
		assertEquals("4a", output);
	}

	@Test
	void transformToStringTestNegative() {
		byte input=-83;
		String output=Util.transformToString(input);
		
		assertEquals("ad", output);
	}


}
