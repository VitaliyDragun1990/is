package com.revenat.ishop.infrastructure.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@PropertySource("classpath:application.properties")
public class EmailConfig {

	@Autowired
	@Bean
	public JavaMailSender javaMailSender(Environment env) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(env.getRequiredProperty("email.smtp.server"));
		if (env.containsProperty("email.smtp.username")) {
			String usernamePlaceholder = env.getRequiredProperty("email.smtp.username");
			String username = env.getProperty(getValue(usernamePlaceholder));
			mailSender.setUsername(username);
		}
		if (env.containsProperty("email.smtp.password")) {
			String passwordPlaceholder = env.getRequiredProperty("email.smtp.password");
			String password = env.getProperty(getValue(passwordPlaceholder));
			mailSender.setPassword(password);
		}
		mailSender.setPort(env.getRequiredProperty("email.smtp.port", Integer.class));
		mailSender.setDefaultEncoding("UTF-8");
		mailSender.setJavaMailProperties(javaMailProperties());
		
		return mailSender;
	}

	private String getValue(String valuePlaceholder) {
		int startIndex = "systemProperties.".length();
		return valuePlaceholder.substring(startIndex);
	}

	private Properties javaMailProperties() {
		Properties p = new Properties();
		p.setProperty("mail.transport.protocol", "smtp");
		p.setProperty("mail.smtp.auth", "true");
		p.setProperty("mail.smtp.starttls.enable", "true");
		p.setProperty("mail.debug", "true");
		return p;
	}
}
