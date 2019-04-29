package hr.fer.zemris.java.custom.scripting.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;

class SmartScriptParserTest {

	@Test
	void forTestFailsTooLittleArguments() {
		assertThrows(SmartScriptParserException.class, () -> {
	//		SmartScriptParser parser = new SmartScriptParser("{$ FOR year 1 $}{$END$}");
		});
	}

	@Test
	void forTestFailsNotClosed() {
		assertThrows(SmartScriptParserException.class, () -> {
	//		SmartScriptParser parser = new SmartScriptParser("{$ FOR year 1 \"hmm\"  asd $}");
		});
	}

	@Test
	void numberOfChildrenTest() {
		SmartScriptParser parser = new SmartScriptParser("{$ FOR year 1 last_year$}{$END$}\r\n"
				+ "{$ FOR year \"1\" last_year $}{$END$}\r\n" + "{$ FOR year 1 \"\\\\  \"  asd $}{$END$}\r\n"
				+ "{$ FOR year 1 \"\\\\ marko \"  asd$}{$END$}\r\n" + "{$ FOR year 1 \"\\\\  \"  asd$}{$END$}");
		assertTrue(parser.getDocumentNode().numberOfChildren() == 9); // because of textNodes created for \r\n

	}

	@Test
	void numberOfChildrenTestZero() {
		SmartScriptParser parser = new SmartScriptParser("");
		assertTrue(parser.getDocumentNode().numberOfChildren() == 0);
	}

	@Test
	void reconstructDocument01ExampleFromTask() throws IOException {
		String input = new String(Files.readAllBytes(Paths.get("src/test/resources/document1.txt")),
				StandardCharsets.UTF_8);

		SmartScriptParser parser1 = new SmartScriptParser(input);
		DocumentNode document1 = parser1.getDocumentNode();
		String documentBody1 = document1.toString();

		SmartScriptParser parser2 = new SmartScriptParser(documentBody1);
		DocumentNode document2 = parser2.getDocumentNode();
		String documentBody2 = document2.toString();

		assertEquals(documentBody1, documentBody2);

	}

	@Test
	void reconstructDocument02EchoTestAndProperTokenization() throws IOException {
		String input = new String(Files.readAllBytes(Paths.get("src/test/resources/document2.txt")),
				StandardCharsets.UTF_8);

		SmartScriptParser parser1 = new SmartScriptParser(input);
		DocumentNode document1 = parser1.getDocumentNode();
		String documentBody1 = document1.toString();

		SmartScriptParser parser2 = new SmartScriptParser(documentBody1);
		DocumentNode document2 = parser2.getDocumentNode();
		String documentBody2 = document2.toString();

		assertEquals(documentBody1, documentBody2);
	}

	@Test
	void reconstructDocument03TestingSupportForRandomSymbolsInTextNode() throws IOException {
		String input = new String(Files.readAllBytes(Paths.get("src/test/resources/document3.txt")),
				StandardCharsets.UTF_8);

		SmartScriptParser parser1 = new SmartScriptParser(input);
		DocumentNode document1 = parser1.getDocumentNode();
		String documentBody1 = document1.toString();

		SmartScriptParser parser2 = new SmartScriptParser(documentBody1);
		DocumentNode document2 = parser2.getDocumentNode();
		String documentBody2 = document2.toString();

		assertEquals(documentBody1, documentBody2);
	}

	@Test
	void reconstructDocument04EchoTest() throws IOException {
		String input = new String(Files.readAllBytes(Paths.get("src/test/resources/document4.txt")),
				StandardCharsets.UTF_8);

		SmartScriptParser parser1 = new SmartScriptParser(input);
		DocumentNode document1 = parser1.getDocumentNode();
		String documentBody1 = document1.toString();

		SmartScriptParser parser2 = new SmartScriptParser(documentBody1);
		DocumentNode document2 = parser2.getDocumentNode();
		String documentBody2 = document2.toString();

		assertEquals(documentBody1, documentBody2);
	}

	@Test
	void reconstructDocument05ForNodeTests() throws IOException {
		String input = new String(Files.readAllBytes(Paths.get("src/test/resources/document5.txt")),
				StandardCharsets.UTF_8);

		SmartScriptParser parser1 = new SmartScriptParser(input);
		DocumentNode document1 = parser1.getDocumentNode();
		String documentBody1 = document1.toString();

		SmartScriptParser parser2 = new SmartScriptParser(documentBody1);
		DocumentNode document2 = parser2.getDocumentNode();
		String documentBody2 = document2.toString();

		assertEquals(documentBody1, documentBody2);
	}

	@Test
	void reconstructDocument06TrickyEscapingInText() throws IOException {
		String input = new String(Files.readAllBytes(Paths.get("src/test/resources/document6.txt")),
				StandardCharsets.UTF_8);

		SmartScriptParser parser1 = new SmartScriptParser(input);
		DocumentNode document1 = parser1.getDocumentNode();
		String documentBody1 = document1.toString();

		SmartScriptParser parser2 = new SmartScriptParser(documentBody1);
		DocumentNode document2 = parser2.getDocumentNode();
		String documentBody2 = document2.toString();

		assertEquals(documentBody1, documentBody2);
	}

	@Test
	void reconstructDocument07TrickyEscapingInText() throws IOException {
		String input = new String(Files.readAllBytes(Paths.get("src/test/resources/document7.txt")),
				StandardCharsets.UTF_8);

		SmartScriptParser parser1 = new SmartScriptParser(input);
		DocumentNode document1 = parser1.getDocumentNode();
		String documentBody1 = document1.toString();

		SmartScriptParser parser2 = new SmartScriptParser(documentBody1);
		DocumentNode document2 = parser2.getDocumentNode();
		String documentBody2 = document2.toString();

		assertEquals(documentBody1, documentBody2);
	}

//	@Test
//	void reconstructDocument08WrongEscaping() throws IOException {
//		String input = new String(Files.readAllBytes(Paths.get("src/test/resources/document8.txt")),
//				StandardCharsets.UTF_8);
//		assertThrows(SmartScriptParserException.class, () -> {
//			SmartScriptParser parser = new SmartScriptParser(input);
//		});
//	}

//	@Test
//	void reconstructDocument09NotClosedTag() throws IOException {
//		String input = new String(Files.readAllBytes(Paths.get("src/test/resources/document9.txt")),
//				StandardCharsets.UTF_8);
//		assertThrows(SmartScriptParserException.class, () -> {
//			SmartScriptParser parser = new SmartScriptParser(input);
//		});
//	}
//
//	@Test
//	void reconstructDocument10NotClosedFor() throws IOException {
//		String input = new String(Files.readAllBytes(Paths.get("src/test/resources/document10.txt")),
//				StandardCharsets.UTF_8);
//		assertThrows(SmartScriptParserException.class, () -> {
//			SmartScriptParser parser = new SmartScriptParser(input);
//		});
//	}

}
