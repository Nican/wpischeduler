package edu.wpi.scheduler.client.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.wpi.scheduler.shared.model.Course;
import edu.wpi.scheduler.shared.model.Section;
import edu.wpi.scheduler.shared.model.Term;

public class SectionProducer {
	Course course;

	List<Section> deniedSections = new ArrayList<Section>();
	
	private StudentSchedule schedule;

	public SectionProducer(StudentSchedule schedule, Course course) {
		this.schedule = schedule;
		this.course = course;
	}

	public Course getCourse() {
		return course;
	}

	public boolean isTermDenied(Term term) {
		for (Section section : course.sections) {
			if( section.getTerms().contains(term) && !isSectionDenied(section))
				return false;			
		}
		
		return true;		
	}
	
	public boolean isSectionDenied(Section section){
		return deniedSections.contains(section);
	}

	public void denyTerm(Term term) {
		boolean hasChange = false;
		
		for (Section section : course.sections) {
			List<Term> sectionTerms = section.getTerms();

			if (sectionTerms.contains(term) && !deniedSections.contains(section) ) {
				deniedSections.add(section);
				hasChange = true;
			}		
			
		}
		
		if( hasChange )
			schedule.courseUpdated(this.course);
	}

	public void removeDenyTerm(Term term) {	
		boolean hasChange = false;
		Iterator<Section> sectionIterator = deniedSections.iterator();
		
		while(sectionIterator.hasNext()){
			if( sectionIterator.next().getTerms().contains(term)){
				sectionIterator.remove();
				hasChange = true;
			}	
		}
		
		if( hasChange )
			schedule.courseUpdated(this.course);
	}
	
	public void denySection(Section section){
		if( !deniedSections.contains(section) ){
			deniedSections.add(section);
			schedule.courseUpdated(this.course);
		}
	}
	
	public void removeDenySection(Section section){
		if( deniedSections.contains(section) ){
			deniedSections.remove(section);
			schedule.courseUpdated(this.course);
		}
	}

	public List<Section> getSections() {
		List<Section> sections = new ArrayList<Section>();

		for (Section section : this.course.sections) {
			if (!isSectionDenied(section))
				sections.add(section);
		}

		return sections;
	}
}
