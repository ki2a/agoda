package com.agoda.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.Properties;

import com.agoda.task.Task;

/**
 * This is a service locator class to locate the required 
 * service to process the request
 *
 * @see java.lang.Object
 * @author Akash Kumar
 */
public class TaskLocator {

	private static final String resource = "handler.properties";

	private static Properties properties;

	static {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		properties = new Properties();
		try (InputStream stream = loader.getResourceAsStream(resource)) {
			properties.load(stream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.print("Unable to load properties" + resource);
			System.exit(0);
		}
	}

	public static Task getTask(String type, URI uri) {
		String className = (String) properties.get(type);
		Task task = null;
		if (className != null) {
			try {
				Constructor<?> ctr = Class.forName(className).getConstructor(URI.class);
				Object obj = ctr.newInstance(new Object[] { uri });
				task = (Task) obj;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return task;
	}

}
