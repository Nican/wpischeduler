package edu.wpi.scheduler.client.permutation;

import com.google.gwt.user.client.ui.VerticalPanel;

import edu.wpi.scheduler.client.controller.SectionProducer;

public class StudentCourseList extends VerticalPanel {
	
	private PermutationController controller;

	public StudentCourseList(PermutationController controller){
		this.controller = controller;
	}
	
	
	@Override
	protected void onLoad() {
		update();
	}
	
	public void update() {
		clear();
		boolean first = true;

		for (SectionProducer producer : controller.getStudentSchedule().sectionProducers) {
			CourseItem item = new CourseItem(controller, producer);
			
			add( item );
			
			if( first ){
				item.togglePeriods();
				first = false;
			}
		}
	}
	
}
