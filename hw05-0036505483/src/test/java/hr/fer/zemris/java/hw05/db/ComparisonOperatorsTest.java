package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ComparisonOperatorsTest {

	@Test
	public void lessTest() {
		IComparisonOperator operator = ComparisonOperators.LESS;
		assertTrue(operator.satisfied("Mali", "ZZZZZ Veliki"));
	}

	@Test
	public void greaterTest() {
		IComparisonOperator operator = ComparisonOperators.GREATER;
		assertTrue(operator.satisfied("ZZZZ Veliki", "Mali"));
	}
	
	

	@Test
	public void equalTest() {
		IComparisonOperator operator = ComparisonOperators.EQUALS;
		assertTrue(operator.satisfied("hmmm", "hmmm"));
	}

	@Test
	public void lessOrEqualTest() {
		IComparisonOperator operator = ComparisonOperators.LESS_OR_EQUALS;
		assertTrue(operator.satisfied("Mali", "Mali"));
		assertTrue(operator.satisfied("Mali", "Veci"));
	}

	@Test
	public void greaterOrEqualTest() {
		IComparisonOperator operator = ComparisonOperators.GREATER_OR_EQUALS;
		assertTrue(operator.satisfied("Veci", "Veci"));
		assertTrue(operator.satisfied("Veci", "Mali"));
	}

	@Test
	public void notEqualTest() {
		IComparisonOperator operator = ComparisonOperators.NOT_EQUALS;
		assertTrue(operator.satisfied("jednako", "nije jednako"));
	}

	@Test
	public void likeTestRegularUse() {
		IComparisonOperator operator = ComparisonOperators.LIKE;
		assertTrue(operator.satisfied("Biiić", "B*ć"));

	}

	@Test
	public void likeTestwildcardReplaceOneLetter() {
		IComparisonOperator operator = ComparisonOperators.LIKE;
		assertTrue(operator.satisfied("bić", "b*ć"));
	}

	@Test
	public void likeTestWildcardRepalcesNoLetter() {
		IComparisonOperator operator = ComparisonOperators.LIKE;
		assertTrue(operator.satisfied("bć", "b*ć"));

	}

	@Test
	public void likeTestWildcardCanCauseProblemsBecauseSymetry() {
		IComparisonOperator operator = ComparisonOperators.LIKE;
		assertFalse(operator.satisfied("AAA", "AA*AA"));

	}

	@Test
	public void likeTestOnlyWildcard() {
		IComparisonOperator operator = ComparisonOperators.LIKE;
		assertTrue(operator.satisfied("Ivan", "*"));

	}
	
	@Test
	public void greaterTest2() {
		IComparisonOperator operator = ComparisonOperators.GREATER;
		assertTrue(operator.satisfied("ZZZZ Veliki", "ZMali"));
	}

	@Test
	public void likeTestEmptyString() {
		IComparisonOperator operator = ComparisonOperators.LIKE;
		assertTrue(operator.satisfied("", "*"));

	}

	@Test
	public void likeTestNotTrue() {
		IComparisonOperator operator = ComparisonOperators.LIKE;
		assertFalse(operator.satisfied("Zagreb", "Aba*"));

	}

	@Test
	public void likeTestWildcardSymetry() {
		IComparisonOperator operator = ComparisonOperators.LIKE;
		assertTrue(operator.satisfied("AAAA", "AA*AA"));

	}


}
