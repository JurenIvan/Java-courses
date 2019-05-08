package hr.fer.zemris.math;

/**
 * Class representing model for 3D vector. This model can be interpred as point.
 * 
 * @author juren
 *
 */
public class Vector3 {

	/** Point/vector x-component */
	private double x;
	/** Point/vector y-component */
	private double y;
	/** Point/vector z-component */
	private double z;

	/**
	 * Constructor for Vector
	 * 
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param z z-coordinate
	 */
	public Vector3(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Method used to calculate norm when observed as vector
	 * 
	 * @return norm
	 */
	public double norm() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * Method used to normalize vector. Returns new one.
	 * 
	 * @return reference to new vector
	 */
	public Vector3 normalized() {
		double vectorLenght = norm();
		return new Vector3(x / vectorLenght, y / vectorLenght, z / vectorLenght);
	}

	/**
	 * Returns new vector that is equals to current translated by vector (x,y,z)
	 * 
	 * @param other vector
	 * @return translated vector
	 */
	public Vector3 add(Vector3 other) {
		return new Vector3(x + other.x, y + other.y, z + other.z);
	}

	/**
	 * Returns new vector that is equals to current translated by vector (x,y,z) but
	 * in negative
	 * 
	 * @param other vector
	 * @return translated vector
	 */
	public Vector3 sub(Vector3 other) {
		return new Vector3(x - other.x, y - other.y, z - other.z);
	}

	/**
	 * Calculates dot product this * p.
	 * 
	 * @param other vector
	 * @return scalar product
	 */
	public double dot(Vector3 other) {
		return norm() * other.norm() * cosAngle(other);
	}

	/**
	 * Calculates cross product.
	 * 
	 * @param other vector
	 * @return cross product
	 */
	public Vector3 cross(Vector3 other) {

		double newX = y * other.z - z * other.y;
		double newY = z * other.x - x * other.z;
		double newZ = x * other.y - y * other.x;
		return new Vector3(newX, newY, newZ);
	}

	/**
	 * Method that scales this vectors with given constant.
	 * 
	 * @param s factor
	 * @return new enlarged vector
	 */
	public Vector3 scale(double s) {
		return new Vector3(x * s, y * s, z * s);
	}

	/**
	 * Method that calculates angle between this and other vector
	 * 
	 * @param other vector
	 * @return angle
	 */
	public double cosAngle(Vector3 other) {
		return (x * other.x + y * other.y + z * other.z) / (this.norm() * other.norm());

	}

	/**
	 * Standard getter.
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * Standard getter.
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * Standard getter.
	 * @return the z
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Method that returns this point as array with 3 elements
	 * 
	 * @return array of point
	 */
	public double[] toArray() {
		return new double[] { x, y, z };
	}

	@Override
	public String toString() {
		return String.format("(%.5f, %.5f, %.5f)", x, y, z);

	}

}
