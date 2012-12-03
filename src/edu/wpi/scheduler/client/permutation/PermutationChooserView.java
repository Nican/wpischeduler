package edu.wpi.scheduler.client.permutation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.SchedulePermutation;
import edu.wpi.scheduler.client.controller.SectionProducer;
import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.client.controller.StudentScheduleEvent;
import edu.wpi.scheduler.client.controller.StudentScheduleEventHandler;

public class PermutationChooserView extends Composite implements StudentScheduleEventHandler {

	private static PermutationChooserViewUiBinder uiBinder = GWT
			.create(PermutationChooserViewUiBinder.class);

	interface PermutationChooserViewUiBinder extends
			UiBinder<Widget, PermutationChooserView> {
	}

	@UiField
	public VerticalPanel thumbList;

	@UiField
	public SimplePanel body;

	@UiField
	public VerticalPanel courseList;

	public WeekCourseView weekCourseView;

	boolean initalized = false;

	private final StudentSchedule studentSchedule;

	public PermutationChooserView(StudentSchedule studentSchedule) {
		this.studentSchedule = studentSchedule;

		initWidget(uiBinder.createAndBindUi(this));

		courseList.setWidth("100%");

		getElement().getStyle().setLeft(0, Unit.PX);
		getElement().getStyle().setRight(0, Unit.PX);
		getElement().getStyle().setTop(0, Unit.PX);
		getElement().getStyle().setBottom(0, Unit.PX);
		getElement().getStyle().setPosition(Position.ABSOLUTE);
	}

	public void updateCourses() {

		courseList.clear();

		for (SectionProducer producer : studentSchedule.sectionProducers) {
			courseList.add(new CourseItem(studentSchedule, producer));
		}

	}

	public void updatePermutations() {

		thumbList.clear();
		body.clear();
		weekCourseView = null;

		int count = 0;

		for (SchedulePermutation permutation : studentSchedule.getSchedulePermutations()) {
			addPermutation(permutation);

			if (count++ > 30)
				break;
		}

	}

	private void addPermutation(final SchedulePermutation permutation) {
		PermutationCanvas canvas = new PermutationCanvas(permutation);
		canvas.setSize("150px", "150px");
		canvas.paint();

		thumbList.add(canvas.canvas);

		canvas.canvas.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setPermutation(permutation);
			}
		});

		if (weekCourseView == null) {
			setPermutation(permutation);
		}
	}

	public void setPermutation(SchedulePermutation permutation) {
		body.clear();
		weekCourseView = new WeekCourseView(permutation);
		body.add(weekCourseView);
	}

	public void update() {
		updateCourses();
		updatePermutations();

	}

	@Override
	protected void onLoad() {
		studentSchedule.addStudentScheduleHandler(this);
	}

	@Override
	protected void onUnload() {
		studentSchedule.removeStudentScheduleHandler(this);
	}

	@Override
	public void onCoursesChanged(StudentScheduleEvent studentScheduleEvent) {
		this.updatePermutations();
	}
}
