package edu.wpi.scheduler.client.courseselection;

import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.client.tabs.BaseTab;

public class CourseSelectionTab extends BaseTab {
	
	final CourseSelectorView selectorView;

	public CourseSelectionTab(StudentSchedule studentSchedule) {
		super(studentSchedule, "Courses", "Browse and choose courses that you wish to attend");
		
		this.selectorView = new CourseSelectorView(studentSchedule);
	}

	@Override
	public Widget getBody() {
		return this.selectorView;
	}

	
	
}
