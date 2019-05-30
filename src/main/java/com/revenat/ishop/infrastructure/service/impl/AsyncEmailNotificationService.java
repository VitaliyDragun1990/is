package com.revenat.ishop.infrastructure.service.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revenat.ishop.infrastructure.framework.annotation.di.Component;
import com.revenat.ishop.infrastructure.framework.annotation.di.OnDestroy;
import com.revenat.ishop.infrastructure.framework.annotation.di.Value;
import com.revenat.ishop.infrastructure.service.NotificationService;

/**
 * This implementation of the {@link NotificationService} asynchronously sends
 * email notifications.
 * 
 * @author Vitaly Dragun
 *
 */
@Component
public class AsyncEmailNotificationService implements NotificationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncEmailNotificationService.class);
	private final ExecutorService executorService;
	
	@Value("email.fromEmail")
	private String fromEmail;
	@Value("email.sendTryAttempts")
	private String sendTryAttempts;
	@Value("email.smtp.server")
	private String server;
	@Value("email.smtp.port")
	private String port;
	@Value("email.smtp.username")
	private String username;
	@Value("email.smtp.password")
	private String password;

	public AsyncEmailNotificationService() {
		this.executorService = Executors.newCachedThreadPool();
	}

	@Override
	public void sendNotification(String recipientEmail, String title, String content) {
		executorService.submit(new EmailItem(recipientEmail, title, content));
	}

	@OnDestroy
	@Override
	public void shutdown() {
		executorService.shutdown();
	}

	private class EmailItem implements Runnable {
		private final String recipientEmail;
		private final String subject;
		private final String content;
		private int tryAttempts;

		EmailItem(String email, String subject, String content) {
			this.recipientEmail = email;
			this.subject = subject;
			this.content = content;
			this.tryAttempts = Integer.parseInt(sendTryAttempts);
		}

		private boolean isValidTry() {
			return tryAttempts > 0;
		}

		@Override
		public void run() {
			try {
				SimpleEmail email = new SimpleEmail();
				email.setCharset("utf-8");

				email.setHostName(server);
				email.setSmtpPort(Integer.parseInt(port));
				email.setSSLCheckServerIdentity(true);
				email.setStartTLSEnabled(true);

				email.setAuthenticator(
						new DefaultAuthenticator(username,password));
				email.setFrom(fromEmail);
				email.setSubject(subject);
				email.setMsg(content);
				email.addTo(recipientEmail);
				email.send();
				LOGGER.info("Email with subject '{}' and content '{}' has been sent to {}", subject, content,
						recipientEmail);
			} catch (EmailException e) {
				LOGGER.error("Can't send email: " + e.getMessage(), e);
				tryAttempts--;
				if (isValidTry()) {
					LOGGER.info("Resend email: {}", this);
					executorService.submit(this);
				} else {
					LOGGER.error("Email has not been sent: limit of try attempts");
				}
			} catch (Exception e) {
				LOGGER.error("Error during sending email: {}", e.getMessage(), e);
			}
		}
	}
}
