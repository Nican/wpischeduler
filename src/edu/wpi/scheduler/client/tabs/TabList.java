package edu.wpi.scheduler.client.tabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.MainView;
import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.client.courseselection.CourseSelectionTab;
import edu.wpi.scheduler.client.timechooser.TimeTab;

public class TabList extends Composite {

	private static TabListUiBinder uiBinder = GWT.create(TabListUiBinder.class);

	interface TabListUiBinder extends UiBinder<Widget, TabList> {
	}
	
	@UiField
	HorizontalPanel horizontalPanel;
	
	final MainView mainView;
	
	final CourseSelectionTab courseSelection;

	public TabList( MainView mainView, StudentSchedule studentSchedule ) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.mainView = mainView;
		
		courseSelection = new CourseSelectionTab(studentSchedule);
		
		addTab( courseSelection  );
		addTab( new TimeTab(studentSchedule)  );
	}
	
	public Widget getHomeView(){
		return courseSelection.getBody();
	}
	
	public void addTab( final BaseTab baseTab ){
		this.horizontalPanel.add( baseTab );
		
		baseTab.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				mainView.setBody( baseTab.getBody() );
			}
		});
	}


}
