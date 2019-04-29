package hr.fer.zemris.java.hw03;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 * Simple class with main method to demonstrate programs functionality.
 * 
 * @author juren
 *
 */
public class SmartScriptTester {

	/**
	 * Main method used to demonstrate functionality of parser and lexer. as
	 * argument takes Path to text file that has text that you want to parse and
	 * convert into Document node.
	 *
	 * When parser finishes its job, DocumentNode and all of its content is printed
	 * to show that we can reconstruct original code.
	 * 
	 * @param args path to text file with input data for task.
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("You should input exactly one argument");
			return;
		}

		String docBody = null;
		try {
			docBody = new String(Files.readAllBytes(Paths.get(args[0])), StandardCharsets.UTF_8);
		} catch (IOException e1) {
			System.out.println("Reading file didn't work!");
			return;
		}

		SmartScriptParser parser = null;
		try {
			parser = new SmartScriptParser(docBody);
		} catch (SmartScriptParserException e) {
			e.printStackTrace();
			System.out.println("Unable to parse document!");
			System.exit(-1);
		} catch (Exception e) {
			System.out.println("If this line ever executes, you have failed this class!");
			System.exit(-1);
			
		}
		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = createOriginalDocumentBody(document);
		
		System.out.println(originalDocumentBody); // should write something like original
		// content of docBody

//		used to see whether parsing parsed data results in same thing and visually inspect if any changes occur
		SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
		String s2 = parser2.getDocumentNode().toString();
//		System.out.println(s2);
		System.out.println();
		System.out.println(s2.equals(originalDocumentBody));
	}

	/**
	 * Method used to create Original-like document body. Some information is lost
	 * due to parsing but the core stays the same.
	 * 
	 * Implemented by overriding toString() methods of nodes.
	 * 
	 * @param document {@link DocumentNode} whose content we print
	 * @return String representation of documents contents
	 */
	private static String createOriginalDocumentBody(DocumentNode document) {
		return document.toString();
	}

}
