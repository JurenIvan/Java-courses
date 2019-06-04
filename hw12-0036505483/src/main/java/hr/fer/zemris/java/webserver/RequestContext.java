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

/**
 * Class representing context. Has method to write header and given text.
 * 
 * @author juren
 *
 */
public class RequestContext {

	/** constant used for storing max age tag */
	private static final String MAX_AGE_TAG = "Max-Age";
	/** constant used for storing protocol tag and version */
	private static final String PROTOCOL_TAG = "HTTP/1.1";
	/** constant used for storing content type tag */
	private static final String CONTENT_TYPE_TAG = "Content-Type:";
	/** constant used for storing domain tag */
	private static final String DOMAIN_TAG = "Domain";
	/** constant used for storing set cookie tag */
	private static final String SET_COOKIE_TAG = "Set-Cookie:";
	/** constant used for storing content length tag */
	private static final String CONTENT_LENGTH_ADDON = "Content-Length:";
	/** constant used for storing charset tag */
	private static final String MIME_TYPE_CHARSET_ADDON = "; charset=";
	/** constant used for storing default encoding */
	private static final String DEFAULT_ENCODING = "UTF-8";
	/** constant used for storing default status code */
	private static final int DEFAULT_STATUS_CODE = 200;
	/** constant used for storing default status text */
	private static final String DEFAULT_STATUS_TEXT = "OK";
	/** constant used for storing default mime type */
	private static final String DEFAULT_MIME_TYPE = "text/html";
	/** constant used for storing default content length */
	private static final Long DEFAULT_CONTENT_LENGHT = null;
	/**
	 * constant used for storing whether the header is generated at the beginning
	 */
	private static final boolean DEFAULT_HEADER_GENERATED = false;
	/** constant used for storing default charset used for heather */
	private static final String DEFAULT_HEADER_CHARSET_NAME = "ISO_8859_1";
	/** constant used for storing default way to go into new line */
	private static final String LINE_SEPARATOR = "\r\n";
	/** constant used for storing mime type charset needed */
	private static final String MIME_TYPE_CHARSET_NEEDED = "text/";

	/**
	 * Variable that stores reference to stream used for writing header onto
	 */
	private OutputStream outputStream;

	/**
	 * Variable that stores reference to charset used to write data
	 */
	private Charset charset;

	/**
	 * Variable that stores reference to string holding encoding type
	 */
	private String encoding;

	/**
	 * Variable that stores status code of header
	 */
	private int statusCode;

	/**
	 * Variable that stores reference to String that holds text displaying status
	 */
	private String statusText;

	/**
	 * Variable that stores reference to String that holds mime type
	 */
	private String mimeType;

	/**
	 * Variable that stores reference to Long that stores size of content that is
	 * written, if it is null that is signal that it is not stored yet.
	 */
	private Long contentLength;

	/**
	 * Variable that stores reference to map containing parameters of Context
	 */
	private Map<String, String> parameters;

	/**
	 * Variable that stores reference to map containing temporaryParameters of
	 * Context
	 */
	private Map<String, String> temporaryParameters;

	/**
	 * Variable that stores reference to map containing persistentParameters of
	 * Context
	 */
	private Map<String, String> persistentParameters;

	/**
	 * Variable that stores reference to list containing outputCookies of Context
	 */
	private List<RCCookie> outputCookies;

	/**
	 * Variable that stores flag whether header has been generated so far
	 */
	private boolean headerGenerated;

	/**
	 * Variable that stores reference to {@link IDispatcher} stored in context
	 */
	private IDispatcher dispatcher;
	/**
	 * Variable that stores reference to String that is made of randomly generated
	 * sequence of letters
	 */
	private String SID;

	/**
	 * Private constructor used to set up default values
	 */
	private RequestContext() {
		encoding = DEFAULT_ENCODING;
		statusCode = DEFAULT_STATUS_CODE;
		statusText = DEFAULT_STATUS_TEXT;
		mimeType = DEFAULT_MIME_TYPE;
		contentLength = DEFAULT_CONTENT_LENGHT;
		headerGenerated = DEFAULT_HEADER_GENERATED;
		charset = Charset.forName(encoding);
	}

