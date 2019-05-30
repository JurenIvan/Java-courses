package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class RequestContext {

	private static final String MAX_AGE_TAG = "Max-Age";
	private static final String PROTOCOL_TAG = "HTTP/1.1";
	private static final String CONTENT_TYPE_TAG = "Content-Type:";
	private static final String DOMAIN_TAG = "Domain";
	private static final String SET_COOKIE_TAG = "Set-Cookie:";
	private static final String CONTENT_LENGTH_ADDON = "Content-Length:";
	private static final String MIME_TYPE_CHARSET_ADDON = "; charset=";
	private static final String DEFAULT_ENCODING = "UTF-8";
	private static final int DEFAULT_STATUS_CODE = 200;
	private static final String DEFAULT_STATUS_TEXT = "OK";
	private static final String DEFAULT_MIME_TYPE = "text/html";
	private static final Long DEFAULT_CONTENT_LENGHT = null;
	private static final boolean DEFAULT_HEADER_GENERATED = false;
	private static final String DEFAULT_HEADER_CHARSET_NAME = "ISO_8859_1";
	private static final String LINE_SEPARATOR = "\r\n";
	private static final String MIME_TYPE_CHARSET_NEEDED = "text/";

	private static RCCookie rcCookie;
	
	private OutputStream outputStream;
	private Charset charset;

	private String encoding;
	private int statusCode;
	private String statusText;
	private String mimeType;
	private Long contentLength;

	private Map<String, String> parameters;
	private Map<String, String> temporaryParameters;
	private Map<String, String> persistentParameters;
	private List<RCCookie> outputCookies;

	private boolean headerGenerated;

	private IDispatcher dispatcher;
	private String SID;

	private RequestContext() {
		encoding = DEFAULT_ENCODING;
		statusCode = DEFAULT_STATUS_CODE;
		statusText = DEFAULT_STATUS_TEXT;
		mimeType = DEFAULT_MIME_TYPE;
		contentLength = DEFAULT_CONTENT_LENGHT;
		headerGenerated = DEFAULT_HEADER_GENERATED;
		charset = Charset.forName(encoding);
	}

	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies,Map<String, String> temporaryParameters, IDispatcher dispatcher,String SID) {
		this(outputStream, parameters, persistentParameters, outputCookies,SID);
		this.temporaryParameters=temporaryParameters;
		this.dispatcher=dispatcher;
	}

	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies,String SID) {
		this();
		this.outputStream = Objects.requireNonNull(outputStream, "OutputStream provided must not be null reference");
		this.parameters = Collections.unmodifiableMap(new HashMap<String, String>(parameters));
		this.temporaryParameters = new HashMap<String, String>();
		this.persistentParameters = persistentParameters;
		this.outputCookies = new ArrayList<RequestContext.RCCookie>(outputCookies);
		this.SID=SID;
	}
	
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies) {
		this(outputStream,parameters,persistentParameters,outputCookies,null);
	}

	/**
	 * method that retrieves value from parameters map (or null if no association
	 * exists):
	 * 
	 * @param name
	 * @return
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}


	/**
	 * method that retrieves names of all parameters in parameters map (note, this
	 * set must be read-only):
	 * 
	 * @return
	 */
	public Set<String> getParameterNames() {
		return Collections.unmodifiableSet(parameters.keySet());
	}

	/**
	 * method that retrieves value from persistentParameters map (or null if no
	 * association exists):
	 */
	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}

	/**
	 * method that retrieves value from persistentParameters map (or null if no
	 * association exists):
	 * 
	 * 
	 * @return
	 */
	public Set<String> getPersistentParameterNames() {
		return Collections.unmodifiableSet(persistentParameters.keySet());
	}

	/**
	 * * method that stores a value to persistentParameters map:
	 * 
	 * @param name
	 * @param value
	 */
	public void setPersistentParameter(String name, String value) { // provjere
		persistentParameters.put(name, value);
	}

	/**
	 * method that removes a value from persistentParameters map:
	 * 
	 * @param name
	 */
	public void removePersistentParameter(String name) {
		persistentParameters.remove(name);
	}

	/**
	 * method that retrieves value from getTemporaryParameter map (or null if no
	 * association exists):
	 * 
	 * @param name
	 * @return
	 */
	public String getTemporaryParameter(String name) {
		return temporaryParameters.get(name);
	}

	/**
	 * method that retrieves names of all parameters in temporary parameters map
	 * (note, this set must be read-only):
	 * 
	 * @return
	 */
	public Set<String> getTemporaryParameterNames() {
		return Collections.unmodifiableSet(temporaryParameters.keySet());
	}

	/**
	 * method that retrieves an identifier which is unique for current user session
	 * 
	 * @return
	 */
	public String getSessionID() {
		return SID;
	}
	
	/**
	 * @return the dispatcher
	 */
	public IDispatcher getDispatcher() {
		return dispatcher;
	}

	/**
	 * * method that stores a value to temporaryParameters map
	 * 
	 * @param name
	 * @param value
	 */
	public void setTemporaryParameter(String name, String value) {
		temporaryParameters.put(name, value);
	}

	/**
	 * @param contentLength the contentLength to set
	 */
	public void setContentLength(Long contentLength) {
		if (!this.headerGenerated) {
			this.contentLength = contentLength;
		}
	}

	/**
	 * method that removes a value from temporaryParameters map:
	 * 
	 * @param name
	 */
	public void removeTemporaryParameter(String name) {
		temporaryParameters.remove(name);
	}

	public RequestContext write(String text) throws IOException {
		return write(text.getBytes(charset));
	}

	public RequestContext write(byte[] data) throws IOException {
		return write(data, 0, data.length);
	}

	public RequestContext write(byte[] data, int offset, int len) throws IOException {
		if (!this.headerGenerated) {
			charset = Charset.forName(encoding);
			byte[] generatedHeader = generateHeader();
			outputStream.write(generatedHeader, 0, generatedHeader.length);
			this.headerGenerated = true;
		}
		outputStream.write(data, offset, len);
		return this;
	}

	private byte[] generateHeader() {
		StringBuilder sb = new StringBuilder();

		sb.append(PROTOCOL_TAG);
		sb.append(' ');
		sb.append(statusCode);
		sb.append(' ');
		sb.append(statusText);
		sb.append(LINE_SEPARATOR);
		sb.append(CONTENT_TYPE_TAG);
		sb.append(' ');
		if (mimeType!=null && mimeType.startsWith(MIME_TYPE_CHARSET_NEEDED)) {
			sb.append(mimeType);
			sb.append(MIME_TYPE_CHARSET_ADDON);
			sb.append(encoding);
		} else {
			sb.append(mimeType);
		}
		sb.append(LINE_SEPARATOR);

		if (contentLength != null) {
			sb.append(CONTENT_LENGTH_ADDON);
			sb.append(contentLength);
			sb.append(LINE_SEPARATOR);
		}

		for (var cookie : outputCookies) {
			sb.append(cookieOutput(cookie));
			sb.append(LINE_SEPARATOR);
		}
		sb.append(LINE_SEPARATOR);
		return sb.toString().getBytes(Charset.forName(DEFAULT_HEADER_CHARSET_NAME));
	}

	private String cookieOutput(RCCookie cookie) {
		StringBuilder sb = new StringBuilder();
		sb.append(SET_COOKIE_TAG);
		sb.append(' ');
		if (cookie.name != null) {
			sb.append(cookie.name);
			sb.append("=\"");
			sb.append(cookie.value);
			sb.append("\"; ");
		}
		if (cookie.domain != null) {
			sb.append(DOMAIN_TAG);
			sb.append('=');
			sb.append(cookie.domain);
			sb.append("; ");
		}
		if (cookie.path != null) {
			sb.append("Path=");
			sb.append(cookie.path);
			sb.append("; ");
		}
		if (cookie.maxAge != null) {
			sb.append(MAX_AGE_TAG);
			sb.append('=');
			sb.append(cookie.maxAge);
			sb.append("; ");
		}
		if(cookie.isFinal) {
			sb.append("HttpOnly  ");
		}

		return sb.toString().substring(0, sb.length() - 2);
	}

	

	public void setEncoding(String encoding) {
		if (headerGenerated)
			throw new RuntimeException("Headther already generated");
		this.encoding = encoding;
	}

	public void setMimeType(String mimeType) {
		if (headerGenerated)
			throw new RuntimeException("Headther already generated");
		this.mimeType = mimeType;
	}

	public void setStatusCode(int statusCode) {
		if (headerGenerated)
			throw new RuntimeException("Headther already generated");
		this.statusCode = statusCode;
	}

	public void setStatusText(String statusText) {
		if (headerGenerated)
			throw new RuntimeException("Headther already generated");
		this.statusText = statusText;
	}

	public void addRCCookie(RCCookie rcCookie) {
		if (headerGenerated)
			throw new RuntimeException("Headther already generated");
		outputCookies.add(rcCookie);
	}
	
	public static class RCCookie {
		public final String name;
		public final String value;
		public final String domain;
		public final String path;
		public final Integer maxAge;
		public boolean isFinal;

		/**
		 * @param name
		 * @param value
		 * @param domain
		 * @param path
		 * @param maxAge
		 */
		public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
			isFinal=false;
		}
		
		public void setFinal(boolean flag) {
			this.isFinal=flag;
		}

	}

}
