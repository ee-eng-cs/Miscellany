package kp.web.httpserver;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

/**
 * The web server.
 * 
 */
public class WebServer {
	/*-
	http://www.fileformat.info/info/unicode/block/combining_diacritical_marks/list.htm
	http://www.fileformat.info/info/unicode/block/thai/list.htm 
		'THAI CHARACTER MAITAIKHU' (U+0E47)
		'THAI CHARACTER MAI THO' (U+0E49)
	http://www.fileformat.info/info/unicode/block/cyrillic/images.htm?start=1153
		U+0483 	COMBINING CYRILLIC TITLO
		\u0483\u0484\u0485\u0486
	*/
	/**
	 * HTTP server
	 */
	private static HttpServer server;
	/**
	 * Input tag file.
	 */
	private static final Path INPUT_TAGS_FILE = new File("src/main/java/kp/web/httpserver/InputTags.html").toPath();

	/**
	 * Port
	 */
	private static final int PORT = 8080;
	/**
	 * Number
	 */
	private static final int NUM = 15;
	/**
	 * Samples characters
	 */
	private static final Character[][] DATA_CHARS = { /*-*/
			{ NUM, '\u0E47' }, /*-*/
			{ NUM, '\u0E49' }, /*-*/
			{ NUM, '\u033A', '\u0346' }, /*-*/
			{ NUM, '\u030A', '\u0325' }, /*-*/
			{ NUM, '\u0329', '\u030D' }, /*-*/
			{ NUM, '\u0348', '\u030E' }, /*-*/
			{ NUM, '\u0307', '\u0323' }, /*-*/
			{ NUM, '\u0332', '\u0483' }, /*-*/
			{ NUM, '\u034E' }, /*-*/
	};
	/**
	 * Samples codepoints
	 */
	private static final Integer[][] DATA_CODEPOINTS = { /*-*/
			{ 0x1F468, 0x200D, 0x1F469, 0x200D, 0x1F467, 0x200D, 0x1F466 }, /*-*/
			{ 0x1F446, 0x1F446, 0x1F3FB, 0x1F446, 0x1F3FC, 0x1F446, 0x1F3FD, 0x1F446, 0x1F3FE, 0x1F446, 0x1F3FF }, /*-*/
			{ 0x1F648, 0x1F649, 0x1F64A },/*-*/
	};
	/**
	 * Begin HTML
	 */
	private static final String BEGIN = "<!DOCTYPE html>\n"/*-*/
			+ "<html><head>\n"/*-*/
			+ "<meta charset='utf-8'>\n"/*-*/
			+ "<style>\n"/*-*/
			+ "div{font-size:500%;margin-top:400px;margin-left:80px;float:left}\n"/*-*/
			+ "div.alt{margin-top:120px}\n"/*-*/
			+ "span{writing-mode:vertical-lr;letter-spacing:12px;text-orientation: upright}\n"/*-*/
			+ "</style>\n"/*-*/
			+ "</head><body>\n";
	/**
	 * Links HTML
	 */
	private static final String LINKS = /*-*/
			/*-*/ "<h1><a href='combining'>Combining</a></h1>" +
			/*-*/ "<h1><a href='emoji'>Emoji</a></h1>" +
			/*-*/ "<h1><a href='input_tags'>HTML 5 Input Tags</a></h1>";
	/**
	 * Div HTML
	 */
	private static final String DIV = "<div>%s</div>\n";
	/**
	 * Div span HTML
	 */
	private static final String DIV_SPAN = "<div class=alt><span>%s</span></div>\n";
	/**
	 * End HTML
	 */
	private static final String END = "</body></html>";

	/**
	 * Pages
	 *
	 */
	private enum Page {
		/**
		 * Home
		 */
		HOME,
		/**
		 * Combining
		 */
		COMBINING,
		/**
		 * Emoji
		 */
		EMOJI
	};

