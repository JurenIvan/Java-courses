package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

/**
 * Class that models conditional expression. It has 1 field value getter, String
 * Literal and one comparison operator as well as getters, setters and
 * constructor.
 * 
 * @author juren
 *
 */
public class ConditionalExpression {
	/**
	 * FieldGetter for expression
	 */
	private IFieldValueGetter fieldGetter;
	/**
	 * Literal upon a comparison is executed
	 */
	private String stringLiteral;
	/**
	 * Instance of {@link IComparisonOperator} that defines operation betwen
	 * fieldGetter and stringLiteral
	 */
	private IComparisonOperator comparisonOperator;

	/**
	 * Standard Constructor for this class.
	 * 
	 * @param valueGetter        FieldGetter for expression
	 * @param literal            Literal upon a comparison is executed
	 * @param comparasonOperator instance of {@link IComparisonOperator} that
	 *                           defines operation between fieldGetter and
	 *                           stringLiteral
	 */
	public ConditionalExpression(IFieldValueGetter fieldGetter, String stringLiteral,
			IComparisonOperator comparisonOperator) {
		super();
		Objects.requireNonNull(fieldGetter,"FieldGetter must not be null");
		Objects.requireNonNull(stringLiteral,"stringLiteral must not be null");
		Objects.requireNonNull(comparisonOperator,"Comparason operator argument must not be null");
		
		this.fieldGetter = fieldGetter;
		this.stringLiteral = stringLiteral;
		this.comparisonOperator = comparisonOperator;
	}

	/**
	 * Standard getter for {@link #FieldValue}
	 * 
	 * @return FieldGetter
	 */
	public IFieldValueGetter getFieldGetter() {
		return fieldGetter;
	}

	/**
	 * Standard getter for {@link #stringLiteral}
	 * 
	 * @return {@link #stringLiteral}
	 */
	public String getStringLiteral() {
		return stringLiteral;
	}

	/**
	 * Standard getter for {@link #comparisonOperator}
	 * 
	 * @return comparison operator
	 */
	public IComparisonOperator getComparisonOperator() {
		return comparisonOperator;
	}

}
