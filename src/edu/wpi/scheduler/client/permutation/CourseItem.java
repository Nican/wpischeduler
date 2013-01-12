package edu.wpi.scheduler.client.permutation;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CellPanel;

import edu.wpi.scheduler.client.controller.SectionProducer;
import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.client.courseselection.TermViewSelection;
import edu.wpi.scheduler.shared.model.Course;

/**
 * Items on the course list, that will display what terms the class will be taken
 * @author Nican
 *
 */
public class CourseItem extends CellPanel implements ClickHandler {

	public final SectionProducer producer;
	public final StudentSchedule studentSchedule;

	private Element periodsRow = null;
	private CourseItemPeriods itemPeriods = null;

	public CourseItem(StudentSchedule studentSchedule, SectionProducer producer) {
		this.studentSchedule = studentSchedule;
		this.producer = producer;

		this.setStyleName("permutationCourseItem");

		Element titleRow = DOM.createTR();
		Element collapseCell = DOM.createTD();
		Element nameCell = DOM.createTD();
		Element termsCell = DOM.createTD();

		DOM.appendChild(titleRow, collapseCell);
		DOM.appendChild(titleRow, nameCell);
		DOM.appendChild(titleRow, termsCell);

		nameCell.setAttribute("align", "center");
		termsCell.setAttribute("align", "right");

		Button collapse = new Button(">");
		this.add(collapse, collapseCell);
		collapse.addClickHandler(this);

		nameCell.setInnerText(getName());

		this.add(new TermViewSelection(producer.getCourse(), studentSchedule), termsCell);

		DOM.appendChild(getBody(), titleRow);
	}

	public String getName() {
		Course course = producer.getCourse();

		return course.department.abbreviation + course.number;
	}

	@Override
	public void onClick(ClickEvent event) {
		this.togglePeriods();
	}

	private void togglePeriods() {

		if (periodsRow == null) {
			periodsRow = DOM.createTR();
			Element periodsCell = DOM.createTD();
			itemPeriods = new CourseItemPeriods(this.studentSchedule, this.producer);
			
			periodsRow.getStyle().setDisplay(Display.NONE);
			periodsCell.setAttribute("colspan", "3");

			DOM.appendChild(periodsRow, periodsCell);
			DOM.appendChild(getBody(), periodsRow);

			this.add(itemPeriods, periodsCell);			
		}
		
		
		if( periodsRow.getStyle().getDisplay().equals(Display.NONE.getCssName()) ){
			periodsRow.getStyle().setProperty("display", null);
		} else {
			periodsRow.getStyle().setDisplay(Display.NONE);
		}

		
	}

}
