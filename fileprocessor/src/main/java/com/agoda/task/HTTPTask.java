package com.agoda.task;

import java.net.URI;

import org.apache.commons.io.FileUtils;

/**
 * This is a Callable task class to download file from HTTP URLs
 *
 * @see java.lang.Object
 * @author Akash Kumar
 */
public class HTTPTask extends Task {

	public HTTPTask(URI uri) {
		super(uri);
	}

	/**
	 * HTTPTask call method
	 * @return a <code> Task </code> object
	 * @throws Exception in case of download fails
	 */
	@Override
	public Task call() throws Exception {
		FileUtils.copyURLToFile(getUri().toURL(), getTmpFile(), getTimeOut(), getReadTimeOut());
		setCompleted(true);
		return this;
	}
}
