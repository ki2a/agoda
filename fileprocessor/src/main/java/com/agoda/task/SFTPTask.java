package com.agoda.task;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.ProxyHTTP;
import com.jcraft.jsch.Session;

/**
 * This is a Callable task class to download file from SFTP URLs
 *
 * @see java.lang.Object
 * @author Akash Kumar
 */
public class SFTPTask extends Task {

	public SFTPTask(URI uri) {
		super(uri);
		setPort(22);
	}
	/**
	 * SFTPTask call method
	 * @return a <code> Task </code> object
	 * @throws Exception in case of download fails
	 */
	@Override
	public Task call() throws Exception {

		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		BufferedOutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(getUser(), getUri().getHost(), getPort());
			session.setPassword(getPassword());
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			isSftpProxyEnabled(session);
			session.connect(getTimeOut());
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;
			inputStream = channelSftp.get(getUri().getPath());
			outputStream = new BufferedOutputStream(new FileOutputStream(getTmpFile()));

			byte[] byteArray = new byte[4096];
			int byteRead = -1;
			while ((byteRead = inputStream.read(byteArray)) != -1) {
				outputStream.write(byteArray, 0, byteRead);
			}
			setCompleted(true);
		}finally {
			if(outputStream != null) {
				outputStream.close();
			}
			if(inputStream != null) {
				inputStream.close();
			}
			if (channelSftp != null) {
				channelSftp.exit();
			}
			if (channel != null) {
				channel.disconnect();
			}
			if (session != null) {
				session.disconnect();
			}
		}
		return this;
	}

	/**
	 * 
	 * @param session
	 */
	private void isSftpProxyEnabled(Session session) {
		// Fetching the sftp proxy flag set as part of the properties file
		boolean isSftpProxyEnabled = Boolean.valueOf("<sftp.proxy.enable>");
		// Validating if proxy is enabled to access the sftp
		if (isSftpProxyEnabled) {
			// Setting host and port of the proxy to access the SFTP
			session.setProxy(new ProxyHTTP("<sftp.proxy.host>", getPort()));
		}
		System.out.println("Proxy status: " + isSftpProxyEnabled);
	}
}
