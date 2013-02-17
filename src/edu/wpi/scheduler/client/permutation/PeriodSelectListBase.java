package edu.wpi.scheduler.client.permutation;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Float;
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
import com.google.gwt.user.client.ui.Widget;

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

			Element elem = DOM.createSpan();
			elem.setInnerText(section.periods.get(0).professor);
			elem.getStyle().setFloat(Float.RIGHT);

			this.addDomHandler(this, MouseOverEvent.getType());
			this.addDomHandler(this, MouseOutEvent.getType());
			this.add(checkBox, this.getElement());
			this.getElement().appendChild(elem);
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
			((PeriodCheckbox) widget).update();
		}
	}

}
