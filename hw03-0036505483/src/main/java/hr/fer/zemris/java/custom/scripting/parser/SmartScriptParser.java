package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.lexer.SmartLexer;
import hr.fer.zemris.java.custom.scripting.lexer.SmartLexerState;
import hr.fer.zemris.java.custom.scripting.lexer.SmartTokenType;
import hr.fer.zemris.java.custom.scripting.lexer.Token;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.hw03.prob1.LexerException;

/**
 * Class that represent Parser. Has parse method that takes docBody string and
 * with help of lexer turns it into tree. The whole tree is represented by one
 * {@link DocumentNode} and subNodes are stored in tree-like structure inside
 * its collection.
 * 
 * @author juren
 *
 */
public class SmartScriptParser {
	/**
	 * Variable that stores tree that is made out of provided String
	 */
	private DocumentNode baseDocumentNode;
	/**
	 * Lexer that is responsible for providing tokens to parser
	 */
	private SmartLexer lexer;

	/**
	 * Constructor that creates lexer out of provided string and parses it into
	 * DocumentTree
	 * 
	 * @param docBody
	 */
	public SmartScriptParser(String docBody) {
		lexer = new SmartLexer(docBody);
		parse();
		
	}

	/**
	 * Getter for BaseDocumentNode variable.
	 * 
	 * @return DocumentNode that contains parsed tree structure
	 */
	public DocumentNode getDocumentNode() {
		return baseDocumentNode;
	}

	/**
	 * Method that is called automatically when constructor is called. Takes docBody
	 * string and with help of lexer turns it into tree. The whole tree is
	 * represented by one {@link DocumentNode} and subNodes are stored in tree-like
	 * structure inside its collection. Possible Node types are DocumentNode,
	 * {@link EchoNode}, {@link ForLoopNode} and {@link TextNode}. Each of those can
	 * store different types of statements and elements, as described in their
	 * javaDocs.
	 * 
	 * Method uses stack to build proper tree structure.
	 */
	private void parse() {
		ObjectStack stack = new ObjectStack();
		baseDocumentNode = new DocumentNode();
		stack.push(baseDocumentNode);

		do {
			lexer.setState(SmartLexerState.TEXT);
			getNextTokenSafe();
			if (isTokenOfType(SmartTokenType.TEXT)) {
				((Node) stack.peek()).addChildNode(new TextNode((String) lexer.getToken().getValue()));
				continue;
			}
			if (lexer.getToken().getType() == SmartTokenType.TAG) {
				lexer.setState(SmartLexerState.TAG);
				try {
					getNextTokenSafe();
				} catch (LexerException e) {
					throw new SmartScriptParserException("End of String");
				}
				switch (lexer.getToken().getType()) {
				case FOR:
					ForLoopNode forNode = makeForLoopNode();
					((Node) stack.peek()).addChildNode(forNode);
					stack.push(forNode);
					continue;

				case ECHO:
					EchoNode echoNode = makeEchoNode();
					((Node) stack.peek()).addChildNode(echoNode);
					continue;

				case END:
					try {
						getNextTokenSafe();
						if (!isClosingTag()) {
							throw new SmartScriptParserException("Input Text not formated properly");
						}
						stack.pop();
					} catch (EmptyStackException e) {
						throw new SmartScriptParserException("Input Text not formated properly");
					}
					continue;
				default:
					throw new SmartScriptParserException("Tag has to start with {$ followed by: FOR or = or END");
				}
			} else {
				if (isTokenOfType(SmartTokenType.EOF))
					break;
				throw new SmartScriptParserException("Improper structure");
			}
		} while (!isTokenOfType(SmartTokenType.EOF));
		if (stack.size() != 1) {
			throw new SmartScriptParserException("Doc not formated properly. Too much END tags");
		}
	}

	/**
	 * Makes Echo node using tokens that lexer provides.
	 * 
	 * @return EchoNode build from tokens that lexer provided while in echo
	 *         statement
	 */
	private EchoNode makeEchoNode() {
		ArrayIndexedCollection collection = new ArrayIndexedCollection();
		getNextTokenSafe();
		while (!isClosingTag()) {
			collection.add(getAppropriateForEcho());
			getNextTokenSafe();
		}
		Element[] elements = new Element[collection.size()];
		for (int i = 0; i < collection.size(); i++) {
			elements[i] = (Element) collection.get(i);
		}
		return new EchoNode(elements);
	}

