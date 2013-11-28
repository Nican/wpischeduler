package edu.wpi.scheduler.client.timechooser;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.StudentChosenTimes;
import edu.wpi.scheduler.shared.model.Term;
import edu.wpi.scheduler.shared.model.Time;
import edu.wpi.scheduler.shared.model.TimeCell;

public class TimeTable extends Widget implements MouseDownHandler, MouseUpHandler, MouseMoveHandler
{
	// CSS constants
	static final String ROW = "TimeTable_Row";
	static final String TERM = "TimeTable_TermLabel";
	static final String DAY = "TimeTable_DayLabels";
	static final String HOUR = "TimeTable_HourLabels";
	static final String CELL = "TimeTable_Cell";
	static final String STATIC = "TimeTable_Cell_Static";
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

	public TimeTable(Term term, StudentChosenTimes model) 
	{
		this.model = model;
		controller = new TimeChooserController(this, model);
		// Create blank "table"
		table = DOM.createDiv();
		table.setAttribute("class", "TimeTable");
		setElement(table);
		// Add Days Row
		Element days_row = DOM.createDiv();
		days_row.setAttribute("class", ROW);
		// Add Term Cell
		Element term_cell = DOM.createDiv();
		term_cell.setInnerText(term.toString() + "-Term");
		term_cell.setAttribute("class", TERM);
		days_row.appendChild(term_cell);
		// Add Days Cell
		for(int x = 0; x < TimeCell.NUM_DAYS; x++)
		{
			Element day_cell = DOM.createDiv();
			day_cell.setInnerText(TimeCell.week[TimeCell.START_DAY+x].getName().toUpperCase());
			day_cell.setAttribute("class", DAY);
			days_row.appendChild(day_cell);
		}
		table.appendChild(days_row);
		// Add time choosing rows / hour labels
		String current_row = EVEN;
		Time time = new Time(TimeCell.START_HOUR, TimeCell.START_MIN);
		for (int y = 0; y < TimeCell.NUM_HOURS * TimeCell.CELLS_PER_HOUR ; y++) 
		{
			// Create each row
			Element row = DOM.createDiv();
			row.setAttribute("class", ROW);
			// Add hour label cell
			Element hour_cell = DOM.createDiv();
			String classID = HOUR + " " + STATIC + " " + current_row;
			hour_cell.setAttribute("class", classID);
			hour_cell.setInnerText(time.toString());
			row.appendChild(hour_cell);
			// For each cell in a row
			for (int x = 0; x < TimeCell.NUM_DAYS; x++)
			{
				// Create the cell and set it to the proper values
				TimeElement cell = DOM.createDiv().cast();
				cell.getStyle().setCursor(Cursor.POINTER);
				cell.setTime(y);
				cell.setDay(x);
				// cell.setInnerHTML(x + " " + y);
				row.appendChild(cell);
			}
			// Add the new row to the table
			table.appendChild(row);
			// Update row counter
			current_row = current_row.equals(EVEN) ? ODD : EVEN;
			time.increment(0, 60 / TimeCell.CELLS_PER_HOUR);
		}
		update();
	}

	private void update()
	{
		// Update the cells
		// Starting with an even row
		String current_row = EVEN;
		for(int y = 1; y < table.getChildCount(); y++)
		{
			Element row = table.getChild(y).cast();
			for(int x = 1; x < row.getChildCount(); x++)
			{
				// Update cell visual state
				TimeElement cell = row.getChild(x).cast();
				String classID = CELL + " " + STATIC + " ";
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
			// Update cell visual state
			String classID = CELL + " ";
			// Figure out if the cells should be marked as being selected or not
			classID += isAlreadySelected ? DESELECTED : SELECTED;
			// Mark the cells being dragged
			for(int y = y1+1; y <= y2+1; y++)
			{
				Element row = table.getChild(y).cast();
				for(int x = x1+1; x <= x2+1; x++)
				{
					// Update cell visual state
					TimeElement cell = row.getChild(x).cast();
					// Set the CSS classes
					cell.setAttribute("class", classID);	
				}
			}
		}
	}

	public void setSize(int width, int height) 
	{
		// Update the table
		table.getStyle().setWidth(width, Unit.PX);
		table.getStyle().setHeight(height, Unit.PX);
		//table.getStyle().setPosition(Position.RELATIVE);
		double cellWidth = ((double) width) / (TimeCell.NUM_DAYS + 1);
		double cellHeight = ((double)height) / ((TimeCell.NUM_HOURS * TimeCell.CELLS_PER_HOUR) + 1);
		// Update the cells
		for(int y = 0; y < table.getChildCount(); y++)
		{
			Element row = table.getChild(y).cast();
			row.getStyle().setHeight(cellHeight, Unit.PX);
			for(int x = 0; x < row.getChildCount(); x++)
			{
				// Update cell's size
				TimeElement cell = row.getChild(x).cast();
				cell.getStyle().setWidth(cellWidth - 1.0, Unit.PX);
				cell.getStyle().setHeight(cellHeight - 1.0, Unit.PX);
				cell.getStyle().setLineHeight(cellHeight - 1.0, Unit.PX);
				cell.getStyle().setTextAlign(TextAlign.CENTER);
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