package edu.wpi.scheduler.client.controller;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

import edu.wpi.scheduler.shared.model.Course;

// FIXME should we put this in the model somewhere? It's confusing me that it's in the controller folder
public class StudentSchedule implements HasHandlers {
	
	public final List<SectionProducer> sectionProducers = new ArrayList<SectionProducer>();
	
	private HandlerManager handlerManager = new HandlerManager(this);
	
	public SectionProducer addCourse( Course course ){
		for( SectionProducer producer : sectionProducers ){
			if( producer.getCourse().equals(course)){
				throw new UnsupportedOperationException("The course it already in the list of producers!");
			}
		}
		
		SectionProducer producer = new SectionProducer(this, course);
		
		sectionProducers.add(producer);
		
		this.fireEvent(new StudentScheduleEvent(course, StudentScheduleEvents.ADD));
		
		return producer;
	}
	
	public SectionProducer getSectionProducer(Course course){
		
		for( SectionProducer producer : sectionProducers ){
			if( producer.getCourse().equals(course))
				return producer;
		}
		
		return null;
	}
	
	public void removeCourse( Course course ){
		
		SectionProducer producer = getSectionProducer(course);
		
		if( course != null )
			sectionProducers.remove( producer );
		
		this.fireEvent(new StudentScheduleEvent(course, StudentScheduleEvents.REMOVE));
	}
	
	public List<SchedulePermutation> getSchedulePermutations(){
		
		SchedulePermutationProducer producer = new SchedulePermutationProducer(this);
		return producer.getPermutations();
		
		
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}
	
	public HandlerRegistration addStudentScheduleHandler( StudentScheduleEventHandler handler ){
		return handlerManager.addHandler(StudentScheduleEvent.TYPE, handler);
	}
	
	public void removeStudentScheduleHandler( StudentScheduleEventHandler handler ){
		handlerManager.removeHandler(StudentScheduleEvent.TYPE, handler);
	}

	public void courseUpdated(Course course) {
		this.fireEvent(new StudentScheduleEvent(course, StudentScheduleEvents.UPDATE));
	}
	
}
