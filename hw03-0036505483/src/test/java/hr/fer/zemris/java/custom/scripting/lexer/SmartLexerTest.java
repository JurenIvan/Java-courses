package hr.fer.zemris.java.custom.scripting.lexer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import hr.fer.zemris.java.hw03.prob1.LexerException;

class SmartLexerTest {
	
	@Test
	public void testNotNull() {
		SmartLexer lexer = new SmartLexer("");
		assertNotNull(lexer.nextToken());
	}
	
	@Test
	public void testNullInput() {
		assertThrows(NullPointerException.class, () -> new SmartLexer(null));
	}
	
	@Test
	void eOFTestTest() {
		
		SmartLexer lexer=new SmartLexer("FOR year 1");
		lexer.nextToken();
		lexer.nextToken();
		assertEquals(SmartTokenType.EOF, lexer.getToken().getType());
		assertEquals(null, lexer.getToken().getValue());
	}
	
	@Test
	void eOFEmptyStringTest() {
		SmartLexer lexer=new SmartLexer(" ");
		lexer.nextToken();
		assertEquals(SmartTokenType.TEXT, lexer.getToken().getType());
		assertEquals(" ", lexer.getToken().getValue());
		lexer.nextToken();
		assertEquals(SmartTokenType.EOF, lexer.getToken().getType());
		assertEquals(null, lexer.getToken().getValue());
	}
	
	@Test
	void openTagTest() {
		SmartLexer lexer=new SmartLexer("{$ FOR year 1");
		lexer.nextToken();
		lexer.setState(SmartLexerState.TAG);
		assertEquals(SmartTokenType.TAG, lexer.getToken().getType());
		assertEquals("{$", lexer.getToken().getValue());
	}
	
	@Test
	void closedTagTest() {
		SmartLexer lexer=new SmartLexer("$} FOR year 1");
		lexer.setState(SmartLexerState.TAG);
		lexer.nextToken();
		
		assertEquals(SmartTokenType.TAG, lexer.getToken().getType());
		assertEquals("$}", lexer.getToken().getValue());
	}
	
	@Test
	void eNDConstructTest() {
		SmartLexer lexer=new SmartLexer("{$END$}");
		
		lexer.nextToken();
		
		assertEquals(SmartTokenType.TAG, lexer.getToken().getType());
		assertEquals("{$", lexer.getToken().getValue());
		
		lexer.setState(SmartLexerState.TAG);
		lexer.nextToken();
		
		assertEquals(SmartTokenType.END, lexer.getToken().getType());
		assertEquals(null, lexer.getToken().getValue());
		
		lexer.nextToken();
		
		assertEquals(SmartTokenType.TAG, lexer.getToken().getType());
		assertEquals("$}", lexer.getToken().getValue());
		
	}
	
	@Test
	void textEscapeTest() {
		SmartLexer lexer=new SmartLexer("FOR \"year\" 1");
		lexer.nextToken();
		assertEquals(SmartTokenType.TEXT, lexer.getToken().getType());
		assertEquals("FOR \"year\" 1", lexer.getToken().getValue());
	}
	
	@Test
	void textDoubleEscapeTest() {
		SmartLexer lexer=new SmartLexer("FOR \"ye\"year\"ar\" 1");
		lexer.nextToken();
		assertEquals(SmartTokenType.TEXT, lexer.getToken().getType());
		assertEquals("FOR \"ye\"year\"ar\" 1", lexer.getToken().getValue());
	}
	
	@Test
	void getterTest() {
		SmartLexer lexer=new SmartLexer("FOR");
		lexer.nextToken();
		assertEquals(lexer.getToken(), lexer.getToken());
	}
	
	
	@Test
	void eOFExceptionTest(){
		SmartLexer lexer=new SmartLexer("FOR");
		lexer.nextToken();
		lexer.nextToken();
		assertEquals(SmartTokenType.EOF,lexer.getToken().getType());
		assertThrows(LexerException.class, () -> lexer.nextToken());
	}
	
	@Test
	void forConstructTest() {
		SmartLexer lexer=new SmartLexer("{$ FOR year 1");
		lexer.nextToken();
		lexer.setState(SmartLexerState.TAG);
		assertEquals(SmartTokenType.TAG, lexer.getToken().getType());
		assertEquals("{$", lexer.getToken().getValue());
		lexer.nextToken();
		assertEquals(SmartTokenType.FOR, lexer.getToken().getType());
		assertEquals(null, lexer.getToken().getValue());
		lexer.nextToken();
		assertEquals(SmartTokenType.VARIABLE, lexer.getToken().getType());
		assertEquals("year", lexer.getToken().getValue());
		lexer.nextToken();
		assertEquals(SmartTokenType.INTEGER, lexer.getToken().getType());
		assertEquals(1, lexer.getToken().getValue());
		
		
	}
	@Test
	void echoConstructTest() {
		SmartLexer lexer=new SmartLexer("{$ = \"last_year\" $}");
		lexer.nextToken();
		lexer.setState(SmartLexerState.TAG);
		assertEquals(SmartTokenType.TAG, lexer.getToken().getType());
		assertEquals("{$", lexer.getToken().getValue());
		lexer.nextToken();
		assertEquals(SmartTokenType.ECHO, lexer.getToken().getType());
		assertEquals(null, lexer.getToken().getValue());
		lexer.nextToken();
		assertEquals(SmartTokenType.STRING, lexer.getToken().getType());
		assertEquals("last_year", lexer.getToken().getValue());
		lexer.nextToken();
		assertEquals(SmartTokenType.TAG, lexer.getToken().getType());
		assertEquals("$}", lexer.getToken().getValue());
	}
	
	@Test
	void typesOfTokensTest() {
		SmartLexer lexer=new SmartLexer("{$END varijabla 123 -123 -1.23 @varijabla + }");
		lexer.nextToken();
		lexer.setState(SmartLexerState.TAG);
		assertEquals(SmartTokenType.TAG, lexer.getToken().getType());
		assertEquals("{$", lexer.getToken().getValue());
		
		lexer.nextToken();
		
		assertEquals(SmartTokenType.END, lexer.getToken().getType());
		assertEquals(null, lexer.getToken().getValue());
		
		lexer.nextToken();
		assertEquals(SmartTokenType.VARIABLE, lexer.getToken().getType());
		assertEquals("varijabla", lexer.getToken().getValue());
		
		lexer.nextToken();
		assertEquals(SmartTokenType.INTEGER, lexer.getToken().getType());
		assertEquals(123, lexer.getToken().getValue());
		
		lexer.nextToken();
		assertEquals(SmartTokenType.INTEGER, lexer.getToken().getType());
		assertEquals(-123, lexer.getToken().getValue());
		
		lexer.nextToken();
		assertEquals(SmartTokenType.DOUBLE, lexer.getToken().getType());
		assertEquals(-1.23, lexer.getToken().getValue());
		
		lexer.nextToken();
		assertEquals(SmartTokenType.FUNCTION, lexer.getToken().getType());
		assertEquals("varijabla", lexer.getToken().getValue());
		
		lexer.nextToken();
		assertEquals(SmartTokenType.OPERATOR, lexer.getToken().getType());
		assertEquals('+', lexer.getToken().getValue());
	}
	
	

}
