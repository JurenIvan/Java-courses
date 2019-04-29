package hr.fer.zemris.java.hw06.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class that has main method able to interact through console with user who can
 * demand encryption or decryption or just SHA-256 check of files.
 * 
 * @author juren
 *
 */
public class Crypto {
	/**
	 * Constant for keyword used to call {@link Crypto#checksha} method
	 */
	private static final String CHECK_SHA_CALL = "checksha";
	/**
	 * Constant for keyword used to call {@link Crypto#encrypting} method and call
	 * encrypt functionality
	 */
	private static final String ENCRYPT_CALL = "encrypt";
	/**
	 * Constant for keyword used to call {@link Crypto#encrypting} method and call
	 * decrypt functionality
	 */
	private static final String DECRYPT_CALL = "decrypt";

	/**
	 * Main method used to start program and demonstrate it's capabilities.
	 * 
	 * @param args used for determining mode in which program works and for
	 *             providing paths to files
	 */
	public static void main(String[] args) {

		if (args.length < 2) {
			System.out.println("You should enter atleast 2 arguments.");
			return;
		}

		if (args[0].trim().equals(CHECK_SHA_CALL)) {
			if (args.length != 2) {
				System.out.println("You should enter 2 arguments for checksha command.");
				return;
			}
			checksha(args[1].trim());
			return;
		}

		if (args[0].trim().equals(ENCRYPT_CALL) || args[0].trim().equals(DECRYPT_CALL)) {
			if (args.length != 3) {
				System.out.println("You should enter 3 arguments for this command.");
				return;
			}
			encrypting(args[0].trim().equals(ENCRYPT_CALL), args[1].trim(), args[2].trim());
			return;
		}

		System.out.println("Not supported operation!");
		return;
	}

	/**
	 * Method that encrypts/decrypts file whose path has been provided as arguments.
	 * first argument is boolean and it determines whether file is encrypted or
	 * opposite.
	 * 
	 * @param isEncrypting flag if true then encrypting, else decrypting
	 * @param sorcePath path of file upon which operation is executed
	 * @param targetPath path of file upon which operation is executed
	 */
	private static void encrypting(boolean isEncrypting, String sorcePath, String targetPath) {
		Scanner sc = new Scanner(System.in);
		System.out.print("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):\r\n> ");
		String keyText = sc.nextLine();
		System.out.print("Please provide initialization vector as hex-encoded text (32 hex-digits):\r\n> ");
		String ivText = sc.nextLine();
		sc.close();

		try (InputStream is = Files.newInputStream(Paths.get(sorcePath));
				OutputStream os = Files.newOutputStream(Paths.get(targetPath), StandardOpenOption.CREATE_NEW)) {

			SecretKeySpec keySpec = new SecretKeySpec(Util.hextobyte(keyText), "AES");
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hextobyte(ivText));
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(isEncrypting ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);

			byte[] buffIn = new byte[1024 * 4];
			while (true) {
				int r = is.read(buffIn);
				if (r < 1)
					break;
				os.write(cipher.update(buffIn, 0, r));
			}
			os.write(cipher.doFinal());

			System.out.println(
					"Decryption completed. Generated file " + targetPath + " based on file " + sorcePath + ".");
		} catch (IOException e) {
			System.out.println("Digesting not completed.IO error occured.");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			System.out.println("Decryption NOT completed. Encrypting error occured.");
		}
	}

	/**
	 * Method that awaits user input that represent sha-256 digest and compares it
	 * with one that it calculated itself.
	 * 
	 * @param string users digest string
	 */
	private static void checksha(String string) {
		System.out.print("Please provide expected sha-256 digest for " + string + ":\r\n> ");
		Scanner sc = new Scanner(System.in);
		if (!sc.hasNextLine()) {
			System.out.println("Input stream closed. Terminating program. ");
			sc.close();
			return;
		}
		String expectedDigest = sc.nextLine();
		sc.close();

		byte[] calculatedSha = null;
		try (InputStream is = Files.newInputStream(Paths.get(string))) {
			
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] buffIn = new byte[1024 * 4];
			while (true) {
				int r = is.read(buffIn);
				if (r < 1)
					break;
				md.update(buffIn, 0, r);
			}
			calculatedSha = md.digest();
			
			if (Util.bytetohex(calculatedSha).equals(expectedDigest)) {
				System.out.println("Digesting completed. Digest of " + string + " matches expected digest.");
				return;
			}
			System.out.println("Digesting completed. Digest of " + string
					+ " does not match the expected digest. Digest\r\n" + "was: " + Util.bytetohex(calculatedSha));

		} catch (NoSuchAlgorithmException e) {
			System.out.println("Digesting not completed.Error occured. No algorithm was found.");
		} catch (IOException e) {
			System.out.println("Digesting not completed.IO Error occured.");
		}

		
	}
}
