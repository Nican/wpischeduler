package edu.wpi.scheduler.client.tabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.MainView;
import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.client.controller.StudentScheduleEvent;
import edu.wpi.scheduler.client.controller.StudentScheduleEventHandler;
import edu.wpi.scheduler.client.courseselection.CourseSelectionTab;
import edu.wpi.scheduler.client.permutation.PermutationTab;
import edu.wpi.scheduler.client.timechooser.TimeTab;
import edu.wpi.scheduler.client.welcome.WelcomeTab;

public class TabList extends Composite{

	private static TabListUiBinder uiBinder = GWT.create(TabListUiBinder.class);

	interface TabListUiBinder extends UiBinder<Widget, TabList> {
	}
	
	@UiField
	HorizontalPanel horizontalPanel;
	
	final MainView mainView;
	
	final WelcomeTab welcome;
	final CourseSelectionTab courseSelection;
	final TimeTab timeChooser;
	
	BaseTab lastSelected;

	public TabList( MainView mainView, StudentSchedule studentSchedule ) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.mainView = mainView;
		
		welcome = new WelcomeTab(studentSchedule);
		courseSelection = new CourseSelectionTab(studentSchedule);
		timeChooser = new TimeTab(studentSchedule);
		
		//addTab( welcome );
		addTab( courseSelection  );
		addTab( timeChooser );
		addTab( new PermutationTab(studentSchedule)  );		
	}
	
	public Widget getHomeView(){
		return courseSelection.getBody();
	}
	
	public void addTab( final BaseTab baseTab ){
		this.horizontalPanel.add( baseTab );
		
		horizontalPanel.setCellHeight(baseTab, "100%");
		
		baseTab.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				mainView.setBody( baseTab.getBody() );
				baseTab.updateView();
				lastSelected = baseTab;
				update();
			}
		});
		
		if( lastSelected == null )
			lastSelected = baseTab;
		
		update();
	}
	
	public void update(){
		int count = horizontalPanel.getWidgetCount();
		String useClass;
		
		for( int i = 0; i < count; i++ ){
			Widget widget = horizontalPanel.getWidget(i);
			Style style = widget.getElement().getStyle();
			
			style.setZIndex(count-i);
			
			useClass = (lastSelected == widget) 
					? "sched-TopButtonSelected" : "sched-TopButton";
			widget.setStyleName(useClass);
			
			useClass = (((FocusWidget) widget).isEnabled())
					? "sched-TopButtonEnabled" : "sched-TopButtonDisabled";
			widget.addStyleName(useClass);
		}
		
		/*
		int count = horizontalPanel.getWidgetCount();
		String bgColor = "#FFFFFF";
		
		for( int i = 0; i < count; i++ ){
			Widget widget = horizontalPanel.getWidget(i);
			Style style = widget.getElement().getStyle();
			
			style.setZIndex(count-i);
			
			
			if( lastSelected == widget){
				style.setBackgroundColor("#EFFFEF");
				bgColor = null;
			} else {
				style.setBackgroundColor(bgColor);
			}
			
			widget.setStyleName( i == count-1 ? "sched-TopButton" : "sched-TopButton-notLast");
		}
		*/
	}
}
