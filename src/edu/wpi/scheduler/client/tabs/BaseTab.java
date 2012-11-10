package edu.wpi.scheduler.client.tabs;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.StudentSchedule;

public abstract class BaseTab extends Button {

	public final StudentSchedule studentSchedule;

	public BaseTab(StudentSchedule studentSchedule, String name) {
		this.studentSchedule = studentSchedule;

		this.setText(name);

		this.setHeight("100%");
	}

	public abstract Widget getBody();

}
