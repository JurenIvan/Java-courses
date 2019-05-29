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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;
import hr.fer.zemris.java.webserver.workers.IWebWorker;

public class SmartHttpServer {
	private String address;
	private String domainName;
	private int port;
	private int workerThreads;
	private int sessionTimeout;
	private static Map<String, String> mimeTypes;
	private ServerThread serverThread;
	private ExecutorService threadPool;
	private Path documentRoot;

	private Map<String, IWebWorker> workersMap;

	public SmartHttpServer(String configFileName)
			throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {

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
		documentRoot = Path.of(serverProperties.getProperty("server.documentRoot"));

		mimeTypes = new HashMap<String, String>();
		for (String mimeProperty : mimeProperties.stringPropertyNames()) {
			mimeTypes.put(mimeProperty, mimeProperties.getProperty(mimeProperty));
		}

		workersMap = new HashMap<String, IWebWorker>();
		for (var path : workersProperties.stringPropertyNames()) {
			Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(workersProperties.getProperty(path));
			Object newObject = referenceToClass.newInstance();
			workersMap.put(path, (IWebWorker) newObject);
		}

	}

	protected synchronized void start() {
		if (serverThread == null) {
			serverThread = new ServerThread();
			serverThread.start();
		}
		threadPool = Executors.newFixedThreadPool(workerThreads);
	}

	protected synchronized void stop() {
		serverThread.terminateThread();
		serverThread.interrupt();
		serverThread.stop();
		threadPool.shutdown();
	}

	protected class ServerThread extends Thread {

		private boolean run = true;

		public void terminateThread() {
			this.run = false;
		}

		@Override
		public void run() {
			try (ServerSocket serverSocket = new ServerSocket()) {
				// serverSocket.bind(new InetSocketAddress(InetAddress.getByName(address),
				// port));
				serverSocket.bind(new InetSocketAddress((InetAddress) null, port));
				while (run) {
					Socket client = serverSocket.accept();
					ClientWorker cw = new ClientWorker(client);
					threadPool.submit(cw);
				}

			} catch (IOException e) {
				e.printStackTrace();// nest pametnije?
			}
		}
	}

	private class ClientWorker implements Runnable, IDispatcher {
		private Socket csocket;
		private PushbackInputStream istream;
		private OutputStream ostream;
		private String version;
		private String method;
		private String host;
		private Map<String, String> params = new HashMap<String, String>();
		private Map<String, String> tempParams = new HashMap<String, String>();
		private Map<String, String> permPrams = new HashMap<String, String>();
		private List<RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
		private String SID;
		private RequestContext context;

		public ClientWorker(Socket csocket) {
			super();
			this.csocket = csocket;
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
					sendError(ostream, 400, "Bad request");
					csocket.close();
					return;
				}
				String requestStr = new String(request, StandardCharsets.US_ASCII);

				// extracting first line
				List<String> headers = extractHeaders(requestStr);
				String[] firstLine = headers.isEmpty() ? null : headers.get(0).split(" ");
				if (firstLine == null || firstLine.length != 3) {
					sendError(ostream, 400, "Bad request");
					csocket.close();
					return;
				}
				// determine method
				if (firstLine[0] == null) {
					sendError(ostream, 400, "Method Not Allowed");
					csocket.close();
					return;
				}
				method = firstLine[0].toUpperCase();
				if (!method.equals("GET")) {
					sendError(ostream, 400, "Method Not Allowed");
					csocket.close();
					return;
				}

				// determine version
				if (firstLine[2] == null) {
					sendError(ostream, 400, "HTTP Version must be declared");
					csocket.close();
					return;
				}
				version = firstLine[2].toUpperCase();
				if (!version.equals("HTTP/1.1") && !version.equals("HTTP/1.0")) {
					sendError(ostream, 400, "HTTP Version Not Supported");
					csocket.close();
					return;
				}

				for (var head : headers) {
					if (head.startsWith("Host:")) {
						host = head.split(":")[1].strip();
						break;
					}
				}

//				 get path
//				 if (firstLine[1] == null) { //needing no path is legit i suppose
//				 sendError(ostream, 400, "HTTP Version Not Supported");
//				 return;
//				 }
				String requestedPath = firstLine[1];
				String requestedPathSplitted[] = requestedPath.split("\\?");

				// get file part
				String path = requestedPathSplitted[0];
				Path requestedFile = documentRoot.resolve(path.substring(1));

				// get param part
				if (requestedPathSplitted.length == 2) {
					parseParameters(requestedPathSplitted[1]);
				} else if (requestedPathSplitted.length != 1) {
					sendError(ostream, 400, "Bad request");
					csocket.close();
					return;
				}
				internalDispatchRequest(requestedFile.toString(), true);

			} catch (Exception e) {
				e.printStackTrace();//
			}
		}

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

		public void internalDispatchRequest(String urlPath, boolean directCall) throws Exception {


			if (context == null) {
				context = new RequestContext(ostream, params, permPrams, outputCookies, tempParams, this);
			}

			if (!Paths.get(urlPath).normalize().startsWith(documentRoot)) {
				sendError(ostream, 403, "Forbidden");
				csocket.close();
				return;
			}

			if (urlPath.split("\\?")[0].split("ext").length == 2) {
				String[] niz = urlPath.split("\\?");
				String[] niz2 = niz[0].split("ext");

				String wanted = niz2[niz2.length - 1].substring(1);

				for (var path : workersMap.keySet()) {
					String[] lol=workersMap.get(path).getClass().getName().split("\\.");
					if (wanted.equals(lol[lol.length-1])) {
						workersMap.get(path).processRequest(context);
						ostream.flush();
						csocket.close();
						return;
					}
				}
				return;
			}
			

			if (!Files.isReadable(Paths.get(urlPath))) {
				sendError(ostream, 404, "File not found");
				csocket.close();
				
				return;
			}

			context.setMimeType(getMimeType(urlPath));
			context.setStatusText("200");

			if (urlPath.endsWith(".smscr")) {
				byte[] bytes = Files.readAllBytes(Paths.get(urlPath));
				String text = new String(bytes);
				SmartScriptEngine sse = new SmartScriptEngine((new SmartScriptParser(text)).getDocumentNode(), context);
				sse.execute();

				ostream.flush();
				ostream.close();

				return;
			}

			// serveFile(ostream, requestedFile);
			Long contentLenght = Files.size(Paths.get(urlPath));
			context.setContentLength(contentLenght);
			context.write(Files.readAllBytes(Paths.get(urlPath)));

			ostream.flush();
			csocket.close();
		}

		public void dispatchRequest(String urlPath) throws Exception {
			internalDispatchRequest(urlPath, false);
		}

	}

	private static String getMimeType(String requestedFile) {
		String extension = null;
		int i = requestedFile.lastIndexOf('.');
		if (i > 0) {
			extension = requestedFile.toString().substring(i + 1);
		}
		return mimeTypes.get(extension);
	}

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

	private static void sendError(OutputStream cos, int statusCode, String statusText) throws IOException {

		cos.write(("HTTP/1.1 " + statusCode + " " + statusText + "\r\n" + "Server: simple java server\r\n"
				+ "Content-Type: text/plain;charset=UTF-8\r\n" + "Content-Length: 0\r\n" + "Connection: close\r\n"
				+ "\r\n").getBytes(StandardCharsets.US_ASCII));
		cos.flush();

	}

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

	public static void main(String[] args)
			throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		new SmartHttpServer(args[0]).start();
		;
	}

}