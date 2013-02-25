package edu.wpi.scheduler.client.permutation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.SchedulePermutation;
import edu.wpi.scheduler.client.controller.StudentScheduleEvent;
import edu.wpi.scheduler.client.controller.StudentScheduleEventHandler;
import edu.wpi.scheduler.shared.model.Course;
import edu.wpi.scheduler.shared.model.Section;

/**
 * Togglable item below each course that will be able to show which sections are
 * being taken, and be able to remove them
 * 
 * @author Nican
 * 
 */
public class PeriodSelectList extends FlowPanel implements StudentScheduleEventHandler {

	public final PermutationController controller;

	public class PeriodProfessorLabel extends Label implements MouseOverHandler, MouseOutHandler, ClickHandler {

		private Section section;

		public PeriodProfessorLabel(Section section) {
			this.section = section;
			this.setStyleName("PeriodSelectProf");

			this.addDomHandler(this, MouseOverEvent.getType());
			this.addDomHandler(this, MouseOutEvent.getType());
			this.addClickHandler(this);
			this.setText(getName());
		}

		private String getName() {
			if (section.periods.size() > 0)
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
			controller.displayDescription(section);
		}

	}

	public class PeriodListItem extends ComplexPanel implements MouseOverHandler, MouseOutHandler {

		public final Section section;

		public PeriodListItem(Section section) {
			setElement(DOM.createDiv());
			this.section = section;

			this.addDomHandler(this, MouseOverEvent.getType());
			this.addDomHandler(this, MouseOutEvent.getType());
			this.add(new SectionCheckbox(controller.getStudentSchedule(), section), this.getElement());
			this.add(new PeriodProfessorLabel(section), this.getElement());
			update();
		}



		public void update() {
			SchedulePermutation permutation = controller.getSelectedPermutation();

			if (permutation != null && permutation.sections.contains(section)) {
				this.getElement().getStyle().setBackgroundColor(controller.getCourseColor(section.course));
			} else {
				this.getElement().getStyle().setBackgroundColor(null);
			}
		}

		@Override
		public void onMouseOut(MouseOutEvent event) {
			controller.setSelectedSection(null);
		}

		@Override
		public void onMouseOver(MouseOverEvent event) {
			controller.setSelectedSection(section);
		}

	}

	public PeriodSelectList(PermutationController permutationController) {
		this.controller = permutationController;
		this.setStyleName("courseItemPeriods");
	}

	public void setSections(List<Section> conflictList, boolean courseSimpleName) {
		this.clear();
		HashMap<Course, List<Section>> conflictMap = new HashMap<Course, List<Section>>();

		for (Section section : conflictList) {
			//If the section is no longer in the student section, we do not care about it.
			if( controller.getStudentSchedule().getSectionProducer(section.course) == null )
				continue;
			
			if (!conflictMap.containsKey(section.course))
				conflictMap.put(section.course, new ArrayList<Section>());

			conflictMap.get(section.course).add(section);
		}

		for (Entry<Course, List<Section>> entry : conflictMap.entrySet()) {
			Label label = new Label(courseSimpleName ? entry.getKey().name : entry.getKey().toString());
			label.getElement().getStyle().setFontWeight(FontWeight.BOLD);
			label.getElement().getStyle().setMarginLeft(4.0, Unit.PX);
			this.add(label);

			for (Section section : entry.getValue()) {
				PeriodListItem checkbox = new PeriodListItem(section);
				checkbox.getElement().getStyle().setPaddingLeft(8.0, Unit.PX);
				this.add(checkbox);
			}
		}
	}

	@Override
	protected void onLoad() {
		controller.getStudentSchedule().addStudentScheduleHandler(this);
		update();
	}

	@Override
	protected void onUnload() {
		controller.getStudentSchedule().removeStudentScheduleHandler(this);
	}

	@Override
	public void onCoursesChanged(StudentScheduleEvent studentScheduleEvent) {
		this.update();
	}

	public void update() {
		for (Widget widget : this.getChildren()) {
			if (widget instanceof PeriodListItem)
				((PeriodListItem) widget).update();
		}
	}

}
