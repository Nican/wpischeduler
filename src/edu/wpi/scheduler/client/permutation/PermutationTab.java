package edu.wpi.scheduler.client.permutation;

import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.client.controller.StudentScheduleEvent;
import edu.wpi.scheduler.client.controller.StudentScheduleEventHandler;
import edu.wpi.scheduler.client.tabs.BaseTab;

public class PermutationTab extends BaseTab implements StudentScheduleEventHandler {

	private PermutationChooserView permutationView;
	
	public PermutationTab(StudentSchedule studentSchedule) {
		super(studentSchedule, "Schedules", "List of available courses");
		
		studentSchedule.addStudentScheduleHandler(this);
		
		this.updateEnabled();
	}

	@Override
	public Widget getBody() {
		if( permutationView == null )
			permutationView = new PermutationChooserView(studentSchedule);
		
		return permutationView;
	}
	
	@Override
	public void onCoursesChanged(StudentScheduleEvent studentScheduleEvent) {
		this.updateEnabled();
	}

	private void updateEnabled() {
		this.setEnabled( studentSchedule.sectionProducers.size() > 0 );
	}
	
	@Override
	public void updateView() {
		permutationView.update();
	}
}
