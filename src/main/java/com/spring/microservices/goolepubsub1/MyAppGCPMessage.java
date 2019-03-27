package com.spring.microservices.goolepubsub1;

public class MyAppGCPMessage {

	private String name;
	private String emailid;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailid() {
		return emailid;
	}

	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}

	@Override
	public String toString() {
		return "MyAppGCPMessage [name=" + name + ", emailid=" + emailid + "]";
	}

	

}
