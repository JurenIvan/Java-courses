package hr.fer.zemris.java.hw03.prob1;

import java.util.Objects;

/**
 * Class that represent a collection of characters that we call token. Instances
 * of this class are used as medium between Lexer and Parser. This class
 * contains methods used to retrieve data stored in private instance variable.
 * 
 * @author juren
 *
 */
public class Token {
	/**
	 * Variable used to store type of token.
	 */
	private TokenType tokenType;
	
	/**
	 * Variable used to store value as object.
	 */
	private Object	value;
	

	/**
	 * Constructor that sets {@link TokenType} and {@link Object} variable that
	 * define token.
	 * 
	 * @param tokenType type of Token
	 * @param value     value store din token
	 *
	 * @throws NullPointerException if value argument is null reference
	 */
	public Token(TokenType tokenType, Object value) {
		Objects.requireNonNull(tokenType);
		
		this.tokenType=tokenType;
		this.value=value;
	}

	/**
	 * Getter for value stored in class.
	 * @return value stored as Object
	 */
	public Object getValue() {
		return this.value;
	}
	
	/**
	 * Getter for type of token.
	 * @return token type as {@link TokenType}
	 */
	public TokenType getType() {
		return this.tokenType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(tokenType, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Token))
			return false;
		Token other = (Token) obj;
		return tokenType == other.tokenType && Objects.equals(value, other.value);
	}
	
	
}
