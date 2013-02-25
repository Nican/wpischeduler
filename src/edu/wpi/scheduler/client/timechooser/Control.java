package edu.wpi.scheduler.client.timechooser;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;

import edu.wpi.scheduler.client.controller.StudentChosenTimes;

/**
 * Canvas for selection drawing and event triggering
 * @author Ryan Danas
 *
 */
public class Control {

	final int width;
	final int height;
	final int numDays;
	final int numHours;
	
	Canvas canvas = Canvas.createIfSupported();
	int dragX = -1;
	int dragY = -1;
	int dropX = -1;
	int dropY = -1;
	TimeChooserController tcc;

	/**
	 * 
	 */
	public Control(TimeChooser tc, int w, int h, int dy, int hr, StudentChosenTimes model)
	{
		// Can't initialize if canvas is unsupported by web browser
		if(canvas == null)
		{
			throw new RuntimeException("Canvas is unsupported");
		}
		// Set needed constants
		width = w;
		height = h;
		numDays = dy;
		numHours = hr;
		// Initalize controller
		tcc = new TimeChooserController(tc, model);
		// Set size
		canvas.setCoordinateSpaceWidth(width);
		canvas.setCoordinateSpaceHeight(height);
		// Mouse Down Handler
		canvas.addMouseDownHandler(new MouseDownHandler() 
		{	
			@Override
			public void onMouseDown(MouseDownEvent event) 
			{
				dragX = event.getX();
				dragY = event.getY();
			}
		});
		// Mouse Up Handler
		canvas.addMouseUpHandler(new MouseUpHandler() 
		{	
			@Override
			public void onMouseUp(MouseUpEvent event) 
			{
				dropX = event.getX();
				dropY = event.getY();
				tcc.timeChosen(dragX, dragY, dropX, dropY);
				dragX = -1;
				dragY = -1;
				dropX = -1;
				dropY = -1;
			}
		});
		// Mouse Move Handler
		canvas.addMouseMoveHandler(new MouseMoveHandler() 
		{	
			@Override
			public void onMouseMove(MouseMoveEvent event) 
			{
				dropX = event.getX();
				dropY = event.getY();
				tcc.mouseDrag(dragX, dragY, dropX, dropY);
			}
		});
		// Mouse Out Handler
		canvas.addMouseOutHandler(new MouseOutHandler() 
		{	
			@Override
			public void onMouseOut(MouseOutEvent event) 
			{
				dragX = -1;
				dragY = -1;
			    dropX = -1;
				dropY = -1;
				tcc.mouseOut();
			}
		});
	}
	
	void drawDrag(int i1, int j1, int i2, int j2, boolean isSelected)
	{
		final String color = (isSelected) ?  "#CC3333" : "#33CC33";
		canvas.getContext2d().setStrokeStyle(color);
		// Stroke drag rectangle
		canvas.getContext2d().strokeRect(j1, i1, (j2-j1), (i2-i1));
	}
}
