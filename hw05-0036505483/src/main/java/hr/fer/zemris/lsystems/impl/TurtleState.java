package hr.fer.zemris.lsystems.impl;

import java.awt.Color;
import java.util.Objects;

import hr.fer.zemris.math.Vector2D;

/**
 * Class that models state of our imaginary Turtle that draws. Remembers
 * position, direction,color, effective length and methods to set and retrieve
 * that data.
 * 
 * @author juren
 *
 */
public class TurtleState {
	/**
	 * variable that stores current position in {@link Vector2D} instance.
	 */
	private Vector2D currentPosition;
	/**
	 * variable that stores current direction in {@link Vector2D} instance.
	 */
	private Vector2D direction;
	/**
	 * variable that stores current color.
	 */
	private Color color;
	/**
	 * variable that stores current effective length
	 */
	private double efectiveLenght;

	/**
	 * Constructor for {@link TurtleState}. All of input arguments must not be null
	 * references.
	 * 
	 * @param currentPosition current position of turtle
	 * @param direction       current direction of turtle
	 * @param color           current color of turtle
	 * @param efectiveLenght  current length of "step"
	 * @throws NullPointerException if any of provided arguments is null
	 */
	public TurtleState(Vector2D currentPosition, Vector2D direction, Color color, double efectiveLenght) {
		Objects.requireNonNull(currentPosition, "currentPosition must not be null");
		Objects.requireNonNull(direction, "direction vector must not be null\"");
		Objects.requireNonNull(color, "color must not be null\"");
		this.currentPosition = currentPosition;
		this.color = color;
		this.direction = direction;
		this.efectiveLenght = efectiveLenght;
	}

	/**
	 * method makes a copy of current states *
	 * 
	 * @return copy of current {@link TurtleState}
	 */
	public TurtleState copy() {
		return new TurtleState(currentPosition.copy(), direction.copy(), new Color(color.getRGB()), efectiveLenght);
	}

	/**
	 * Standard getter for position
	 * 
	 * @return the currentPosition
	 */
	public Vector2D getCurrentPosition() {
		return currentPosition;
	}

	/**
	 * Standard setter for current position
	 * 
	 * @param currentPosition the currentPosition to set
	 */
	public void setCurrentPosition(Vector2D currentPosition) {
		this.currentPosition = currentPosition;
	}

	/**
	 * Standard getter for direction
	 * 
	 * @return the direction
	 */
	public Vector2D getDirection() {
		return direction;
	}

	/**
	 * Standard setter for direction
	 * 
	 * @param direction the direction to set
	 */
	public void setDirection(Vector2D direction) {
		this.direction = direction;
	}

	/**
	 * Standard getter for efectiveLenght
	 * 
	 * @return the efectiveLenght
	 */
	public double getEfectiveLenght() {
		return efectiveLenght;
	}

	/**
	 * Standard getter for current color
	 * 
	 * @return the color
	 */
	public Color getColor() {
		return new Color(this.color.getRGB());
	}

	/**
	 * Standard setter for effective length
	 * 
	 * @param efectiveLenght the efectiveLenght to set
	 */
	public void setEfectiveLenght(double efectiveLenght) {
		this.efectiveLenght = efectiveLenght;
	}

	/**
	 * Standard setter for color
	 * 
	 * @param color to be set.
	 */
	public void setColor(Color color2) {
		this.color = color2;
	}

}
