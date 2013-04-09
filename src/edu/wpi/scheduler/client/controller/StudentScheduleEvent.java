package edu.wpi.scheduler.client.controller;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.shared.model.Course;

public class StudentScheduleEvent  extends GwtEvent<StudentScheduleEventHandler> {

	public static Type<StudentScheduleEventHandler> TYPE = new Type<StudentScheduleEventHandler>();
	private Course course;
	
	public final StudentScheduleEvents event;
	private Widget widget;
	
	public StudentScheduleEvent(Course course, StudentScheduleEvents event){
		this.event = event;
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
	
	/**
	 * The course may be null
	 * @return
	 */
	public Course getCourse(){
		return course;
	}

	public void setWidgetSource(Widget source){
		this.widget = source;
	}
	
	public Widget getWidgetSourse(){
		return this.widget;
	}

}
