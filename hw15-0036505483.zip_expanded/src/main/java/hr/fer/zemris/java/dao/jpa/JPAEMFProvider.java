package hr.fer.zemris.java.dao.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * Singleton that is used to optimise database performanse
 * 
 * @author juren
 *
 */
public class JPAEMFProvider {

	/**
	 * variable that stores singletone
	 */
	public static EntityManagerFactory emf;

	/**
	 * Standard constructor
	 * 
	 * @return
	 */
	public static EntityManagerFactory getEmf() {
		return emf;
	}

	/**
	 * Setter
	 * 
	 * @param emf
	 */
	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}
}