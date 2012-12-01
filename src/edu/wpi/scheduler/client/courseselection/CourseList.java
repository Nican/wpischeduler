package edu.wpi.scheduler.client.courseselection;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.shared.model.Course;
import edu.wpi.scheduler.shared.model.Department;

public class CourseList extends ComplexPanel {

	public CourseList(final CourseSelectionController selectionController, Department department) {
		
		this.setElement(DOM.createTable());
		
		for( Course course : department.courses ){
			CourseListItemBase item = new CourseListItemBase(selectionController, course);
			
			item.add(null, new Label(course.name));
			item.add(null, new Label(course.sections.get(0).term));
			
			this.add( item );
		}
		
		this.setStyleName("courseList");
		
	}
	
	@Override
	public void add( Widget child ){
		this.add( child, this.getElement() );
	}

}
