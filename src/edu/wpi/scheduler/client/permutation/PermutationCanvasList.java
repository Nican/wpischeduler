package edu.wpi.scheduler.client.permutation;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.wpi.scheduler.client.controller.SchedulePermutation;

public class PermutationCanvasList extends VerticalPanel implements TimeRangeChangEventHandler {

	private PermutationController controller;
	private Canvas background;

	public PermutationCanvasList(PermutationController controller) {
		this.controller = controller;
		this.updateBackground();
	}

	public void addPermutation(final SchedulePermutation permutation) {
		
		PermutationCanvas canvas = new PermutationCanvas(controller, permutation);
		canvas.setSize("150px", "150px");
		canvas.paint(background);

		add(canvas.canvas);
		
		canvas.canvas.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				controller.selectPermutation(permutation);
			}
		});

	}
	
	public int childCount(){
		return this.getChildren().size();
	}

	@Override
	protected void onLoad() {
		this.controller.addTimeChangeListner(this);
		updateBackground();
	}

	@Override
	protected void onUnload() {
		this.controller.removeTimeChangeListner(this);
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
			//Draw in the middle: http://stackoverflow.com/questions/9311428/draw-single-pixel-line-in-html5-canvas
			double yValue = Math.floor((hour - controller.getStartHour()) * heightPerHour) + 0.5;
			
			if( hour == 12.0 ){
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
}
