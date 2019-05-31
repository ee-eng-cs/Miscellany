package a.latest.java.subjects;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import kp.utils.Utils;

/**
 * About system and environment.
 *
 */
public interface About {
	/**
	 * Shows elapsed time.
	 * 
	 */
	public static void showElapsed() {

		final Instant startMethod = Instant.now();
		final Instant startProcess = ProcessHandle.current().info().startInstant().get();
		System.out.printf("Start time of the process[%s      ]%n", startProcess);
		System.out.printf("Start time of the  method[%s]%n", startMethod);
		System.out.println(Utils.formatElapsed(startProcess, startMethod));
		System.out.println("- ".repeat(50));
	}

	/**
	 * Lists system properties.
	 * 
	 */
	public static void listSystemProperties() {

		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try (PrintWriter printWriter = new PrintWriter(outputStream)) {
			System.getProperties().list(printWriter);
		}
		System.out.println("System properties listing fragment:");
		final List<String> systemPropertiesList = outputStream.toString().lines().collect(Collectors.toList());
		if (systemPropertiesList.size() > 4) {
			System.out.printf("%s%n%s%n%s%n[ ... ]%n%s%n", systemPropertiesList.get(0), systemPropertiesList.get(1),
					systemPropertiesList.get(2), systemPropertiesList.get(systemPropertiesList.size() - 1));
		}
		System.out.println("- ".repeat(50));
	}

	/**
	 * Shows environment.
	 * 
	 */
	public static void showEnvironment() {

		System.out.printf("Java version[%s], native process ID[%d]%n", System.getProperty("java.version"),
				ProcessHandle.current().pid());
		System.out.printf("Name representing the running JVM  [%s]%n", ManagementFactory.getRuntimeMXBean().getName());
		final ProcessHandle.Info processInfo = ProcessHandle.current().info();
		System.out.printf("Process arguments  %s, process command line[%s], process total CPU time[%dms]%n",
				Arrays.asList(processInfo.arguments().orElse(new String[] { "" })),
				processInfo.commandLine().orElse(""), processInfo.totalCpuDuration().get().toMillis());

		final String args = ManagementFactory.getRuntimeMXBean().getInputArguments().toString();
		System.out.printf("Input arguments passed to the JVM:%n%s%n", Utils.breakLine(args, 100));
		System.out.println("Classloaders:");
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		while (true) {
			if (Objects.isNull(classLoader)) {
				System.out.println("   BOOTSTRAP CLASSLOADER");
				break;
			} else {
				System.out.printf("   %s%n", classLoader.getClass().getName());
			}
			classLoader = classLoader.getParent();
		}
		System.out.printf("This class[%s] is loaded from location[%s]%n%n", About.class.getSimpleName(),
				About.class.getProtectionDomain().getCodeSource().getLocation());

		System.out.printf("Maximum amount of memory that the JVM will attempt to use[%s]%n",
				Utils.formatNumber(Runtime.getRuntime().maxMemory()));
		System.out.printf("Total amount of memory in JVM[%s], amount of free memory in JVM[%s]%n%n",
				Utils.formatNumber(Runtime.getRuntime().totalMemory()),
				Utils.formatNumber(Runtime.getRuntime().freeMemory()));

		final OperatingSystemMXBean mxBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
		System.out.printf("Operating system: architecture[%s], name[%s], version[%s]%n", mxBean.getArch(),
				mxBean.getName(), mxBean.getVersion());
		System.out.printf(
				"Available processors[%s], object name[%s], the system load average for the last minute[%s]%n%n",
				mxBean.getAvailableProcessors(), mxBean.getObjectName(), mxBean.getSystemLoadAverage());

		System.out.printf("Current Working Directory[%s]%n", System.getProperty("user.dir"));
		System.out.printf("Current relative path ( )[%s]%n", Paths.get("").toAbsolutePath().normalize());
		System.out.printf("Current relative path (.)[%s]%n", Paths.get(".").toAbsolutePath().normalize());
		System.out.println("- ".repeat(50));
	}

	/**
	 * Shows file stores.
	 * 
	 */
	public static void showFileStores() {

		try {
			for (FileStore store : FileSystems.getDefault().getFileStores()) {
				printFileStore(store);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("- ".repeat(50));
	}

	/**
	 * Prints file store.
	 * 
	 * @param store the file store
	 * @throws IOException if an I/O error occurs
	 */
	private static void printFileStore(FileStore store) throws IOException {

		final long total = store.getTotalSpace() / 1_073_741_824;
		final long used = (store.getTotalSpace() - store.getUnallocatedSpace()) / 1_073_741_824;
		final long available = store.getUsableSpace() / 1_073_741_824;
		System.out.printf("Filesystem[%10s], total size[%6s]GB, used size[%4s]GB, available size[%6s]GB%n", store,
				Utils.formatNumber(total), Utils.formatNumber(used), Utils.formatNumber(available));
	}
}