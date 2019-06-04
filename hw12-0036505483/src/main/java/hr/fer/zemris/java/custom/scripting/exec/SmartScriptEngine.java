package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.Stack;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.tokens.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.tokens.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.tokens.ElementFunction;
import hr.fer.zemris.java.custom.scripting.tokens.ElementOperator;
import hr.fer.zemris.java.custom.scripting.tokens.ElementString;
import hr.fer.zemris.java.custom.scripting.tokens.ElementVariable;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Class that holds implementation of engine for defined language. Contains
 * execute method which is called upon parsed document tree parsed by
 * {@link SmartScriptParser} where more rules can be found.
 * 
 * @author juren
 *
 */
public class SmartScriptEngine {

	/**
	 * Variable that stores tree that is made out of provided text loaded from file
	 */
	private DocumentNode documentNode;
	/**
	 * variable that stores reference to {@link RequestContext} needed for operation
	 * of engine
	 */
	private RequestContext requestContext;
	/**
	 * variable that store reference to an instance of {@link ObjectMultistack} used
	 * for executing smrsc scripts.
	 */
	private ObjectMultistack multistack;

	/**
	 * Standard Constructor
	 * 
	 * @param documentNode   reference to a tree that is made out of provided text
	 *                       loaded from file
	 * @param requestContext variable that stores reference to
	 *                       {@link RequestContext} needed for operation of engine
	 */
	public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
		multistack = new ObjectMultistack();
		this.documentNode = documentNode;
		this.requestContext = requestContext;
	}

	/**
	 * Implementation of {@link INodeVisitor} interface that is combination of
	 * Composite pattern and Visitor pattern used for executing code parsed by
	 * {@link SmartScriptParser}
	 */
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
				return;
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

		/**
		 * Method that checks whether the condition of provided {@link ForLoopNode} is
		 * true
		 * 
		 * @param node node for which we check state
		 * @return true of false, result of Checking
		 * 
		 * @throws NullPointerException if provided node is null
		 */
		private boolean contitionTrue(ForLoopNode node) {
			Objects.requireNonNull(node, "Cannot check contition for null reference node.");
			ValueWrapper v1 = new ValueWrapper((multistack.peek(node.getVariable().asText()).getValue()));
			ValueWrapper v2 = new ValueWrapper(node.getEndExpression().asText());

			return v1.numCompare(v2.getValue()) <= 0;
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			Stack<ValueWrapper> tempStack = new Stack<>();
			try {
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
						e.printStackTrace();
					}
				}
			} catch (RuntimeException e) {
				System.out.println("Operation is not possible due to types of data");
				e.printStackTrace();
			}
		}

		/**
		 * Method created by refactoring of {@link #visitEchoNode(EchoNode)} method.
		 * Contains algorithms that are performed when {@link ElementFunction} of some
		 * sort is found
		 * 
		 * @param token     token that is found and whose operation we want to do
		 * @param tempStack used for doing operations
		 */
		private void doFunctions(ElementFunction token, Stack<ValueWrapper> tempStack) {
			Objects.requireNonNull(token, "Token provided must not be null");
			Objects.requireNonNull(tempStack, "Stack provided must not be null");
			
			if (token.asText().equals("sin")) {
				doFunctionSin(tempStack);
			} else if (token.asText().equals("decfmt")) {
				doFunctionDecfmt(tempStack);
			} else if (token.asText().equals("dup")) {
				doFunctionDup(tempStack);
			} else if (token.asText().equals("swap")) {
				doFunctionSwap(tempStack);
			} else if (token.asText().equals("setMimeType")) {
				doFunctionSetMimeType(tempStack);
			} else if (token.asText().equals("paramGet")) {
				doFunctionParamGet(tempStack);
			} else if (token.asText().equals("pparamGet")) {
				doFunctionPparamGet(tempStack);
			} else if (token.asText().equals("pparamSet")) {
				doFunctionPParamSet(tempStack);
			} else if (token.asText().equals("pparamDel")) {
				doFunctionPparamDel(tempStack);
			} else if (token.asText().equals("tparamGet")) {
				doFunctionTParamGet(tempStack);
			} else if (token.asText().equals("tparamSet")) {
				doFunctionTParamSet(tempStack);
			} else if (token.asText().equals("tparamDel")) {
				doFunctionTParamDel(tempStack);
			}
		}

		/**
		 * Method that describes what happens in case @setMimeType token is found. Short
		 * description:" takes string x and calls requestContext.setMimeType(x). Does
		 * not produce any result"
		 * 
		 * @param tempStack structure used for doing operations
		 */
		private void doFunctionSetMimeType(Stack<ValueWrapper> tempStack) {
			requestContext.setMimeType(tempStack.pop().getValue().toString());
		}

		/**
		 * Method that describes what happens in case tparamDel token is found. Short
		 * description:" removes association for name from requestContext
		 * temporaryParameters map. "
		 * 
		 * @param tempStack structure used for doing operations
		 */
		private void doFunctionTParamDel(Stack<ValueWrapper> tempStack) {
			ValueWrapper name = tempStack.pop();
			requestContext.removeTemporaryParameter(name.getValue().toString());
		}

		/**
		 * Method that describes what happens in case tparamSet token is found.Short
		 * description:"stores a value into requestContext temporaryParameters map.
		 * Conceptually, equals to: name = pop(), value = pop(),
		 * reqCtx.setTmpParam(name, value)."
		 * 
		 * @param tempStack structure used for doing operations
		 */
		private void doFunctionTParamSet(Stack<ValueWrapper> tempStack) {
			ValueWrapper name = tempStack.pop();
			ValueWrapper value = tempStack.pop();
			requestContext.setTemporaryParameter(name.getValue().toString(), value.getValue().toString());
		}

		/**
		 * Method that describes what happens in case tparamGet token is found.Short
		 * description:"Obtains from {@link RequestContext} temporaryParameters map a
		 * value mapped for name and pushes it onto stack. If there is no such mapping,
		 * it pushes instead defValue onto stack. Conceptually, equals to: dv = pop(),
		 * name = pop(), value=reqCtx.getParam(name), push(value==null ? defValue :
		 * value).:
		 * 
		 * @param tempStack structure used for doing operations
		 */
		private void doFunctionTParamGet(Stack<ValueWrapper> tempStack) {
			ValueWrapper dv = tempStack.pop();
			ValueWrapper name = tempStack.pop();
			String value = requestContext.getTemporaryParameter(name.getValue().toString());
			tempStack.push(new ValueWrapper(value == null ? dv.getValue().toString() : value));
		}

		/**
		 * Method that describes what happens in case @pparamDel token is found.Short
		 * description:"removes association for name from requestContext
		 * persistentParameters map.
		 * 
		 * @param tempStack structure used for doing operations
		 */
		private void doFunctionPparamDel(Stack<ValueWrapper> tempStack) {
			ValueWrapper name = tempStack.pop();
			requestContext.removePersistentParameter(name.getValue().toString());
		}

		/**
		 * Method that describes what happens in case @pparamSet token is found.Short
		 * description:"; stores a value into requestContext persistent parameters map.
		 * Conceptually, equals to: name = pop(), value = pop(),
		 * reqCtx.setPerParam(name, value).
		 * 
		 * @param tempStack structure used for doing operations
		 */
		private void doFunctionPParamSet(Stack<ValueWrapper> tempStack) {
			ValueWrapper name = tempStack.pop();
			ValueWrapper value = tempStack.pop();
			requestContext.setPersistentParameter(name.getValue().toString(), value.getValue().toString());
		}

		/**
		 * Method that describes what happens in case @pparamGet token is found.Short
		 * description:" Obtains from requestContext persistent parameters map a value
		 * mapped for name and pushes it onto stack. If there is no such mapping, it
		 * pushes instead defValue onto stack. Conceptually, equals to: dv = pop(), name
		 * = pop(), value=reqCtx.getParam(name), push(value==null ? defValue : value)."
		 * 
		 * @param tempStack structure used for doing operations
		 */
		private void doFunctionPparamGet(Stack<ValueWrapper> tempStack) {
			ValueWrapper dv = tempStack.pop();
			ValueWrapper name = tempStack.pop();
			String value = requestContext.getPersistentParameter(name.getValue().toString());
			tempStack.push(new ValueWrapper(value == null ? dv.getValue().toString() : value));
		}

		/**
		 * Method that describes what happens in case @paramGet token is found.Short
		 * description:" Obtains from requestContext parameters map a value mapped for
		 * name and pushes it onto stack. If there is no such mapping, it pushes instead
		 * defValue onto stack. Conceptually, equals to: dv = pop(), name = pop(),
		 * value=reqCtx.getParam(name), "
		 * 
		 * @param tempStack structure used for doing operations
		 */
		private void doFunctionParamGet(Stack<ValueWrapper> tempStack) {
			ValueWrapper dv = tempStack.pop();
			ValueWrapper name = tempStack.pop();
			String value = requestContext.getParameter(name.getValue().toString());
			tempStack.push(new ValueWrapper(value == null ? dv.getValue().toString() : value));
		}

		/**
		 * Method that describes what happens in case @swap token is found.Short
		 * description:" replaces the order of two topmost items on stack. Conceptually,
		 * equals to: a = pop(), b = pop(), push(a), push(b)"
		 * 
		 * @param tempStack structure used for doing operations
		 */
		private void doFunctionSwap(Stack<ValueWrapper> tempStack) {
			ValueWrapper p = tempStack.pop();
			ValueWrapper d = tempStack.pop();
			tempStack.push(p);
			tempStack.push(d);
		}

		/**
		 * Method that describes what happens in case @dup token is found.Short
		 * description:" duplicates current top value from stack. Conceptually, equals
		 * to: x = pop(), push(x), push(x)"
		 * 
		 * @param tempStack structure used for doing operations
		 */
		private void doFunctionDup(Stack<ValueWrapper> tempStack) {
			ValueWrapper f = tempStack.pop();
			tempStack.push(new ValueWrapper(f.getValue()));
			tempStack.push(new ValueWrapper(f.getValue()));
		}

		/**
		 * Method that describes what happens in case @decfmt token is found.Short
		 * description:" formats decimal number using given format f which is compatible
		 * with DecimalFormat; produces a string. X can be integer, double or string
		 * representation of a number. Conceptually, equals to: f = pop(), x = pop(), r
		 * = decfmt(x,f), push(r)."
		 * 
		 * @param tempStack structure used for doing operations
		 */
		private void doFunctionDecfmt(Stack<ValueWrapper> tempStack) {
			ValueWrapper f = tempStack.pop();
			ValueWrapper x = tempStack.pop();
			DecimalFormat df = new DecimalFormat(f.getValue().toString());
			String output = df.format(Double.parseDouble(x.getValue().toString()));
			tempStack.push(new ValueWrapper(output));
		}

		/**
		 * Method that describes what happens in case @sin token is found.Short
		 * description:"calculates sinus from given argument and stores the result back
		 * to stack. Conceptually, equals to: x = pop(), r = sin(x), push(r)."
		 * 
		 * @param tempStack structure used for doing operations
		 */
		private void doFunctionSin(Stack<ValueWrapper> tempStack) {
			ValueWrapper vw = tempStack.pop();
			double degrees = Double.parseDouble(vw.getValue().toString());
			vw = new ValueWrapper(Math.sin(Math.toRadians(degrees)));
			tempStack.push(vw);
		}

		/**
		 * Method that takes two {@link ValueWrapper}s and based on
		 * {@link ElementOperator} does proper mathematical operation over them
		 * 
		 * @param vw1   first argument
		 * @param vw2   second argument
		 * @param token holder for mathematical operation
		 * @return {@link ValueWrapper} that is result of operation
		 */
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

	/**
	 * Method that starts the run over parsed document and executes code written
	 */
	public void execute() {
		documentNode.accept(visitor);
	}

}