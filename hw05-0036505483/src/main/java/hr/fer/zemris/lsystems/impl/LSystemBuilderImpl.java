package hr.fer.zemris.lsystems.impl;

import java.awt.Color;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.Dictionary;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.commands.ColorCommand;
import hr.fer.zemris.lsystems.impl.commands.DrawCommand;
import hr.fer.zemris.lsystems.impl.commands.PopCommand;
import hr.fer.zemris.lsystems.impl.commands.PushCommand;
import hr.fer.zemris.lsystems.impl.commands.RotateCommand;
import hr.fer.zemris.lsystems.impl.commands.SkipCommand;
import hr.fer.zemris.math.Vector2D;

/**
 * Class representing implementation of {@link LSystemBuilder} interface. Having
 * two dictionaries, it is able to remember productions and commands and
 * generate appropriate L-system
 * 
 * @author juren
 *
 */
public class LSystemBuilderImpl implements LSystemBuilder {

	/**
	 * Constant representing default {@link #unitLength}
	 */
	private static final double DEFAULT_UNIT_LENGHT = 0.1;

	/**
	 * Constant representing default {@link #unitLengthDegreeScaler}
	 */
	private static final double DEFAULT_UNIT_LENGHT_DEGREE_SCALER = 1;

	/**
	 * Constant representing default {@link #angle}
	 */
	private static final double DEFAULT_ANGLE = 0;

	/**
	 * Constant representing default {@link #origin}
	 */
	private static final Vector2D DEFAULT_ORIGIN = new Vector2D(0, 0);

	/**
	 * Constant representing default {@link #axiom}
	 */
	private static final String DEFAULT_AXIOM = "";

	/**
	 * Variable that saves angle towards positive x axis.
	 */
	private double angle;

	/**
	 * Variable that saves string representing level 0 axiom.
	 */
	private String axiom;

	/**
	 * Variable that saves number resembling scaling proportion of unitLeght needed
	 * due to growth of L-system.
	 */
	private double unitLengthDegreeScaler;

	/**
	 * Variable that saves length of basic turtle move.
	 */
	private double unitLength;

	/**
	 * Variable that saves point on canvas where the origin is.
	 */
	private Vector2D origin;

	/**
	 * Dictionary where so called productions are saved. While developing full
	 * L-system expression, each char in axiom can be replaced with array of chars.
	 */
	private Dictionary<Character, String> registeredProductions;

	/**
	 * Dictionary where each character used in production of L-system have binded
	 * Command.
	 */
	private Dictionary<Character, Command> registeredCommands;

	/**
	 * Default {@link LSystemBuilderImpl} constructor. Sets variables to predefined
	 * constants.
	 */
	public LSystemBuilderImpl() {
		this.unitLength = DEFAULT_UNIT_LENGHT;
		this.unitLengthDegreeScaler = DEFAULT_UNIT_LENGHT_DEGREE_SCALER;
		this.origin = DEFAULT_ORIGIN;
		this.angle = DEFAULT_ANGLE;
		this.axiom = DEFAULT_AXIOM;
		registeredCommands = new Dictionary<Character, Command>();
		registeredProductions = new Dictionary<Character, String>();
	}

	@Override
	public LSystem build() {
		return new LSystemImpl();
	}

	@Override
	public LSystemBuilder configureFromText(String[] data) {
		for (String line : data) {
			if (line.isBlank())
				continue;
			String[] splitted = line.split("\\s+");
			try {
				switch (splitted[0]) {
				case "origin":
					setOrigin(Double.parseDouble(splitted[1]), Double.parseDouble(splitted[2]));
					break;
				case "angle":
					setAngle(Double.parseDouble(splitted[1]));
					break;
				case "unitLength":
					setUnitLength(Double.parseDouble(splitted[1]));
					break;
				case "unitLengthDegreeScaler":
					setUnitLengthDegreeScaler(getUnitLengthDegreeScalerFromLine(line));
					break;
				case "command":
					getCommandPart(line);
					break;
				case "axiom":
					setAxiom(splitted[1]);
					break;
				case "production":
					registerProduction(splitted[1].charAt(0), splitted[2]);
					break;
				default:
					throw new IllegalArgumentException();
				}
			} catch (Exception e) {
				throw new IllegalArgumentException("Cant interpretate data.");
			}
		}

		return this;
	}

	/**
	 * Part of parser whose job is to recognize line with command, parses it , and
	 * sends it to {@link #registerCommand(char, String)}
	 * 
	 * @param line where string "command" is found
	 * @return {@link LSystemBuilder} so that multiple commands can be applied
	 *         before {@link #build()}
	 */
	private LSystemBuilder getCommandPart(String line) {
		char arg0 = line.split("\\s+")[1].charAt(0);
		StringBuilder arg1 = new StringBuilder();
		String splitted[] = line.split("\\s+");
		for (int i = 2; i < splitted.length; i++) {
			arg1.append(splitted[i] + " ");
		}
		return registerCommand(arg0, arg1.toString());
	}

