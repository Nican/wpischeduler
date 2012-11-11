package edu.wpi.scheduler.client.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.wpi.scheduler.shared.model.DaysOfWeek;
import edu.wpi.scheduler.shared.model.Period;
import edu.wpi.scheduler.shared.model.Section;


public class SchedulePermutationProducer {
	
	private StudentSchedule studentSchedule;
	private List<SchedulePermutation> permutations = new ArrayList<SchedulePermutation>();

	public SchedulePermutationProducer(StudentSchedule studentSchedule ){
		this.studentSchedule =studentSchedule;
	}
	
	private void generate(){
		generate( 0, Collections.<Section> emptyList() );
		
		
	}
	
	public void generate( int index, List<Section> studentSections ){
		
		if( index >= getProducers().size() ){
			return;
		}
		
		SectionProducer producer = getProducers().get(index);	
		
		for( Section sections : producer.getSections() ){
			
			
			
		}
		
	}
	
	public boolean hasConflicts( Section newSection, List<Section> studentSections ){
		for( Section section : studentSections ){
			if( hasConflicts( newSection, section ))
				return true;
			
		}
		return false;
	}
	
	
	private boolean hasConflicts(Section newSection, Section section) {
		
		for( Period period : section.periods ){
			for( Period newPeriod : newSection.periods ){
				if( hasConflitcs( period, newPeriod ))
					return true;
			}
		}
		
		return false;
		
	}

	private boolean hasConflitcs(Period period, Period newPeriod) {
		
		for( DaysOfWeek day : period.days ){
			for( DaysOfWeek day2 : newPeriod.days ){
				if( day2 == day ){
					return false;
				}
			}
		}
		
		double periodStart = parseTime( period.startTime );
		double periodEnd = parseTime( period.endTime );
		
		double otherStart = parseTime( newPeriod.startTime );
		double otherEnd = parseTime( newPeriod.endTime );
		
		return  (otherStart >= periodStart && otherStart <= periodEnd ) ||
				 (otherEnd >= periodStart && otherEnd <= periodEnd) || 
				 (otherStart <= periodStart && otherEnd >= periodEnd);
	}
	
	private double parseTime( String time ){
		boolean meridian = time.substring( time.length() - 2).equals("AM");
		double hour = Float.valueOf( time );
		double minutes = Float.valueOf( time.substring(time.length() - 4, time.length() - 2) );
		double value = hour;
		
		if( meridian || hour == 12)
			value -= 8.0f;
		else
			value += 12.0f - 8.0f;
		
		value += minutes / 60;
		
		return value / 14;		
	}

	private List<SectionProducer> getProducers(){
		return studentSchedule.sectionProducers;
	}
	
	
	
}
