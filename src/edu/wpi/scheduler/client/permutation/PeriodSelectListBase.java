package edu.wpi.scheduler.client.permutation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.SchedulePermutation;
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
public abstract class PeriodSelectListBase extends FlowPanel implements StudentScheduleEventHandler {

	public final PermutationController permutationController;
	
	public class PeriodProfessorLabel extends Label implements MouseOverHandler, MouseOutHandler, ClickHandler {
		
		private Section section;

		public PeriodProfessorLabel( Section section ){
			this.section = section;
			this.setStyleName("PeriodSelectProf");
			
			this.addDomHandler(this, MouseOverEvent.getType());
			this.addDomHandler(this, MouseOutEvent.getType());
			this.addClickHandler(this);
			this.setText(getName());
		}
		
		private String getName(){
			if( section.periods.size() > 0 )
				return section.periods.get(0).professor;
			
			return "Unkown";
		}

		@Override
		public void onMouseOut(MouseOutEvent event) {
			this.setText(getName());
		}

		@Override
		public void onMouseOver(MouseOverEvent event) {
			this.setText(getName() + "→");
		}

		@Override
		public void onClick(ClickEvent event) {
			permutationController.displayDescription(section);
		}
			
	}

	public class PeriodCheckbox extends ComplexPanel implements ValueChangeHandler<Boolean>, MouseOverHandler, MouseOutHandler {

		public final CheckBox checkBox = new CheckBox();
		public final Section section;
		public final SectionProducer producer;

		public PeriodCheckbox(Section section, SectionProducer producer) {
			assert (producer != null);
			setElement(DOM.createDiv());
			this.producer = producer;
			this.section = section;

			checkBox.setText(section.number);
			checkBox.addValueChangeHandler(this);

			//Element elem = DOM.createSpan();
			//elem.setInnerText(section.periods.get(0).professor);
			//elem.setClassName("PeriodSelectProf");

			this.addDomHandler(this, MouseOverEvent.getType());
			this.addDomHandler(this, MouseOutEvent.getType());
			this.add(checkBox, this.getElement());
			//this.getElement().appendChild(elem);
			this.add( new PeriodProfessorLabel(section), this.getElement() );
		}

		@Override
		public void onValueChange(ValueChangeEvent<Boolean> event) {
			if (event.getValue() == true) {
				producer.removeDenySection(section);
			} else {
				producer.denySection(section);
			}
		}

		public void update() {
			checkBox.setValue(!producer.isSectionDenied(section));
			
			SchedulePermutation permutation = permutationController.getSelectedPermutation();
			
			if( permutation != null && permutation.sections.contains(section) ){
				this.getElement().getStyle().setBackgroundColor(permutationController.getCourseColor(section.course));
			} else {
				this.getElement().getStyle().setBackgroundColor(null);
			}
		}

		@Override
		public void onMouseOut(MouseOutEvent event) {
			permutationController.setSelectedSection(null);
		}

		@Override
		public void onMouseOver(MouseOverEvent event) {
			permutationController.setSelectedSection(section);
		}

	}

	public PeriodSelectListBase(PermutationController permutationController) {
		this.permutationController = permutationController;
		this.setStyleName("courseItemPeriods");
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
		this.update();
	}

	public void update() {
		for (Widget widget : this.getChildren()) {
			if( widget instanceof PeriodCheckbox )
				((PeriodCheckbox) widget).update();
		}
	}

}
