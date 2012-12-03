package edu.wpi.scheduler.client.controller;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.scheduler.shared.model.Course;
import edu.wpi.scheduler.shared.model.Section;
import edu.wpi.scheduler.shared.model.Term;

public class SectionProducer {
	Course course;
	List<Term> deniedTerms = new ArrayList<Term>();
	private StudentSchedule schedule;
	
	public SectionProducer(StudentSchedule schedule, Course course) {
		this.schedule = schedule;
		this.course = course;
	}

	public Course getCourse() {
		return course;
	}
	
	public boolean isTermDenied( Term term ){
		return deniedTerms.contains(term);
	}
	
	public void denyTerm( Term term ){
		if( !deniedTerms.contains(term) ){
			deniedTerms.add(term);
			schedule.courseUpdated( this.course );
		}
	}
	
	public void removeDenyTerm( Term term ){
		deniedTerms.remove(term);
		
		schedule.courseUpdated( this.course );
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
		
		if( deniedTerms.contains( Term.getTermByName(section.term) ) ) 
				return false;
				
		return true;
	}
	
	
	
}