package hr.fer.zemris.java.hw05.db.lexer;

import java.util.Objects;

/**
 * Class representing lexer. Has classic lexer methods; getToken, setState,
 * NextToken and a constructor. This lexer can give back 12 types of tokens:
 * OPERATOR, STRING,AND, VARIABLE,STRING,EOF. * @author juren
 */
public class Lexer {
	/**
	 * Array of input chars. Text that it has to tokenize
	 */
	private char[] data;

	/**
	 * Class remembers the last token that was sent because parser might need same
	 * token more than once
	 */
	private Token token;

	/**
	 * represents index of character where we got while tokenising
	 */
	private int currentIndex;

	/**
	 * Array of characters that represent whitespaces used to separate tokens
	 */
	private static final char[] WHITESPACES = { '\r', '\n', '\t', ' ' };

	/**
	 * Constructor for this class. It receives String and turns it into array of
	 * characters.
	 * 
	 * @param text String that lexer turns into tokens
	 * 
	 * @throws NullPointerException if null reference is provided
	 */
	public Lexer(String text) {
		Objects.requireNonNull(text);

		this.data = text.toCharArray();
		this.currentIndex = 0;
		this.token = null;
	}

	/**
	 * Method that checks whether the EOF token already appeared
	 * 
	 * @return has EOF token not been returned before
	 * 
	 */
	public boolean hasNext() {
		return getToken().getType() != TokenType.EOF;
	}

	/**
	 * Getter for last generated token.
	 * 
	 * @return Token that was generated last
	 * @throws new {@link LexerException} if no tokens has been requested before
	 *         method was called
	 */
	public Token getToken() {
		if (token == null) {
			throw new LexerException();
		}
		return this.token;
	}

	/**
	 * Method that returns next Token in query. Multiple kinds of tokens allowed(*
	 * OPERATOR, STRING, AND, VARIABLE, STRING, EOF.)
	 * 
	 * @return Token parsed from text
	 */
	public Token nextToken() {
		if (token != null && token.getType().equals(TokenType.EOF))
			throw new LexerException("Reading after EOF token was sent");
		if (currentIndex == data.length)
			return token = new Token(TokenType.EOF, null);

		while (currentIndex < data.length) {
			return token = getNextToken();
		}
		throw new LexerException();
	}

	/**
	 * private method used to beautify nextToken and to actually get the next token
	 * after previous method assured that there is something to be returned.
	 * 
	 * @return Token parsed from text
	 */
	private Token getNextToken() {
		skipBlanks();
		if (currentIndex == data.length)
			return new Token(TokenType.EOF, null);

		Token t;

		t = checkOperators();
		if (t != null)
			return t;

		t = checkAnd();
		if (t != null)
			return t;

		t = readString();
		if (t != null)
			return t;

		return readVariable();
	}

	/**
	 * Method that is called when we want to check for strings. It knows how to make
	 * a token out of string. Method changes currentIndex.
	 * 
	 * @return Token made by reading part of input char array
	 * 
	 * @throws LexerException if invalid input is given.
	 */
	private Token readString() {
		if (data[currentIndex] != '\"')
			return null;
		currentIndex++;
		StringBuilder sb = new StringBuilder();
		while (currentIndex < data.length) {

			char c = data[currentIndex++];
			if (c == '\"')
				return new Token(TokenType.STRING, sb.toString());
			sb.append(c);
		}
		throw new LexerException("Never Ending String");
	}

	/**
	 * Checks if next few chars in data represent AND
	 * 
	 * @return Token with TokenType.AND or null if substring doesnt't represent AND
	 */
	private Token checkAnd() {
		if (currentIndex + 3 < data.length) {
			String isItAND = "" + data[currentIndex] + data[currentIndex + 1] + data[currentIndex + 2];
			if (isItAND.toLowerCase().equals("and")) {
				currentIndex = currentIndex + 3;
				return new Token(TokenType.AND, null);
			}
		}
		return null;
	}

	/**
	 * check if next few characters represent one of supported operators Less -> L
	 * Greater -> G Greater or equal -> GOE Less or equal LOE Equals ->E Not Equals
	 * -> NE LIKE -> L
	 * 
	 * @return Token with tokenType.OPERATOR and appropriate value to identify
	 *         operator
	 */
	private Token checkOperators() {
		if (currentIndex + 4 < data.length) {
			String isItLike = "" + data[currentIndex] + data[currentIndex + 1] + data[currentIndex + 2]
					+ data[currentIndex + 3];
			if (isItLike.equals("LIKE")) {
				currentIndex = currentIndex + 4;
				return new Token(TokenType.OPERATOR, "L");
			}else if(isItLike.toLowerCase().equals("like")) {
				throw new LexerException("LIKE operator should have been written with uppercase.");
			}
		}

		if (currentIndex + 2 < data.length) {
			if (data[currentIndex] == '>' && data[currentIndex + 1] == '=') {
				currentIndex = currentIndex + 2;
				return new Token(TokenType.OPERATOR, "GOE");
			}
			if (data[currentIndex] == '<' && data[currentIndex + 1] == '=') {
				currentIndex = currentIndex + 2;
				return new Token(TokenType.OPERATOR, "SOE");
			}
			if (data[currentIndex] == '!' && data[currentIndex + 1] == '=') {
				currentIndex = currentIndex + 2;
				return new Token(TokenType.OPERATOR, "NE");
			}

		}
		if (data[currentIndex] == '>') {
			currentIndex = currentIndex + 1;
			return new Token(TokenType.OPERATOR, "G");
		}
		if (data[currentIndex] == '<') {
			currentIndex = currentIndex + 1;
			return new Token(TokenType.OPERATOR, "S");
		}
		if (data[currentIndex] == '=') {
			currentIndex = currentIndex + 1;
			return new Token(TokenType.OPERATOR, "E");
		}
		return null;
	}

	/**
	 * Method that is called when string in form of variable is detected. It knows
	 * how to make a proper token out of string. Method changes currentIndex.
	 * 
	 * @return Token made by reading part of input char array
	 * @throws IllegalArgumentException if not supported operator of format is used
	 */
	private Token readVariable() {
		StringBuilder sb = new StringBuilder();
		if (Character.isLetter(data[currentIndex])) {
			sb.append(data[currentIndex++]);
		} else {
			throw new IllegalArgumentException("Not supported operator or format used.");
		}

		while (currentIndex < data.length) {
			char c = data[currentIndex];
			if (Character.isLetter(c) || Character.isDigit(c) || c == '_') {
				sb.append(c);
				currentIndex++;
			} else
				break;
		}
		return new Token(TokenType.VARIABLE, sb.toString());
	}

	/**
	 * Method used to skip whitespaces predefined at top of class to make nextToken
	 * method easier to read. Changes currentIndex.
	 */
	private void skipBlanks() {
		while (currentIndex < data.length && isWhiteSpace(data[currentIndex])) {
			currentIndex++;
		}
	}

	/**
	 * Method that determines if char is considered whitespace based on constant
	 * array predefined at the top of class
	 * 
	 * @param c char that needs to be checked
	 * @return boolean that answers is this char a whitespace
	 */
	private boolean isWhiteSpace(char c) {
		for (var charToStopToken : WHITESPACES) {
			if (c == charToStopToken) {
				return true;
			}
		}
		return false;
	}

}
