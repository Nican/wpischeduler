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
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.ProducerUpdateEvent.UpdateType;
import edu.wpi.scheduler.client.controller.ScheduleProducer;
import edu.wpi.scheduler.client.controller.ScheduleProducer.ProducerEventHandler;
import edu.wpi.scheduler.client.permutation.view.GridCourseView;
import edu.wpi.scheduler.client.permutation.view.WeekCourseView;

public class PermutationScheduleView extends ComplexPanel implements PermutationSelectEventHandler, ProducerEventHandler {

	enum ViewMode {
		GRID,
		SINGLE,
		PROGRESS
	}

	private PermutationController controller;

	public ViewMode viewMode = ViewMode.GRID;
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
				viewMode = ViewMode.GRID;
				createNewBody();
			}
		});

		Button singleButton = new Button("Single", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				viewMode = ViewMode.SINGLE;
				createNewBody();
			}
		});

		progressButton = new Button("0 Schedules...", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				viewMode = ViewMode.PROGRESS;
				createNewBody();
			}
		});
		progressButton.getElement().getStyle().setFloat(Float.RIGHT);

		add(gridButton, header);
		add(singleButton, header);
		add(progressButton, header);

		getElement().appendChild(header);
		getElement().appendChild(body);

		createNewBody();
	}

	@Override
	protected void onLoad() {
		controller.addSelectListner(this);
		controller.addProduceHandler(this);
		updateProgressButton();
	}

	@Override
	protected void onUnload() {
		controller.removeSelectListner(this);
		controller.removeProduceHandler(this);
	}

	public void createNewBody() {
		if (bodyWidget != null)
			remove(bodyWidget);

		bodyWidget = getNewView();

		if (bodyWidget == null)
			return;

		add(bodyWidget, body);
	}

	private Widget getNewView() {
		switch (viewMode) {
		case GRID:
			return new GridCourseView(controller);
		case SINGLE:
			return new WeekCourseView(controller);
		case PROGRESS:
			return new CanvasProgress(controller);
		}

		return null;
	}

	public void resetView() {
		ScheduleProducer producer = controller.getProducer();

		if (producer.getPermutations().size() > 0)
			viewMode = ViewMode.PROGRESS;
		else
			viewMode = ViewMode.GRID;

		createNewBody();
	}

	@Override
	public void onPermutationSelected(PermutationSelectEvent permutation) {
		if (viewMode == ViewMode.PROGRESS) {
			viewMode = ViewMode.GRID;
			createNewBody();
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

	}

}
