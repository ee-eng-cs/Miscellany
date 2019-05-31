package kp.web.sockets.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.Phaser;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import kp.utils.Utils;
import kp.web.sockets.SocketsLauncher;
import kp.web.sockets.TheSockets;

/**
 * The secure sockets.
 * 
 */
public class SecureSockets extends TheSockets {

	private static final SSLServerSocketFactory SSL_SERVER_SOCKET_FACTORY = (SSLServerSocketFactory) SSLServerSocketFactory
			.getDefault();

	private static final SSLSocketFactory SSL_SOCKET_FACTORY = (SSLSocketFactory) SSLSocketFactory.getDefault();

	/**
	 * Constructor.
	 * 
	 */
	public SecureSockets() {

		super();
		final Instant start = Instant.now();
		try {
			serverSocket = (ServerSocket) SSL_SERVER_SOCKET_FACTORY.createServerSocket(PORT);
		} catch (IOException ex) {
			ex.printStackTrace();
			System.exit(1);
		}
		final Instant finish = Instant.now();
		System.out.printf("SecureSockets(): server socket created, host[%s], port[%d], %s%n", HOST, PORT,
				Utils.formatElapsed(start, finish));
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public Void runServer(Phaser endPhaser, String content, int number) {

		if (SocketsLauncher.PROLIX) {
			System.out.printf("runServer(): number[%d], start%n", number);
		}
		final SSLSocket socket;
		final PrintWriter writer;
		final BufferedReader reader;
		final ArrayList<Instant> instantList = new ArrayList<>();
		instantList.add(Instant.now());// ◄ place '0'
		try {
			socket = (SSLSocket) ((SSLServerSocket) serverSocket).accept();
			showSessionAttributes(number, socket, "server");
			instantList.add(Instant.now());// ◄ place '1'
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			try (socket; reader; writer) {
				instantList.add(Instant.now());// ◄ place '2'
				String line = null;
				while ((line = reader.readLine()) != null && !line.isEmpty()) {
					if (SocketsLauncher.PROLIX) {
						System.out.printf("runServer(): number[%d], content...[%s], received server%n", number,
								line.substring(line.length() - 15));
					}
				}
				instantList.add(Instant.now());// ◄ place '3'
				writer.println(content);
				writer.flush();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			System.exit(1);
		}
		instantList.add(Instant.now());// ◄ place '4'
		if (SocketsLauncher.PROLIX) {
			System.out.printf("runServer(): number[%d], %s%n", number, Utils.calculateElapsedTimes(instantList));
		}
		endPhaser.arriveAndDeregister();
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public Void runClient(Phaser endPhaser, String content, int number) {

		if (SocketsLauncher.PROLIX) {
			System.out.printf("runClient(): number[%d], start%n", number);
		}
		final SSLSocket socket;
		final PrintWriter writer;
		final BufferedReader reader;
		final ArrayList<Instant> instantList = new ArrayList<>();
		instantList.add(Instant.now());// ◄ place '0'
		try {
			socket = (SSLSocket) SSL_SOCKET_FACTORY.createSocket(HOST, PORT);
			showSessionAttributes(number, socket, "client");
			instantList.add(Instant.now());// ◄ place '1'
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			try (socket; reader; writer) {
				instantList.add(Instant.now());// ◄ place '2'
				socket.startHandshake();
				instantList.add(Instant.now());// ◄ place '3'
				writer.printf("%s%n%n", content);
				writer.flush();
				if (writer.checkError()) {
					System.out.println("runClient(): java.io.PrintWriter error");
					System.exit(1);
				}
				String line;
				while ((line = reader.readLine()) != null && !line.isEmpty()) {
					if (SocketsLauncher.PROLIX) {
						System.out.printf("runClient(): number[%d], content...[%s], received client%n", number,
								line.substring(line.length() - 15));
					}
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			System.exit(1);
		}
		instantList.add(Instant.now());// ◄ place '4'
		if (SocketsLauncher.PROLIX) {
			System.out.printf("runClient(): number[%d], %s%n", number, Utils.calculateElapsedTimes(instantList));
		}
		endPhaser.arriveAndDeregister();
		return null;
	}

	/**
	 * Shows the SSL session attributes.<br>
	 * It is active only for run with number 5.
	 * 
	 * @param number the number
	 * @param socket the SSL socket
	 * @param label  the label
	 */
	private void showSessionAttributes(int number, SSLSocket socket, String label) {

		if (number != 5) {
			return;
		}
		socket.addHandshakeCompletedListener(event -> {
			System.out.printf("showSessionAttributes(): protocol[%s], cipher suite[%s], %s%n",
					event.getSession().getProtocol(), event.getCipherSuite(), label);
		});
	}

}