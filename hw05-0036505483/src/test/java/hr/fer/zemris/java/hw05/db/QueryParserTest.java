package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class QueryParserTest {

	@Test
	public void queryTestNotAnDirectQuerry() {
		QueryParser queryParser = new QueryParser(" firstName>\"Juren\" and jmbag=\"shouldHaveBeenNumbers\" ");
		assertFalse(queryParser.isDirectQuery());
	}
	
	@Test
	public void directQueryTestNormalUsecase() {
		QueryParser queryParser = new QueryParser(" jmbag =\"shouldHaveBeenNumbers\" ");
		assertTrue( queryParser.isDirectQuery()); 
	}
	
	@Test
	public void directQueryTestGettingTheValue() {
		QueryParser queryParser = new QueryParser(" jmbag =\"shouldHaveBeenNumbers\" ");
		assertEquals("shouldHaveBeenNumbers", queryParser.getQueriedJMBAG()); 
	}
	
	@Test
	public void queryTestNumberOfConditionalExpressions() {
		QueryParser queryParser = new QueryParser("jmbag=\"shouldHaveBeenNumbers\" and lastName>\"Juren\"");
		assertEquals(2,  queryParser.getQuery().size());
	}
	
	
	
	@Test
	public void queryTestThrowsExceptionWhenGetQueriedJmbagIsCalled() {
		QueryParser queryParser = new QueryParser("jmbag=\"shouldHaveBeenNumbers\" and lastName>\"Juren\"");
		assertThrows(IllegalStateException.class, ()->queryParser.getQueriedJMBAG());
	}
	
	
}
