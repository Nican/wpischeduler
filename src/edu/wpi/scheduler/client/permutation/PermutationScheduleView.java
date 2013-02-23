package edu.wpi.scheduler.client.permutation;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.ProducerUpdateEvent.UpdateType;
import edu.wpi.scheduler.client.controller.ScheduleProducer;
import edu.wpi.scheduler.client.controller.ScheduleProducer.ProducerEventHandler;
import edu.wpi.scheduler.client.permutation.view.GridCourseView;
import edu.wpi.scheduler.client.permutation.view.WeekCourseView;

public class PermutationScheduleView extends ComplexPanel implements PermutationSelectEventHandler, ProducerEventHandler, RequiresResize {

	enum ViewMode {
		GRID,
		SINGLE,
		PROGRESS
	}

	private PermutationController controller;

	private ViewMode viewMode = null;
	private ViewMode selectedViewMode = ViewMode.GRID;
	
	Element body = DOM.createDiv();
	public Widget bodyWidget;
	Button progressButton;

	public PermutationScheduleView(PermutationController controller) {
		setElement(DOM.createDiv());
		this.controller = controller;
		Element header = DOM.createDiv();

		body.getStyle().setTop(32.0, Unit.PX);
		body.getStyle().setRight(0.0, Unit.PX);
		body.getStyle().setLeft(0.0, Unit.PX);
		body.getStyle().setBottom(0.0, Unit.PX);
		body.getStyle().setPosition(Position.ABSOLUTE);

		Button gridButton = new Button("Grid", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setView(ViewMode.GRID);
			}
		});

		Button singleButton = new Button("Single", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setView(ViewMode.SINGLE);
			}
		});

		progressButton = new Button("0 Schedules...", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setView(ViewMode.PROGRESS);
			}
		});
		progressButton.getElement().getStyle().setFloat(Float.RIGHT);

		add(gridButton, header);
		add(singleButton, header);
		add(progressButton, header);

		getElement().appendChild(header);
		getElement().appendChild(body);

		setView(ViewMode.GRID);
	}

	@Override
	protected void onLoad() {
		controller.addSelectListner(this);
		controller.addProduceHandler(this);
		updateProgressButton();
		update();
	}

	@Override
	protected void onUnload() {
		controller.removeSelectListner(this);
		controller.removeProduceHandler(this);
	}
	
	public void update(){
		ViewMode target = selectedViewMode;
		ScheduleProducer producer = controller.getProducer();
		int size = producer.getPermutations().size();

		if (size == 0)
			target = ViewMode.PROGRESS;
		
		if (target == viewMode)
			return;

		if (bodyWidget != null)
			remove(bodyWidget);

		bodyWidget = getNewView(target);

		if (bodyWidget == null)
			return;

		add(bodyWidget, body);
		viewMode = target;
		
	}

	public void setView(ViewMode mode) {
		selectedViewMode = mode;
		update();
	}

	private Widget getNewView(ViewMode mode) {
		switch (mode) {
		case GRID:
			return new GridCourseView(controller);
		case SINGLE:
			return new WeekCourseView(controller);
		case PROGRESS:
			return new CanvasProgress(controller);
		}

		return null;
	}

	@Override
	public void onPermutationSelected(PermutationSelectEvent permutation) {
		if (viewMode == ViewMode.PROGRESS) {
			setView(ViewMode.GRID);
		}
	}

	public void updateProgressButton() {
		ScheduleProducer producer = controller.getProducer();
		int size = producer.getPermutations().size();

		String text = (size > 1000 ? (size / 1000) + "k" : size) + " Schedules";

		if (producer.isActive())
			text += "...";

		if (!progressButton.getText().equals(text))
			progressButton.setText(text);
	}

	@Override
	public void onPermutationUpdated(UpdateType type) {
		updateProgressButton();
		update();
	}

	@Override
	public void onResize() {
		if( bodyWidget instanceof RequiresResize ){
			((RequiresResize) bodyWidget).onResize();
		}
	}

}
