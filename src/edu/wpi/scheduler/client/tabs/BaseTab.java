package edu.wpi.scheduler.client.tabs;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.StudentSchedule;

public abstract class BaseTab extends Button {

	public final StudentSchedule studentSchedule;
	
	public BaseTab(StudentSchedule studentSchedule, String name, String description) {
		this(studentSchedule, name);
		setTitle(description);
	}

	public BaseTab(StudentSchedule studentSchedule, String name) {
		super(name);
		this.studentSchedule = studentSchedule;

		setText(name);
		setHeight("100%");
		setStyleName("sched-TopButton");
	}

	public abstract Widget getBody();
	
	public void updateView() {
		
	}

}
