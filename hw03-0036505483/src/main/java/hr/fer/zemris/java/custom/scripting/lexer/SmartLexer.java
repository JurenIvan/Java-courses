package hr.fer.zemris.java.custom.scripting.lexer;

import java.util.Objects;

import hr.fer.zemris.java.hw03.prob1.LexerException;

/**
 * Class representing lexer. Has classic lexer methods; getToken, setState,
 * NextToken and a constructor. This lexer can give back 12 types of tokens:
 * TEXT TAG EOF INTEGER DOUBLE STRING FUCNTION OPERATOR VARIABLE FOR ECHO END.
 * 
 * Lexer has 2 states. They're called TEXT and TAG.
 * 
 * If lexer is in state TEXT it can give back only TEXT or TAG tokens. Text is
 * everything between start and first {$ and everything between $} and {$ not
 * including those because thowse are TAG-s
 *
 * When in TAG state, lexer can read all other types of token. Token is
 * considered Integer if it's only a digits and possibly with minus sign. Token
 * is considered Double if it's in number-dot-number form (minus is acceptable)
 * Token is considered String if part of text inside tag is surrounded by double
 * quotes Token is considered Function if @ is before it. Token is considered
 * Operator if its + - * / ^ Token is considered Variable if its string that
 * starts with letter and after that digit or number Token is considered for if
 * it is a string thats equals("for") but it is not case sensitive Token is
 * considered echo if it is a string thats equals("echo") but it not case
 * sensitive Token is considered end if it is a string thats equals("end") but
 * it not case sensitive Token is EOF if end of file is reached Escaping is also
 * allowed and its rules are determined by LexerState.
 * 
 * 
 * @author juren
 *
 */
public class SmartLexer {
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
	 * represents index of character where we got while tokenizing
	 */
	private int currentIndex;
	/**
	 * State that determines rules by which lexer operates
	 */
	private SmartLexerState lexerState;

	/**
	 * Array of characters that represent whitespaces used to separate tokens
	 */
	private static final char[] WHITESPACES = { '\r', '\n', '\t', ' ' };
	/**
	 * Array of supported Operators
	 */
	private static final char[] OPERATORS = { '+', '-', '*', '/', '^', };

	/**
	 * Constructor for this class. It receives String and turns it into array of
	 * characters.
	 * 
	 * @param text String that lexer turns into tokens
	 * 
	 * @throws NullPointerException if null reference is provided
	 */
	public SmartLexer(String text) {
		Objects.requireNonNull(text);

		this.data = text.toCharArray();
		this.currentIndex = 0;
		this.token = null;
		this.lexerState = SmartLexerState.TEXT;
	}

	/**
	 * Getter for last generated token
	 * 
	 * @return Token that was generated last
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
	public void setState(SmartLexerState state) {
		Objects.requireNonNull(state);
		this.lexerState = state;
	}

	/**
	 * Method that gives a user a next token. Supports escaping and two sets of
	 * rules based on Lexers state.
	 * 
	 * gives tokens on next set of rules If lexer is in state TEXT it can give back
	 * only TEXT or TAG tokens. Text is everything between start and first {$ and
	 * everything between $} and {$ not including those because thowse are TAG-s
	 * 
	 * When in TAG state, lexer can read all other types of token. Token is
	 * considered Integer if it's only a digits and possibly with minus sign. Token
	 * is considered Double if it's in number-dot-number form (minus is acceptable)
	 * Token is considered String if part of text inside tag is surrounded by
	 * doublequotes Token is considered Function if @ is before it. Token is
	 * considered Operator if its + - * / ^ Token is considered Variable if its
	 * string that starts with letter and after that digit or number Token is
	 * considered for if it is a string thats equals("for") but it is not case
	 * sensitive Token is considered echo if it is a string thats equals("echo") but
	 * it not case sensitive Token is considered end if it is a string thats
	 * equals("end") but it not case sensitive Token is EOF if end of file is
	 * reached Escaping is also allowed and its rules are determined by LexerState.
	 * 
	 * 
	 * @return Token based on rules defined above
	 * 
	 * @throws LexerException
	 */
	public Token nextToken() {
		if (token != null && token.getType().equals(SmartTokenType.EOF))
			throw new LexerException("Reading after EOF token was sent");
		if (currentIndex == data.length)
			return token = new Token(SmartTokenType.EOF, null);

		while (currentIndex < data.length) {
			switch (lexerState) {
			case TEXT:
				return dealWithText();
			case TAG:
				return dealWithTag();
			}
		}
		throw new LexerException();

	}

	/**
	 * Method that is called when lexer is in TAG state.
	 * 
	 * @return Token when tag part of input string is analyzed
	 */
	private Token dealWithTag() {
		skipBlanks();
		if (currentIndex == data.length)
			return token = new Token(SmartTokenType.EOF, null);

		if (Character.isLetter(data[currentIndex])) {
			Token tempToken = readVariable();
			if (((String) tempToken.getValue()).equalsIgnoreCase("for"))
				return token = new Token(SmartTokenType.FOR, null);
			if (((String) tempToken.getValue()).equalsIgnoreCase("end"))
				return token = new Token(SmartTokenType.END, null);
			return token = tempToken;
		}

		if (Character.isDigit(data[currentIndex]))
			return token = readNumbers();

		if (data[currentIndex] == '=') {
			currentIndex++;
			return token = new Token(SmartTokenType.ECHO, null);
		}
		if (data[currentIndex] == '\"')
			return token = readString();

		if (currentIndex < (data.length - 1) && data[currentIndex] == '-'
				&& Character.isDigit(data[currentIndex + 1])) {
			currentIndex++;
			Token tempToken = readNumbers();
			if (tempToken.getType() == SmartTokenType.DOUBLE)
				return token = new Token(SmartTokenType.DOUBLE, -1 * (Double) tempToken.getValue());
			return token = new Token(SmartTokenType.INTEGER, -1 * (Integer) tempToken.getValue());
		}
		if (isSupportedOperator(data[currentIndex]))
			return token = new Token(SmartTokenType.OPERATOR, data[currentIndex++]);

		if (data[currentIndex] == '@') {
			currentIndex++;
			Token tempToken = readVariable();
			return token = new Token(SmartTokenType.FUNCTION, tempToken.getValue());
		}
		if (currentIndex < data.length - 1 && data[currentIndex] == '$') {
			if (currentIndex < data.length - 1 && data[currentIndex + 1] == '}') {
				currentIndex = currentIndex + 2;
				return token = new Token(SmartTokenType.TAG, "$}");
			}
			throw new LexerException();
		}
		throw new LexerException();
	}

