package edu.wpi.scheduler.client.permutation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.FavoriteEvent;
import edu.wpi.scheduler.client.controller.FavoriteEventHandler;
import edu.wpi.scheduler.client.controller.SectionProducer;
import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.client.controller.FavoriteEvent.FavoriteEventType;
import edu.wpi.scheduler.client.courseselection.CourseAddAnimation;

public class PermutationChooserView extends Composite implements FavoriteEventHandler {

	private static PermutationChooserViewUiBinder uiBinder = GWT
			.create(PermutationChooserViewUiBinder.class);

	interface PermutationChooserViewUiBinder extends
			UiBinder<Widget, PermutationChooserView> {
	}

	@UiField(provided = true)
	public PermutationCanvasList thumbList;

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

		getElement().getStyle().setLeft(0, Unit.PX);
		getElement().getStyle().setRight(0, Unit.PX);
		getElement().getStyle().setTop(0, Unit.PX);
		getElement().getStyle().setBottom(0, Unit.PX);
		getElement().getStyle().setPosition(Position.ABSOLUTE);
	}

	public void update() {
		courseList.clear();
		boolean first = true;

		for (SectionProducer producer : studentSchedule.sectionProducers) {
			CourseItem item = new CourseItem(permutationController, producer);
			
			courseList.add( item );
			
			if( first ){
				item.togglePeriods();
				first = false;
			}
		}

		scheduleView.update();
	}
	
	@Override
	protected void onLoad() {
		studentSchedule.addFavoriteHandler(this);
		update();
	}

	@Override
	protected void onUnload() {
		studentSchedule.removeFavoriteHandler(this);
	}

	@Override
	public void onFavoriteUpdate(FavoriteEvent favoriteEvent) {
		if( favoriteEvent.type == FavoriteEventType.ADD ){
			CourseAddAnimation anim = new CourseAddAnimation(thumbList.favoriteButton.getElement(), scheduleView.body);
			anim.run(500);
		}
	}

}
