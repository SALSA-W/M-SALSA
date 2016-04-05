/**
 * Copyright 2015 Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.salsaw.msalsa.services;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.salsaw.msalsa.config.ConfigurationManager;

public class EmailSender {	
	public static final String PROP_MAIL_SMTP_AUTH = "mail.smtp.auth";
	public static final String PROP_MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
	public static final String PROP_MAIL_SMTP_HOST = "mail.smtp.host";
	public static final String PROP_MAIL_SMTP_PORT = "mail.smtp.port";

	private Session session;
	private Message message;
	private Multipart multipart;

	public EmailSender() {
		this.multipart = new MimeMultipart();
	}

	public void setSender(String username, String password) throws AddressException, MessagingException {
		this.session = getSession(username, password);
		this.message = new MimeMessage(session);
		this.message.setFrom(new InternetAddress(username));
	}

	public void addRecipient(String recipient) throws AddressException, MessagingException {
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
	}

	public void setSubject(String subject) throws MessagingException {
		message.setSubject(subject);
	}

	public void setBody(String body, String charset, String contentType) throws MessagingException {
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setText(body, charset, contentType);
		multipart.addBodyPart(messageBodyPart);		
		message.setContent(multipart);
	}

	public void send() throws MessagingException {
		// Send message
		Transport.send(message);
	}

	public void addAttachment(String filePath) throws MessagingException {
		BodyPart messageBodyPart = getFileBodyPart(filePath);
		multipart.addBodyPart(messageBodyPart);

		message.setContent(multipart);
	}

	private BodyPart getFileBodyPart(String filePath) throws MessagingException {
		BodyPart messageBodyPart = new MimeBodyPart();
		DataSource dataSource = new FileDataSource(filePath);
		messageBodyPart.setDataHandler(new DataHandler(dataSource));
		Path p = Paths.get(filePath);
		messageBodyPart.setFileName(p.getFileName().toString());

		return messageBodyPart;
	}

	private static Session getSession(String username, String password) {
		Properties properties = System.getProperties();

		properties.put(PROP_MAIL_SMTP_AUTH,
				ConfigurationManager.getInstance().getServerConfiguration().getMailSmtpAuth());
		properties.put(PROP_MAIL_SMTP_STARTTLS_ENABLE,
				ConfigurationManager.getInstance().getServerConfiguration().isMailSmtpStartTtlsEnable());
		properties.put(PROP_MAIL_SMTP_HOST,
				ConfigurationManager.getInstance().getServerConfiguration().getMailSmtpHost());
		properties.put(PROP_MAIL_SMTP_PORT,
				ConfigurationManager.getInstance().getServerConfiguration().getMailSmtpPort());

		Authenticator authenticator = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		};

		// Get the Session object.
		Session session = Session.getInstance(properties, authenticator);

		return session;
	}
}