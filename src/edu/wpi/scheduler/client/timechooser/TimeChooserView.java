package edu.wpi.scheduler.client.timechooser;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.StudentSchedule;

public class TimeChooserView extends Composite
{
	private static TimeChooserViewUiBinder uiBinder = GWT.create(TimeChooserViewUiBinder.class);
	interface TimeChooserViewUiBinder extends UiBinder<Widget, TimeChooserView> {}

	@UiField SplitLayoutPanel SplitPanel;
	TimeTablesGrid timeChoosers;
	VerticalPanel sectionProducers;
	
	public TimeChooserView( final StudentSchedule studentSchedule) 
	{	
		initWidget(uiBinder.createAndBindUi(this));
		getElement().getStyle().setLeft(0, Unit.PX);
		getElement().getStyle().setRight(0, Unit.PX);
		getElement().getStyle().setTop(0, Unit.PX);
		getElement().getStyle().setBottom(0, Unit.PX);
		getElement().getStyle().setPosition(Position.ABSOLUTE);
		
		timeChoosers = new TimeTablesGrid(studentSchedule);
		sectionProducers = new VerticalPanel();
		SplitPanel.forceLayout();
		SplitPanel.addEast(sectionProducers, 200);
		SplitPanel.add(timeChoosers);
		SplitPanel.onResize();
	}
}
