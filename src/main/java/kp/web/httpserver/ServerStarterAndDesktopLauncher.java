package kp.web.httpserver;

import java.awt.Desktop;
import java.net.URI;

/**
 * The HTTP server starter and the default browser launcher.
 *
 */
public interface ServerStarterAndDesktopLauncher {
	/**
	 * Launches the HTTP server and the default browser.
	 * 
	 */
	public static void launch() {

		if (!Desktop.isDesktopSupported()) {
			System.err.println("Desktop not supported!");
			System.exit(1);
		}
		try {
			WebServer.startServer();
			Desktop.getDesktop().browse(new URI("http://localhost:8080"));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}