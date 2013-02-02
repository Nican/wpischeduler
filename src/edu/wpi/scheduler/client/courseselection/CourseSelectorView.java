/**
 * 
 */
package edu.wpi.scheduler.client.courseselection;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.ListBox;

import edu.wpi.scheduler.client.Scheduler;
import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.shared.model.Department;

/**
 * @author Nican
 * 
 */
public class CourseSelectorView extends Composite implements
		CourseSelectedEventHandler {

	private static CourseSelectorViewUiBinder uiBinder = GWT
			.create(CourseSelectorViewUiBinder.class);

	interface CourseSelectorViewUiBinder extends
			UiBinder<DockLayoutPanel, CourseSelectorView> {
	}

	final StudentSchedule studentSchedule;
	final CourseSelectionController selectionController;

	@UiField(provided = true)
	ListBox departmentList;

	@UiField(provided=true)
	CourseList courseList;

	@UiField
	CourseDescription courseDescription;

	@UiField(provided = true)
	CourseSelection courseSelection;

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 * 
	 * @param studentSchedule
	 */
	public CourseSelectorView(StudentSchedule studentSchedule) {
		selectionController = new CourseSelectionController(studentSchedule);
		courseSelection = new CourseSelection(selectionController);
		departmentList = new ListBox(true);
		courseList = new CourseList(selectionController);

		initWidget(uiBinder.createAndBindUi(this));

		this.studentSchedule = studentSchedule;

		getElement().getStyle().setLeft(0, Unit.PX);
		getElement().getStyle().setRight(0, Unit.PX);
		getElement().getStyle().setTop(0, Unit.PX);
		getElement().getStyle().setBottom(0, Unit.PX);
		getElement().getStyle().setPosition(Position.ABSOLUTE);

		updateDepartments();

		selectionController.addCourseSelectedListner(this);
	}

	public void updateDepartments() {

		departmentList.clear();

		List<Department> departments = Scheduler.getDatabase().departments;

		for (int i = 0; i < departments.size(); i++) {
			departmentList.insertItem(departments.get(i).name, i);
		}

		updateCourseList();

	}

	@UiHandler("departmentList")
	public void dropdownEvent(ChangeEvent event) {
		updateCourseList();
	}

	/**
	 * Choose the new department from the dropdown menu, and display the
	 * corresponding body.
	 * 
	 * @param department
	 */
	public void updateCourseList() {

		// Clear the body from any existing elements
		courseList.clear();

		List<Department> departments = Scheduler.getDatabase().departments;
		
		for (int i = 0; i < departments.size(); i++) {
			if(departmentList.isItemSelected(i))
				courseList.addDeparment(departments.get(i));
		}
		
	}

	/**
	 * Called when a course is selected in the main view
	 */
	@Override
	public void onCourseSelected(CourseSelectedEvent event) {
		// Update the description box with the latest course
		courseDescription.setCourse(event.getCourse());
	}

}
