package hr.fer.zemris.java.hw06.crypto;

import java.util.Objects;

/**
 * This class has three public static methods: {@link Util#hextobyte(keyText)}
 * and {@link Util#bytetohex(bytearray)}. Method {@link Util#hextobyte(keyText)}
 * takes hex-encoded String and return appropriate byte[] and
 * {@link Util#bytetohex(bytearray)} does the opposite.
 * 
 * Besides those two methods that were required, I added a third one
 * {@link #transformToString(byte)} that transforms a single byte to String
 * consisting of two hex chars.
 * 
 * @author juren
 *
 */
public class Util {

	/**
	 * Method that takes hex-encoded String and return appropriate byte[]. Method
	 * can handle both upper-case and lower-case letters.
	 * 
	 * @param keyText text that will be converted. Has to have even number of chars.
	 * @return byte array with proper value representation of string
	 * 
	 * @throws NullPointerException if null reference is provided
	 */
	public static byte[] hextobyte(String keyText) {
		Objects.requireNonNull(keyText, "String that was supposed to be transformed into byte array is null reference");
		if (keyText.length() % 2 == 1)
			throw new IllegalArgumentException("Input is odd-sized and can not be transformed into byte array.");

		byte[] byteValue = new byte[keyText.length() / 2];
		for (int i = 0; i < keyText.length(); i = i + 2) {
			byteValue[i / 2] = transformToByte(keyText.charAt(i), keyText.charAt(i + 1));
		}
		return byteValue;
	}

	/**
	 * Method that takes two chars gets their value and compose byte value out of
	 * them.
	 * 
	 * @param char1 char whose value is multiplied by 16
	 * @param char2 char whose value is added to first one
	 * @return byte representation of hex value
	 */
	private static byte transformToByte(char char1, char char2) {
		int value = getDecadicValueOfChar(char1) * 16 + getDecadicValueOfChar(char2);
		return value > 127 ? (byte) -(256 - value) : (byte) value;
	}

	/**
	 * Method that returns value of hex character
	 * 
	 * @param character in hexadecimal system
	 * @return its value as integer
	 */
	private static int getDecadicValueOfChar(char character) {
		if (!((character <= 'F' && character >= 'A') || (character <= 'f' && character >= 'a')
				|| Character.isDigit(character)))
			throw new IllegalArgumentException("Not an hex value");
		return Character.isDigit(character) ? character - '0' : Character.toLowerCase(character) - 'a' + 10;
	}

	/**
	 * Method that takes byte array and returns appropriate hex-encoded String.
	 * Method returns hex value with lowercase letters representing hex characters
	 * 
	 * @param bytearray that will be transformed
	 * @return hex-encoded String
	 */
	public static String bytetohex(byte[] bytearray) {
		Objects.requireNonNull(bytearray,
				"String that was supposed to be transformed into byte array is null reference");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytearray.length; i++) {
			sb.append(transformToString(bytearray[i]));
		}
		return sb.toString();
	}

	/**
	 * Method takes a byte transforms a single byte to String consisting of two hex
	 * chars.
	 * 
	 * @param b byte that is converted
	 * @return String(hex) representation of byte b
	 */
	public static String transformToString(byte b) {
		// I made it public because i needed its functionality later in shell
		int value = b < 0 ? 128 + (128 + b) : b;
		return Integer.toHexString(value / 16) + Integer.toHexString(value % 16);
	}

}
