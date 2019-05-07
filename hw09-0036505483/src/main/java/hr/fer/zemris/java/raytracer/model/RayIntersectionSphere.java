package hr.fer.zemris.java.raytracer.model;

public class RayIntersectionSphere extends RayIntersection {

	private double kdr;
	private double kdg;
	private double kdb;
	private double krr;
	private double krg;
	private double krb;
	private double krn;

	protected RayIntersectionSphere(Point3D point, double distance, boolean outer, double kdr, double kdg, double kdb,
			double krr, double krg, double krb, double krn) {
		super(point, distance, outer);
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}

	@Override
	public Point3D getNormal() {
		return super.getPoint().normalize();
	}

	@Override
	public double getKdr() {
		return kdr;
	}

	@Override
	public double getKdg() {
		return kdg;
	}

	@Override
	public double getKdb() {
		return kdb;
	}

	@Override
	public double getKrr() {
		return krr;
	}

	@Override
	public double getKrg() {
		return krg;
	}

	@Override
	public double getKrb() {
		return krb;
	}

	@Override
	public double getKrn() {
		return krn;
	}

}
