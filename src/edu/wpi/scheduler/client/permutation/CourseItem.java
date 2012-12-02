package edu.wpi.scheduler.client.permutation;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.CellPanel;

import edu.wpi.scheduler.client.controller.SectionProducer;
import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.client.courseselection.TermViewSelection;
import edu.wpi.scheduler.shared.model.Course;

public class CourseItem extends CellPanel {

	public final SectionProducer producer;

	public CourseItem(StudentSchedule studentSchedule, SectionProducer producer) {
		this.producer = producer;
		
		this.setStyleName("permutationCourseItem");
		
		 Element titleRow = DOM.createTR();
		 Element collapseCell = DOM.createTD();
		 Element nameCell = DOM.createTD();
		 Element termsCell = DOM.createTD();
		
		 DOM.appendChild(getBody(), collapseCell);
		 DOM.appendChild(getBody(), nameCell);
		 DOM.appendChild(getBody(), termsCell);
		 
		 nameCell.setAttribute("align", "center");
		 termsCell.setAttribute("align", "right");
		 
		 collapseCell.setInnerText(">");
		 nameCell.setInnerText(getName());
		 
		 this.add( new TermViewSelection(producer.getCourse(), studentSchedule), termsCell );
		 
		 DOM.appendChild(getBody(), titleRow);
	}

	
	
	public String getName(){
		Course course = producer.getCourse();
		
		return course.department.abbreviation + course.number;
	}
	
}
