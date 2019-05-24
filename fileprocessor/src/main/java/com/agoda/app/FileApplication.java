package com.agoda.app;

public class FileApplication {

	/**
	 * FileApplication main method
	 * 
	 * @param args      is list of java runtime argument
	 */
	public static void main(String... args) {
		if (args.length != 1) {
			System.out.println("Usage URLs");
			System.exit(0);
		}
		TaskService service = new TaskService();
		service.process(args[0]);
	}
}
