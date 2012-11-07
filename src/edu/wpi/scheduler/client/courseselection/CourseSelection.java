package edu.wpi.scheduler.client.courseselection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.SectionProducer;
import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.client.controller.StudentScheduleEvent;
import edu.wpi.scheduler.client.controller.StudentScheduleEventHandler;
import edu.wpi.scheduler.shared.model.Course;

/**
 * Displays a list of the courses that the student has added.
 * @author Nican
 *
 */
public class CourseSelection extends Composite implements StudentScheduleEventHandler {

	private static CourseSelectionUiBinder uiBinder = GWT
			.create(CourseSelectionUiBinder.class);

	interface CourseSelectionUiBinder extends UiBinder<Widget, CourseSelection> {
	}
	
	public final StudentSchedule studentSchedule;

	public CourseSelection( StudentSchedule studentSchedule ) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.studentSchedule = studentSchedule;
		
		TextColumn<SectionProducer> courseName = new TextColumn<SectionProducer>() {
			@Override
			public String getValue(SectionProducer object) {
				Course course = object.getCourse();
				
				return course.department.abbreviation + course.number;
			}
		};
		dataGrid.addColumn(courseName, "Course");
		
		this.refreshList();
	}
	
	@UiField
	DataGrid<SectionProducer> dataGrid;
	
	public void refreshList() {
		// Set the data to be the right size, and add in all the data
		dataGrid.setRowCount(studentSchedule.sectionProducers.size(), true);
		dataGrid.setRowData(0, studentSchedule.sectionProducers);		
	}
	
	/**
	 * Add listeners when the object is added to the document/dom tree
	 */
	@Override
	protected void onLoad(){
		studentSchedule.addStudentScheduleHandler(this);
	
		this.refreshList();
	}
	
	@Override
	protected void onUnload(){
		studentSchedule.removeStudentScheduleHandler(this);
	}

	@Override
	public void onCoursesChanged(StudentScheduleEvent studentScheduleEvent) {
		this.refreshList();
	}
	
	

}
