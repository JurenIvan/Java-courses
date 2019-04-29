package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class ConditionalExpressionTest {

	@Test
	public void testContitionalExpressesionGettersAndEvaluationTrue1() throws IOException {

		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Jusufadis", 2);
		ConditionalExpression expression1 = new ConditionalExpression(FieldValueGetters.LAST_NAME, "Brezović",
				ComparisonOperators.EQUALS);

		assertTrue(expression1.getComparisonOperator().satisfied(expression1.getFieldGetter().get(record),
				expression1.getStringLiteral()));

	}

	@Test
	public void testContitionalExpressesionGettersAndEvaluationTrue2() throws IOException {

		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Jusufadis", 2);
		ConditionalExpression expression1NOT = new ConditionalExpression(FieldValueGetters.LAST_NAME, "Brez",
				ComparisonOperators.NOT_EQUALS);

		assertTrue(expression1NOT.getComparisonOperator().satisfied(expression1NOT.getFieldGetter().get(record),
				expression1NOT.getStringLiteral()));

	}

	@Test
	public void testContitionalExpressesionGettersAndEvaluationFalse() throws IOException {

		StudentRecord record = new StudentRecord("0000000005", "Brezović", "Jusufadis", 2);
		ConditionalExpression expression2 = new ConditionalExpression(FieldValueGetters.LAST_NAME, "Brez",
				ComparisonOperators.EQUALS);

		assertFalse(expression2.getComparisonOperator().satisfied(expression2.getFieldGetter().get(record),
				expression2.getStringLiteral()));

	}

}
