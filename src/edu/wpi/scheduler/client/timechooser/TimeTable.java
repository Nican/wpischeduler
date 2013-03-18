package edu.wpi.scheduler.client.timechooser;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.shared.model.DayOfWeek;
import edu.wpi.scheduler.shared.model.Time;
import edu.wpi.scheduler.shared.model.TimeCell;

public class TimeTable extends Widget implements MouseDownHandler, MouseUpHandler, MouseMoveHandler 
{
	// Native DOM Cell Element
	public static class TimeElement extends Element 
	{
		protected TimeElement() {}

		public native final void setDay(int day) 
		/*-{
			this.day = day;
		}-*/;

		public native final int getDay() 
		/*-{
			return this.day;
		}-*/;

		public native final void setTime(int time) 
		/*-{
			this.time = time;
		}-*/;

		public native final int getTime() 
		/*-{
			return this.time;
		}-*/;
	}

	HandlerRegistration mouseDown;
	HandlerRegistration mouseMove;
	HandlerRegistration mouseUp;
	int dragX = -1;
	int dragY = -1;
	int dropX = -1;
	int dropY = -1;

	public TimeTable() 
	{
		// Create blank table
		setElement(DOM.createTable());
		// For each row
		Time t = new Time(TimeCell.START_HOUR, TimeCell.START_MIN);
		for (int y = 0; y < TimeCell.NUM_HOURS * TimeCell.CELLS_PER_HOUR ; y++) 
		{
			// Create each row
			Element row = DOM.createTR();
			// For each cell in a row
			for (int x = 0; x < TimeCell.NUM_DAYS; x++)
			{
				// Create the cell and set it to the proper values
				TimeElement cell = DOM.createTD().cast();
				cell.setTime(y);
				cell.setDay(x);
				cell.getStyle().setHeight(9, Unit.PX);
				cell.getStyle().setWidth(44, Unit.PX);
				cell.getStyle().setBackgroundColor("#FFEEEE");
				// cell.setInnerHTML(x + " " + y);

				row.appendChild(cell);
			}
			t.increment(0, 60 / TimeCell.CELLS_PER_HOUR);
			// Add the new row to the table
			getElement().appendChild(row);
		}
	}

	@Override
	public void onLoad()
	{
		// Add local handlers
		mouseDown = this.addDomHandler(this, MouseDownEvent.getType());
		mouseMove = this.addDomHandler(this, MouseMoveEvent.getType());
		// Add global handler to catch mouse-ups outside of this grid
		mouseUp = RootPanel.get().addDomHandler(this, MouseUpEvent.getType());
	}
	
	@Override 
	public void onUnload()
	{
		// Remove any handlers
		mouseDown.removeHandler();
		mouseMove.removeHandler();
		mouseUp.removeHandler();
	}

	@Override
	public void onMouseUp(MouseUpEvent event) 
	{
		TimeElement elem = event.getNativeEvent().getEventTarget().cast();
		try 
		{
			DayOfWeek day = TimeCell.week[elem.getDay() + TimeCell.START_DAY];
			Time time = new Time(TimeCell.START_HOUR, TimeCell.START_MIN);
			time.increment(0, elem.getTime() * (60 / TimeCell.CELLS_PER_HOUR));
			System.out.println("Up: " + day + ", " + time);
		}
		// Element or its values were undefined
		catch(Exception e)
		{
			System.out.println("Up: Outside");
		}
	}

	@Override
	public void onMouseDown(MouseDownEvent event) 
	{
		TimeElement elem = event.getNativeEvent().getEventTarget().cast();
		try 
		{
			DayOfWeek day = TimeCell.week[elem.getDay() + TimeCell.START_DAY];
			Time time = new Time(TimeCell.START_HOUR, TimeCell.START_MIN);
			time.increment(0, elem.getTime() * (60 / TimeCell.CELLS_PER_HOUR));
			System.out.println("Down: " + day + ", " + time);
		}
		// Element or its values were undefined
		catch(Exception e)
		{
			System.out.println("Down: Outside");
		}
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) 
	{
		TimeElement elem = event.getNativeEvent().getEventTarget().cast();
		try 
		{
			DayOfWeek day = TimeCell.week[elem.getDay() + TimeCell.START_DAY];
			Time time = new Time(TimeCell.START_HOUR, TimeCell.START_MIN);
			time.increment(0, elem.getTime() * (60 / TimeCell.CELLS_PER_HOUR));
			System.out.println("Move: " + day + ", " + time);
		}
		// Element or its values were undefined
		catch(Exception e)
		{
			System.out.println("Move: Outside");
		}
	}
}