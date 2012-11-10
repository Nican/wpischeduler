package edu.wpi.scheduler.client.timechooser;

import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.client.controller.StudentScheduleEvent;
import edu.wpi.scheduler.client.controller.StudentScheduleEventHandler;
import edu.wpi.scheduler.client.tabs.BaseTab;

public class TimeTab extends BaseTab implements StudentScheduleEventHandler {

	final TimeChooserView chooserView;
	
	public TimeTab(StudentSchedule studentSchedule) {
		super(studentSchedule, "Times");
		
		chooserView = new TimeChooserView(studentSchedule);
		
		studentSchedule.addStudentScheduleHandler(this);
		
		this.updateEnabled();
	}

	@Override
	public Widget getBody() {
		return chooserView;
	}

	@Override
	public void onCoursesChanged(StudentScheduleEvent studentScheduleEvent) {
		this.updateEnabled();
	}

	private void updateEnabled() {
		this.setEnabled( studentSchedule.sectionProducers.size() > 0 );
	}
	
	

}
