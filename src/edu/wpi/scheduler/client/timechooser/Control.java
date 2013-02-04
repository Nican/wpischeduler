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

import edu.wpi.scheduler.client.controller.StudentSchedule;


/**
 * Canvas for selection drawing and event triggering
 * @author Ryan Danas
 *
 */
public class Control {

	int width;
	int height;
	int numDays;
	int numHours;
	
	Canvas canvas = Canvas.createIfSupported();
	int dragX = -1;
	int dragY = -1;
	int dropX = -1;
	int dropY = -1;
	TimeChooserController tcc;

	/**
	 * 
	 */
	public Control(TimeChooser tc, int w, int h, int dy, int hr, StudentSchedule model){
		numDays = dy;
		numHours = hr;
		tcc = new TimeChooserController(tc, model);
		width = w;
		height = h;
		if(canvas == null){
			throw new RuntimeException("Canvas is unsupported");
		}
		canvas.setCoordinateSpaceWidth(width);
		canvas.setCoordinateSpaceHeight(height);
		canvas.addMouseDownHandler(new MouseDownHandler() {	
			@Override
			public void onMouseDown(MouseDownEvent event) {
				dragX = event.getX();
				dragY = event.getY();
			}
		});
		canvas.addMouseUpHandler(new MouseUpHandler() {	
			@Override
			public void onMouseUp(MouseUpEvent event) {
				dropX = event.getX();
				dropY = event.getY();
				tcc.timeChosen(dragX, dragY, dropX, dropY);
				dragX = -1;
				dragY = -1;
				dropX = -1;
				dropY = -1;
			}
		});
		canvas.addMouseMoveHandler(new MouseMoveHandler() {	
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				dropX = event.getX();
				dropY = event.getY();
				tcc.mouseDrag(dragX, dragY, dropX, dropY);
			}
		});
		canvas.addMouseOutHandler(new MouseOutHandler() {	
			@Override
			public void onMouseOut(MouseOutEvent event) {
				tcc.mouseOut();
				dragX = -1;
				dragY = -1;
			    dropX = -1;
				dropY = -1;
			}
		});
		canvas.getContext2d().drawImage(tc.grid.canvas.getCanvasElement(), 0, 0);
	}
	
	/**
	 * 
	 * @param i1
	 * @param j1
	 * @param i2
	 * @param j2
	 */
	void drawDrag(int i1, int j1, int i2, int j2){
		canvas.getContext2d().setStrokeStyle("blue");
		// Stroke drag rect
		canvas.getContext2d().strokeRect(j1, i1, (j2-j1), (i2-i1));
	}
}
