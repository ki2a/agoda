package com.agoda.app;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.agoda.task.Task;
import com.agoda.utils.FileUtils;
import com.agoda.utils.TaskLocator;

/**
 * This is a service class to process URLs for file downnload
 *
 * @author Akash
 */
public class TaskService {

	private static String tmpFileLocation = "C:/temp";

	private static String outputFileLocation = "C:/Sky/Projects/log/";
	
	private static final String resource = "app.properties";

	private static Properties properties;

	static {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		properties = new Properties();
		try (InputStream stream = loader.getResourceAsStream(resource)) {
			properties.load(stream);
			setOutputFileLocation(properties.getProperty("OutputFileLocation"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.print("Unable to load properties" + resource);
			System.exit(0);
		}
	}

	/**
	 * TaskService process
	 * This will use executor service to process the task
	 * in asynchronous way.
	 * 
	 * @param input      is list of URI for download
	 */
	public void process(String input) {

		List<Task> tasks = new ArrayList<>();
		List<Future<Task>> futures = new ArrayList<>();
		URI uri = null;
		ExecutorService service = Executors.newCachedThreadPool();
		try {
			StringTokenizer tokens = new StringTokenizer(input, ",");
			while (tokens.hasMoreElements()) {
				uri = new URI(tokens.nextToken());
				Task task = TaskLocator.getTask(uri.getScheme(), uri);
				if (task == null) {
					System.out.println("Protocol " + uri.getScheme() + " is not supported");
					continue;
				}
				if(uri.getPort() != -1) {
					task.setPort(uri.getPort());
				}
				task.setTmpFile(new File(tmpFileLocation + uri.getPath()));
				tasks.add(task);
			}
			for (Task task : tasks) {
				futures.add(service.submit(task));
			}
			for (Future<Task> future : futures) {
				try {
					Task task = future.get(10, TimeUnit.MINUTES);
					if (task.isCompleted()) {
						boolean isCompleted = FileUtils.writeFile(task.getTmpFile(),
								new File(getOutputFileLocation() + task.getUri().getPath()));
						System.out
								.println("File download from URL: " + task.getUri().toString() + " is " + isCompleted);
					} else {
						System.out.println("File download fails from URL: " + task.getUri().toString());
					}
				} catch (TimeoutException e) {
					e.printStackTrace();
				}

			}
			service.shutdown();
		} catch (URISyntaxException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	public static String getOutputFileLocation() {
		return outputFileLocation;
	}

	public static void setOutputFileLocation(String outputFileLocation) {
		TaskService.outputFileLocation = outputFileLocation;
	}
}
