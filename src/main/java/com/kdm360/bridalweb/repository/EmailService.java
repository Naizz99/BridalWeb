package com.kdm360.bridalweb.repository;

import com.kdm360.bridalweb.model.Email;

public interface EmailService {

	 void sendMail(Email email);
	
	 void sendMailWithAttachment(Email email);
}
