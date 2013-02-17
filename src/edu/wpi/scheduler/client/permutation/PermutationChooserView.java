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

import edu.wpi.scheduler.client.controller.ProducerUpdateEvent.UpdateType;
import edu.wpi.scheduler.client.controller.SchedulePermutation;
import edu.wpi.scheduler.client.controller.ScheduleProducer.ProducerEventHandler;
import edu.wpi.scheduler.client.controller.SectionProducer;
import edu.wpi.scheduler.client.controller.StudentSchedule;

public class PermutationChooserView extends Composite implements ScrollHandler, ProducerEventHandler {

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
	public final PermutationScheduleView scheduleView;

	@UiField
	public VerticalPanel courseList;

	boolean initalized = false;

	private final StudentSchedule studentSchedule;
	public final PermutationController permutationController;

	public PermutationChooserView(StudentSchedule studentSchedule) {
		this.permutationController = new PermutationController(studentSchedule);
		this.studentSchedule = studentSchedule;
		this.thumbList = new PermutationCanvasList(permutationController);
		this.scheduleView = new PermutationScheduleView(permutationController);

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

	public void update() {
		updateCourses();
	}

	@Override
	protected void onLoad() {
		permutationController.addProduceHandler(this);
		update();
		updateThumbnails();
	}

	@Override
	protected void onUnload() {
		permutationController.addProduceHandler(this);
	}

	/**
	 * If we are near the end of the scroll, we are going to check if there are
	 * any more images to be loaded
	 */
	@Override
	public void onScroll(ScrollEvent event) {
		if (thumbScroll.getVerticalScrollPosition() + 300 < thumbScroll.getMaximumVerticalScrollPosition())
			return;

		updateThumbnails();
	}

	@Override
	public void onPermutationUpdated(UpdateType type) {
		if( type == UpdateType.NEW)
			thumbList.clear();
		
		if (thumbList.childCount() < 20)
			updateThumbnails();
		
		List<SchedulePermutation> permutations = permutationController.getProducer().getPermutations();
		
		if( permutations.size() > 0 && permutationController.getSelectedPermutation() == null )
			permutationController.selectPermutation( permutations.get(0) );
	}

	public void updateThumbnails() {
		List<SchedulePermutation> permutations = permutationController.getProducer().getPermutations();
		int thumbSize = thumbList.childCount();
		int permutationSize = permutations.size();

		if (permutationSize <= thumbSize)
			return;

		int limit = Math.min(thumbSize + 20, permutationSize);

		for (int i = thumbSize; i < limit; i++) {
			thumbList.addPermutation(permutations.get(i));
		}
	}

}
