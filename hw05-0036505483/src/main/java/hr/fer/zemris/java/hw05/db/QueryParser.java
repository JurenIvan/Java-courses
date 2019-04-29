package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw05.db.lexer.Lexer;
import hr.fer.zemris.java.hw05.db.lexer.LexerException;
import hr.fer.zemris.java.hw05.db.lexer.Token;
import hr.fer.zemris.java.hw05.db.lexer.TokenType;

/**
 * Class representing parser for queries.
 * 
 * @author juren
 *
 */
public class QueryParser {
	/**
	 * Variable where list of {@link ConditionalExpression} parsed from data is
	 * stored.
	 */
	private List<ConditionalExpression> query;

	/**
	 * Constructor of Parser which gets data and then parses is using tokens that
	 * lexer provided into {@link ConditionalExpression}. Query has to be formated
	 * as variable-operation-value. Command, attribute name, operator, string
	 * literal and logical operator AND can be separated by more than one tabs or
	 * spaces. However, space is not needed between attribute and operator, and
	 * between operator and string literal. Logical operator AND can be written with
	 * any casing: AND, and, AnD etc is OK. Command names, attribute names and
	 * literals are case sensitive.
	 * 
	 * @param data String representing query that gets parsed
	 * 
	 * @throws IllegalArgumentException if query isn't properly formatted
	 */
	public QueryParser(String data) {
		query = new ArrayList<ConditionalExpression>();
		Lexer lexer = new Lexer(data);
		Token variable, operand, value;
		do {
			try {
				variable = lexer.nextToken();
				operand = lexer.nextToken();
				value = lexer.nextToken();
			} catch (LexerException e) {
				if (e.getMessage().isBlank()) {
					throw new IllegalArgumentException("Not enough tokens to make a legit expression");
				}else {
					throw new IllegalArgumentException(e.getMessage());
				}
			}

			if (variable.getType() != TokenType.VARIABLE || operand.getType() != TokenType.OPERATOR
					|| value.getType() != TokenType.STRING)
				throw new IllegalArgumentException("query has to be formated as variable-operation-value");

			query.add(composeQuery(variable, operand, value));
		} while (lexer.nextToken().getType() == TokenType.AND);

	}

	/**
	 * method that composes {@link ConditionalExpression} from 3 tokens.
	 * 
	 * @param variable token whose type has to be TokenType.VARIABLE
	 * @param operand  token whose type has to be TokenType.OPERAND
	 * @param value    token whose type has to be TokenType.STRING
	 * @return {@link ConditionalExpression} made up out of these three tokens
	 * 
	 * @throws IllegalArgumentException if unsuported fieldName tried to be used or
	 *                                  unsuported operator is used or
	 */
	private ConditionalExpression composeQuery(Token variable, Token operand, Token value) {
		IFieldValueGetter fieldGetter;
		String stringLiteral;
		IComparisonOperator comparisonOperator;

		if (variable.getValue().equals("jmbag")) {
			fieldGetter = FieldValueGetters.JMBAG;
		} else if (variable.getValue().equals("lastName")) {
			fieldGetter = FieldValueGetters.LAST_NAME;
		} else if (variable.getValue().equals("firstName")) {
			fieldGetter = FieldValueGetters.FIRST_NAME;
		} else
			throw new IllegalArgumentException(variable.getValue() + " is not supported fieldName.");

		if (operand.getValue().equals("E")) {
			comparisonOperator = ComparisonOperators.EQUALS;
		} else if (operand.getValue().equals("NE")) {
			comparisonOperator = ComparisonOperators.NOT_EQUALS;
		} else if (operand.getValue().equals("G")) {
			comparisonOperator = ComparisonOperators.GREATER;
		} else if (operand.getValue().equals("S")) {
			comparisonOperator = ComparisonOperators.LESS;
		} else if (operand.getValue().equals("SOE")) {
			comparisonOperator = ComparisonOperators.LESS_OR_EQUALS;
		} else if (operand.getValue().equals("GOE")) {
			comparisonOperator = ComparisonOperators.GREATER_OR_EQUALS;
		} else if (operand.getValue().equals("L")) {
			comparisonOperator = ComparisonOperators.LIKE;
		} else
			throw new IllegalArgumentException("Not supported comparison operator.");

		stringLiteral = value.getValue().toString();

		return new ConditionalExpression(fieldGetter, stringLiteral, comparisonOperator);
	}

	/**
	 * Method that checks whether a query is direct or not. If query was of the form
	 * jmbag="xxx" (i.e. it must have only one comparison, on attribute jmbag, and
	 * operator must be equals) then it is direct
	 * 
	 * @return boolean representing whether query is direct or not
	 */
	public boolean isDirectQuery() {
		if (query.size() != 1)
			return false;
		if (!query.get(0).getComparisonOperator().equals(ComparisonOperators.EQUALS))
			return false;
		if (!query.get(0).getFieldGetter().equals(FieldValueGetters.JMBAG))
			return false;
		return true;
	}

	/**
	 * Method that returns jmbag if query is Direct. If query was of the form
	 * jmbag="xxx" (i.e. it must have only one comparison, on attribute jmbag, and
	 * operator must be equals) then it is direct.
	 * 
	 * @return String containing jmbag part of direct query
	 * @throws IllegalStateException if query wasn't direct one.
	 */
	public String getQueriedJMBAG() {
		if (isDirectQuery())
			return query.get(0).getStringLiteral();
		throw new IllegalStateException("Only direct querries have obtainable querried JMBAG");
	}

	/**
	 * Method that returns copy of list containing {@link ConditionalExpression}s.
	 * Of given
	 * 
	 * @return copy of list containing {@link ConditionalExpression} that parser
	 *         produced on given data.
	 */
	public List<ConditionalExpression> getQuery() {
		return new ArrayList<>(query);
	}

}
