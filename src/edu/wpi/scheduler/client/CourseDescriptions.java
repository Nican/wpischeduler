package edu.wpi.scheduler.client;

import com.google.gwt.core.client.JavaScriptObject;

import edu.wpi.scheduler.shared.model.Course;

public final class CourseDescriptions extends JavaScriptObject{

	protected CourseDescriptions() {
	}
	
	public final CourseDescription getDescription(Course course){
		return getDescription( course.department.abbreviation.toUpperCase() + " " + course.number );
	}
	
	public final native CourseDescription getDescription(String course) /*-{
		return this[course];		
	}-*/;

}
