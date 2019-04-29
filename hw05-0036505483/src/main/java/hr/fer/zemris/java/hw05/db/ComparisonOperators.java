package hr.fer.zemris.java.hw05.db;

/**
 * Class containing seven basic implementations of strategy IComparisonOperators.
 * 
 * @author juren
 *
 */
public class ComparisonOperators {

	/**
	 * Instance of {@link IComparisonOperator} which is functional interface whose
	 * function is overridden in static block and when two objects are passed to it,
	 * it calculates whether the relation described in name (Less,greater,etc) is
	 * true for those two objects.Function that is overridden in static block is
	 * satisfied(String, String) that returns booleans.
	 * 
	 */
	public static final IComparisonOperator LESS, LESS_OR_EQUALS, GREATER, GREATER_OR_EQUALS, EQUALS, NOT_EQUALS;
	/**
	 * Instance of {@link IComparisonOperator} which is functional interface whose
	 * function is overridden in static block and when two objects are passed to it,
	 * it calculates whether the second string if found inside first one similar to
	 * regex expression.
	 * 
	 * User can use 1 * that represent 0,1 or more characters.
	 * 
	 * for example LIKE "Word*" is true for all strings that start with word or Like
	 * "Ho*i" is true for all string that start with "Ho" and end with "i"
	 */
	public static final IComparisonOperator LIKE;

	static {
		LESS = (value1, value2) -> value1.compareTo(value2) < 0 ? true : false;
		LESS_OR_EQUALS = (value1, value2) -> value1.compareTo(value2) <= 0 ? true : false;
		GREATER = (value1, value2) -> value1.compareTo(value2) > 0 ? true : false;
		GREATER_OR_EQUALS = (value1, value2) -> value1.compareTo(value2) >= 0 ? true : false;
		EQUALS = (value1, value2) -> value1.compareTo(value2) == 0 ? true : false;
		NOT_EQUALS = (value1, value2) -> value1.compareTo(value2) != 0 ? true : false;
		LIKE = (text, pattern) -> ComparisonOperators.likenessResolver(text, pattern);
	}

	/**
	 * Private method that is used to beautify code of static block. it takes String
	 * and tries to find pattern provided inside it using wildcard as described in
	 * {@link ComparisonOperators#LIKE}
	 * 
	 * @param text    string where we are finding pattern
	 * @param pattern patter that (can but donesn't have to) contain wildcard.
	 * @return boolean representing whether initial text is like pattern.
	 * 
	 * @throws IllegalArgumentException if more than one wildcard is given in
	 *                                  pattern
	 */
	private static boolean likenessResolver(String text, String pattern) {
		int positionOfWildCard = pattern.indexOf("*");

		if (positionOfWildCard == -1)
			return text.equals(pattern);
		if (positionOfWildCard != pattern.lastIndexOf("*"))
			throw new IllegalArgumentException("Using more than one wildcard is not supported.");

		// beggining
		if (positionOfWildCard == 0) {
			if (text.endsWith(pattern.substring(1)))
				return true;
			return false;
		}

		// end
		if (positionOfWildCard == pattern.length() - 1) {
			if (text.startsWith(pattern.substring(0, pattern.length() - 1)))
				return true;
			return false;
		}

		// middle
		if (text.startsWith(pattern.substring(0, positionOfWildCard)) && text.substring(positionOfWildCard)
				.endsWith(pattern.substring(positionOfWildCard + 1, pattern.length())))
			return true;

		return false;
	}

}
