package com.kdm360.bridalweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Email {

	private String recipient;
	private String msgBody;
	private String subject;
	private String attachment;
	
	public Email(String recipient,String subject,String msgBody){
		this.recipient = recipient;
		this.msgBody = msgBody;
		this.subject = subject;
	}
	
}

