package com.agoda.task;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 * This is a Callable task class to download file from FTP URLs
 *
 * @see java.lang.Object
 * @author Akash Kumar
 */
public class FTPTask extends Task {

	public FTPTask(URI uri) {
		super(uri);
	}

	/**
	 * FTPTask call method
	 * 
	 * @return a <code> Task </code> object
	 * @throws Exception in case of download fails
	 */
	@Override
	public Task call() throws Exception {
		FTPClient ftpClient = new FTPClient();
		OutputStream outputStream = null;
		InputStream inputStream = null;
		int byteRead = -1;
		boolean isSuccess = false;
		byte[] byteArray = new byte[4096];

		if (getTmpFile().exists()) {
			getTmpFile().delete();
		}
		ftpClient.connect(getUri().getHost(), getPort());
		if (getUser() != null && getPassword() != null) {
			ftpClient.login(getUser(), getPassword());
		}
		ftpClient.enterLocalPassiveMode();
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		outputStream = new BufferedOutputStream(new FileOutputStream(getTmpFile()));
		inputStream = ftpClient.retrieveFileStream(getUri().getPath());

		while ((byteRead = inputStream.read(byteArray)) != -1) {
			outputStream.write(byteArray, 0, byteRead);
		}
		isSuccess = ftpClient.completePendingCommand();
		if (isSuccess) {
			setCompleted(true);
		}
		outputStream.close();
		inputStream.close();
		return this;
	}
}
