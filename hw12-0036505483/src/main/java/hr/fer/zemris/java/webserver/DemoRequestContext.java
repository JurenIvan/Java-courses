package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Class used for demonstration of {@link RequestContext} functionalities
 * 
 * @author juren
 *
 */
public class DemoRequestContext {

	/**
	 * main method that calls {@link #demo1(String, String)} and
	 * {@link #demo2(String, String)} in order to show it's capabilities
	 * 
	 * @param args not used
	 * @throws IOException ignorable (in case file cannot be written)
	 */
	public static void main(String[] args) throws IOException {
		demo1("primjer1.txt", "ISO-8859-2");
		demo1("primjer2.txt", "UTF-8");
		demo2("primjer3.txt", "UTF-8");
	}

	/**
	 * First example of {@link RequestContext} class and its capabilities. shows
	 * basic functionality of html header generation.
	 * 
	 * @param filePath path where file will be written
	 * @param encoding encoding used for output
	 * @throws IOException ignorable (in case file cannot be written)
	 */
	private static void demo1(String filePath, String encoding) throws IOException {
		OutputStream os = Files.newOutputStream(Paths.get(filePath));
		RequestContext rc = new RequestContext(os, new HashMap<String, String>(), new HashMap<String, String>(),
				new ArrayList<RequestContext.RCCookie>());
		rc.setEncoding(encoding);
		rc.setMimeType("text/plain");
		rc.setStatusCode(205);
		rc.setStatusText("Idemo dalje");
// Only at this point will header be created and written...
		rc.write("Čevapčići i Šiščevapčići.");
		os.close();
	}

	/**
	 * Second example of {@link RequestContext} class and its capabilities.
	 * Generates kinda complicated header.
	 * 
	 * @param filePath path where file will be written
	 * @param encoding encoding used for output
	 * @throws IOException ignorable (in case file cannot be written)
	 */
	private static void demo2(String filePath, String encoding) throws IOException {
		OutputStream os = Files.newOutputStream(Paths.get(filePath));
		RequestContext rc = new RequestContext(os, new HashMap<String, String>(), new HashMap<String, String>(),
				new ArrayList<RequestContext.RCCookie>());
		rc.setEncoding(encoding);
		rc.setMimeType("text/plain");
		rc.setStatusCode(205);
		rc.setStatusText("Idemo dalje");
		rc.addRCCookie(new RCCookie("korisnik", "perica", 3600, "127.0.0.1", "/"));
		rc.addRCCookie(new RCCookie("zgrada", "B4", null, null, "/"));
// Only at this point will header be created and written...
		rc.write("Čevapčići i Šiščevapčići.");
		os.close();
	}

}