	/**
	 * Standard constructor for {@link RequestContext}
	 * 
	 * @param outputStream         reference to stream used for writing header onto
	 * @param parameters           reference to map containing parameters of Context
	 * @param persistentParameters reference to map containing persistentParameters
	 *                             of Context
	 * @param outputCookies        list containing outputCookies of Context
	 * @param temporaryParameters  reference to map containing temporaryParameters
	 *                             of Context
	 * @param dispatcher           reference to {@link IDispatcher} stored in
	 *                             context
	 * @param SID                  String that is made of randomly generated
	 *                             sequence of letters
	 * @throws NullPointerException if outputStream is null reference
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies,
			Map<String, String> temporaryParameters, IDispatcher dispatcher, String SID) {
		this(outputStream, parameters, persistentParameters, outputCookies, SID);
		this.temporaryParameters = temporaryParameters;
		this.dispatcher = dispatcher;
	}

	/**
	 * Standard constructor for {@link RequestContext}
	 * 
	 * @param outputStream         reference to stream used for writing header onto
	 * @param parameters           reference to map containing parameters of Context
	 * @param persistentParameters reference to map containing persistentParameters
	 *                             of Context
	 * @param outputCookies        list containing outputCookies of Context
	 * @param SID                  String that is made of randomly generated
	 *                             sequence of letters
	 * @throws NullPointerException if outputStream is null reference
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies, String SID) {
		this();
		this.outputStream = Objects.requireNonNull(outputStream, "OutputStream provided must not be null reference");
		this.parameters = Collections.unmodifiableMap(new HashMap<>(parameters != null ? parameters : new HashMap<>()));
		this.temporaryParameters = new HashMap<>();
		this.persistentParameters = persistentParameters != null ? persistentParameters : new HashMap<>();
		this.outputCookies = outputCookies != null ? outputCookies : new ArrayList<>();
		this.SID = SID;
	}

	/**
	 * Standard constructor for {@link RequestContext}
	 * 
	 * @param outputStream         reference to stream used for writing header onto
	 * @param parameters           reference to map containing parameters of Context
	 * @param persistentParameters reference to map containing persistentParameters
	 * @param outputCookies        list containing outputCookies of Context
	 * @throws NullPointerException if outputStream is null reference
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies) {
		this(outputStream, parameters, persistentParameters, outputCookies, null);
	}

	/**
	 * Method that retrieves value from parameters map (or null if no association
	 * exists)
	 * 
	 * @param name name of parameter
	 * @return parameter stored
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}

	/**
	 * Method that retrieves names of all parameters in parameters map.this set is
	 * read-only
	 * 
	 * @return names of all parameters in parameters map
	 */
	public Set<String> getParameterNames() {
		return Collections.unmodifiableSet(parameters.keySet());
	}

