package hr.fer.zemris.java.hw08.shell.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NameBuilderParser {

	/**
	 * List that holds all builders that parser created.
	 */
	private List<NameBuilder> builders;
	/**
	 * Variable that holds index of character that was lastly read
	 */
	private int currentChar;
	/**
	 * Array of input data
	 */
	private char[] data;

	/**
	 * Constructor that automatically starts parsing expression into groups and
	 * makes apropriate list of NameBuilders from String that was given to it
	 * 
	 * @param outputRegex string that is parsed into groups
	 * @throws IllegalArgumentException if given string is empty or outputRegex if not properly formated
	 */
	public NameBuilderParser(String outputRegex) {
		if(outputRegex==null)
			throw new IllegalArgumentException("Provided string must not be null");
		builders = new ArrayList<>();
		currentChar = 0;
		data = outputRegex.toCharArray();

		if (outputRegex.length() == 0)
			throw new IllegalArgumentException("Such name can not be produced.");
		try {
		while (currentChar < data.length) {
			if (currentChar + 1 < data.length) {
				if (data[currentChar] == '$' && data[currentChar + 1] == '{') {
					dealWithBlocks();
				}
			}
			dealWithText();
		}
		}catch (NullPointerException e) {
			throw new IllegalArgumentException(e.getMessage());
		}catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException(e.getMessage());
		}

	}

	/**
	 * Method that reads text from dataArray until it reaches "${" or the end of the
	 * string. When either of those conditions is met, method makes one NameBuilder
	 * that returns that particular part of string.
	 */
	private void dealWithText() {
		StringBuilder sb = new StringBuilder();
		while (currentChar < data.length) {
			if (currentChar + 1 < data.length && data[currentChar] == '$' && data[currentChar + 1] == '{')
				break;
			sb.append(data[currentChar++]);
		}
		builders.add(text(sb.toString()));
	}

	/**
	 * Method that reads text until it reaches to } or until the end of the string.
	 * when either of those conditions is met, method makes one NameBuilder that is
	 * of type {@link #group(int, char, int)} if substring read was like
	 * ${number,number} or {@link #group(int)} f substring read was like ${number}
	 * 
	 * @throws IllegalArgumentException if end is reached before group is closed
	 * @throws NumberFormatException    if numbers inside ${} can not be read
	 */
	private void dealWithBlocks() throws NumberFormatException {
		StringBuilder sb = new StringBuilder();
		currentChar = currentChar + 2;
		while (currentChar < data.length) {
			if (data[currentChar] == '}') {
				builders.add(group(Integer.parseInt(sb.toString())));
				currentChar++;
				return;
			}
			if (data[currentChar] == ',') {
				twoArgumentBuilder(sb);
				return;
			}
			sb.append(data[currentChar++]);
		}
		throw new IllegalArgumentException("Block never closed.");
	}

	/**
	 * Method that does part of the job of {@link #dealWithBlocks()} responsible for
	 * {@link #group(int, char, int)} situations.
	 * 
	 * @param upUntilNow String builder containing first half of expression
	 * @throws NumberFormatException if numbers inside ${} can not be read
	 */
	private void twoArgumentBuilder(StringBuilder upUntilNow) throws NumberFormatException {
		StringBuilder sb = new StringBuilder();
		char padding = ' ';
		while (currentChar < data.length && Character.isWhitespace(data[currentChar++]))
			;

		if (data[currentChar] == '0') {
			padding = '0';
			currentChar++;
		}
		while (currentChar < data.length) {

			if (data[currentChar] == '}') {
				currentChar++;
				builders.add(group(Integer.parseInt(upUntilNow.toString()), padding, Integer.parseInt(sb.toString())));
				return;
			}
			sb.append(data[currentChar++]);
		}
		throw new IllegalArgumentException("Block never closed.");
	}

	/**
	 * Implementation for NameBuilder that sums all the functionalities of
	 * NameBuilders that parser made.
	 * 
	 * @return NameBuilder that can build proper name
	 */
	public NameBuilder getNameBuilder() {
		return (result, sb) -> builders.stream().forEach(t -> t.execute(result, sb));
	}

	/**
	 * Implementation for NameBuilder that adds provided text to Stringbuilder
	 * (current name)
	 * 
	 * @return NameBuilder that can build proper name
	 * @throws NullPointerException if provided text is null
	 */
	public static NameBuilder text(String text) {
		Objects.requireNonNull(text, "Text provided must not be null");
		return (result, sb) -> sb.append(text);
	}

	/**
	 * Implementation for NameBuilder that adds index-th group of FilterResults
	 * groups to name (current name)
	 * @param index of group that is added onto name
	 * 
	 * @return NameBuilder that can build proper name
	 * @throws IndexOutOfBoundsException
	 */
	public static NameBuilder group(int index) {
		return (result, sb) -> sb.append(result.group(index));
	}

	/**
	 * Implementation for NameBuilder that adds index-th group of FilterResults and
	 * if it is smaller than minWidth then it fills the gap with char provided
	 * (current name)
	 * 
	 * @param index of group that is added onto name
	 * @param char what is used as padding if length of index-th group of FilterResults is smaller than minWidth
	 * @param minWidth minimal length of string provided
	 * 
	 * @return NameBuilder that can build proper name
	 * @throws IllegalArgumentException if minWidth is negative or padding is not space or zero
	 */
	public static NameBuilder group(int index, char padding, int minWidth) {
		if(minWidth<0)
			throw new IllegalArgumentException("MinWidth can not be smaller than zero");
		if(padding!=' ' && padding!='0')
			throw new IllegalArgumentException("Padding can be only '0' or ' ' ");
		return (result, sb) -> {
			String thingToWrite = result.group(index);
			int len = thingToWrite.length();
			sb.append(len < minWidth ? String.valueOf(padding).repeat(minWidth - len) : "");
			sb.append(thingToWrite);
		};
	}

}
