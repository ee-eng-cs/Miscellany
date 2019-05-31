package kp.files;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.PropertyResourceBundle;
import java.util.function.Consumer;

/**
 * Reading the property files.
 *
 */
public class PropertyFiles {

	/**
	 * Reads properties.
	 * 
	 */
	public static void readProperties() {

		System.out.printf("▼▼▼ properties from file[%s] ▼▼▼%n", Constants.EXAMPLE_PROPERTIES_PATH);
		try (InputStream inputStream = Files.newInputStream(Constants.EXAMPLE_PROPERTIES_PATH)) {
			final PropertyResourceBundle properties = new PropertyResourceBundle(inputStream);
			final Consumer<String> action = key -> System.out.printf("key[%s], value[%s]%n", key,
					properties.getString(key));
			properties.getKeys().asIterator().forEachRemaining(action);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("- ".repeat(50));
	}
}