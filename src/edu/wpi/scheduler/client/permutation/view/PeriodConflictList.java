package edu.wpi.scheduler.client.permutation.view;

import edu.wpi.scheduler.client.controller.ScheduleConflictController.ConflictedList;
import edu.wpi.scheduler.client.permutation.PeriodSelectListBase;
import edu.wpi.scheduler.client.permutation.PermutationController;
import edu.wpi.scheduler.shared.model.Section;

public class PeriodConflictList extends PeriodSelectListBase {

	public PeriodConflictList(ConflictedList conflictList, PermutationController controller) {
		super(controller);
		
		for( Section section : conflictList ){
			PeriodCheckbox checkbox = new PeriodCheckbox( section, controller.getStudentSchedule().getSectionProducer(section.course) );
			this.add( checkbox );
		}
		
		this.update();
		
	}

}
