package hr.fer.zemris.java.webserver;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Class that models HttpServer. Has multi thread support, supports cookies. Has
 * "garbage collector" that cleans memory when sessions are expired.
 * 
 * @author juren
 *
 */
public class SmartHttpServer {

	/**
	 * Constant that holds default cleaning time interval. measured in seconds
	 */
	public static final int DEFAULT_CLEANING_TIME = 5 * 60;
	/**
	 * Variable that stores reference to string representing address of host where
	 * user listens for change.
	 */
	private String address;
	/**
	 * Variable that stores reference to string representing domain name
	 */
	private String domainName;
	/**
	 * Variable that stores integer representing port number
	 */
	private int port;
	/**
	 * Variable that stores integer representing number of worker Threads
	 */
	private int workerThreads;
	/**
	 * Variable that stores integer representing length before session expires
	 */
	private int sessionTimeout;
	/**
	 * Variable that stores reference to map containing supported mime types
	 */
	private static Map<String, String> mimeTypes;
	/**
	 * Variable that stores reference to server thread responsible for socket
	 * ordering
	 */
	private ServerThread serverThread;
	/**
	 * Variable that stores reference to ExecutorService because multi threading is
	 * made possible by pool-ing
	 */
	private ExecutorService threadPool;
	/**
	 * Variable that stores reference to path to document root
	 */
	private Path documentRoot;
	/**
	 * Variable that stores reference to map containing non expired
	 * {@link SessionMapEntry}s
	 */
	private Map<String, SessionMapEntry> sessions;
	/**
	 * Variable that stores reference to object of type {@link Random} so we can
	 * create random SID
	 */
	private Random sessionRandom = new Random();
	/**
	 * Variable that stores reference to map containing {@link IWebWorker}s
	 */
	private Map<String, IWebWorker> workersMap;

	/**
	 * Standard constructor that configures server based on config file found on
	 * path that is provided through argument
	 * 
	 * @param configFileName path of config file
	 * @throws Exception if server cannot be configured with this path
	 */
	public SmartHttpServer(String configFileName) throws Exception {

		Properties serverProperties = new Properties();
		Properties mimeProperties = new Properties();
		Properties workersProperties = new Properties();

		serverProperties.load(Files.newInputStream(Paths.get(configFileName)));
		mimeProperties.load(Files.newInputStream(Paths.get(serverProperties.getProperty("server.mimeConfig"))));
		workersProperties.load(Files.newInputStream(Paths.get(serverProperties.getProperty("server.workers"))));

		address = serverProperties.getProperty("server.adress");
		domainName = serverProperties.getProperty("server.domainName");
		port = Integer.parseInt(serverProperties.getProperty("server.port"));
		workerThreads = Integer.parseInt(serverProperties.getProperty("server.workerThreads"));
		sessionTimeout = Integer.parseInt(serverProperties.getProperty("session.timeout"));
		documentRoot = Path.of(serverProperties.getProperty("server.documentRoot")).normalize();

		mimeTypes = new HashMap<String, String>();
		for (String mimeProperty : mimeProperties.stringPropertyNames()) {
			mimeTypes.put(mimeProperty, mimeProperties.getProperty(mimeProperty));
		}

		workersMap = new HashMap<String, IWebWorker>();
		for (var path : workersProperties.stringPropertyNames()) {
			Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(workersProperties.getProperty(path));
			Object newObject = referenceToClass.getDeclaredConstructor().newInstance();
			workersMap.put(path, (IWebWorker) newObject);
		}

		sessions = new HashMap<>();
		sessionRandom = new Random();
	}

	/**
	 * Method used to start {@link ServerThread}
	 */
	protected synchronized void start() {
		if (serverThread != null)
			return;

		serverThread = new ServerThread();
		serverThread.start();
		threadPool = Executors.newFixedThreadPool(workerThreads);

		Thread cleanerThread = new Thread(() -> doAction());
		cleanerThread.setDaemon(true);
		cleanerThread.start();
	}

	/**
	 * method whose functionality is used as a garbage collector. Has defined
	 * interval and cleans expired sessions.
	 */
	private void doAction() {
		for (var s : sessions.values()) {
			if (!stillValid(s.validUntil)) {
				sessions.remove(s.sid);
			}
		}
		try {
			Thread.sleep(DEFAULT_CLEANING_TIME * 1000);
		} catch (InterruptedException e) {
			doAction();
		}

	}

