package kp.reactive.streams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import kp.reactive.streams.impl.SubscriberImpl;
import kp.reactive.streams.impl.SubscriberImplForByteBufferList;
import kp.utils.Utils;

/**
 * Reads the content from the web server with subscribers.
 *
 */
public class WebWithSubscribers {

	private static final int PORT = 8080;
	private static final URI LOCAL_URI = URI.create(String.format("http://localhost:%d/", PORT));
	private static final String DATA = "ABC";

	/**
	 * Launches HTTP server and HTTP client.<br>
	 * Receives the response using the line subscriber.
	 * 
	 */
	public static void receiveResponseUsingLineSubscriber() {

		try {
			final HttpServer server = startServer();
			final HttpClient httpClient = HttpClient.newBuilder().build();
			final HttpRequest httpRequest = HttpRequest.newBuilder(LOCAL_URI)/*- */
					.header("Content-Type", "text/plain; charset=UTF-8")/*- */
					.POST(BodyPublishers.ofString(DATA))/*- */
					.build();
			final BodyHandler<Void> responseBodyHandler = BodyHandlers.fromLineSubscriber(new SubscriberImpl<>());
			// 'Void' because all response body is forwarded to the given subscriber
			final HttpResponse<Void> httpResponseFromLine = httpClient.send(httpRequest, responseBodyHandler);
			System.out.printf("receiveResponseUsingLineSubscriber(): response status code[%s]%n",
					httpResponseFromLine.statusCode());
			Utils.sleepMillis(100);
			server.stop(0);
		} catch (IOException | InterruptedException e) {
			System.out.printf("receiveResponseUsingLineSubscriber(): exception[%s]%n", e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("- ".repeat(50));
	}

	/**
	 * Launches HTTP server and HTTP client.<br>
	 * Receives the response using the byte buffer list subscriber.
	 * 
	 */
	public static void receiveResponseUsingListSubscriber() {

		try {
			final HttpServer server = startServer();
			final HttpClient httpClient = HttpClient.newBuilder().build();
			final HttpRequest httpRequest = HttpRequest.newBuilder(LOCAL_URI)/*- */
					.header("Content-Type", "text/plain; charset=UTF-8")/*- */
					.POST(BodyPublishers.ofString(DATA))/*- */
					.build();
			final Subscriber<List<ByteBuffer>> byteBufferListSubscriber = new SubscriberImplForByteBufferList();

			// 'Void' because all response body is forwarded to the given subscriber
			final HttpResponse<Void> httpResponse = httpClient.send(httpRequest,
					BodyHandlers.fromSubscriber(byteBufferListSubscriber));
			System.out.printf("receiveResponseUsingListSubscriber(): response status code[%s]%n",
					httpResponse.statusCode());
			Utils.sleepMillis(100);
			server.stop(0);
		} catch (IOException | InterruptedException e) {
			System.out.printf("receiveResponseUsingListSubscriber(): exception[%s]%n", e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("- ".repeat(50));
	}

	/**
	 * Launches HTTP server and HTTP client.<br>
	 * Receives the response using the publisher.
	 * 
	 */
	public static void receiveResponseUsingPublisher() {

		try {
			final HttpServer server = startServer();
			final HttpClient httpClient = HttpClient.newBuilder().build();
			final HttpRequest httpRequest = HttpRequest.newBuilder(LOCAL_URI)/*- */
					.header("Content-Type", "text/plain; charset=UTF-8").POST(BodyPublishers.ofString(DATA)).build();

			final HttpResponse<Publisher<List<ByteBuffer>>> httpResponse = httpClient.send(httpRequest,
					BodyHandlers.ofPublisher());
			Utils.sleepMillis(10);
			server.stop(0);

			System.out.printf("receiveResponseUsingPublisher(): server stopped, response status code[%s]%n%n",
					httpResponse.statusCode());
			httpResponse.body().subscribe(new SubscriberImplForByteBufferList());
		} catch (IOException | InterruptedException e) {
			System.out.printf("receiveResponseUsingPublisher(): exception[%s]%n", e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("- ".repeat(50));
	}

	/**
	 * Start the HTTP server.
	 * 
	 * @return the server
	 * @throws IOException if an I/O error occurs
	 */
	private static HttpServer startServer() throws IOException {

		final HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
		server.createContext("/", arg -> handle(arg));
		server.start();
		Utils.sleepMillis(100);
		System.out.printf("startServer(): uri[%s]%n", LOCAL_URI);
		return server;
	}

	/**
	 * Handles the response on the HTTP server.
	 * 
	 * @param httpExchange the HTTP exchange
	 * @throws IOException if an I/O error occurs
	 */
	private static void handle(HttpExchange httpExchange) throws IOException {

		String reqText = null;
		try (InputStream inputStream = httpExchange.getRequestBody()) {
			reqText = new String(inputStream.readAllBytes());
		}
		final String respText = new StringBuilder(reqText).reverse().toString();
		final byte[] bytes = respText.getBytes(StandardCharsets.UTF_8);

		httpExchange.getRequestHeaders().entrySet().stream()
				.forEach(entry -> httpExchange.getResponseHeaders().put(entry.getKey(), entry.getValue()));
		httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, bytes.length);

		try (OutputStream outputStream = httpExchange.getResponseBody()) {
			outputStream.write(bytes);
		}
		httpExchange.close();
		System.out.printf("handle(): received request[%s], sent response[%s]%n", reqText, respText);
	}
}
