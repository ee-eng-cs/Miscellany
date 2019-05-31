package kp.web.sockets;

import kp.web.sockets.impl.InsecureSockets;
import kp.web.sockets.impl.SecureSockets;

/**
 * The sockets factory.
 *
 */
public class TheSocketsFactory {

	/**
	 * Builds sockets. Factory method.
	 * 
	 * @param socketSecurityType the socket security type
	 * @return TheSockets
	 */
	public static TheSockets buildSockets(SocketSecurityType socketSecurityType) {

		switch (socketSecurityType) {
		case INSECURE:
			return new InsecureSockets();
		case SECURE:
			return new SecureSockets();
		default:
			return null;
		}
	}

}
