package com.revenat.ishop.infrastructure.service.impl;

import com.revenat.ishop.infrastructure.util.Checks;

final class EmailData {
	private final String fromEmail;
	private final int tryAttempts;
	private final String smtpServer;
	private final String smtpPort;
	private final String smtpUsername;
	private final String smtpPassword;

	public EmailData(String fromEmail, int tryAttempts, String smtpServer, String smtpPort,
			String smtpUsername, String smtpPassword) {
		checkParams(fromEmail, tryAttempts, smtpServer, smtpPort, smtpUsername, smtpPassword);
		this.fromEmail = fromEmail;
		this.tryAttempts = tryAttempts;
		this.smtpServer = smtpServer;
		this.smtpPort = smtpPort;
		this.smtpUsername = smtpUsername;
		this.smtpPassword = smtpPassword;
	}

	private static void checkParams(String fromEmail, int tryAttempts, String smtpServer,
			String smtpPort, String smtpUsername, String smtpPassword) {
		Checks.checkParam(fromEmail != null, "From email can not be null");
		Checks.checkParam(tryAttempts > 0, "Try attempts can not be less than 0");
		Checks.checkParam(smtpServer != null, "SMTP server can not be null");
		Checks.checkParam(smtpPort != null, "SMTP port can not be null");
		Checks.checkParam(smtpUsername != null, "SMTP username can not be null");
		Checks.checkParam(smtpPassword != null, "SMTP password can not be null");
		
	}

	public String getFromEmail() {
		return fromEmail;
	}
	
	public int getTryAttempts() {
		return tryAttempts;
	}

	public String getSmtpServer() {
		return smtpServer;
	}

	public String getSmtpPort() {
		return smtpPort;
	}

	public String getSmtpUsername() {
		return smtpUsername;
	}

	public String getSmtpPassword() {
		return smtpPassword;
	}
}
