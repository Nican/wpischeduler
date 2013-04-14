package edu.wpi.scheduler.client.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.user.client.Timer;

import edu.wpi.scheduler.shared.model.Course;
import edu.wpi.scheduler.shared.model.DayOfWeek;
import edu.wpi.scheduler.shared.model.Period;
import edu.wpi.scheduler.shared.model.Section;
import edu.wpi.scheduler.shared.model.Term;
import edu.wpi.scheduler.shared.model.Time;

/**
 * Find all conflicts between all sections in a list of courses.
 * 
 * @author Nican
 * 
 */
public class ConflictController {

	public static class ConflictedList extends ArrayList<Section> {
		private static final long serialVersionUID = 8823532784912354546L;
		public Section section;
	}
	
	private class ProducerTimer extends Timer {
		@Override
		public void run() {
			generate();

			if (!canGenerate()) {
				System.out.println("Found all conflicts!");
				this.cancel();
			}
		}
	}

	/**
	 * A mapping to what sections conflict to what sections If section A
	 * conflicts with section B, then section B conflicts with section A
	 */
	private Map<Section, ConflictedList> conflicts = new HashMap<Section, ConflictedList>();
	
	/**
	 * Timer that will generate more conflicts as needed
	 */
	ProducerTimer timer = new ProducerTimer();

	final List<Course> courses = new ArrayList<Course>();
	final List<Section> sectionQueue = new ArrayList<Section>();

	public ConflictController() {		

	}
	
	public void addCourse( Course course ){
		if( this.courses.contains(course) )
			return;
		
		this.courses.add(course);
		sectionQueue.addAll( course.sections );
		
		// Start generating!
		timer.scheduleRepeating(10);
	}

	public void generate() {
		
		Section section = sectionQueue.remove(0);
		ConflictedList list = new ConflictedList();
		list.section = section;
		
		for( Entry<Section, ConflictedList> entry : conflicts.entrySet() ){
			if( entry.getKey().course.equals(section.course) )
				continue;
			
			if (!hasConflictsNoCache(entry.getKey(), section))
				continue;
			
			list.add(entry.getKey());
			entry.getValue().add(section);
		}
		
		conflicts.put(section, list);
	}
	
	public ConflictedList getConflicts(Section section){
		return conflicts.get(section);
	}
	
	public boolean hasConflicts(Section newSection, Section section) {
		ConflictedList list1 = getConflicts(newSection);
		ConflictedList list2 = getConflicts(section);
		
		if(list1 != null && list2 != null )
			return list1.contains(section);
		
		return hasConflictsNoCache(newSection, section);
	}

	private boolean canGenerate() {
		return !sectionQueue.isEmpty();
	}

	private boolean hasConflictsNoCache(Section newSection, Section section) {
		List<Term> newTerms = newSection.getTerms();
		List<Term> terms = section.getTerms();
		boolean conflictingTerms = false;

		// Checks if any terms exists in both sections
		for (Term term : terms) {
			if (newTerms.contains(term)) {
				conflictingTerms = true;
			}
		}

		// The classes are not even in the same days of the year
		// There are no conflict here
		if (conflictingTerms == false)
			return false;

		for (Period period : section.periods) {
			for (Period newPeriod : newSection.periods) {
				if (hasConflitcs(period, newPeriod))
					return true;
			}
		}

		return false;

	}

	private boolean containsAny(HashSet<DayOfWeek> days1, HashSet<DayOfWeek> days2) {
		for (DayOfWeek day : days1) {
			if (days2.contains(day)) {
				return true;
			}
		}
		return false;
	}

	private boolean hasConflitcs(Period period, Period newPeriod) {

		// No days in common, no conflicts
		if (!containsAny(period.days, newPeriod.days))
			return false;

		Time periodStart = period.startTime;
		Time periodEnd = period.endTime;

		Time otherStart = newPeriod.startTime;
		Time otherEnd = newPeriod.endTime;

		return (otherStart.compareTo(periodStart) >= 0 && otherStart.compareTo(periodEnd) <= 0)
				|| (otherEnd.compareTo(periodStart) >= 0 && otherEnd.compareTo(periodEnd) <= 0)
				|| (otherStart.compareTo(periodStart) <= 0 && otherEnd.compareTo(periodEnd) >= 0);
	}
}
