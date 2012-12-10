package edu.wpi.scheduler.client.courseselection;

import com.google.gwt.event.shared.GwtEvent;

import edu.wpi.scheduler.shared.model.Course;

public class CourseSelectedEvent extends GwtEvent<CourseSelectedEventHandler> {

	public static Type<CourseSelectedEventHandler> TYPE = new Type<CourseSelectedEventHandler>();
	private Course course;
	
	public CourseSelectedEvent(Course course){
		this.course = course;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<CourseSelectedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CourseSelectedEventHandler handler) {
		handler.onCourseSelected(this);
	}
	
	public Course getCourse(){
		return course;
	}

}
