package edu.wpi.scheduler.client.courseselection;

import com.google.gwt.event.dom.client.ClickHandler;

import edu.wpi.scheduler.shared.model.Course;

public class CourseSelectionItem extends CourseListItemBase implements ClickHandler {

	public CourseSelectionItem(CourseSelectionController selectionController, Course course) {
		super(selectionController, course);

		this.add("100px", new TermViewSelection(course, selectionController.getStudentSchedule()));
	}

}
