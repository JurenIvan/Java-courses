package hr.fer.zemris.java.hw05.db.lexer;

/**
 * Enum used to describe possible types of tokens
 * 
 * @author juren
 *
 */
public enum TokenType {
	/**
	 * Represents token that stores operator. Under value of this token is string
	 * that differentiates different kinds of operators
	 */
	OPERATOR,
	/**
	 * Represents token that stores String (usually value that we compare to
	 */
	STRING,
	/**
	 * Represents token AND in data
	 */
	AND,
	/**
	 * Represents token that point us that the end of file is reached
	 */
	EOF,
	/**
	 * Represents token that stores variable (jmbag, lastName or firstName)
	 */
	VARIABLE
}
