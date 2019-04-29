package hr.fer.zemris.java.hw03.prob1;

import java.util.Objects;

/**
 * Class representing lexer. Has classic lexer methods; getToken, setState,
 * NextToken and a constructor. This lexer can give back 3 types of tokens;
 * Words, Numbers and symbols. Escaping is supported and it's used to present
 * digit as a letter if we desire. Numbers that it can interpret are in range of
 * Long type. At the end of file EOF token is generated.
 * 
 * @author juren
 *
 */
public class Lexer {
	/**
	 * Array of input chars. Text that it has to tokenize.
	 */
	private char[] data;
	/**
	 * Class remembers the last token that was sent because parser might need same.
	 * token more than once
	 */
	private Token token;
	/**
	 * represents index of character where we got while tokenizing.
	 */
	private int currentIndex;
	/**
	 * State that determines rules by which lexer operates.
	 */
	private LexerState lexerState;

	/**
	 * Array of characters that represent whitespaces used to separate tokens.
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
		this.lexerState = LexerState.BASIC;
	}

	/**
	 * Getter for last generated token.
	 * 
	 * @return Token that was generated last
	 * 
	 * @throws LexerException if no token has been produced so far.
	 */
	public Token getToken() {
		if (token == null) {
			throw new LexerException();
		}
		return this.token;
	}

	/**
	 * Dependent on state, lexer tokenizes by different rules. This method allows
	 * parser to change rules of the game
	 * 
	 * @param state that we want to put our lexer into
	 * 
	 * @throws NullPointerException if given argument is null reference
	 */
	public void setState(LexerState state) {
		Objects.requireNonNull(state);
		this.lexerState = state;
	}

	/**
	 * Method that gives us the next token. Supports escaping and it can work under
	 * a two sets of rules.
	 * 
	 * If and when string comes to an end (all tokens from text are returned) EOF
	 * token is sent.
	 * 
	 * Lexer organizes text into tokens by splitting them based on whitespaces. If
	 * method notices that the current character is not the same type as last one,
	 * it returns token. If method find something that isn't letter or digit it
	 * sends token whose type is SYMBOL.
	 * 
	 * If // is followed by digit, it is considered as a letter.
	 * 
	 * @return Token that it recognized
	 * 
	 * @throws if invalid escaping occurred or number that it should have been parsed
	 *         is too big.
	 */
	public Token nextToken() {
		boolean escape = false;
		skipBlanks();
		if (token != null && token.getType().equals(TokenType.EOF)) {
			throw new LexerException("Reading after EOF token was sent");
		}
		if (currentIndex == data.length)
			return token = new Token(TokenType.EOF, null);

		StringBuilder sb = new StringBuilder();
		TokenType thisTokenType = getTokenType(data[currentIndex], escape);

		if (thisTokenType == TokenType.SYMBOL)
			return token = new Token(TokenType.SYMBOL, data[currentIndex++]);

		while (currentIndex < data.length && !isWhiteSpace(data[currentIndex])) {

			if (data[currentIndex] != '#' && lexerState == LexerState.EXTENDED) {
				thisTokenType = TokenType.WORD;
				sb.append(data[currentIndex]);
				escape = false;
				currentIndex++;
				continue;
			}
			if (getTokenType(data[currentIndex], escape) != thisTokenType)
				break;

			if (escape == false && data[currentIndex] == '\\') {
				currentIndex++;
				escape = true;
				continue;
			}
			if (escape == true && !(Character.isDigit(data[currentIndex]) || data[currentIndex] == '\\')) {
				throw new LexerException();
			}
			sb.append(data[currentIndex]);
			escape = false;
			currentIndex++;
		}
		if (escape == true) {
			throw new LexerException();
		}
		return token = makeToken(sb, thisTokenType);
	}

	/**
	 * Method used to skip whitespaces to make nextToken method easier to read.
	 */
	private void skipBlanks() {
		while (currentIndex < data.length && isWhiteSpace(data[currentIndex])) {
			currentIndex++;
		}
	}

	/**
	 * Method used to make token out of char array. It reads characters and moves
	 * currentIndex
	 * 
	 * @param sb            {@link StringBuilder} used to make string needed for
	 *                      token
	 * @param thisTokenType method needs to know whether it will make word or number
	 *                      token
	 * @return requested token or EOF if end of method is reached
	 * 
	 * @throws LexerException if invalid input is given (number too big)
	 */
	private Token makeToken(StringBuilder sb, TokenType thisTokenType) {
		switch (thisTokenType) {
		case WORD:
			return new Token(TokenType.WORD, sb.toString());
		case NUMBER:
			try {
				long number = Long.parseLong(sb.toString());
				return new Token(TokenType.NUMBER, number);
			} catch (NumberFormatException e) {
				// I'd write string below as "Invalid input" but this output was (kinda) specified in
				// task so i don't want to risk about it :"(u tom slučaju lexer mora baciti iznimku: ulaz ne valja!)"
				throw new LexerException("Ulaz ne valja!");
			}
		default:
			return new Token(TokenType.EOF, null);
		}
	}

	/**
	 * Method used to determine type of token based on char.
	 * 
	 * @param c      char that determines the type
	 * @param escape boolean determining whether we should consider numbers as
	 *               letters
	 * @return TokenType based on arguments
	 */
	private TokenType getTokenType(char c, boolean escape) {
		if (escape == true)
			return TokenType.WORD;
		if (c == '\\')
			return TokenType.WORD;
		if (Character.isLetter(c))
			return TokenType.WORD;
		if (Character.isDigit(c))
			return TokenType.NUMBER;

		return TokenType.SYMBOL;
	}

	/**
	 * Method that determines if char is considered whitespace based on constant
	 * array predefined at the top of class.
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