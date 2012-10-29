package edu.wpi.scheduler.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;


public enum PeriodType implements IsSerializable {
	LECTURE("Lecture"),
	WEB("Web"),
	LABORATORY("Lab"), 
	SEMINAR("Seminar"), 
	CONFERENCE("Conference"),
	BLENDED_LEARNING("Blended Learning"),
	VIDEO_TYPE("Video tape"),
	PRACTICUM("Practicum"),
	Other("Other")
	;
	
	private final String name;
	
	PeriodType(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
}
