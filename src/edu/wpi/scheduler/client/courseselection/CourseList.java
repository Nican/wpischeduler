package edu.wpi.scheduler.client.courseselection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.IdentityColumn;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.wpi.scheduler.client.controller.StudentScheduleEvent;
import edu.wpi.scheduler.client.controller.StudentScheduleEventHandler;
import edu.wpi.scheduler.shared.model.Course;
import edu.wpi.scheduler.shared.model.Department;

/**
 * Show a list of all courses given the department
 * 
 * @author Nican
 * 
 */
public class CourseList extends Composite implements Handler,
		StudentScheduleEventHandler {

	private static CourseListUiBinder uiBinder = GWT
			.create(CourseListUiBinder.class);

	interface CourseListUiBinder extends UiBinder<DataGrid<Course>, CourseList> {
	}

	@UiField
	DataGrid<Course> dataGrid;

	public final CourseSelectionController selectionController;

	/**
	 * Creates a selection model for the datagrid, such that we can select
	 * courses and get their description
	 */
	SingleSelectionModel<Course> selectionModel = new SingleSelectionModel<Course>();

	public CourseList(final CourseSelectionController selectionController,
			Department department) {
		initWidget(uiBinder.createAndBindUi(this));

		this.selectionController = selectionController;

		/*
		 * Course number (Ex. CS2201)
		 */
		TextColumn<Course> courseName = new TextColumn<Course>() {
			@Override
			public String getValue(Course object) {
				return object.department.abbreviation + object.number;
			}
		};
		dataGrid.addColumn(courseName, "Course");
		dataGrid.setColumnWidth(courseName, "110px");
		courseName.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		/*
		 * Course name
		 */
		dataGrid.addColumn(new TextColumn<Course>() {
			@Override
			public String getValue(Course object) {
				return object.name;
			}
		}, "Title");

		/*
		 * Add column Add/Remove the course from the @StudentSchedule.
		 */

		final IdentityColumn<Course> addColumn = new IdentityColumn<Course>(
				new CourseActionCell(selectionController));

		addColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		dataGrid.addColumn(addColumn, "ADD");

		/*
		 * Send an event when a new course is selected
		 */
		selectionModel.addSelectionChangeHandler(this);
		dataGrid.setSelectionModel(selectionModel);

		// Set the data to be the right size, and add in all the data
		dataGrid.setRowCount(department.courses.size(), true);
		dataGrid.setRowData(0, department.courses);

		// Remove all border, and make it scratch for all the window
		getElement().getStyle().setLeft(0, Unit.PX);
		getElement().getStyle().setRight(0, Unit.PX);
		getElement().getStyle().setTop(0, Unit.PX);
		getElement().getStyle().setBottom(0, Unit.PX);
		getElement().getStyle().setPosition(Position.ABSOLUTE);
	}

	/**
	 * Method from Handler interface, called when a new course is selected
	 */
	@Override
	public void onSelectionChange(SelectionChangeEvent event) {
		Course course = selectionModel.getSelectedObject();
		if (course != null) {
			selectionController.fireEvent(new CourseSelectedEvent(course));
		}
	}

	/**
	 * Add listeners when the object is added to the document/dom tree
	 */
	@Override
	protected void onLoad() {
		selectionController.getStudentSchedule()
				.addStudentScheduleHandler(this);

		dataGrid.redraw();
	}

	@Override
	protected void onUnload() {
		selectionController.getStudentSchedule().removeStudentScheduleHandler(
				this);
	}

	@Override
	public void onCoursesChanged(StudentScheduleEvent studentScheduleEvent) {
		// Redraw so we update the "ADD/REMOVE" button
		dataGrid.redraw();
	}

}
