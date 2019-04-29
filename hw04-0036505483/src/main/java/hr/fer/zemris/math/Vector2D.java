package hr.fer.zemris.math;

import java.util.Objects;

/**
 * Class that contains methods used to do basic vector mathematics.
 * 
 * @author juren
 *
 */
public class Vector2D {
	/**
	 * Used to store x component of vector.
	 */
	private double x;
	/**
	 * Used to store y component of vector.
	 */
	private double y;

	/**
	 * Constructor for {@link Vector2D} class. Takes 2 parameters.
	 * 
	 * @param x x-component of vector
	 * @param y y-component of vector
	 * 
	 */
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Standard getter for x-component.
	 * 
	 * @return x-component of vector
	 */
	public double getX() {
		return x;
	}

	/**
	 * Standard getter for y-component.
	 * 
	 * @return y-component of vector
	 */
	public double getY() {
		return y;
	}

	/**
	 * Method that modifies instance of class upon it is called by adding x and y
	 * components of referenced vector to this one.
	 * 
	 * @param offset double value that represent offset of vector
	 * @throws NullPointerException if provided vector is null reference
	 */
	public void translate(Vector2D offset) {
		Objects.requireNonNull(offset);
		this.x += offset.getX();
		this.y += offset.getY();
	}

	/**
	 * Method that makes a new instance instance of class upon it is called by
	 * adding x and y components of referenced vector to this one.
	 * 
	 * @param offset double value that represent offset of vector
	 * @throws NullPointerException if provided vector is null reference
	 */
	public Vector2D translated(Vector2D offset) {
		Vector2D oneToReturn = copy();
		oneToReturn.translate(offset);
		return oneToReturn;
	}

	/**
	 * Method that modifies instance of class upon it is called by rotating x and y
	 * components by angle(in radians) provided.
	 * 
	 * @param angle double value that represent angle by which vector is rotated
	 */
	public void rotate(double angle) {
		double newX, newY;
		newX = Math.cos(angle) * x - Math.sin(angle) * y;
		newY = Math.sin(angle) * x + Math.cos(angle) * y;
		x = newX;
		y = newY;
	}

	/**
	 * Method that makes new instance of class upon it is called by rotating x and y
	 * components by angle(in radians) provided.
	 * 
	 * @param angle double value that represent angle by which vector is
	 *              rotated.Expects radians.
	 */
	public Vector2D rotated(double angle) {
		//works faster but not ideal object oriented solution.
		// return new Vector2D(Math.cos(angle) * x - Math.sin(angle) * y,
		// Math.sin(angle) * x + Math.cos(angle) * y);
		
		Vector2D oneToReturn = copy();
		oneToReturn.rotate(angle);
		return oneToReturn;
	}

	/**
	 * Method that modifies instance of class upon it is called by scaling x and
	 * y components by number provided
	 * 
	 * @param scaler double value that represent valued by which vector is scaled
	 */
	public void scale(double scaler) {
		this.x *= scaler;
		this.y *= scaler;
	}

	/**
	 * Method that makes a new instance of class upon it is called by multiplying x
	 * and y components by number provided.
	 * 
	 * @param scaler double value that represent valued by which vector is scaled
	 */
	public Vector2D scaled(double scaler) {
		// return new Vector2D(x * scaler, y * scaler);
		Vector2D oneToReturn = copy();
		oneToReturn.scale(scaler);
		return oneToReturn;
	}

	/**
	 * Makes a new {@link Vector2D} with same parameters.
	 * 
	 * @return copy of {@link Vector2D}, new instance.
	 */
	public Vector2D copy() {
		return new Vector2D(x, y);
	}

}
