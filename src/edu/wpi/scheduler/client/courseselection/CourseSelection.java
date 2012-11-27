package edu.wpi.scheduler.client.courseselection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.SectionProducer;
import edu.wpi.scheduler.client.controller.StudentScheduleEvent;
import edu.wpi.scheduler.client.controller.StudentScheduleEventHandler;
import edu.wpi.scheduler.shared.model.Course;

/**
 * Displays a list of the courses that the student has added.
 * 
 * @author Nican
 * 
 */
public class CourseSelection extends Composite implements
		StudentScheduleEventHandler {

	private static CourseSelectionUiBinder uiBinder = GWT
			.create(CourseSelectionUiBinder.class);

	interface CourseSelectionUiBinder extends UiBinder<Widget, CourseSelection> {
	}

	public final CourseSelectionController selectionController;

	@UiField
	DataGrid<SectionProducer> dataGrid;

	public CourseSelection(final CourseSelectionController selectionController) {
		initWidget(uiBinder.createAndBindUi(this));

		this.selectionController = selectionController;

		TextColumn<SectionProducer> courseName = new TextColumn<SectionProducer>() {
			@Override
			public String getValue(SectionProducer object) {
				Course course = object.getCourse();

				return course.department.abbreviation + course.number;
			}
		};
		dataGrid.addColumn(courseName);

		// TODO: remove duplicate code from @CourseList
		Column<SectionProducer, Course> addColumn = new Column<SectionProducer, Course>(new CourseActionCell(selectionController)) {
			@Override
			public Course getValue(SectionProducer producer) {
				return producer.getCourse();
			}
		};
		addColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		dataGrid.addColumn(addColumn);

		this.refreshList();
	}

	public void refreshList() {
		// Set the data to be the right size, and add in all the data
		dataGrid.setRowCount(selectionController.getStudentSchedule().sectionProducers.size(), true);
		dataGrid.setRowData(0, selectionController.getStudentSchedule().sectionProducers);
	}

	/**
	 * Add listeners when the object is added to the document/dom tree
	 */
	@Override
	protected void onLoad() {
		selectionController.getStudentSchedule().addStudentScheduleHandler(this);

		this.refreshList();
	}

	@Override
	protected void onUnload() {
		selectionController.getStudentSchedule().removeStudentScheduleHandler(this);
	}

	@Override
	public void onCoursesChanged(StudentScheduleEvent studentScheduleEvent) {
		this.refreshList();
	}

}
