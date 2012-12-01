package edu.wpi.scheduler.client.controller;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.scheduler.shared.model.DayOfWeek;
import edu.wpi.scheduler.shared.model.Period;
import edu.wpi.scheduler.shared.model.Section;
import edu.wpi.scheduler.shared.model.Term;
import edu.wpi.scheduler.shared.model.Time;

public class SchedulePermutationProducer {

	private StudentSchedule studentSchedule;
	private List<SchedulePermutation> permutations = new ArrayList<SchedulePermutation>();

	public SchedulePermutationProducer(StudentSchedule studentSchedule) {
		this.studentSchedule = studentSchedule;
		
		generate();
	}
	
	public List<SchedulePermutation> getPermutations(){
		return permutations;
	}

	private void generate() {
		generate(0, new ArrayList<Section>());
	}

	private void generate(int index, List<Section> studentSections) {

		if (index > getProducers().size()) {
			return;
		}

		SectionProducer producer = getProducers().get(index);

		for (Section section : producer.getSections()) {

			if (hasConflicts(section, studentSections)) {
				continue;
			}

			List<Section> newStudentSections = new ArrayList<Section>(
					studentSections);

			newStudentSections.add(section);

			if (index == getProducers().size() - 1) {
				SchedulePermutation permutation = new SchedulePermutation();
				permutation.sections = newStudentSections;
				permutations.add(permutation);
			} else {
				generate(index + 1, newStudentSections);
			}

		}

	}

	public boolean hasConflicts(Section newSection,
			List<Section> studentSections) {
		for (Section section : studentSections) {
			if (hasConflicts(newSection, section))
				return true;

		}
		return false;
	}

	private boolean hasConflicts(Section newSection, Section section) {
		List<Term> newTerms = newSection.getTerms();
		List<Term> terms = section.getTerms();
		boolean conflictingTerms = false;
		
		//Checks if any terms exists in both sections
		for( Term term : terms ){
			if( newTerms.contains(term) ){
				conflictingTerms = true;
			}
		}
		
		//The classes are not even in the same days of the year
		//There are no conflict here
		if( conflictingTerms == false )
			return false;
		

		for (Period period : section.periods) {
			for (Period newPeriod : newSection.periods) {
				if (hasConflitcs(period, newPeriod))
					return true;
			}
		}

		return false;

	}

	private boolean hasConflitcs(Period period, Period newPeriod) {

		for (DayOfWeek day : period.days) {
			for (DayOfWeek day2 : newPeriod.days) {
				if (day2 == day) {
					return false;
				}
			}
		}
		
		Time periodStart = period.startTime;
		Time periodEnd = period.endTime;

		Time otherStart = newPeriod.startTime;
		Time otherEnd = newPeriod.endTime;
		
		return (otherStart.compareTo(periodStart) >= 0 && otherStart.compareTo(periodEnd) <= 0)
				|| (otherEnd.compareTo(periodStart) >= 0 && otherEnd.compareTo(periodEnd) <= 0)
				|| (otherStart.compareTo(periodStart) <= 0 && otherEnd.compareTo(periodEnd) >= 0);
	}

	private List<SectionProducer> getProducers() {
		return studentSchedule.sectionProducers;
	}

}
