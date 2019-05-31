package kp.web.httpclient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/*-
 * HTTP/2 works only over TLS secure channel. 
 * 
 * HttpClient:
 * the default version is HTTP/2
 * the default redirection policy is NEVER
 * 
 * HttpRequest:
 * the default method is GET
 */
/**
 * The HTTP client launcher.
 *
 */
public class ClientLauncher {

	private static final URI EXAMPLE_URI = URI.create("https://example.org/");
	private static final URI EXAMPLE_404_URI = URI.create("https://www.google.com/404");

	/**
	 * Reads synchronously with status code '200'.
	 * 
	 */
	public static void readSynchronously() {
		readSynchronously(EXAMPLE_URI);
	}

	/**
	 * Reads synchronously with status code '404'.
	 * 
	 */
	public static void readSynchronouslyNotFound() {
		readSynchronously(EXAMPLE_404_URI);
	}

	/**
	 * Reads synchronously.
	 * 
	 * @param uri the Uniform Resource Identifier reference
	 */
	private static void readSynchronously(URI uri) {

		final HttpClient httpClient = HttpClient.newBuilder().build();
		final HttpRequest httpRequest = HttpRequest.newBuilder(uri).build();
		HttpResponse<String> httpResponse = null;
		try {
			httpResponse = httpClient.send(httpRequest, BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.printf("Synchronous response status code[%s]%n", httpResponse.statusCode());
		System.out.println("First 2 lines from synchronous response body:");
		httpResponse.body().lines().limit(2).forEach(System.out::println);
		System.out.println("- ".repeat(50));
	}

	/**
	 * Reads asynchronously.
	 * 
	 */
	public static void readAsynchronously() {

		final HttpClient httpClient = HttpClient.newBuilder().build();
		final HttpRequest httpRequest = HttpRequest.newBuilder(EXAMPLE_URI).build();
		System.out.println("First 2 lines from asynchronous response body:");
		final Consumer<String> consumer = arg -> arg.lines().limit(2).forEach(System.out::println);

		final CompletableFuture<Void> future = httpClient/*-*/
				.sendAsync(httpRequest, BodyHandlers.ofString())/*-*/
				.thenApply(HttpResponse::body)/*-*/
				.exceptionally(arg -> arg.getMessage())/*-*/
				.thenAcceptAsync(consumer);
		CompletableFuture.allOf(future).join();
		System.out.println("- ".repeat(50));
	}
}
