package edu.wpi.scheduler.client.permutation;

import java.util.List;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.ToggleButton;

import edu.wpi.scheduler.client.controller.ProducerUpdateEvent.UpdateType;
import edu.wpi.scheduler.client.controller.SchedulePermutation;
import edu.wpi.scheduler.client.controller.ScheduleProducer;
import edu.wpi.scheduler.client.controller.ScheduleProducer.CoursePair;
import edu.wpi.scheduler.client.controller.ScheduleProducer.ProducerEventHandler;
import edu.wpi.scheduler.shared.model.DayOfWeek;
import edu.wpi.scheduler.shared.model.Period;
import edu.wpi.scheduler.shared.model.Section;
import edu.wpi.scheduler.shared.model.Term;
import edu.wpi.scheduler.shared.model.Time;

public class PermutationCanvasList extends FlowPanel implements TimeRangeChangEventHandler, ProducerEventHandler, ScrollHandler {

	private PermutationController controller;
	private Canvas background;

	private final FlowPanel thumbList = new FlowPanel();
	private final ScrollPanel scroll = new ScrollPanel(thumbList);
	private final ToggleButton favoriteButtom = new ToggleButton("Favorites (0)");

	public static final double favoriteButtonSize = 20.0;

	public PermutationCanvasList(PermutationController controller) {
		this.controller = controller;
		updateBackground();

		add(favoriteButtom, getElement());
		add(scroll, getElement());

		favoriteButtom.getElement().getStyle().setTextAlign(TextAlign.CENTER);
		favoriteButtom.getElement().getStyle().setLeft(0.0, Unit.PX);
		favoriteButtom.getElement().getStyle().setRight(0.0, Unit.PX);
		favoriteButtom.getElement().getStyle().setTop(0.0, Unit.PX);
		favoriteButtom.getElement().getStyle().setHeight(favoriteButtonSize, Unit.PX);

		scroll.getElement().getStyle().setPosition(Position.ABSOLUTE);
		scroll.getElement().getStyle().setLeft(0.0, Unit.PX);
		scroll.getElement().getStyle().setRight(0.0, Unit.PX);
		scroll.getElement().getStyle().setTop(favoriteButtonSize + 8, Unit.PX);
		scroll.getElement().getStyle().setBottom(0.0, Unit.PX);

		scroll.addScrollHandler(this);

	}

	public void addPermutation(final SchedulePermutation permutation) {
		
		Canvas canvas = Canvas.createIfSupported();
		canvas.setCoordinateSpaceHeight( background.getCoordinateSpaceHeight() );
		canvas.setCoordinateSpaceWidth( background.getCoordinateSpaceWidth() );
		canvas.setStyleName("permutationCanvas");
		
		Context2d context = canvas.getContext2d();

		double columnWidth = ((double) canvas.getCoordinateSpaceWidth()) / controller.getValidDaysOfWeek().size();
		double hourHeight = ((double) canvas.getCoordinateSpaceHeight()) / (controller.getEndHour() - controller.getStartHour());
		List<DayOfWeek> daysOfWeek = controller.getValidDaysOfWeek();

		// context.setFillStyle("black");
		context.drawImage(background.getCanvasElement(), 0.0, 0.0);
		
		//So many For loops... But it is not that bad.
		for (Section section : permutation.sections) {
			context.setFillStyle(controller.getCourseColor(section.course));
			
			for (Term term : section.getTerms()) {
				for (int i = 0; i < daysOfWeek.size(); i++) {
					for (Period period : section.periods) {
						if (period.days.contains(daysOfWeek.get(i))) {

							double start = getDayProgress(period.startTime);
							double end = getDayProgress(period.endTime);

							context.fillRect(
									i * columnWidth + columnWidth * term.ordinal() * 0.25,
									start * hourHeight,
									columnWidth * 0.25,
									(end - start) * hourHeight);
						}
					}
				}
			}
		}
			
		thumbList.add(canvas);
		
		canvas.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				controller.selectPermutation(permutation);
			}
		});

	}

	private double getDayProgress(Time time) {
		return ((double) time.hour) - controller.getStartHour() + ((double) time.minutes) / 60;
	}

	/**
	 * If we are near the end of the scroll, we are going to check if there are
	 * any more images to be loaded
	 */
	@Override
	public void onScroll(ScrollEvent event) {
		if (scroll.getVerticalScrollPosition() + 300 < scroll.getMaximumVerticalScrollPosition())
			return;

		updateThumbnails();
	}

	public int childCount() {
		return this.getChildren().size();
	}

	@Override
	protected void onLoad() {
		controller.addTimeChangeListner(this);
		controller.addProduceHandler(this);
		updateBackground();
		updateThumbnails();
	}

	@Override
	protected void onUnload() {
		controller.removeTimeChangeListner(this);
		controller.addProduceHandler(this);
	}

	@Override
	public void onTimeRangeChange(TimeRangeChangeEvent timeRangeChangeEvent) {
		this.updateBackground();
	}

	public void updateBackground() {
		background = Canvas.createIfSupported();

		background.getElement().setAttribute("width", "150px");
		background.getElement().setAttribute("height", "150px");
		Context2d context = background.getContext2d();
		double heightPerHour = 150.0 / (controller.getEndHour() - controller.getStartHour());

		context.setLineWidth(1.0);

		for (double hour = controller.getStartHour(); hour <= controller.getEndHour(); hour += 1.0) {
			// Draw in the middle:
			// http://stackoverflow.com/questions/9311428/draw-single-pixel-line-in-html5-canvas
			double yValue = Math.floor((hour - controller.getStartHour()) * heightPerHour) + 0.5;

			if (hour == 12.0) {
				context.setStrokeStyle(CssColor.make(200, 200, 200));
			} else {
				context.setStrokeStyle(CssColor.make(230, 230, 230));
			}

			context.beginPath();
			context.moveTo(0.0, yValue);
			context.lineTo(150.0, yValue);
			context.stroke();
		}

		double weekDaySize = (double) controller.getValidDaysOfWeek().size();

		for (double i = 1.0; i < weekDaySize; i += 1.0) {
			context.beginPath();
			context.moveTo(i * (150 / weekDaySize), 0.0);
			context.lineTo(i * (150 / weekDaySize), 150.0);
			context.stroke();
		}
	}

	@Override
	public void onPermutationUpdated(UpdateType type) {
		if (type == UpdateType.NEW)
			thumbList.clear();

		if (thumbList.getWidgetCount() < 20)
			updateThumbnails();
		
		if( type == UpdateType.FINISH ){
			ScheduleProducer producer = controller.getProducer();
			
			if( producer.getPermutations().size() == 0 ){
				thumbList.clear();
				CoursePair pair = producer.getConflictCourse();
				
				String msg = "Can not find any schedules!";
				
				if( pair != null )
					msg += pair.course1 + " - " + pair.course2;
				
				thumbList.add( new Label( msg ) );
			}
		}
	}

	public void updateThumbnails() {
		List<SchedulePermutation> permutations = controller.getProducer().getPermutations();
		int thumbSize = thumbList.getWidgetCount();
		int permutationSize = permutations.size();

		if (permutationSize <= thumbSize)
			return;

		int limit = Math.min(thumbSize + 20, permutationSize);

		for (int i = thumbSize; i < limit; i++) {
			addPermutation(permutations.get(i));
		}
	}

}
