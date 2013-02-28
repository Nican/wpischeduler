package edu.wpi.scheduler.client.courseselection;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.HasCourse;
import edu.wpi.scheduler.shared.model.Course;

public class CourseListItemBase extends ComplexPanel implements ClickHandler, HasCourse {

	private Course course;
	private CourseSelectionController selectionController;

	public CourseListItemBase( CourseSelectionController selectionController, Course course ){
		this.course = course;
		this.selectionController = selectionController;
		
		this.setElement(DOM.createTR());
		
		Element addButton = this.add("36px", new CourseButton(selectionController.getStudentSchedule(), course));
		addButton.getStyle().setProperty("textAlign", "center");
		
		Element courseAbbr = this.add("100px", course.department.abbreviation + course.number);
		courseAbbr.getStyle().setProperty("textAlign", "center");

		
		this.addDomHandler(this, ClickEvent.getType());
	}

	public Element add(String width, Widget child) {
		Element elem = addCell(width);
		this.add(child, (com.google.gwt.user.client.Element) elem);
		return elem;
	}
	
	public Element add(String width, String innerHTML) {
		Element elem = addCell(width);
		elem.setInnerHTML(innerHTML);
		return elem;
	}
	
	private Element addCell(String width){
		Element elem = DOM.createTD();

		elem.getStyle().setProperty("width", width);

		this.getElement().appendChild(elem);

		return elem;
	}

	@Override
	public void onClick(ClickEvent event) {
		selectionController.selectCourse(course);
	}

	@Override
	public Course getCourse() {
		return course;
	}

}
