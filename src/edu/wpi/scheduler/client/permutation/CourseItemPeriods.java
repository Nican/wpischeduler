package edu.wpi.scheduler.client.permutation;

import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;

import edu.wpi.scheduler.client.controller.SectionProducer;
import edu.wpi.scheduler.client.controller.StudentScheduleEvent;
import edu.wpi.scheduler.client.controller.StudentScheduleEventHandler;
import edu.wpi.scheduler.shared.model.Section;

/**
 * Togglable item below each course that will be able to show which sections are
 * being taken, and be able to remove them
 * 
 * @author Nican
 * 
 */
public class CourseItemPeriods extends FlowPanel implements StudentScheduleEventHandler {

	public final PermutationController permutationController;
	public final SectionProducer producer;
	public final HashMap<Section, CheckBox> sectionCheckbox = new HashMap<Section, CheckBox>();

	public CourseItemPeriods(PermutationController permutationController, final SectionProducer producer) {
		this.permutationController = permutationController;
		this.producer = producer;

		for (final Section section : producer.getCourse().sections) {
			
			CheckBox checkBox = new CheckBox( section.number );
			
			checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					if( event.getValue() == true ){
						producer.removeDenySection(section);
					} else {
						producer.denySection(section);
					}
				}
			});
			
			sectionCheckbox.put(section, checkBox);

			this.add( checkBox );

		}
		
		this.update();
		
	}

	@Override
	protected void onLoad() {
		permutationController.getStudentSchedule().addStudentScheduleHandler(this);
	}

	@Override
	protected void onUnload() {
		permutationController.getStudentSchedule().removeStudentScheduleHandler(this);
	}

	@Override
	public void onCoursesChanged(StudentScheduleEvent studentScheduleEvent) {
		if (!studentScheduleEvent.getCourse().equals(producer.getCourse()))
			return;
		
		this.update();
	}

	public void update() {
		for (Entry<Section, CheckBox> entry : sectionCheckbox.entrySet()) {	
			entry.getValue().setValue(!producer.isSectionDenied(entry.getKey()));
			entry.getValue().setVisible(!producer.deniesSectionByTerm(entry.getKey()));
		}
	}

}
