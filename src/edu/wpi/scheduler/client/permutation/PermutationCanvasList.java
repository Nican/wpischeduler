package edu.wpi.scheduler.client.permutation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.wpi.scheduler.client.controller.SchedulePermutation;

public class PermutationCanvasList extends VerticalPanel implements TimeRangeChangEventHandler {

	private PermutationController controller;
	private PermutationCanvasBackground canvasBackground;

	public PermutationCanvasList(PermutationController controller) {
		this.controller = controller;
		this.updateBackground();
	}

	public void addPermutation(final SchedulePermutation permutation) {
		
		PermutationCanvas canvas = new PermutationCanvas(controller, permutation);
		canvas.setSize("150px", "150px");
		canvas.paint(canvasBackground.canvasBackground);

		add(canvas.canvas);
		
		canvas.canvas.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				controller.selectPermutation(permutation);
			}
		});

	}

	@Override
	protected void onLoad() {
		this.controller.addTimeChangeListner(this);
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
		this.canvasBackground = new PermutationCanvasBackground(controller);
	}
}
