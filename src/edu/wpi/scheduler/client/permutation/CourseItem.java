package edu.wpi.scheduler.client.permutation;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CellPanel;

import edu.wpi.scheduler.client.controller.SectionProducer;
import edu.wpi.scheduler.client.courseselection.TermViewSelection;
import edu.wpi.scheduler.shared.model.Course;

/**
 * Items on the course list, that will display what terms the class will be
 * taken
 * 
 * @author Nican
 * 
 */
public class CourseItem extends CellPanel implements ClickHandler {

	public final SectionProducer producer;
	public final PermutationController permutationController;

	private Element periodsRow = null;
	private PeriodSelectList itemPeriods = null;
	protected Button collapseButton = new Button("&#9654;");
	boolean isHidden = true;

	public CourseItem(PermutationController permutationController, SectionProducer producer) {
		this.permutationController = permutationController;
		this.producer = producer;
		collapseButton.addClickHandler(this);

		this.setStyleName("permutationCourseItem");
		
		setSpacing(0);

		Element titleRow = DOM.createTR();
		Element collapseCell = DOM.createTD();
		Element nameCell = DOM.createTD();
		Element termsCell = DOM.createTD();

		DOM.appendChild(titleRow, collapseCell);
		DOM.appendChild(titleRow, nameCell);
		DOM.appendChild(titleRow, termsCell);

		nameCell.setAttribute("align", "center");
		termsCell.setAttribute("align", "right");
		nameCell.setInnerText(getName());

		this.add(collapseButton, collapseCell);
		this.add(new TermViewSelection(producer.getCourse(), permutationController.getStudentSchedule()), termsCell);
		getBody().appendChild(titleRow);
	}

	public String getName() {
		Course course = producer.getCourse();

		return course.department.abbreviation + course.number;
	}

	@Override
	public void onClick(ClickEvent event) {
		this.togglePeriods();
	}

	public void togglePeriods() {

		if (periodsRow == null) {
			periodsRow = DOM.createTR();
			Element periodsCell = DOM.createTD();
			itemPeriods = new PeriodSelectList(permutationController);
			itemPeriods.setSections(producer.getCourse().sections, true);
			
			//periodsRow.getStyle().setDisplay(Display.NONE);
			periodsCell.setAttribute("colspan", "3");
			periodsCell.getStyle().setOverflow(Overflow.HIDDEN);

			DOM.appendChild(periodsRow, periodsCell);
			DOM.appendChild(getBody(), periodsRow);

			this.add(itemPeriods, periodsCell);			
		}
		
		
		if( isHidden ){
			isHidden = false;
			periodsRow.getStyle().setProperty("display", "");
			collapseButton.setHTML("&#9660;"); //Arrow down
			
			//new IncomingAnimation(itemPeriods.getElement(), false).run();
		} else {
			isHidden = true;
			periodsRow.getStyle().setDisplay(Display.NONE);
			collapseButton.setHTML("&#9654;"); //Arrow right
			
			//new IncomingAnimation(itemPeriods.getElement(), false, true).run();
		}

		
	}
}
