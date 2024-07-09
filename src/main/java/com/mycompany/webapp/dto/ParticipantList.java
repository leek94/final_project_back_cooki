package com.mycompany.webapp.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ParticipantList {
	private String mid;
	private String mname;
	private String ctitle;
	private Date cdday;
	private String cstarttime;
	private boolean isActive;
	private char isParticipant;
	
}
