package com.agoda.task;

import java.io.File;
import java.net.URI;
import java.util.concurrent.Callable;

/**
 * This is a model callable class
 *
 * @see java.lang.Object
 * @author Akash Kumar
 */
public abstract class Task implements Callable<Task> {
	
	private URI uri;

	private int port = 21;

	private String user = "demo";

	private String password = "password";

	private int timeOut = 10000;

	private int readTimeOut = 10000;
	
	private File tmpFile;
	
	private boolean isCompleted;
	
	public Task(URI uri) {
		this.uri = uri;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public int getReadTimeOut() {
		return readTimeOut;
	}

	public void setReadTimeOut(int readTimeOut) {
		this.readTimeOut = readTimeOut;
	}

	public File getTmpFile() {
		return tmpFile;
	}

	public void setTmpFile(File tmpFile) {
		this.tmpFile = tmpFile;
	}

	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
		setPort(uri.getPort());
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

}
