package hr.fer.zemris.math;
//kaj ak je duljina nula i takve spike
public class Vector3 {

	private double x;
	private double y;
	private double z;

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector3(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double norm() {
		return Math.sqrt(x*x+y*y+z*z);
	}
	
	public Vector3 normalized() {
		double vectorLenght=norm();
		return new Vector3(x/vectorLenght, y/vectorLenght, z/vectorLenght);
	}
	
	
	public Vector3 add(Vector3 other) {
		return new Vector3(x+other.x, y+other.y, z+other.z);
	} 
	
	public Vector3 sub(Vector3 other) {
		return new Vector3(x-other.x, y-other.y, z-other.z);
	}
	
	public double dot(Vector3 other) {
		return norm()*other.norm()*cosAngle(other);
	}
	
	public Vector3 cross(Vector3 other) {
		
		double newX=y*other.z-z*other.y;
		double newY=z*other.x-x*other.z;
		double newZ=x*other.y-y*other.x;
		return new Vector3(newX,newY,newZ);	
	} 
	
	public Vector3 scale(double s) {
		return new Vector3(x*s, y*s, z*s);
	}
	public double cosAngle(Vector3 other) {
		return (x*other.x+y*other.y+z*other.z)/(this.norm()*other.norm());
	
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @return the z
	 */
	public double getZ() {
		return z;
	}

	public double[] toArray() {
		return new double[]{x,y,z};
	} 
	public String toString() {
		return String.format("(%.5f, %.5f, %.5f)", x,y,z);
		
	}
	
}