	/**
	 * Method that retrieves value from persistentParameters map (or null if no
	 * association exists)
	 * 
	 * @param name name of parameter
	 * @return value from persistentParameters map
	 */
	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}

	/**
	 * Method that retrieves value from persistentParameters map (or null if no
	 * association exists):
	 * 
	 * @return value from persistentParameters map
	 */
	public Set<String> getPersistentParameterNames() {
		return Collections.unmodifiableSet(persistentParameters.keySet());
	}

	/**
	 * Method that stores a value to persistentParameters map:
	 * 
	 * @param name  name of parameter
	 * @param value value of parameter
	 */
	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(name, value);
	}

	/**
	 * Method that removes a value from persistentParameters map:
	 * 
	 * @param name of parameter that will be removed
	 */
	public void removePersistentParameter(String name) {
		persistentParameters.remove(name);
	}

	/**
	 * Method that retrieves value from getTemporaryParameter map (or null if no
	 * association exists)
	 * 
	 * @param name of parameter that will be retrieved
	 * @return
	 */
	public String getTemporaryParameter(String name) {
		return temporaryParameters.get(name);
	}

	/**
	 * Mthod that retrieves names of all parameters in temporary parameters map
	 * (note, this set must be read-only):
	 * 
	 * @return Set of names of all parameters in temporary parameters map
	 */
	public Set<String> getTemporaryParameterNames() {
		return Collections.unmodifiableSet(temporaryParameters.keySet());
	}

	/**
	 * Method that retrieves an identifier which is unique for current user session
	 * 
	 * @return identifier which is unique for current user session
	 */
	public String getSessionID() {
		return SID;
	}

	/**
	 * Getter for Dispathcer
	 * 
	 * @return the dispatcher
	 */
	public IDispatcher getDispatcher() {
		return dispatcher;
	}

	/**
	 * Method that stores a value to temporaryParameters map
	 * 
	 * @param name  name of parameter
	 * @param value value of parameter
	 */
	public void setTemporaryParameter(String name, String value) {
		temporaryParameters.put(name, value);
	}

	/**
	 * Setter for content lenght
	 *
	 * @param contentLength the contentLength to set
	 */
	public void setContentLength(Long contentLength) {
		if (!this.headerGenerated) {
			this.contentLength = contentLength;
		}
	}

	/**
	 * Method that removes a value from temporaryParameters map:
	 * 
	 * @param name of parameter that will be removed if found
	 */
	public void removeTemporaryParameter(String name) {
		temporaryParameters.remove(name);
	}

	/**
	 * Method that writes content of {@link RequestContext} onto
	 * {@link OutputStream}. First time of writing, header will be generated and
	 * printed.
	 * 
	 * @param text that will be written
	 * @return reference to this object
	 * @throws IOException if data can not be written onto outputStream
	 */
	public RequestContext write(String text) throws IOException {
		return write(text.getBytes(charset));
	}

	/**
	 * Method that writes content of {@link RequestContext} onto
	 * {@link OutputStream}. First time of writing, header will be generated and
	 * printed.
	 * 
	 * @param data that will be written
	 * @return reference to this object
	 * @throws IOException if data can not be written onto outputStream
	 */
	public RequestContext write(byte[] data) throws IOException {
		return write(data, 0, data.length);
	}

	/**
	 * Method that writes content of {@link RequestContext} onto
	 * {@link OutputStream}. First time of writting, header will be generated and
	 * printed.
	 * 
	 * @param data that will be written
	 * @return reference to this object
	 * @throws IOException if data can not be written onto outputStream
	 */

	/**
	 * Method that writes content of {@link RequestContext} onto
	 * {@link OutputStream}. First time of writing, header will be generated and
	 * printed.
	 * 
	 * @param data   that will be written
	 * @param offset from which letter do we start writing
	 * @param len    how much bytes of data gets written
	 * @return reference to this object
	 * @throws IOException if data can not be written onto outputStream
	 */
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

	/**
	 * Method that is used to generate header of context
	 * 
	 * @return array of bytes containing data generated for header
	 */
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
		
		if (mimeType != null && mimeType.startsWith(MIME_TYPE_CHARSET_NEEDED)) {
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

	/**
	 * Method that is used to generate line of code that holds data for for cookies
	 * 
	 * @param cookie that will be written
	 * @return text representation of cookie
	 */
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
		if (cookie.isHTTPOnly) {
			sb.append("HttpOnly  ");
		}

		return sb.toString().substring(0, sb.length() - 2);
	}

	/**
	 * Setter for encoding
	 * 
	 * @param encoding
	 */
	public void setEncoding(String encoding) {
		if (headerGenerated)
			throw new RuntimeException("Header already generated");
		this.encoding = encoding;
	}

	/**
	 * Setter for mimeType
	 * 
	 * @param mimeType
	 * @throws RuntimeException if header is already generated
	 */
	public void setMimeType(String mimeType) {
		if (headerGenerated)
			throw new RuntimeException("Header already generated");
		this.mimeType = mimeType;
	}

	/**
	 * Setter for Status Code
	 * 
	 * @param StatusCode
	 * @throws RuntimeException if header is already generated
	 */
	public void setStatusCode(int statusCode) {
		if (headerGenerated)
			throw new RuntimeException("Header already generated");
		this.statusCode = statusCode;
	}

	/**
	 * Setter for Status Text
	 * 
	 * @param StatusText
	 * @throws RuntimeException if header is already generated
	 */
	public void setStatusText(String statusText) {
		if (headerGenerated)
			throw new RuntimeException("Header already generated");
		this.statusText = statusText;
	}

	/**
	 * Method that adds new {@link RCCookie} into list
	 * 
	 * @param rcCookie that will be added
	 */
	public void addRCCookie(RCCookie rcCookie) {
		if (headerGenerated)
			throw new RuntimeException("Header already generated");
		outputCookies.add(rcCookie);
	}

	/**
	 * Class used as a structure for {@link RCCookie}
	 */
	public static class RCCookie {
		/** Variable used for storing name of cookie */
		public final String name;
		/** Variable used for storing value of cookie */
		public final String value;
		/** Variable used for storing domain of cookie */
		public final String domain;
		/** Variable used for storing path of cookie */
		public final String path;
		/** Variable used for storing maxAge of cookie */
		public final Integer maxAge;
		/**
		 * Variable used for storing flag representing whether {@link RCCookie} is http
		 * only or not
		 */
		public boolean isHTTPOnly;

		/**
		 * Constructor for {@link RCCookie}
		 * 
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
			isHTTPOnly = false;
		}

		/**
		 * setter for flag representing whether {@link RCCookie} is final or not
		 * 
		 * @param flag
		 */
		public void setFinal(boolean flag) {
			this.isHTTPOnly = flag;
		}

	}

}
