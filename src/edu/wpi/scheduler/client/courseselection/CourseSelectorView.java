/**
 * 
 */
package edu.wpi.scheduler.client.courseselection;

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
import com.google.gwt.user.client.ui.SimplePanel;

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

	@UiField
	ListBox departmentList;

	@UiField
	SimplePanel courseListHolder;

	@UiField
	CourseDescription courseDescription;

	@UiField(provided = true)
	CourseSelection courseSelection;

	CourseList courseList;

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

		for (Department dept : Scheduler.getDatabase().departments) {
			departmentList.addItem(dept.name);
		}

		updateCourseList(getSelectedDepartment());

	}

	@UiHandler("departmentList")
	public void dropdownEvent(ChangeEvent event) {
		updateCourseList(getSelectedDepartment());
	}

	public Department getSelectedDepartment() {
		// Get the current selected event name
		int index = departmentList.getSelectedIndex();
		String name = departmentList.getItemText(index);

		// Find the corresponding department
		for (Department dept : Scheduler.getDatabase().departments) {
			if (dept.name.equals(name)) {
				return dept;
			}
		}

		// Make sure we actually found the corresponding department
		throw new IllegalStateException(
				"Could not find the department with name '" + name + "'");

	}

	/**
	 * Choose the new department from the dropdown menu, and display the
	 * corresponding body.
	 * 
	 * @param department
	 */
	public void updateCourseList(Department department) {

		// Clear the body from any existing elements
		courseListHolder.clear();

		courseList = new CourseList(selectionController, department);
		courseListHolder.add(courseList);

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
