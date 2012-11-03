package edu.wpi.scheduler.client.courseselection;

import com.google.gwt.event.shared.EventHandler;

public interface CourseSelectedEventHandler extends EventHandler {
	void onCourseSelected(CourseSelectedEvent event);
}
