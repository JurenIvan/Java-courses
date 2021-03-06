package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.nodes.TimeNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 * Simple class with main method to demonstrate programs functionality.
 * 
 * @author juren
 *
 */

public class TreeWriter {

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

		WriterVisitor visitor = new WriterVisitor();
		parser.getDocumentNode().accept(visitor);

		// CHECK PART
		// should write something like original content of docBody
		System.out.println(visitor.getText());

		// used to see whether parsing parsed data results in same thing and visually
		// inspect if any changes occur
		SmartScriptParser parser2 = new SmartScriptParser(visitor.getText());
		WriterVisitor visitorCheck = new WriterVisitor();
		parser2.getDocumentNode().accept(visitorCheck);

		// TRUE OR FALSE ULTIMATE CHECK
		System.out.println(visitorCheck.getText().equals(visitor.getText()));
	}

	/**
	 * Class containing implementation of {@link INodeVisitor} that is responsible
	 * for walking the parsed document tree and printing parsed document.
	 * 
	 * @author juren
	 *
	 */
	private static class WriterVisitor implements INodeVisitor {

		// i did NOT follow instructions to make checking if Writer is capable of
		// re-parsing parsed document easier. Writer visitor saves text that it should
		// have printed in finalText variable.

		// to if you want to make this writer do exactly as provided in homework,
		// uncomment all commented syso-s...
		/**
		 * Variable used for storing final text.
		 */
		private String finalText;

		public WriterVisitor() {
			this.finalText = new String();
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < node.numberOfChildren(); i++) {
				node.getChild(i).accept(this);
			}
			finalText += sb.toString();
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			StringBuilder sb = new StringBuilder();
			sb.append("{$= ");
			for (var elem : node.getElements()) {
				sb.append(elem.toString());
				sb.append(" ");
			}
			sb.append("$}");
			// System.out.println(sb.toString());
			finalText = finalText + sb.toString();
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			StringBuilder sb = new StringBuilder();
			sb.append("{$ FOR ");

			sb.append(node.getVariable().toString());
			sb.append(" ");
			
			sb.append(node.getStartExpression().toString());
			sb.append(" ");
			
			sb.append(node.getEndExpression().toString());
			sb.append(" ");

			if (node.getStepExpression() != null) {
				sb.append(node.getStepExpression().toString());
			}
			sb.append("$}");
			finalText = finalText + sb.toString();
			
			for (int i = 0; i < node.numberOfChildren(); i++) {
				node.getChild(i).accept(this);
			}
			// System.out.println(sb.toString());
			finalText = finalText + "{$END$}";
		}

		@Override
		public void visitTextNode(TextNode textNode) {
			// System.out.println(textNode.getText());
			finalText += textNode.getText();
		}

		/**
		 * Getter for text collected by treeWisitor. Outputing this will result in
		 * printing parsed document.
		 * 
		 * @return Text for output
		 */
		public String getText() {
			return finalText;
		}

		@Override
		public void visitTimeNode(TimeNode timeNode) {
			// TODO Auto-generated method stub
			
		}

	}

}
