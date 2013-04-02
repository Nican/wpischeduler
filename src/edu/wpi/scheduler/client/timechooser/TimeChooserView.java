package edu.wpi.scheduler.client.timechooser;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.StudentSchedule;

public class TimeChooserView extends Composite implements ResizeHandler 
{
	private static TimeChooserViewUiBinder uiBinder = GWT.create(TimeChooserViewUiBinder.class);

	@UiField SimplePanel ATerm;
	@UiField AbsolutePanel ATermHours;
	@UiField AbsolutePanel ATermDays;
	@UiField AbsolutePanel BTermGrid;
	@UiField AbsolutePanel BTermHours;
	@UiField AbsolutePanel BTermDays;
	@UiField AbsolutePanel CTermGrid;
	@UiField AbsolutePanel CTermHours;
	@UiField AbsolutePanel CTermDays;
	@UiField AbsolutePanel DTermGrid;
	@UiField AbsolutePanel DTermHours;
	@UiField AbsolutePanel DTermDays;
	
	TimeTable ATermTable; 

	HandlerRegistration resize;
	interface TimeChooserViewUiBinder extends UiBinder<Widget, TimeChooserView> {}

	public TimeChooserView(StudentSchedule studentSchedule) 
	{
		initWidget(uiBinder.createAndBindUi(this));
		getElement().getStyle().setLeft(0, Unit.PX);
		getElement().getStyle().setRight(0, Unit.PX);
		getElement().getStyle().setTop(0, Unit.PX);
		getElement().getStyle().setBottom(0, Unit.PX);
		getElement().getStyle().setPosition(Position.ABSOLUTE);
		
		ATermTable = new TimeTable(studentSchedule.ATermTimes); 
		ATerm.add(ATermTable);
		onResize(null);
	}
	
	@Override
	public void onLoad()
	{
		resize = Window.addResizeHandler(this);
		onResize(null);
	}
	
	@Override
	public void onUnload()
	{
		resize.removeHandler();
	}

	@Override
	public void onResize(ResizeEvent event) 
	{
		ATermTable.setSize(ATerm.getElement().getClientWidth(), ATerm.getElement().getClientHeight());
	}
		
		/*try
		{
			initWidget(uiBinder.createAndBindUi(this));

			ATermGrid.add(new TimeChooser(studentSchedule.ATermTimes, gridWidth, gridHeight));
			BTermGrid.add(new TimeChooser(studentSchedule.BTermTimes, gridWidth, gridHeight));
			CTermGrid.add(new TimeChooser(studentSchedule.CTermTimes, gridWidth, gridHeight));
			DTermGrid.add(new TimeChooser(studentSchedule.DTermTimes, gridWidth, gridHeight));

			ATermGrid.setPixelSize(gridWidth, gridHeight);
			ATermHours.setPixelSize(columnWidth, totalHeight);
			ATermDays.setPixelSize(totalWidth, rowHeight);
			BTermGrid.setPixelSize(gridWidth, gridHeight);
			BTermHours.setPixelSize(columnWidth, totalHeight);
			BTermDays.setPixelSize(totalWidth, rowHeight);
			CTermGrid.setPixelSize(gridWidth, gridHeight);
			CTermHours.setPixelSize(columnWidth, totalHeight);
			CTermDays.setPixelSize(totalWidth, rowHeight);
			DTermGrid.setPixelSize(gridWidth, gridHeight);
			DTermHours.setPixelSize(columnWidth, totalHeight);
			DTermDays.setPixelSize(totalWidth, rowHeight);

			for(int i = 0; i < TimeCell.NUM_DAYS; i++)
			{
				ATermDays.add(new Label(TimeCell.week[TimeCell.START_DAY+i].name()), (gridWidth/TimeCell.NUM_DAYS)*(i), 0);
				BTermDays.add(new Label(TimeCell.week[TimeCell.START_DAY+i].name()), (gridWidth/TimeCell.NUM_DAYS)*(i), 0);
				CTermDays.add(new Label(TimeCell.week[TimeCell.START_DAY+i].name()), (gridWidth/TimeCell.NUM_DAYS)*(i), 0);
				DTermDays.add(new Label(TimeCell.week[TimeCell.START_DAY+i].name()), (gridWidth/TimeCell.NUM_DAYS)*(i), 0);
			}

			Time t = new Time(TimeCell.START_HOUR, TimeCell.START_MIN);
			for(int i = 0; i <= TimeCell.NUM_HOURS * TimeCell.CELLS_PER_HOUR; i++)
			{
				ATermHours.add(new Label(t.toString()), 0, (int) ((gridHeight/(TimeCell.NUM_HOURS*TimeCell.CELLS_PER_HOUR))*( i + .5)));
				BTermHours.add(new Label(t.toString()), 0, (int) ((gridHeight/(TimeCell.NUM_HOURS*TimeCell.CELLS_PER_HOUR))*( i + .5)));
				CTermHours.add(new Label(t.toString()), 0, (int) ((gridHeight/(TimeCell.NUM_HOURS*TimeCell.CELLS_PER_HOUR))*( i + .5)));
				DTermHours.add(new Label(t.toString()), 0, (int) ((gridHeight/(TimeCell.NUM_HOURS*TimeCell.CELLS_PER_HOUR))*( i + .5)));
				t.increment(0, 60/TimeCell.CELLS_PER_HOUR);
			}
		}
		catch(Exception e)
		{
			TimeChooserPanel.add(new Label("Your internet browser does not support canvases; All class times are selected by default\n" +
											"If you would like to select specific times to take classes, please use the latest version of IE, Firefox, or Chrome"));
		}
	}*/
}
