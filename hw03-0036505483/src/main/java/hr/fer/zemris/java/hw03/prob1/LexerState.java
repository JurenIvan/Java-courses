package hr.fer.zemris.java.hw03.prob1;

/**
 * Enum used to describe possible states of Lexer.
 * @author juren
 *
 */
public enum LexerState {
	/**
	 * Represents State when lexer is not between two hashes.
	 */
	BASIC,
	
	/**
	 * Represents State when lexer is between two hashes.
	 */
	EXTENDED
}
