package edu.wpi.scheduler.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** 
 * Department offers a list of courses
 */
@SuppressWarnings("serial")
public class Department implements Serializable{
	
	/** Reference to SchedDB this department is apart of */
	public ScheduleDB scheduleDB;
	/** Department abbreviation; e.g. CS, ECE */
	public String abbreviation;
	/** Department name; e.g. Computer Science */
	public String name;
	/** The list of courses the department offers */
	public List<Course> courses = new ArrayList<Course>();
	
	/**
	 * Construct a new department with a reference to the schedDB it is apart of
	 * @param scheduleDB database this department is apart of
	 */
	public Department(ScheduleDB scheduleDB){
		this.scheduleDB = scheduleDB;
	}
	
	/**
	 * Default department constructor without DB reference
	 * Needed for the GWT RPC to deserialize properly
	 */
	public Department(){}
}
