package kp.web;

import kp.web.httpclient.ClientLauncher;
import kp.web.httpserver.ServerStarterAndDesktopLauncher;
import kp.web.sockets.SocketSecurityType;
import kp.web.sockets.SocketsLauncher;

/*-
 Launch only one of three possibilities: or 'SOCKETS', or 'HTTP_CLIENT', or 'HTTP_SERVER'.
 
 Running 'SOCKETS' and 'HTTP_CLIENT' together causes the exception:
	java.io.IOException: PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException:
 			unable to find valid certification path to requested target
 			
 After launch the HTTP server is not stopped. 
*/
/**
 * The main launcher for web applications research.
 *
 */
public class MainForWeb {

	private static final boolean SOCKETS = !true;
	private static final boolean HTTP_CLIENT = !true;
	private static final boolean HTTP_SERVER = true;

	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		if (SOCKETS) {
			if (true) {
				SocketsLauncher.processLoop(SocketSecurityType.INSECURE);
			}
			if (true) {
				SocketsLauncher.processLoop(SocketSecurityType.SECURE);
			}
		} else if (HTTP_CLIENT) {
			if (true) {
				ClientLauncher.readSynchronously();
			}
			if (true) {
				ClientLauncher.readSynchronouslyNotFound();
			}
			if (true) {
				ClientLauncher.readAsynchronously();
			}
		} else if (HTTP_SERVER) {
			ServerStarterAndDesktopLauncher.launch();
		}
	}
}
