package edu.wpi.scheduler.client.timechooser;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.SimplePanel;

import edu.wpi.scheduler.client.controller.StudentChosenTimes;
import edu.wpi.scheduler.shared.model.Time;
import edu.wpi.scheduler.shared.model.TimeCell;

public class TimeTable extends SimplePanel
{
	private FocusPanel inputListener;

	private FlexTable table;
	private StudentChosenTimes model;
	private TimeChooserController tcc;

	int dragX = -1;
	int dragY = -1;
	int dropX = -1;
	int dropY = -1;

	public TimeTable(StudentChosenTimes model)
	{
		inputListener = new FocusPanel();
		table = new FlexTable();
		inputListener.setWidget(table);
		this.model = model;
		createCells();
		addHandlers();
		this.add(inputListener);
		this.tcc = new TimeChooserController(this, model);;
	}

	private void createCells()
	{
		int i, j = 0;
		// Create hours on the side
		Time t = new Time(TimeCell.START_HOUR, TimeCell.START_MIN);
		for(i = 0; i <= TimeCell.NUM_HOURS; i++)
		{
			table.setText(i*2, 0, t.toString());
			t.increment(1, 0);
		}
		// Create days on the top
		for(j = 0; j < TimeCell.NUM_DAYS; j++)
		{
			table.setText(0, j+1, TimeCell.week[j+TimeCell.START_DAY].getShortName().toUpperCase());
		}
		update();
	}

	private void addHandlers() 
	{
		// Click Handler
		table.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event) 
			{
				table.getCellForEvent(event);
				//System.err.println("CLICK");
			}
		});
		// Mouse Down Handler
		inputListener.addMouseDownHandler(new MouseDownHandler() 
		{	
			@Override
			public void onMouseDown(MouseDownEvent event) 
			{
				dragX = event.getX();
				dragY = event.getY();
				NativeEvent click = Document.get().createClickEvent(0, event.getX(), event.getY(), 
						event.getClientX(), event.getClientX(), false, false, false, false);
				//System.err.println("DOWN");
				DomEvent.fireNativeEvent(click, table);
				
			}
		});
		// Mouse Up Handler
		inputListener.addMouseUpHandler(new MouseUpHandler() 
		{	
			@Override
			public void onMouseUp(MouseUpEvent event) 
			{
				dropX = event.getX();
				dropY = event.getY();
				//tcc.timeChosen(dragX, dragY, dropX, dropY);
				
				dragX = -1;
				dragY = -1;
				dropX = -1;
				dropY = -1;
			}
		});
		// Mouse Move Handler
		inputListener.addMouseMoveHandler(new MouseMoveHandler() 
		{	
			@Override
			public void onMouseMove(MouseMoveEvent event) 
			{
				dropX = event.getX();
				dropY = event.getY();
				//tcc.mouseDrag(dragX, dragY, dropX, dropY);
				NativeEvent click = Document.get().createClickEvent(0, event.getX(), event.getY(), 
						event.getClientX(), event.getClientX(), false, false, false, false);
				//System.err.println("MOVE");
				DomEvent.fireNativeEvent(click, table);
			}
		});
	}

	public void update() 
	{
		int i, j = 0;
		// Update time cells
		for(i = 0; i < TimeCell.NUM_HOURS * TimeCell.CELLS_PER_HOUR; i++)
		{
			for(j = 0; j < TimeCell.NUM_DAYS; j++)
			{
				String cell_contents = "<div style=\"background-color:#339900;\">___</div>";
				table.setHTML(i+1, j+1, cell_contents);
				//table.setText(i+1, j+1, String.valueOf((model.isTimeSelected(i, j))));
			}
		}
	}
}
