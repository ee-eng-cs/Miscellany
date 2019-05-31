package kp.processes;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import kp.utils.Utils;

/**
 * The main class for process starting.<br>
 * It runs the java launcher in a source-file mode.<br>
 * That java command launches a Java application
 * <b>SingleFileSourceCodeProgram</b>.
 */
public class MainForProcesses {

	private static final String DRY_RUN_OPTION = false ? "--dry-run" : "";

	private static final String JAVA_LAUNCHER = Paths.get(System.getProperty("java.home"), "bin", "java.exe")
			.toString();
	private static final String JAVA_PROGRAM = "kp\\processes\\SingleFileSourceCodeProgram.java";
	private static final File WORKING_DIRECTORY = Paths.get("src", "main", "java").toFile();

	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		showProcessInfo(ProcessHandle.current());

		final ProcessBuilder processBuilder = new ProcessBuilder(JAVA_LAUNCHER, DRY_RUN_OPTION, JAVA_PROGRAM);
		processBuilder.redirectErrorStream(true);
		processBuilder.directory(WORKING_DIRECTORY);
		try {
			final Process process = processBuilder.start();
			showProcessInfo(process.toHandle());
			showProcessOutput(process);
			process.waitFor();
			showProcessInfo(process.toHandle());
			final CompletableFuture<ProcessHandle> futureOnExit = process.toHandle().onExit();
			futureOnExit.thenAccept(
					handle -> System.out.printf("The process has terminated, process ID[%d]%n", handle.pid()));
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("- ".repeat(50));
	}

	/**
	 * Shows the process info.
	 * 
	 * @param processHandle the process handle
	 */
	private static void showProcessInfo(ProcessHandle processHandle) {

		/*-
		 * Methods 'info.arguments()' and 'info.commandLine()' return empty results.
		 * That values are not provided by the native code.
		 */
		final ProcessHandle.Info info = processHandle.info();
		System.out.printf("Process info: process ID[%d], user[%s], command[%s]%n", processHandle.pid(),
				info.user().orElse(""), info.command().orElse(""));
		System.out.printf("\t      is alive[%5s], %s, %s%n%n", processHandle.isAlive(),
				Utils.formatInstant("start time", info.startInstant()),
				Utils.formatElapsed("total CPU time", info.totalCpuDuration()));
	}

	/**
	 * Shows the process output.
	 * 
	 * @param process the process
	 * @throws IOException if an I/O error occurs
	 */
	private static void showProcessOutput(Process process) throws IOException {

		System.out.println("⯆".repeat(50));
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
			String line = "";
			while (Objects.nonNull(line = reader.readLine())) {
				System.out.println(line);
			}
		}
		System.out.println("⯅".repeat(50));
	}
}