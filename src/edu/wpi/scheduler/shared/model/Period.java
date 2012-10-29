package edu.wpi.scheduler.shared.model;

import java.io.Serializable;
import java.util.HashSet;

@SuppressWarnings("serial")
public class Period implements Serializable{
	
	
	public Period(Section section) {
		this.section = section;
	}
	
	public Period(){
	}
	
	public Section section;
	public PeriodType type;
	public String professor;
	public HashSet<DaysOfWeek> days = new HashSet<DaysOfWeek>();
	public String startTime;
	public String endTime;
	public String location;
	
}
