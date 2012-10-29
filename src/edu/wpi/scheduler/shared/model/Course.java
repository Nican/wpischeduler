package edu.wpi.scheduler.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
 
@SuppressWarnings("serial")
public class Course implements Serializable {
	
	public Course(Department department) {
		this.department = department;
	}
	
	public Course(){
	}
	
	public Department department;
	public String name;
	public String number;
	public List<Section> sections = new ArrayList<Section>();
	
}