	/**
	 * Method used to stop {@link ServerThread}
	 */
	protected synchronized void stop() {
		serverThread.terminateThread();
		threadPool.shutdown();
	}

	/**
	 * Class that models Thread capable of socket distribution. Can be stopped by
	 * using {@link #terminateThread()} method.
	 * 
	 * @author juren
	 *
	 */
	protected class ServerThread extends Thread {

		/**
		 * flag used to stop while loop
		 */
		private boolean run = true;
		/**
		 * variable that holds reference to server socket. Used for acquiring
		 * connections with users.
		 */
		private ServerSocket serverSocket;

		/**
		 * Method that stops thread.
		 */
		public void terminateThread() {
			run = false;
			try {
				serverSocket.close();
			} catch (IOException e) {
			}
		}

		@Override
		public void run() {
			try {
				serverSocket = new ServerSocket();
				serverSocket.bind(new InetSocketAddress(InetAddress.getByName(address), port));
				while (run) {
					Socket client = serverSocket.accept();
					ClientWorker cw = new ClientWorker(client);
					threadPool.submit(cw);
				}
				serverSocket.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * Class that processes clients requests. Has {@link #run} method that processes
	 * request found in InputStream of {@link Socket}. Result of operations displays
	 * on OutputStrem of same socket
	 * 
	 * @author juren
	 */
	private class ClientWorker implements Runnable, IDispatcher {
		/**
		 * constant holding number representing length of SID sequence
		 */
		private static final int SID_LENGTH = 20;
		/**
		 * String holding path to workers
		 */
		private static final String WORKERS_PATH = "hr.fer.zemris.java.webserver.workers.";
		/**
		 * variable that stored reference to end point for communication between two
		 * machines. in this case sever and client.
		 */
		private Socket csocket;
		/**
		 * {@link InputStream} used for source of data.
		 */
		private PushbackInputStream istream;
		/**
		 * {@link InputStream} used to output data.
		 */
		private OutputStream ostream;
		/**
		 * variable containing htttp version of request
		 */
		private String version;
		/**
		 * variable containing method of request
		 */
		private String method;
		/**
		 * variable containing host that sent request
		 */
		private String host;
		/**
		 * variable storing map of parameters
		 */
		private Map<String, String> params;
		/**
		 * variable storing map of temporary parameters
		 */
		private Map<String, String> tempParams;
		/**
		 * variable storing map of permanent parameters
		 */
		private Map<String, String> permPrams;
		/**
		 * variable storing list of output cookies
		 */
		private List<RCCookie> outputCookies;
		/**
		 * Sequence of random Upper case Letters used as a key
		 */
		private String SID;
		/**
		 * {@link RequestContext} used for this thread
		 */
		private RequestContext context;

		/**
		 * Standard constructor.
		 * 
		 * @param csocket
		 */
		public ClientWorker(Socket csocket) {
			super();
			this.csocket = csocket;
			params = new HashMap<String, String>();
			tempParams = new HashMap<String, String>();
			permPrams = new HashMap<String, String>();
			outputCookies = new ArrayList<RequestContext.RCCookie>();
		}

		@Override
		public void run() {
			try {
				// initialisation of streams
				istream = new PushbackInputStream(csocket.getInputStream());
				ostream = new BufferedOutputStream(csocket.getOutputStream());

				// interpretating header
				byte[] request = readRequest(istream);
				if (request == null) {
					outputErrorCloseConnection(400, "No request found");
					return;
				}
				String requestStr = new String(request, StandardCharsets.US_ASCII);
				// extracting first line
				List<String> headers = extractHeaders(requestStr);
				String[] firstLine = headers.isEmpty() ? null : headers.get(0).split(" ");
				if (firstLine == null || firstLine.length != 3) {
					outputErrorCloseConnection(400, "Bad request");
					return;
				}

				// determine host from headers
				determineHost(headers);
				// determine cookies
				checkSession(headers);
				// determine method

				if (!determineMethod(firstLine[0]))
					return;
				// determine version
				if (!determineVersion(firstLine))
					return;
				// determine parameters and return
				String requestedPathSplitted = determineParameters(firstLine);
				if (requestedPathSplitted == null)
					return;
				// run dispatch request
				internalDispatchRequest(requestedPathSplitted, true);
			} catch (IOException e) {
				// probably can not reach to server so no response is given, but trying again
				// cannot hurt.
				outputErrorCloseConnection(256, "Communication stream error, try reloading.");
			} catch (Exception e) {
				outputErrorCloseConnection(500, "Internal Server Error");
			}
		}

		/**
		 * Method that parses parameters out of provided lines
		 * 
		 * @param firstLine lines provided
		 * @return true if version is valid, false otherwise
		 * @throws IOException if error can not be output
		 */
		private String determineParameters(String[] firstLine) throws IOException {
			String requestedPath = firstLine[1];
			String requestedPathSplitted[] = requestedPath.split("\\?");

			if (requestedPathSplitted.length == 2) {
				parseParameters(requestedPathSplitted[1]);
			} else if (requestedPathSplitted.length != 1) {
				outputErrorCloseConnection(400, "Bad request");
				return null;
			}
			return requestedPathSplitted[0];
		}

		/**
		 * Method that determines version of http request.
		 * 
		 * @param firstLine first line of request
		 * @return true if version is valid, false otherwise
		 * @throws IOException if error can not be output
		 */
		private boolean determineVersion(String[] firstLine) throws IOException {
			if (firstLine[2] == null) {
				outputErrorCloseConnection(365, "HTTP Version must be declared");
				return false;
			}
			version = firstLine[2].toUpperCase();
			if (!version.equals("HTTP/1.1") && !version.equals("HTTP/1.0")) {
				outputErrorCloseConnection(400, "HTTP Version Not Supported");
				return false;
			}
			return true;
		}

		/**
		 * Method that determines type of http request.
		 * 
		 * @param firstLine first line of request
		 * @return true if method is valid, false otherwise
		 * @throws IOException if error can not be output
		 */
		private boolean determineMethod(String firstLine) throws IOException {
			if (firstLine == null) {
				outputErrorCloseConnection(400, "Method not declared");
				return false;
			}
			method = firstLine.toUpperCase();
			if (!method.equals("GET")) {
				outputErrorCloseConnection(400, "Method not allowed");
				return false;
			}
			return true;
		}

		/**
		 * Method that determines host from provided headers
		 * 
		 * @param headers list of strings containing data from clients request
		 */
		private void determineHost(List<String> headers) {
			for (var head : headers) {
				if (head.startsWith("Host:")) {
					host = head.split(":")[1].strip();
					return;
				}
			}
			host = domainName;
		}

		/**
		 * Method that is used to output error and close connection.
		 * 
		 * @param statusNumber of error that occurred
		 * @param explanation  message that describes error
		 */
		private void outputErrorCloseConnection(int statusNumber, String explanation) {
			try {
				sendError(ostream, statusNumber, explanation);
				flushAndClose();
			} catch (IOException e) {
			}
		}

		/**
		 * Method that goes through provided list of headers, checks if line starts with
		 * cookie and tries to parse it to find proper sid. After the sid is found,
		 * checks whether such seed is already saved, and if it is, checks if it is
		 * valid.
		 * 
		 * @param headers list of strings containing data from clients request
		 */
		private void checkSession(List<String> headers) {
			String currSid = null;
			l1: for (String line : headers) {
				if (line.startsWith("Cookie:")) {
					String[] cookies = line.substring("Cookie:".length()).split(";");
					for (String pair : cookies) {
						String[] pairParsed = pair.split("=");
						if (pairParsed[0].toLowerCase().trim().equals("sid")) {
							currSid = pairParsed[1].trim().substring(1);
							currSid = currSid.substring(0, currSid.length() - 1);
							break l1;
						}
					}
				}
			}
			SessionMapEntry s;
			if ((s = sessions.get(currSid)) != null) {
				if (s.host.equals(host)) {
					if (stillValid(s.validUntil)) {
						s.validUntil = System.currentTimeMillis() / 1000 + sessionTimeout;
						permPrams = s.map;
						return;
					} else {
						sessions.remove(currSid);
					}
				}
			}

			SessionMapEntry sessionMapEntry = new SessionMapEntry(generateSid(SID_LENGTH), host,
					System.currentTimeMillis() / 1000 + sessionTimeout);

			RCCookie rcCookie = new RCCookie("sid", sessionMapEntry.sid, null, host, "/");
			rcCookie.setFinal(true);
			permPrams = sessionMapEntry.map;
			outputCookies.add(rcCookie);
			sessions.put(sessionMapEntry.sid, sessionMapEntry);
		}

		/**
		 * Method that generates sequence of random upper case letters.
		 * 
		 * @param len of sequence
		 * @return sequence of random upper case letters as string
		 */
		private synchronized String generateSid(int len) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < len; i++) {
				sb.append((char) ('A' + (Math.abs(sessionRandom.nextInt()) % 26)));
			}
			return sb.toString();
		}

		/**
		 * Method that takes string and parses out parameters
		 * 
		 * @param string that is parsed
		 * @return true if parsing was successful, false otherwise
		 */
		private boolean parseParameters(String string) {
			String[] requests = string.split("&");
			for (var request : requests) {
				String[] params = request.split("=");
				if (params.length != 2) {
					return false;
				}
				this.params.put(params[0], params[1]);
			}
			return true;
		}

		/**
		 * Method used to process given url request. Can be interpret as internal or
		 * external call.
		 * 
		 * @param urlPath    url request that has to be processed
		 * @param directCall boolean flag representing whether call is internal or not
		 * @throws Exception
		 */
		public void internalDispatchRequest(String urlPath, boolean directCall) throws Exception {
			if (directCall == true && (urlPath.startsWith("/private/") || urlPath.equals("/private"))) {
				outputErrorCloseConnection(404, "File not found");
				return;
			}

			if (context == null) {
				context = new RequestContext(ostream, params, permPrams, outputCookies, tempParams, this, SID);
			}

			Path requestedFile = documentRoot.resolve(urlPath.substring(1));
			if (!requestedFile.normalize().startsWith(documentRoot)) {
				outputErrorCloseConnection(403, "Forbidden!");
				return;
			}

			if (urlPath.startsWith("/ext")) {
				String wanted = urlPath.split("/")[2];
				Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(WORKERS_PATH + wanted);
				IWebWorker newObject = (IWebWorker) referenceToClass.getDeclaredConstructor().newInstance();
				newObject.processRequest(context);
				flushAndClose();
				return;
			}

			if (workersMap.containsKey(urlPath)) {
				workersMap.get(urlPath).processRequest(context);
				flushAndClose();
				return;
			}

			if (!Files.isReadable(requestedFile)) {
				outputErrorCloseConnection(404, "File not readable");
				return;
			}

			context.setMimeType(getMimeType(urlPath));
			context.setStatusText("OK");
			context.setStatusCode(200);

			if (urlPath.endsWith(".smscr")) {
				byte[] bytes = Files.readAllBytes(requestedFile);
				String text = new String(bytes);
				(new SmartScriptEngine((new SmartScriptParser(text)).getDocumentNode(), context)).execute();
				flushAndClose();
				return;
			}
			context.setContentLength(Files.size(requestedFile));
			context.write(Files.readAllBytes(requestedFile));

			flushAndClose();
		}

		/**
		 * Method that flushes and closes {@link OutputStream} and {@link Socket}
		 * 
		 * @throws IOException if it can not be closed or flushed
		 */
		private void flushAndClose() throws IOException {
			ostream.flush();
			csocket.close();
		}

		/**
		 * Method that dispatches requests that are internal
		 */
		public void dispatchRequest(String urlPath) throws Exception {
			internalDispatchRequest(urlPath, false);
		}

	}

	/**
	 * Method used to determine mime type of requested file
	 * 
	 * @param requestedFile from which mime type is determined
	 * @return string representing mime type
	 */
	private static String getMimeType(String requestedFile) {
		String extension = null;
		int i = requestedFile.lastIndexOf('.');
		if (i > 0) {
			extension = requestedFile.toString().substring(i + 1);
		}
		return mimeTypes.get(extension);
	}

	/**
	 * Method that simulates Moore machine used to get array of bytes containing
	 * data of headers
	 * 
	 * @param is {@link InputStream} from where data is read
	 * @return array of bytes containing data of headers
	 * @throws IOException if data can not be read from {@link InputStream}
	 */
	private static byte[] readRequest(InputStream is) throws IOException {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int state = 0;
		l: while (true) {
			int b = is.read();
			if (b == -1)
				return null;
			if (b != 13) {
				bos.write(b);
			}
			switch (state) {
			case 0:
				if (b == 13) {
					state = 1;
				} else if (b == 10)
					state = 4;
				break;
			case 1:
				if (b == 10) {
					state = 2;
				} else
					state = 0;
				break;
			case 2:
				if (b == 13) {
					state = 3;
				} else
					state = 0;
				break;
			case 3:
				if (b == 10) {
					break l;
				} else
					state = 0;
				break;
			case 4:
				if (b == 10) {
					break l;
				} else
					state = 0;
				break;
			}
		}
		return bos.toByteArray();
	}

	/**
	 * Method used to print provided error and number representing status code to
	 * provided {@link OutputStream}.
	 * 
	 * @param cos        {@link OutputStream} where data is written
	 * @param statusCode number code for error
	 * @param statusText error explanation
	 * @throws IOException if data can not be written to {@link OutputStream}
	 */
	private static void sendError(OutputStream cos, int statusCode, String statusText) throws IOException {

		cos.write(("HTTP/1.1 " + statusCode + " " + statusText + "\r\n" + "Server: simple java server\r\n"
				+ "Content-Type: text/plain;charset=UTF-8\r\n" + "Content-Length: 0\r\n" + "Connection: close\r\n"
				+ "\r\n").getBytes(StandardCharsets.US_ASCII));
		cos.flush();
	}

	/**
	 * Method used to extract headers lines from client request
	 * 
	 * @param requestHeader lines of client request
	 * @return list of string parsed form client request
	 */
	private static List<String> extractHeaders(String requestHeader) {
		List<String> headers = new ArrayList<String>();
		String currentLine = null;
		for (String s : requestHeader.split("\n")) {
			if (s.isEmpty())
				break;
			char c = s.charAt(0);
			if (c == 9 || c == 32) {
				currentLine += s;
			} else {
				if (currentLine != null) {
					headers.add(currentLine);
				}
				currentLine = s;
			}
		}
		if (!currentLine.isEmpty()) {
			headers.add(currentLine);
		}
		return headers;
	}

	/**
	 * Method that checks whether provided argument is greater than current time.
	 * 
	 * @param validUntil point in time in seconds that is compared to current time
	 * @return boolean representing whether provided argument is greater than
	 *         current time
	 */
	private boolean stillValid(long validUntil) {
		return System.currentTimeMillis() / 1000 < validUntil;
	}

	/**
	 * Main method used for running server.
	 * 
	 * @param args used to get path for config file
	 */
	public static void main(String[] args) {
		SmartHttpServer smartServer;
		try {
			smartServer = new SmartHttpServer(args[0]);
			try {
				smartServer.start();
				// smartServer.stop();
			} catch (Exception e) {
				System.out.println("Server crashed unexpectedly.");
				e.printStackTrace();
			}
		} catch (Exception e1) {
			System.out.println("Server did not succeed to start");
			e1.printStackTrace();
		}
	}

	/**
	 * Class used as a structure holding sid, host, valid until and map.
	 * 
	 * @author juren
	 *
	 */
	private static class SessionMapEntry {
		/**
		 * Variable used for storing session ID of {@link SessionMapEntry}
		 */
		String sid;
		/**
		 * Variable that stores string reference to host that created session
		 */
		String host;
		/**
		 * number that stores number of milliseconds of point in time in which this
		 * session will become expired
		 */
		long validUntil;
		/**
		 * Stores session parameters in map
		 */
		Map<String, String> map;

		/**
		 * Standard constructor.
		 * 
		 * @param sid        Variable used for storing session ID of
		 *                   {@link SessionMapEntry}
		 * @param host       string reference to host that created session
		 * @param validUntil that stores number of milliseconds of point in time in
		 *                   which this session will become expired
		 * @param map        session parameters in map
		 */
		public SessionMapEntry(String sid, String host, long validUntil) {
			super();
			this.sid = sid;
			this.host = host;
			this.validUntil = validUntil;
			this.map = new ConcurrentHashMap<String, String>();
		}
	}

}