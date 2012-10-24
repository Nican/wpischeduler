package edu.wpi.scheduler.client.model;

import java.util.ArrayList;
import java.util.List;

public class Department {
	
	public transient ScheduleDB scheduleDB;
	public String abbreviation;
	public String name;
	public List<Course> courses = new ArrayList<Course>();
	
	public Department(ScheduleDB scheduleDB){
		this.scheduleDB = scheduleDB;
	}
	
}
