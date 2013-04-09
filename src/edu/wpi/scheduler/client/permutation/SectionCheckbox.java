package edu.wpi.scheduler.client.permutation;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;

import edu.wpi.scheduler.client.controller.SectionProducer;
import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.client.controller.StudentScheduleEvent;
import edu.wpi.scheduler.client.controller.StudentScheduleEventHandler;
import edu.wpi.scheduler.shared.model.Section;

public class SectionCheckbox extends CheckBox implements StudentScheduleEventHandler, ValueChangeHandler<Boolean> {
	
	private final StudentSchedule schedule;
	private final Section section;

	public SectionCheckbox( StudentSchedule schedule, Section section ){
		this.schedule = schedule;
		this.section = section;
		
		setText(section.number);
		addValueChangeHandler(this);
		update();
	}
	
	@Override
	protected void onLoad() {
		schedule.addStudentScheduleHandler(this);
	}

	@Override
	protected void onUnload() {
		schedule.removeStudentScheduleHandler(this);
	}

	@Override
	public void onCoursesChanged(StudentScheduleEvent studentScheduleEvent) {
		if( section.course.equals(studentScheduleEvent.getCourse()))
			this.update();
	}
	
	public SectionProducer getProducer(){
		return schedule.getSectionProducer(section.course);
	}

	public void update() {
		setValue(!getProducer().isSectionDenied(section));
	}

	@Override
	public void onValueChange(ValueChangeEvent<Boolean> event) {
		if (event.getValue() == true) {
			getProducer().removeDenySection(section);
		} else {
			getProducer().denySection(section);
		}
	}
	
}
