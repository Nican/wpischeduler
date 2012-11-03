package edu.wpi.scheduler.client.controller;

import com.google.gwt.event.shared.GwtEvent;

import edu.wpi.scheduler.shared.model.Course;

public class StudentScheduleEvent  extends GwtEvent<StudentScheduleEventHandler> {

	public static Type<StudentScheduleEventHandler> TYPE = new Type<StudentScheduleEventHandler>();
	private Course course;
	
	public StudentScheduleEvent(Course course){
		this.course = course;
	}
	
	@Override
	public Type<StudentScheduleEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(StudentScheduleEventHandler handler) {
		handler.onCoursesChanged(this);
	}
	
	public Course getCourse(){
		return course;
	}

}
