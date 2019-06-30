package hr.fer.zemris.java.dao;

import hr.fer.zemris.java.dao.sql.SQLDAO;

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

	/**
	 * static variable that enables singleton design pattern. Stores reference to
	 * implementation of {@link DAO} interface
	 */
	private static DAO dao = new SQLDAO();

	/**
	 * Dohvat primjerka.
	 * 
	 * @return objekt koji enkapsulira pristup sloju za perzistenciju podataka.
	 */
	public static DAO getDao() {
		return dao;
	}

}