	/**
	 * Makes ForLoop node using tokens that lexer provides.
	 * 
	 * @return ForLoopNode build from tokens that lexer provided while in for
	 *         statement
	 */
	private ForLoopNode makeForLoopNode() {
		ElementVariable variable;
		Element[] expressions = new Element[3];
		getNextTokenSafe();
		variable = (ElementVariable) getVariableElement();

		for (int i = 0; i < 2; i++) {
			getNextTokenSafe();
			expressions[i] = getAppropriateForFor();
		}

		getNextTokenSafe();
		if (!isClosingTag()) {
			expressions[2] = getAppropriateForFor();
		} else {
			expressions[2] = null;
			return new ForLoopNode(variable, expressions[0], expressions[1], expressions[2]);
		}
		lexer.nextToken();
		if(!isClosingTag()) {
			throw new SmartScriptParserException("Too much arguments in for tag");
		}
		
		return new ForLoopNode(variable, expressions[0], expressions[1], expressions[2]);
	}

	/**
	 * Method that checks the type of Token and returns proper Element subclass.
	 * 
	 * Echo accepts tokens of type OPERATOR FUNCTION DOUBLE INTEGER STRING and
	 * VARIABLE
	 * 
	 * @return Element
	 * @throws SmartScriptParserException if not recognized token type appears
	 */
	private Element getAppropriateForEcho() {
		if (isTokenOfType(SmartTokenType.OPERATOR))
			return new ElementOperator(lexer.getToken().getValue().toString());
		if (isTokenOfType(SmartTokenType.FUNCTION))
			return new ElementFunction((String) lexer.getToken().getValue().toString());
		return getAppropriateForFor();
	}

	/**
	 * Method that checks the type of Token and returns proper Element subclass.
	 * 
	 * ForLoopNode accepts tokens of type DOUBLE INTEGER STRING and VARIABLE
	 * 
	 * @return Element
	 * @throws ParseException if not recognized token type appears
	 */
	private Element getAppropriateForFor() {
		if (isTokenOfType(SmartTokenType.DOUBLE))
			return new ElementConstantDouble((Double) lexer.getToken().getValue());
		if (isTokenOfType(SmartTokenType.INTEGER))
			return new ElementConstantInteger((Integer) lexer.getToken().getValue());
		if (isTokenOfType(SmartTokenType.STRING))
			return new ElementString((String) lexer.getToken().getValue().toString());
		return getVariableElement();
	}

	/**
	 * Method that checks the type of Token and returns proper Element subclass.
	 * 
	 * Accepts only VARIABLE token
	 * 
	 * @return Element
	 * @throws SmartScriptParserException if not recognized token type appears
	 */
	private Element getVariableElement() {
		if (isTokenOfType(SmartTokenType.VARIABLE))
			return new ElementVariable((String) lexer.getToken().getValue().toString());
		throw new SmartScriptParserException("Arguments must be either double,integer,variable or string.");
	}

	/**
	 * Method that checks whether the Smart Token is the same type as the one
	 * provided in argument.
	 * 
	 * @param type SmartTokenType compared against
	 * @return boolean representing result of comparison
	 */
	private boolean isTokenOfType(SmartTokenType type) {
		if (lexer == null)
			throw new SmartScriptParserException("Type of yet non existant Token needed.");
		return lexer.getToken().getType() == type;
	}

	// I disagree with "If this line ever executes, you have failed this class!". If
	// lexer founds error it is natural (for me) that he throws it appropriately. This
	// method insures that no Lexer exceptions end up past parser.
	/**
	 * This is way to delegate all lexer errors to parser in the way he excepts it
	 * 
	 * @return Token but safely (No {@link LexerException} possible)
	 * 
	 * @throws SmartScriptParserException
	 */
	private Token getNextTokenSafe() {
		try {
			return lexer.nextToken();
		} catch (LexerException e) {
			throw new SmartScriptParserException("Input Text not formated properly");
		}

	}

	/**
	 * Method that checks whether provided token stores "$}". Uses .equals() to
	 * determine equality.
	 * 
	 * @return boolean result of comparison
	 */
	private boolean isClosingTag() {
		return isTokenOfType(SmartTokenType.TAG) ? lexer.getToken().getValue().equals("$}") : false;
	}
//		End up implementing but not using... left here for possible future improvement of code
//	/**
//	 * Method that checks whether provided token stores "{$". Uses .equals() to
//	 * determine equality.
//	 * 
//	 * @return boolean result of comparison
//	 */
//	private boolean isOpeningTag() {
//		return isTokenOfType(SmartTokenType.TAG) ? lexer.getToken().getValue().equals("{$") : false;
//	}

}