	/**
	 * Method that is called when string is detected. It knows how to make a token
	 * out of string. Method changes currentIndex.
	 * 
	 * @return Token made by reading part of input char array
	 * 
	 * @throws LexerException if invalid input is given.
	 */
	private Token readString() {
		if (data[currentIndex++] != '\"')
			throw new LexerException("This method can parse only strings surrounded by double quotes");
		StringBuilder sb = new StringBuilder();
		while (currentIndex < data.length-1) {
			
			char c = data[currentIndex++];
			if (c == '\"')
				return new Token(SmartTokenType.STRING, sb.toString());

			if (c == '\\') {
				if (data[currentIndex] == '\"') {
					currentIndex++;
					sb.append('\"');
					continue;
				}
			}
			
				sb.append(c);
				
		}
		throw new LexerException("Never Ending String");
	}

	/**
	 * Method that is called when number is detected. It knows how to make a token
	 * out of number. Method changes currentIndex.
	 * 
	 * @return Token made by reading part of input char array
	 * 
	 * @throws LexerException if invalid input is given
	 */
	private Token readNumbers() {
		StringBuilder sb = new StringBuilder();
		boolean isDecimalPointPresent = false;

		if (Character.isDigit(data[currentIndex])) {
			sb.append(data[currentIndex++]);
		} else {
			throw new LexerException("Method readNumbers can parse only numbers");
		}

		while (currentIndex < data.length) {
			char c = data[currentIndex];
			if (Character.isDigit(c)) {
				sb.append(c);
				currentIndex++;
				continue;
			}
			if (c == '.') {
				if (isDecimalPointPresent) {
					throw new LexerException("Multiple decimal points inside single number");
				}
				if (Character.isDigit(data[currentIndex + 1])) {
					isDecimalPointPresent = true;
					currentIndex++;
					sb.append('.');
					continue;
				}
			}
			break;
		}
		if (isDecimalPointPresent) {
			try {
				return new Token(SmartTokenType.DOUBLE, Double.parseDouble(sb.toString()));
			} catch (NumberFormatException e) {
				throw new LexerException("Number is too big");
			}
		}
		try {
			return new Token(SmartTokenType.INTEGER, Integer.parseInt(sb.toString()));
		} catch (NumberFormatException e) {
			throw new LexerException("Number is too big");
		}
	}

	/**
	 * Method that is called when string in form of variable is detected. It knows
	 * how to make a proper token out of string. Method changes currentIndex.
	 * 
	 * @return Token made by reading part of input char array
	 */
	private Token readVariable() {
		StringBuilder sb = new StringBuilder();
		if (Character.isLetter(data[currentIndex])) {
			sb.append(data[currentIndex++]);
		} else {
			throw new LexerException("First character has to be letter for this method to work");
		}

		while (currentIndex < data.length) {
			char c = data[currentIndex];
			if (Character.isLetter(c) || Character.isDigit(c) || c == '_') {
				sb.append(c);
				currentIndex++;
			} else
				break;
		}
		return new Token(SmartTokenType.VARIABLE, sb.toString());
	}

	/**
	 * Method that is called when in Lexer state TEXT. It knows how to make a TEXT
	 * token out of string. Method changes currentIndex.
	 * 
	 * @return Token made by reading part of input char array
	 */
	private Token dealWithText() {

		if (currentIndex < (data.length - 1) && data[currentIndex] == '{' && data[currentIndex + 1] == '$') {
			currentIndex = currentIndex + 2;
			return token = new Token(SmartTokenType.TAG, "{$");
		}
		StringBuilder sb = new StringBuilder();

		while (currentIndex < data.length) {
			if (data[currentIndex] == '\\') {
				if (currentIndex < (data.length - 1)
						&& (data[currentIndex + 1] == '{' || data[currentIndex + 1] == '\\')) {
					sb.append(data[currentIndex]);
					sb.append(data[currentIndex + 1]);
					currentIndex = currentIndex + 2;
					continue;
				}
				throw new LexerException("Illegal escaping");
			}

			if (currentIndex < (data.length - 1) && data[currentIndex] == '{' && data[currentIndex + 1] == '$') {
				return token = new Token(SmartTokenType.TEXT, sb.toString());
			}

			sb.append(data[currentIndex++]);
		}

		return token = new Token(SmartTokenType.TEXT, sb.toString());

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

	/**
	 * Method that determines if char is considered operator based on constant array
	 * predefined at the top of class
	 * 
	 * @param c char that needs to be checked
	 * @return boolean that answers is this char a supportedOperator
	 */
	private boolean isSupportedOperator(char c) {
		for (var operator : OPERATORS) {
			if (c == operator) {
				return true;
			}
		}
		return false;
	}

}
