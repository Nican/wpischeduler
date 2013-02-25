package edu.wpi.scheduler.client.permutation;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.FavoriteEventHandler;
import edu.wpi.scheduler.client.controller.ProducerUpdateEvent.UpdateType;
import edu.wpi.scheduler.client.controller.SchedulePermutation;
import edu.wpi.scheduler.client.controller.ScheduleProducer;
import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.client.controller.ScheduleProducer.ProducerEventHandler;
import edu.wpi.scheduler.client.permutation.view.CanvasProgress;
import edu.wpi.scheduler.client.permutation.view.ConflictResolverWidget;
import edu.wpi.scheduler.client.permutation.view.DetailedView;
import edu.wpi.scheduler.client.permutation.view.GridCourseView;
import edu.wpi.scheduler.client.permutation.view.WeekCourseView;

public class PermutationScheduleView extends ComplexPanel implements PermutationSelectEventHandler, ProducerEventHandler, RequiresResize, FavoriteEventHandler {

	enum ViewMode {
		GRID,
		SINGLE,
		PROGRESS,
		CONFLICT,
		DETAIL
	}

	private PermutationController controller;

	private ViewMode viewMode = null;
	private ViewMode selectedViewMode = ViewMode.GRID;
	
	Element body = DOM.createDiv();
	public Widget bodyWidget;
	ToggleButton favoriteButton;

	public PermutationScheduleView(final PermutationController controller) {
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

		Button singleButton = new Button("Detail", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setView(ViewMode.DETAIL);
			}
		});

		favoriteButton = new ToggleButton("Favorite (insert star)", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				StudentSchedule studentSchedule = controller.getStudentSchedule();
				SchedulePermutation permutation = controller.getSelectedPermutation();
				
				if( permutation == null )
					return;
				
				if(!studentSchedule.containsFavorite(permutation))
					studentSchedule.addFavorite(permutation);
				else
					studentSchedule.removeFavorite(permutation);
			}
		});
		favoriteButton.getElement().getStyle().setFloat(Float.RIGHT);

		add(gridButton, header);
		add(singleButton, header);
		add(favoriteButton, header);

		getElement().appendChild(header);
		getElement().appendChild(body);

		setView(ViewMode.GRID);
	}

	@Override
	protected void onLoad() {
		controller.addSelectListner(this);
		controller.addProduceHandler(this);
		controller.getStudentSchedule().addFavoriteHandler(this);
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
		StudentSchedule studentSchedule = controller.getStudentSchedule();
		SchedulePermutation permutation = controller.getSelectedPermutation();
		int size = producer.getPermutations().size();
		
		if(permutation != null )
			favoriteButton.setDown(studentSchedule.containsFavorite(permutation));

		if (size == 0){
			target = producer.isActive() ? ViewMode.PROGRESS : ViewMode.CONFLICT;
		}
			
		if (target == viewMode)
			return;

		if (bodyWidget != null)
			remove(bodyWidget);

		bodyWidget = getNewView(target);

		if (bodyWidget == null)
			return;

		add(bodyWidget, body);
		viewMode = target;
		
		favoriteButton.setVisible( target == ViewMode.SINGLE || target == ViewMode.GRID || target == ViewMode.DETAIL );
		controller.setSelectedSection(null);
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
		case CONFLICT:
			return new ConflictResolverWidget(controller);
		case DETAIL:
			return new DetailedView(controller);
		}

		return null;
	}

	@Override
	public void onPermutationSelected(PermutationSelectEvent permutation) {
		if (viewMode == ViewMode.PROGRESS) {
			setView(ViewMode.GRID);
		}
		update();
	}
	
	/**
	 * Constant change of the view can be a bit ugly for the eyes
	 * We are putting a delay
	 */
	Timer updateTimer = new Timer() {
		@Override
		public void run() {
			update();
		}
	};

	@Override
	public void onPermutationUpdated(UpdateType type) {
		updateTimer.schedule(50);
	}

	@Override
	public void onResize() {
		if( bodyWidget instanceof RequiresResize ){
			((RequiresResize) bodyWidget).onResize();
		}
	}

	@Override
	public void onFavoriteUpdate() {
		update();
	}

}
