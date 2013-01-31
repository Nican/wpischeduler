package edu.wpi.scheduler.client.permutation;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.SchedulePermutation;
import edu.wpi.scheduler.client.controller.SchedulePermutationController;
import edu.wpi.scheduler.client.controller.SchedulePermutationController.PermutationUpdateEventHandler;
import edu.wpi.scheduler.client.controller.ScheduleConflictController;
import edu.wpi.scheduler.client.controller.SectionProducer;
import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.client.controller.StudentScheduleEvent;
import edu.wpi.scheduler.client.controller.StudentScheduleEventHandler;

public class PermutationChooserView extends Composite implements StudentScheduleEventHandler, PermutationSelectEventHandler, ScrollHandler, PermutationUpdateEventHandler {

	private static PermutationChooserViewUiBinder uiBinder = GWT
			.create(PermutationChooserViewUiBinder.class);

	interface PermutationChooserViewUiBinder extends
			UiBinder<Widget, PermutationChooserView> {
	}

	@UiField(provided = true)
	public PermutationCanvasList thumbList;

	@UiField
	public ScrollPanel thumbScroll;

	@UiField(provided = true)
	public PermutationScheduleView body;

	@UiField
	public VerticalPanel courseList;

	boolean initalized = false;

	private final StudentSchedule studentSchedule;
	public final PermutationController permutationController;

	public SchedulePermutationController producer;

	public PermutationChooserView(StudentSchedule studentSchedule) {
		this.permutationController = new PermutationController(studentSchedule);
		this.studentSchedule = studentSchedule;
		this.thumbList = new PermutationCanvasList(permutationController);
		this.body = new PermutationScheduleView(permutationController);

		initWidget(uiBinder.createAndBindUi(this));

		courseList.setWidth("100%");

		thumbScroll.addScrollHandler(this);

		getElement().getStyle().setLeft(0, Unit.PX);
		getElement().getStyle().setRight(0, Unit.PX);
		getElement().getStyle().setTop(0, Unit.PX);
		getElement().getStyle().setBottom(0, Unit.PX);
		getElement().getStyle().setPosition(Position.ABSOLUTE);
	}

	public void updateCourses() {

		courseList.clear();

		for (SectionProducer producer : studentSchedule.sectionProducers) {
			courseList.add(new CourseItem(permutationController, producer));
		}

	}

	public void updatePermutations() {
		thumbList.clear();
		int count = 0;

		if (producer != null) {
			producer.cancel();
			producer.removeUpdateHandler(this);
			producer = null;
		}

		producer = new SchedulePermutationController(studentSchedule);
		List<SchedulePermutation> permutations = producer.getPermutations();

		producer.addUpdateHandler(this);
		permutationController.updateTimeRange(permutations);

		for (SchedulePermutation permutation : permutations) {
			thumbList.addPermutation(permutation);
		}

		System.out.println("Found a total of " + count + " permutations.");

		if (permutations.size() > 0)
			permutationController.selectPermutation(permutations.get(0));

	}

	public void setPermutation(SchedulePermutation permutation) {
		body.setPermutation(permutation);
	}

	public void update() {
		permutationController.update();
		updateCourses();
		updatePermutations();
	}

	@Override
	protected void onLoad() {
		studentSchedule.addStudentScheduleHandler(this);
		permutationController.addSelectListner(this);
	}

	@Override
	protected void onUnload() {
		studentSchedule.removeStudentScheduleHandler(this);
		permutationController.removeSelectListner(this);
	}

	@Override
	public void onCoursesChanged(StudentScheduleEvent studentScheduleEvent) {
		this.updatePermutations();
	}

	@Override
	public void onPermutationSelected(PermutationSelectEvent permutation) {
		setPermutation(permutationController.selectedPermutation);
	}

	/**
	 * If we are near the end of the scroll, we are going to check if there are
	 * any more images to be loaded
	 */
	@Override
	public void onScroll(ScrollEvent event) {

		System.out.println(thumbScroll.getVerticalScrollPosition() + " - " + thumbScroll.getMaximumVerticalScrollPosition());

		if (thumbScroll.getVerticalScrollPosition() + 300 < thumbScroll.getMaximumVerticalScrollPosition())
			return;

		updateThumbnails();
	}

	@Override
	public void onPermutationUpdated() {
		if (thumbList.childCount() < 20)
			updateThumbnails();
	}

	public void updateThumbnails() {
		List<SchedulePermutation> permutations = producer.getPermutations();
		int thumbSize = thumbList.childCount();
		int permutationSize = permutations.size();
		
		System.out.println("Found " + permutationSize + " permutations");

		if (permutationSize <= thumbSize)
			return;

		int limit = Math.min(thumbSize + 10, permutationSize);

		for (int i = thumbSize; i < limit; i++) {
			thumbList.addPermutation(permutations.get(i));
		}
	}

}
