package edu.wpi.scheduler.client.model;

import java.util.EnumSet;

public class Period {
	
	public Period(Section section) {
		this.section = section;
	}
	
	public transient Section section;
	public PeriodType type;
	public String professor;
	public EnumSet<DaysOfWeek> days = EnumSet.noneOf(DaysOfWeek.class);
	public String startTime;
	public String endTime;
	public String location;
	
}
