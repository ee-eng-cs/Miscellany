package kp.web.sockets.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.Phaser;

import kp.utils.Utils;
import kp.web.sockets.SocketsLauncher;
import kp.web.sockets.TheSockets;

/**
 * The insecure sockets.
 *
 */
public class InsecureSockets extends TheSockets {

	/**
	 * Constructor.
	 * 
	 */
	public InsecureSockets() {

		super();
		final Instant start = Instant.now();
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException ex) {
			ex.printStackTrace();
			System.exit(1);
		}
		final Instant finish = Instant.now();
		System.out.printf("InsecureSockets(): server socket created, host[%s], port[%d], %s%n", HOST, PORT,
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
		final Socket socket;
		final PrintWriter writer;
		final BufferedReader reader;
		final ArrayList<Instant> instantList = new ArrayList<>();
		instantList.add(Instant.now());// ◄ place '0'
		try {
			socket = serverSocket.accept();
			instantList.add(Instant.now());// ◄ place '1'
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			try (socket; reader; writer) {
				String line = null;
				while ((line = reader.readLine()) != null && !line.isEmpty()) {
					if (SocketsLauncher.PROLIX) {
						System.out.printf("runServer(): number[%d], content...[%s], received server%n", number,
								line.substring(line.length() - 15));
					}
				}
				writer.println(content);
				writer.flush();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			System.exit(1);
		}
		instantList.add(Instant.now());// ◄ place '2'
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
		final Socket socket;
		final BufferedReader reader;
		final PrintWriter writer;
		final ArrayList<Instant> instantList = new ArrayList<>();
		instantList.add(Instant.now());// ◄ place '0'
		try {
			socket = new Socket(HOST, PORT);
			instantList.add(Instant.now());// ◄ place '1'
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			try (socket; reader; writer) {
				writer.printf("%s%n%n", content);
				writer.flush();
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
		instantList.add(Instant.now());// ◄ place '2'
		if (SocketsLauncher.PROLIX) {
			System.out.printf("runClient(): number[%d], %s%n", number, Utils.calculateElapsedTimes(instantList));
		}
		endPhaser.arriveAndDeregister();
		return null;
	}

}
