package edu.wpi.scheduler.client.courseselection;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.shared.model.Course;

public class CourseSelectionController implements HasHandlers {

	private HandlerManager handlerManager = new HandlerManager(this);
	private final StudentSchedule studentSchedule;
	private Course selectedCourse;

	public CourseSelectionController(StudentSchedule studentSchedule) {
		this.studentSchedule = studentSchedule;
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	public StudentSchedule getStudentSchedule() {
		return studentSchedule;
	}
	
	public Course getSelectedCourse(){
		return this.selectedCourse;
	}
	
	public void selectCourse( Course course ){
		this.selectedCourse = course;
		
		this.fireEvent(new CourseSelectedEvent(course));		
	}

	/**
	 * Add a listener to this widget to when a new course is selected
	 * 
	 * @param handler
	 * @return
	 */
	public HandlerRegistration addCourseSelectedListner(
			CourseSelectedEventHandler handler) {
		return handlerManager.addHandler(CourseSelectedEvent.TYPE, handler);
	}
	
	public void removeCourseSelectedListner( CourseSelectedEventHandler handler ){
		handlerManager.removeHandler(CourseSelectedEvent.TYPE, handler);
	}

}
