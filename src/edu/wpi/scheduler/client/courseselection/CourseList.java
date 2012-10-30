package edu.wpi.scheduler.client.courseselection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;

import edu.wpi.scheduler.shared.model.Course;
import edu.wpi.scheduler.shared.model.Department;

public class CourseList extends Composite {

	private static CourseListUiBinder uiBinder = GWT
			.create(CourseListUiBinder.class);

	interface CourseListUiBinder extends UiBinder<DataGrid<Course>, CourseList> {
	}

	@UiField
	DataGrid<Course> dataGrid;



	public CourseList(Department department) {
		initWidget(uiBinder.createAndBindUi(this));

		dataGrid.addColumn(new TextColumn<Course>() {
			@Override
			public String getValue(Course object) {
				return object.name;
			}
		});

		dataGrid.setRowCount(department.courses.size(), true);

		dataGrid.setRowData(0, department.courses);
		
		getElement().getStyle().setLeft(0, Unit.PX);
		getElement().getStyle().setRight(0, Unit.PX);
		getElement().getStyle().setTop(0, Unit.PX);
		getElement().getStyle().setBottom(0, Unit.PX);
		getElement().getStyle().setPosition(Position.ABSOLUTE);
	}

	public void setDepartment(Department department) {

	}

}
