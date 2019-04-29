package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Enum used to describe possible types of tokens
 * 
 * @author juren
 *
 */
public enum SmartTokenType {
	/**
	 * Represents token that stores string
	 */
	TEXT,

	/**
	 * Represents that token found tag. Only one tag Token type is available. Always
	 * found before and after FOR and ECHO tag
	 */
	TAG,

	/**
	 * Represents End Of File
	 */
	EOF,

	/**
	 * Represents token that stores Integer
	 */
	INTEGER,

	/**
	 * Represents token that stores Double
	 */
	DOUBLE,

	/**
	 * Represents token that stores String
	 */
	STRING,

	/**
	 * Represents token that stores name of function
	 */
	FUNCTION,

	/**
	 * Represents token that stores char, an operator
	 */
	OPERATOR,

	/**
	 * Represents token that stores string that has properties of variable
	 */
	VARIABLE,

	/**
	 * Represents token that stores null, and specifies that the for loop should
	 * start
	 * 
	 */
	FOR,

	/**
	 * Represents token that stores null, and specifies that the echo should start
	 */
	ECHO,

	/**
	 * Represents token that ends for loop
	 */
	END
}
