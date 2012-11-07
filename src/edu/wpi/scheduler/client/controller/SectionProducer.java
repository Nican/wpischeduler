package edu.wpi.scheduler.client.controller;

import edu.wpi.scheduler.shared.model.Course;

public class SectionProducer {
	Course course;
	
	public SectionProducer(Course course) {
		this.course = course;
	}

	public Course getCourse() {
		return course;
	}
	
	//TODO: Add term/class/professor checking
	
	
	
	
	
	
}
