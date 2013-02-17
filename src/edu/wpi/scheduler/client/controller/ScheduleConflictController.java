package edu.wpi.scheduler.client.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.user.client.Timer;

import edu.wpi.scheduler.shared.model.Course;
import edu.wpi.scheduler.shared.model.Section;

/**
 * Find all conflicts between all sections in a list of courses.
 * 
 * @author Nican
 * 
 */
public class ScheduleConflictController {

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

	public ScheduleConflictController() {		

	}
	
	public void addCourse( Course course ){
		if( this.courses.contains(course) )
			return;
		
		this.courses.add(course);
		sectionQueue.addAll( course.sections );
		
		// Start generating!
		timer.scheduleRepeating(60);
	}

	public void generate() {
		
		Section section = sectionQueue.remove(0);
		ConflictedList list = new ConflictedList();
		list.section = section;
		
		for( Entry<Section, ConflictedList> entry : conflicts.entrySet() ){
			if( entry.getKey().course.equals(section.course) )
				continue;
			
			if (!ScheduleProducer.hasConflicts(entry.getKey(), section))
				continue;
			
			list.add(entry.getKey());
			entry.getValue().add(section);
		}
		
		conflicts.put(section, list);
	}
	
	public ConflictedList getConflicts(Section section){
		return conflicts.get(section);
	}

	private boolean canGenerate() {
		return !sectionQueue.isEmpty();
	}

}
