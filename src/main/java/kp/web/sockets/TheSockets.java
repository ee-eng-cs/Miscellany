package kp.web.sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Objects;
import java.util.concurrent.Phaser;

/**
 * The sockets abstract class.
 *
 */
public abstract class TheSockets {

	protected static final String HOST = "127.0.0.1";

	protected static final int PORT = 12345;

	protected ServerSocket serverSocket = null;

	/**
	 * Runs the server.
	 * 
	 * @param endPhaser the end phaser
	 * @param content   the content
	 * @param number    the number
	 * @return the void
	 */
	public abstract Void runServer(Phaser endPhaser, String content, int number);

	/**
	 * Runs the client.
	 * 
	 * @param endPhaser the end phaser
	 * @param content   the content
	 * @param number    the number
	 * @return the void
	 */
	public abstract Void runClient(Phaser endPhaser, String content, int number);

	/**
	 * Closes the server socket.
	 * 
	 */
	public void closeServerSocket() {

		if (Objects.isNull(serverSocket)) {
			return;
		}
		try {
			serverSocket.close();
			serverSocket = null;
		} catch (IOException ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
}
