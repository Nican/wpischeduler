package edu.wpi.scheduler.client.controller;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.scheduler.shared.model.Course;
import edu.wpi.scheduler.shared.model.Section;

public class SectionProducer {
	Course course;
	
	public SectionProducer(Course course) {
		this.course = course;
	}

	public Course getCourse() {
		return course;
	}
	
	public List<Section> getSections(){
		List<Section> sections = new ArrayList<Section>();
		
		for( Section section : this.course.sections ){
			if( acceptSection(section))
				sections.add(section);
		}
		
		return sections;
	}
	
	protected boolean acceptSection( Section section ){
		//TODO: Add term/class/professor checking
		return true;
	}
	
	
	
}
