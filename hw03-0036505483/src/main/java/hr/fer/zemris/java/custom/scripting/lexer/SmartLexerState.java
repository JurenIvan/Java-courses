package hr.fer.zemris.java.custom.scripting.lexer;
/**
 * Enum used to describe possible states of Lexer.
 * @author juren
 *
 */
public enum SmartLexerState {
	/**
	 * Represents State when Lexer is not inside tag part
	 */
	TEXT,
	
	/**
	 * Represents State when Lexer in in tag section
	 */
	TAG
}
