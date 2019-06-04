package com.revenat.ishop.infrastructure.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PreDestroy;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.revenat.ishop.infrastructure.service.NotificationService;

/**
 * This implementation of the {@link NotificationService} asynchronously sends
 * email notifications.
 * 
 * @author Vitaly Dragun
 *
 */
@Service
public class AsyncEmailNotificationService implements NotificationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncEmailNotificationService.class);
	private final ExecutorService executorService;
	
	private String fromEmail;
	private String sendTryAttempts;
	private final JavaMailSender mailSender;

	@Autowired
	public AsyncEmailNotificationService(JavaMailSender mailSender,
			@Value("${email.smtp.fromEmail}") String fromEmail,
			@Value("${email.smtp.sendTryAttempts}") String sendTryAttempts) {
		this.executorService = Executors.newCachedThreadPool();
		this.mailSender = mailSender;
		this.fromEmail = fromEmail;
		this.sendTryAttempts = sendTryAttempts;
	}
	

	@Override
	public void sendNotification(String recipientEmail, String title, String content) {
		executorService.submit(new EmailItem(recipientEmail, title, content));
	}

	@PreDestroy
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
				MimeMailMessage msg = buildMessage(subject, content, recipientEmail);
				mailSender.send(msg.getMimeMessage());
				LOGGER.info("Email with subject '{}' and content '{}' has been sent to {}",
						subject, content, recipientEmail);
			} catch (MailException e) {
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

		private MimeMailMessage buildMessage(String msgSubject, String msgContent, String email) throws UnsupportedEncodingException, MessagingException {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), false);
			messageHelper.setSubject(msgSubject);
			messageHelper.setTo(new InternetAddress(email, ""));
			messageHelper.setFrom(fromEmail, "");
			messageHelper.setText(msgContent);
			return new MimeMailMessage(messageHelper);
		}
	}
}