	/**
	 * Starts the server.
	 * 
	 */
	public static void startServer() {

		try {
			server = HttpServer.create(new InetSocketAddress(PORT), 0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		server.createContext("/", WebServer::handleHome);
		server.createContext("/combining", WebServer::handleCombining);
		server.createContext("/emoji", WebServer::handleEmoji);
		server.createContext("/input_tags", WebServer::handleInputTags);
		server.start();
		System.out.println("web server started");
		System.out.println("- ".repeat(50));
	}

	/**
	 * Stops the server.
	 * 
	 */
	public static void stopServer() {

		server.stop(0);
		System.out.println("web server stoped");
	}

	/**
	 * Handles the 'Home' page.
	 * 
	 * @param httpExchange the HTTP exchange
	 * @throws IOException if an I/O error occurs
	 */
	private static void handleHome(HttpExchange httpExchange) throws IOException {

		handle(httpExchange, prepareContent(Page.HOME));
	}

	/**
	 * Handles the 'Combining' page.
	 * 
	 * @param httpExchange the HTTP exchange
	 * @throws IOException if an I/O error occurs
	 */
	private static void handleCombining(HttpExchange httpExchange) throws IOException {

		handle(httpExchange, prepareContent(Page.COMBINING));
	}

	/**
	 * Handles the 'Emoji' page.
	 * 
	 * @param httpExchange the HTTP exchange
	 * @throws IOException if an I/O error occurs
	 */
	private static void handleEmoji(HttpExchange httpExchange) throws IOException {

		handle(httpExchange, prepareContent(Page.EMOJI));
	}

	/**
	 * Handles input tags.
	 * 
	 * @param httpExchange the HTTP exchange
	 * @throws IOException if an I/O error occurs
	 */
	private static void handleInputTags(HttpExchange httpExchange) throws IOException {

		try (Stream<String> lines = Files.newBufferedReader(INPUT_TAGS_FILE).lines()) {
			handle(httpExchange, lines.collect(Collectors.joining("\n")).getBytes());
		}
	}

	/**
	 * Handles the response.
	 * 
	 * @param httpExchange the HTTP exchange
	 * @param bytes        the bytes
	 * @throws IOException if an I/O error occurs
	 */
	private static void handle(HttpExchange httpExchange, byte[] bytes) throws IOException {

		httpExchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
		httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, bytes.length);

		final OutputStream output = httpExchange.getResponseBody();
		output.write(bytes);
		output.flush();
		httpExchange.close();
	}

	/**
	 * Prepares the content.
	 * 
	 * @param page the page
	 * @return the content
	 */
	private static byte[] prepareContent(Page page) {

		final StringBuffer strBuf = new StringBuffer();
		strBuf.append(BEGIN);
		switch (page) {
		case COMBINING:
			final Stream<Integer> numbers = IntStream.range(0, DATA_CHARS.length).boxed();
			strBuf.append(numbers.map(WebServer::getCharDivs).collect(Collectors.joining()));
			break;
		case EMOJI:
			final Stream<Integer> numbers1 = IntStream.range(0, DATA_CODEPOINTS.length).boxed();
			strBuf.append(numbers1.map(WebServer::getCpDivs).collect(Collectors.joining()));

			final Stream<Integer> numbers2 = IntStream.range(0x1F1E6, 0x1F1FF).boxed();
			final StringBuffer cpBuf = numbers2.collect(StringBuffer::new, StringBuffer::appendCodePoint,
					StringBuffer::append);
			strBuf.append(String.format(DIV_SPAN, cpBuf.toString()));
			break;
		default:// Home
			strBuf.append(LINKS);
			break;
		}
		strBuf.append(END);
		return strBuf.toString().getBytes(StandardCharsets.UTF_8);
	}

	/**
	 * Gets char 'div' element.
	 * 
	 * @param arg the arguments
	 * @return the char divs string
	 */
	private static String getCharDivs(int arg) {

		final StringBuffer innerStrBuf = new StringBuffer();
		for (int i = 1; i < DATA_CHARS[arg].length; i++) {
			final Stream<Character> chars = Collections.nCopies(DATA_CHARS[arg][0], DATA_CHARS[arg][i]).stream();
			innerStrBuf.append(chars.map(String::valueOf).collect(Collectors.joining()));
		}
		return String.format(DIV, innerStrBuf.toString());
	}

	/**
	 * Gets codepoint 'div' element.
	 * 
	 * @param arg the arguments
	 * @return the codepoint divs string
	 */
	private static String getCpDivs(int arg) {

		final StringBuffer innerStrBuf = new StringBuffer();
		final Stream<Integer> codePoints = Arrays.asList(DATA_CODEPOINTS[arg]).stream();
		codePoints.forEach(cp -> innerStrBuf.appendCodePoint(cp));
		return String.format(arg == 1 ? DIV_SPAN : DIV, innerStrBuf.toString());
	}
}