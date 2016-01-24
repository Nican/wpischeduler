package edu.wpi.scheduler.shared.model;

import java.io.Serializable;
import java.util.HashSet;

/**
 * A Period has the days of the week it is scheduled for
 * Note: No period can be offered more than one day a week at different times
 */
@SuppressWarnings("serial")
public class Period implements Serializable{
	
	/** Section this period is apart of */
	public Section section;
	/** Period type; e.g. lecture, lab */
	public String type;
	/** Professor of this period */
	public String professor;
	/** Days of the week this period is scheduled for */
	public HashSet<DayOfWeek> days = new HashSet<DayOfWeek>();
	/** Start time of the period */
	public Time startTime;
	/** End time of the period */
	public Time endTime;
	/** Location period is taught; e.g. FL320 */
	public String location;
	
	/**
	 * Construct a period with a reference to the section it is apart of
	 * @param section Section the period belongs to
	 */
	public Period(Section section) {
		this.section = section;
	}
	
	/**
	 * Default period constructor
	 */
	public Period(){}
}
