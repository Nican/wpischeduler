package edu.wpi.scheduler.client.permutation;

import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.client.controller.StudentScheduleEvent;
import edu.wpi.scheduler.client.controller.StudentScheduleEventHandler;
import edu.wpi.scheduler.client.tabs.BaseTab;

public class PermutationTab extends BaseTab implements StudentScheduleEventHandler {

	public final PermutationChooserView permutationView;
	
	public PermutationTab(StudentSchedule studentSchedule) {
		super(studentSchedule, "Schedules");
		
		permutationView = new PermutationChooserView(studentSchedule);
		
		studentSchedule.addStudentScheduleHandler(this);
		
		this.updateEnabled();
	}

	@Override
	public Widget getBody() {
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
		permutationView.updatePermutations();
	}
	

}
