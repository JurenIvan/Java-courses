package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.tokens.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.tokens.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.tokens.ElementFunction;
import hr.fer.zemris.java.custom.scripting.tokens.ElementOperator;
import hr.fer.zemris.java.custom.scripting.tokens.ElementString;
import hr.fer.zemris.java.custom.scripting.tokens.ElementVariable;
import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

public class SmartScriptEngine {

	private DocumentNode documentNode;
	private RequestContext requestContext;
	private ObjectMultistack multistack;

	public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
		multistack = new ObjectMultistack();
		this.documentNode = documentNode;
		this.requestContext = requestContext;
	}

	private INodeVisitor visitor = new INodeVisitor() {

		@Override
		public void visitDocumentNode(DocumentNode node) {
			for (int i = 0; i < node.numberOfChildren(); i++) {
				node.getChild(i).accept(this);
			}
		}

		@Override
		public void visitTextNode(TextNode node) {
			try {
				requestContext.write(node.getText());
			} catch (IOException e) {
				return; // nest pametnije ?
			}
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			multistack.push(node.getVariable().asText(), new ValueWrapper(node.getStartExpression().asText()));
			while (contitionTrue(node)) {
				for (int i = 0; i < node.numberOfChildren(); i++) {
					node.getChild(i).accept(this);
				}

				ValueWrapper v1 = new ValueWrapper(multistack.pop(node.getVariable().asText()).getValue());
				ValueWrapper v2 = new ValueWrapper(node.getStepExpression().asText());

				v1.add(v2.getValue());
				multistack.push(node.getVariable().asText(), v1);
			}
			multistack.pop(node.getVariable().asText());

		}

		private boolean contitionTrue(ForLoopNode node) {
			ValueWrapper v1 = new ValueWrapper((multistack.peek(node.getVariable().asText()).getValue()));
			ValueWrapper v2 = new ValueWrapper(node.getEndExpression().asText());

			return v1.numCompare(v2.getValue()) <= 0;
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			Stack<ValueWrapper> tempStack = new Stack<>();

			for (var token : node.getElements()) {
				if (token instanceof ElementConstantDouble) {
					tempStack.push(new ValueWrapper(Double.parseDouble(token.toString())));
					continue;
				}
				if (token instanceof ElementConstantInteger) {
					tempStack.push(new ValueWrapper(Integer.parseInt(token.toString())));
					continue;
				}
				if (token instanceof ElementString) {
					tempStack.push(new ValueWrapper(((ElementString) token).asText()));
					continue;
				}
				if (token instanceof ElementVariable) {
					ValueWrapper vw = multistack.peek(token.toString());
					tempStack.push(new ValueWrapper(vw.getValue()));
					continue;
				}
				if (token instanceof ElementOperator) {
					ValueWrapper vw1 = new ValueWrapper(tempStack.pop().getValue());
					ValueWrapper vw2 = new ValueWrapper(tempStack.pop().getValue());
					tempStack.push(doMaths(vw1, vw2, (ElementOperator) token));
					continue;
				}
				if (token instanceof ElementFunction) {
					doFunctions((ElementFunction) token, tempStack);
					continue;
				}
			}

			for (int i = 0; i < tempStack.size(); i++) {
				try {
					requestContext.write(tempStack.get(i).getValue().toString());
				} catch (IOException e) {
					e.printStackTrace(); //nest pametnije
				}
			}

		}

		private void doFunctions(ElementFunction token, Stack<ValueWrapper> tempStack) {
			if (token.asText().equals("sin")) {
				ValueWrapper vw = tempStack.pop();
				double degrees = Double.parseDouble(vw.getValue().toString());
				vw = new ValueWrapper(Math.sin(Math.toRadians(degrees)));
				tempStack.push(vw);

			} else if (token.asText().equals("decfmt")) {
				ValueWrapper f = tempStack.pop();
				ValueWrapper x = tempStack.pop();
				DecimalFormat df = new DecimalFormat(f.getValue().toString());
				String output = df.format(Double.parseDouble(x.getValue().toString()));
				tempStack.push(new ValueWrapper(output));

			} else if (token.asText().equals("dup")) {
				ValueWrapper f = tempStack.pop();
				tempStack.push(new ValueWrapper(f.getValue()));
				tempStack.push(new ValueWrapper(f.getValue()));

			} else if (token.asText().equals("swap")) {
				ValueWrapper p = tempStack.pop();
				ValueWrapper d = tempStack.pop();
				tempStack.push(p);
				tempStack.push(d);

			} else if (token.asText().equals("setMimeType")) {
				requestContext.setMimeType(tempStack.pop().getValue().toString());

			} else if (token.asText().equals("paramGet")) {
				ValueWrapper dv = tempStack.pop();
				ValueWrapper name = tempStack.pop();
				
				 String paramGetValue = requestContext.getParameter(name.toString());
                 if (paramGetValue == null) {
                     paramGetValue = dv.toString();
                 }
                 tempStack.push(new ValueWrapper(paramGetValue));
				
			//	String value = requestContext.getParameter(name.getValue().toString());
			//	tempStack.push(new ValueWrapper(value == null ? dv.getValue().toString() : value));

			} else if (token.asText().equals("pparamGet")) {
				ValueWrapper dv = tempStack.pop();
				ValueWrapper name = tempStack.pop();
				String value = requestContext.getPersistentParameter(name.getValue().toString()); // mozda i
																									// .getvalue().toString()
				tempStack.push(new ValueWrapper(value == null ? dv.getValue().toString() : value));

			} else if (token.asText().equals("pparamSet")) {
				ValueWrapper name = tempStack.pop();
				ValueWrapper value = tempStack.pop();
				requestContext.setPersistentParameter(name.getValue().toString(), value.getValue().toString());

			} else if (token.asText().equals("pparamDel")) {
				ValueWrapper name = tempStack.pop();
				requestContext.removePersistentParameter(name.getValue().toString());

			} else if (token.asText().equals("tparamGet")) {
				ValueWrapper dv = tempStack.pop();
				ValueWrapper name = tempStack.pop();
				String value = requestContext.getTemporaryParameter(name.getValue().toString());
				tempStack.push(new ValueWrapper(value == null ? dv.getValue().toString() : value));

			} else if (token.asText().equals("tparamSet")) {
				ValueWrapper name = tempStack.pop();
				ValueWrapper value = tempStack.pop();
				requestContext.setTemporaryParameter(name.getValue().toString(), value.getValue().toString());

			} else if (token.asText().equals("tparamDel")) {

				ValueWrapper name = tempStack.pop();
				requestContext.removeTemporaryParameter(name.getValue().toString());

			}
		}

		private ValueWrapper doMaths(ValueWrapper vw1, ValueWrapper vw2, ElementOperator token) {
			if (token.asText().equals("+")) {
				vw1.add(vw2.getValue());
			} else if (token.asText().equals("-")) {
				vw1.subtract(vw2.getValue());
			} else if (token.asText().equals("*")) {
				vw1.multiply(vw2.getValue());
			} else if (token.asText().equals("/")) {
				vw1.divide(vw2.getValue());
			}

			return new ValueWrapper(vw1.getValue());
		}
	};

	public static void main(String[] args) {
		String documentBody = readFromDisk("fibonaccih.smscr");
		Map<String,String> parameters = new HashMap<String, String>();
		Map<String,String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		// create engine and execute it
		new SmartScriptEngine(
		new SmartScriptParser(documentBody).getDocumentNode(),
		new RequestContext(System.out, parameters, persistentParameters, cookies)
		).execute();


	}

	private void execute() {
		documentNode.accept(visitor);
	}

	private static String readFromDisk(String path) {
		try {
			return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("Sorry..");
			return null;
		}

	}

}
