package edu.wpi.scheduler.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Types of periods that are apart of sections
 */
public enum PeriodType implements IsSerializable {
	
	/** Static period types */
	LECTURE("Lecture"),
	WEB("Web"),
	LABORATORY("Lab"), 
	SEMINAR("Seminar"), 
	CONFERENCE("Conference"),
	BLENDED_LEARNING("Blended Learning"),
	VIDEO_TYPE("Video tape"),
	PRACTICUM("Practicum"),
	Other("Other");
	
	/** Name of the period type */
	private final String name;
	
	/**
	 * Construct a new period type; only used locally since types are static
	 * @param name Name of new period type
	 */
	PeriodType(String name){
		this.name = name;
	}
	
	/**
	 * Getter
	 * @return name of the period type
	 */
	public String getName(){
		return name;
	}
}
