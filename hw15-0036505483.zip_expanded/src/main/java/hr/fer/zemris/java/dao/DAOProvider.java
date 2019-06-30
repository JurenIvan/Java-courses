package hr.fer.zemris.java.dao;

import hr.fer.zemris.java.dao.jpa.JPADAOImpl;

/**
 * Singleton razred koji zna koga treba vratiti kao pružatelja usluge pristupa
 * podsustavu za perzistenciju podataka. Uočite da, iako je odluka ovdje
 * hardkodirana, naziv razreda koji se stvara mogli smo dinamički pročitati iz
 * konfiguracijske datoteke i dinamički učitati -- time bismo implementacije
 * mogli mijenjati bez ikakvog ponovnog kompajliranja koda.
 * 
 * @author marcupic
 *
 */
public class DAOProvider {

	private static DAO dao = new JPADAOImpl();

	public static DAO getDAO() {
		return dao;
	}

}