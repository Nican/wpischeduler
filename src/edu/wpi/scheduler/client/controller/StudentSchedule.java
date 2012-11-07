package edu.wpi.scheduler.client.controller;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

import edu.wpi.scheduler.shared.model.Course;

public class StudentSchedule implements HasHandlers {
	
	public final List<SectionProducer> sectionProducers = new ArrayList<SectionProducer>();
	
	private HandlerManager handlerManager = new HandlerManager(this);
	
	public SectionProducer addCourse( Course course ){
		SectionProducer producer = new SectionProducer(course);
		
		sectionProducers.add(producer);
		
		this.fireEvent(new StudentScheduleEvent(course));
		
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
		
		this.fireEvent(new StudentScheduleEvent(course));
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
	
}
