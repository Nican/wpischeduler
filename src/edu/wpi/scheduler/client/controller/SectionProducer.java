package edu.wpi.scheduler.client.controller;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.scheduler.shared.model.Course;
import edu.wpi.scheduler.shared.model.Section;
import edu.wpi.scheduler.shared.model.Term;

public class SectionProducer {
	Course course;
	
	List<Term> deniedTerms = new ArrayList<Term>();
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
		return deniedTerms.contains(term);
	}
	
	public boolean isSectionDenied(Section section){
		return deniedSections.contains(section);
	}

	public void denyTerm(Term term) {
		if (!deniedTerms.contains(term)) {
			
			//For courses that takes more than one term, we need to add all of the terms
			for (Section section : course.sections) {
				List<Term> sectionTerms = section.getTerms();

				if (sectionTerms.contains(term)) {
					for (Term sectionTerm : sectionTerms) {
						deniedTerms.add(sectionTerm);
					}
				}
			}

			schedule.courseUpdated(this.course);
		}
	}

	public void removeDenyTerm(Term term) {		
		for (Section section : course.sections) {
			List<Term> sectionTerms = section.getTerms();

			if (sectionTerms.contains(term)) {
				for (Term sectionTerm : sectionTerms) {
					deniedTerms.remove(sectionTerm);
				}
			}
		}

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
			if (acceptSection(section))
				sections.add(section);
		}

		return sections;
	}
	
	public boolean deniesSectionByTerm( Section section ){
		return deniedTerms.contains(Term.getTermByName(section.term));
	}

	public boolean acceptSection(Section section) {
		if (deniesSectionByTerm(section))
			return false;
		
		if(deniedSections.contains(section))
			return false;

		return true;
	}

}
