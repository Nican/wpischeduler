package edu.wpi.scheduler.client.model;

import java.util.ArrayList;
import java.util.List;

public class Course {
	
	public Course(Department department) {
		this.department = department;
	}
	
	transient Department department;
	public String name;
	public String number;
	public List<Section> sections = new ArrayList<Section>();
	
}
