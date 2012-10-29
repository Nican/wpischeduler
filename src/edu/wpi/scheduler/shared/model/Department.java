package edu.wpi.scheduler.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Department implements Serializable{
	
	public ScheduleDB scheduleDB;
	public String abbreviation;
	public String name;
	public List<Course> courses = new ArrayList<Course>();
	
	public Department(ScheduleDB scheduleDB){
		this.scheduleDB = scheduleDB;
	}
	
	public Department(){
		
	}
	
}
