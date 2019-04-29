package hr.fer.zemris.java.hw05.db;

import java.util.List;

/**
 * Class that has constructor and one method. Its use is to filter one record
 * with multiple filters
 * 
 * @author juren
 *
 */
public class QueryFilter implements IFilter {
	/**
	 * list of all conditionals that record has to pass
	 */
	private List<ConditionalExpression> listOfExpressions;

	/**
	 * Constructor for this class that gets list of Conditions and stores it
	 * 
	 * @param listOfExpressions
	 */
	public QueryFilter(List<ConditionalExpression> listOfExpressions) {
		this.listOfExpressions = listOfExpressions;
	}

	/**
	 * returns boolean which is true if record passed satisfies all of the
	 * conditionals passed through constructor.
	 * 
	 * @param record StudentRecord that is tested
	 * @return boolean representing whether the passed argument satisfies all of
	 *         conditions.
	 */
	@Override
	public boolean accepts(StudentRecord record) {
		for (var test : listOfExpressions) {
			if (test.getComparisonOperator().satisfied(test.getFieldGetter().get(record),
					test.getStringLiteral()) == false)
				return false;
		}
		return true;
	}

}
