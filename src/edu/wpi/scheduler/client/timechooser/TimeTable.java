package edu.wpi.scheduler.client.timechooser;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.StudentChosenTimes;
import edu.wpi.scheduler.shared.model.TimeCell;

public class TimeTable extends Widget implements MouseDownHandler, MouseUpHandler, MouseMoveHandler
{
	// CSS constants
	static final String CELL = "TimeTable_Cell";
	static final String SELECTED = "TimeTable_Cell_Selected";
	static final String DESELECTED = "TimeTable_Cell_Deselected";
	static final String EVEN = "TimeTable_Cell_Even";
	static final String ODD = "TimeTable_Cell_Odd";

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

	StudentChosenTimes model;
	Element table; 
	TimeChooserController controller;
	HandlerRegistration mouseDown;
	HandlerRegistration mouseMove;
	HandlerRegistration mouseUp;

	int dragX = -1;
	int dragY = -1;
	int dropX = -1;
	int dropY = -1;

	public TimeTable(StudentChosenTimes model) 
	{
		this.model = model;
		controller = new TimeChooserController(this, model);
		// Create blank table
		table = DOM.createTable();
		table.setDraggable(Element.DRAGGABLE_FALSE);
		table.setAttribute("class", "TimeTable");
		setElement(table);
		// For each row
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
				// cell.setInnerHTML(x + " " + y);
				row.appendChild(cell);
			}
			// Add the new row to the table
			getElement().appendChild(row);
		}
		update();
		setSize();
	}

	private void update()
	{
		// Update the cells
		// Starting with an even row
		String current_row = EVEN;
		for(int y = 0; y < table.getChildCount(); y++)
		{
			Element row = table.getChild(y).cast();
			for(int x = 0; x < row.getChildCount(); x++)
			{
				// Update cell visual state
				TimeElement cell = row.getChild(x).cast();
				String classID = CELL + " ";
				// Add whether the cell is selected or unselected
				classID += model.isTimeSelected(cell.getTime(), cell.getDay()) ? SELECTED : DESELECTED;
				// Add whether the cell is an even or odd cell
				classID += " " + current_row;
				// Set the CSS classes
				cell.setAttribute("class", classID);	
			}
			// Update row counter
			current_row = current_row.equals(EVEN) ? ODD : EVEN;
		}
	}

	private void drawDrag() 
	{
		if(dragX >= 0 && dragY >= 0 && dropX >= 0 && dropY >= 0)
		{
			int x1 = Math.min(dragX, dropX);
			int y1 = Math.min(dragY, dropY);
			int x2 = Math.max(dragX, dropX);
			int y2 = Math.max(dragY, dropY);
			boolean isAlreadySelected = model.isTimeSelected(dragY, dragX);
			// Figure out if the cells should be marked as being selected or not
			String classID = isAlreadySelected ? DESELECTED : SELECTED;
			// Mark the cells being dragged
			for(int y = y1; y <= y2; y++)
			{
				Element row = table.getChild(y).cast();
				for(int x = x1; x <= x2; x++)
				{
					// Update cell visual state
					TimeElement cell = row.getChild(x).cast();
					// Set the CSS classes
					cell.setAttribute("class", classID);	
				}
			}
		}
	}

	public void setSize() 
	{
		// Update the table
		table.getStyle().setWidth(720, Unit.PX);
		table.getStyle().setHeight(480, Unit.PX);
		int cellWidth = 720 / TimeCell.NUM_DAYS;
		int cellHeight = 0;
		// Update the cells
		for(int y = 0; y < table.getChildCount(); y++)
		{
			Element row = table.getChild(y).cast();
			cellHeight = 480 / (TimeCell.NUM_HOURS * TimeCell.CELLS_PER_HOUR);
			for(int x = 0; x < row.getChildCount(); x++)
			{
				// Update cell's size
				TimeElement cell = row.getChild(x).cast();
				cell.getStyle().setWidth(cellWidth, Unit.PX);
				cell.getStyle().setHeight(cellHeight, Unit.PX);
			}
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
		update();
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
	public void onMouseDown(MouseDownEvent event) 
	{
		// Get the cell this happened in
		TimeElement elem = event.getNativeEvent().getEventTarget().cast();
		// Event occurred in an actual cell
		try 
		{
			//Time time = new Time(TimeCell.START_HOUR, TimeCell.START_MIN);
			//time.increment(0, elem.getTime() * (60 / TimeCell.CELLS_PER_HOUR));
			//DayOfWeek day = TimeCell.week[elem.getDay() + TimeCell.START_DAY];
			//System.out.println("Down: " + day + ", " + time);
			dragX = elem.getDay();
			dragY = elem.getTime();
		}
		// Event occurred outside an actual cell
		catch(Exception e)
		{
			//System.out.println("Down: Outside");
		}
		update();
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) 
	{
		// Get the cell this happened in
		TimeElement elem = event.getNativeEvent().getEventTarget().cast();
		// Event occurred in an actual cell
		try 
		{
			//Time time = new Time(TimeCell.START_HOUR, TimeCell.START_MIN);
			//time.increment(0, elem.getTime() * (60 / TimeCell.CELLS_PER_HOUR));
			//DayOfWeek day = TimeCell.week[elem.getDay() + TimeCell.START_DAY];
			//System.out.println("Move: " + day + ", " + time);
			dropX = elem.getDay();
			dropY = elem.getTime();
		}
		// Event occurred outside an actual cell
		catch(Exception e)
		{
			//System.out.println("Move: Outside");
		}
		update();
		drawDrag();
	}

	@Override
	public void onMouseUp(MouseUpEvent event) 
	{
		// Get the cell this happened in
		TimeElement elem = event.getNativeEvent().getEventTarget().cast();
		// Event occurred in an actual cell
		try 
		{
			//Time time = new Time(TimeCell.START_HOUR, TimeCell.START_MIN);
			//time.increment(0, elem.getTime() * (60 / TimeCell.CELLS_PER_HOUR));
			//DayOfWeek day = TimeCell.week[elem.getDay() + TimeCell.START_DAY];
			//System.out.println("Up: " + day + ", " + time);
			dropX = elem.getDay();
			dropY = elem.getTime();
			//System.out.println(dragX+ ", " +  dragY+ " ; " + dropX + ", " + dropY);
		}
		// Event occurred outside an actual cell
		catch(Exception e)
		{
			//System.out.println("Up: Outside");
		}
		controller.timeChosen(dragX, dragY, dropX, dropY);
		dragX = dragY = dropX = dropY = -1;
		update();
	}
}