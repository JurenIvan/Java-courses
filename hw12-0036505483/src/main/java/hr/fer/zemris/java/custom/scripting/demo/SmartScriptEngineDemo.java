package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Class used for demonstrating {@link SmartScriptEngine} capabilities.
 * 
 * @author juren
 *
 */
public class SmartScriptEngineDemo {
	/**
	 * Main method used to demonstrate usage of {@link SmartScriptEngine}.
	 * 
	 * @param args not used.
	 */
	public static void main(String[] args) {
		String documentBody = readFromDisk(".\\webroot\\scripts\\osnovni.smscr");
		if (documentBody == null)
			return;
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		parameters.put("a", "4");
		parameters.put("b", "2");
		// create engine and execute it
		new SmartScriptEngine(new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(System.out, parameters, persistentParameters, cookies)).execute();

	}

	/**
	 * Method used to load text from file on disk.
	 * 
	 * @param path path to text file
	 * @return
	 */
	private static String readFromDisk(String path) {
		try {
			return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("Sorry, cannot read it..");
			return null;
		}

	}

}
