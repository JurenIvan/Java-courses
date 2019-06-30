package hr.fer.zemris.java.servlets;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class that has utilities used for encrypting.
 * 
 * @author juren
 *
 */
public class EncriptingUtil {

	/**
	 * Method that calculates sha1 of provided password
	 * 
	 * @param password
	 * @return
	 */
	public static String calculateSha(String password) {
		MessageDigest shaDigest = null;
		try {
			shaDigest = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException ignorable) {
		}
		shaDigest.update(password.getBytes());
		byte[] hashBytes = shaDigest.digest();
		return new String(hashBytes);
	}
}
