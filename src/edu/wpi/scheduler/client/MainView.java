package edu.wpi.scheduler.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.StudentSchedule;
import edu.wpi.scheduler.client.tabs.TabList;

public class MainView extends Composite {

	private static MainViewUiBinder uiBinder = GWT
			.create(MainViewUiBinder.class);

	interface MainViewUiBinder extends UiBinder<Widget, MainView> {
	}
	
	@UiField
	DockLayoutPanel layoutPanel;
	
	@UiField
	SimplePanel bodyPanel;
	
	@UiField(provided=true)
	TabList tabList;

	public MainView( final StudentSchedule studentSchedule) {
		tabList = new TabList(this, studentSchedule);
		
		initWidget(uiBinder.createAndBindUi(this));
		
		//layoutPanel.add( new CourseSelectorView(studentSchedule)  );
		bodyPanel.add( tabList.getHomeView() );
		
		
		getElement().getStyle().setLeft(0, Unit.PX);
		getElement().getStyle().setRight(0, Unit.PX);
		getElement().getStyle().setTop(0, Unit.PX);
		getElement().getStyle().setBottom(0, Unit.PX);
		getElement().getStyle().setPosition(Position.ABSOLUTE);
	}

	public void setBody(Widget body) {
		bodyPanel.remove(bodyPanel.getWidget());
		bodyPanel.add(body);
	}

}
