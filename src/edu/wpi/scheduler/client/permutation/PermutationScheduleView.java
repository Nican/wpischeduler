package edu.wpi.scheduler.client.permutation;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;

import edu.wpi.scheduler.client.controller.SchedulePermutation;
import edu.wpi.scheduler.client.permutation.view.GridCourseView;
import edu.wpi.scheduler.client.permutation.view.WeekCourseView;

public class PermutationScheduleView extends ComplexPanel {
	
	enum ViewMode {
		GRID,
		SINGLE		
	}
	
	private PermutationController controller;
	private SchedulePermutation permutation;
	
	public ComplexPanel courseView;
	
	public ViewMode viewMode = ViewMode.GRID;
	
	Element body = DOM.createDiv();

	public PermutationScheduleView( PermutationController controller ) {
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
				updateBody();				
			}
		});
		
		Button singleButton = new Button("Single", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				viewMode = ViewMode.SINGLE;
				updateBody();	
			}
		});
		
		add( gridButton, header );
		add( singleButton, header );
		
		getElement().appendChild(header);
		getElement().appendChild(body);		
	}
	
	public void setPermutation(SchedulePermutation permutation) {
		this.permutation = permutation;
		this.updateBody();
	}
	
	
	public void updateBody(){
		if( this.permutation == null )
			return;
		
		if( courseView != null )
			remove(courseView);
		
		courseView = getNewView();
		
		add( courseView, body );
	}
	
	private ComplexPanel getNewView(){
		switch(viewMode){
		case GRID:
			return new GridCourseView(controller, permutation);
		case SINGLE:
			return new WeekCourseView(controller, permutation);
		}
		
		return null;
	}

}
