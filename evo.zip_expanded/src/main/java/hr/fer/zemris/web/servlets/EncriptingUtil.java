package hr.fer.zemris.web.servlets;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncriptingUtil {
	
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
