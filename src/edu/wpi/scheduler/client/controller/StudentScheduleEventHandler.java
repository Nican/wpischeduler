package edu.wpi.scheduler.client.controller;

import com.google.gwt.event.shared.EventHandler;

public interface StudentScheduleEventHandler extends EventHandler  {
	public void onCoursesChanged(StudentScheduleEvent studentScheduleEvent);
}