	/**
	 * Part of parser whose job is to recognize line with "unitLengthDegreeScaler"
	 * parses it( it can be given in two ways,a unitLengthDegreeScaler and double or
	 * unitLengthDegreeScaler and double/double) and returns only double value.
	 * 
	 * @param line where string "unitLengthDegreeScaler" is found
	 * @return {@link LSystemBuilder} so that multiple commands can be applied
	 *         before {@link #build()}
	 * @throws NumberFormatException if it cannot interpreted number
	 * @throws IllegalArgumentException if division with zero occurs while parsing expression
	 */
	private double getUnitLengthDegreeScalerFromLine(String line) {
		String[] splitted = line.split("\\s+");
		if (splitted.length == 2) {
			return Double.parseDouble(splitted[1]);
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < splitted.length; i++) {
			sb.append(splitted[i]);
		}
		String secondPart = sb.toString().replaceAll("\\s+", "");
		String numbers[] = secondPart.split("/");
		if(Double.parseDouble(numbers[1])==0) throw new IllegalArgumentException("Division by zero doesn't make sence in this usecase.");
		return Double.parseDouble(numbers[0]) / Double.parseDouble(numbers[1]);
	}

	@Override
	public LSystemBuilder registerCommand(char arg0, String arg1) {
		String[] splitted = arg1.split("\\s+");
		Command arg1Command;
		try {
			switch (splitted[0]) {
			case "pop":
				arg1Command = new PopCommand();
				break;
			case "push":
				arg1Command = new PushCommand();
				break;
			case "draw":
				arg1Command = new DrawCommand(Double.parseDouble(splitted[1]));
				break;
			case "rotate":
				arg1Command = new RotateCommand(Double.parseDouble(splitted[1]));
				break;
			case "skip":
				arg1Command = new SkipCommand(Double.parseDouble(splitted[1]));
				break;
			case "scale":
				arg1Command = new SkipCommand(Double.parseDouble(splitted[1]));
				break;
			case "color":
				arg1Command = new ColorCommand(Color.decode("#" + splitted[1]));
				break;
			default:
				throw new IllegalArgumentException("Unsuported command!");
			}
		} catch (NumberFormatException e) {
			throw new NumberFormatException("Can't intepret numbers");
		} catch (IllegalArgumentException e) {
			throw new NumberFormatException("Unsuported command");
		}
		registeredCommands.put(arg0, arg1Command);
		return this;
	}

	@Override
	public LSystemBuilder registerProduction(char arg0, String arg1) {
		registeredProductions.put(arg0, arg1);
		return this;
	}

	@Override
	public LSystemBuilder setAngle(double arg0) {
		this.angle = arg0;
		return this;
	}

	@Override
	public LSystemBuilder setAxiom(String arg0) {
		this.axiom = arg0;
		return this;
	}

	@Override
	public LSystemBuilder setOrigin(double arg0, double arg1) {
		this.origin = new Vector2D(arg0, arg1);
		return this;
	}

	@Override
	public LSystemBuilder setUnitLength(double arg0) {
		this.unitLength = arg0;
		return this;
	}

	@Override
	public LSystemBuilder setUnitLengthDegreeScaler(double arg0) {
		this.unitLengthDegreeScaler = arg0;
		return this;
	}

	/**
	 * Class resembling implementation of {@link LSystem} interface. Is able to
	 * generate axioms and to draw them. Has default constructor and
	 * {@link #draw(int, Painter)} and {@link #generate(int)} methods
	 * 
	 * @author juren
	 *
	 */
	private class LSystemImpl implements LSystem {

		/**
		 * Instance of {@link Context} needed for {@link #draw(int, Painter)} and
		 * {@link #generate(int)} methods.
		 */
		private Context context;
		/**
		 * Collection of already calculated axioms so that every axiom level is
		 * calculated only once
		 */
		private ArrayIndexedCollection<String> memoisation;

		/**
		 * Default constructor. Initializes collection used for memoisation.
		 */
		public LSystemImpl() {
			memoisation = new ArrayIndexedCollection<String>();
			memoisation.add(axiom);
		}

		@Override
		public void draw(int arg0, Painter arg1) {
			context = new Context();
			context.pushState(new TurtleState(origin.copy(), (new Vector2D(1, 0)).rotated(Math.toRadians(angle)),
					Color.black, unitLength * (Math.pow(unitLengthDegreeScaler, arg0))));

			String whatToDo = generate(arg0);
			for (int i = 0; i < whatToDo.length(); i++) {
				Command command = registeredCommands.get(whatToDo.charAt(i));
				if (command == null)
					continue;
				command.execute(context, arg1);
			}
		}

		@Override
		public String generate(int arg0) {
			if (memoisation.size() > arg0)
				return memoisation.get(arg0);
			String previousAxiom = generate(arg0 - 1);

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < previousAxiom.length(); i++) {
				String production = registeredProductions.get(previousAxiom.charAt(i));
				if (production == null) {
					sb.append(previousAxiom.charAt(i));
					continue;
				}
				sb.append(production);
			}
			memoisation.insert(sb.toString(), arg0);
			return sb.toString();
		}
	}

}
