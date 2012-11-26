package edu.wpi.scheduler.client.timechooser;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.SimplePanel;


/**
 * Panel containing time choosing canvas
 * @author Ryan Danas
 *
 */
public class TimeChooser extends SimplePanel {

	int numDays = 5;
	int numHours = 10;
	
	Canvas canvas = Canvas.createIfSupported();
	int dragX = -1;
	int dragY = -1;
	int dropX = -1;
	int dropY = -1;
	TimeChooserController tcc = new TimeChooserController(this);

	/**
	 * 
	 */
	public TimeChooser(){
		if(canvas == null){
			throw new RuntimeException("Canvas is unsupported");
		}
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
		canvas.addMouseOutHandler(new MouseOutHandler() {	
			@Override
			public void onMouseOut(MouseOutEvent event) {
				dragX = -1;
				dragY = -1;
				dropX = -1;
				dropY = -1;
			}
		});
		this.add(canvas);
		drawGrid(0, 0, numHours, numDays);
	}
	
	/**
	 * 
	 * @param i1
	 * @param j1
	 * @param i2
	 * @param j2
	 */
	void drawGrid(int i1, int j1, int i2, int j2){
		int width = canvas.getCoordinateSpaceWidth();
		int height = canvas.getCoordinateSpaceHeight();
		int w = width / numDays;
		int h = height / numHours;
		canvas.getContext2d().setStrokeStyle("black");
		// Stroke outside
		canvas.getContext2d().strokeRect(0, 0, width, height);
		// Stroke grid
		for( int i = i1; i < i2; i++){
			for( int j = j1; j < j2; j++){
				canvas.getContext2d().strokeRect((j*w), (i*h), w, h);
			}
		}
	}
	
	/**
	 * 
	 * @param i1
	 * @param j1
	 * @param i2
	 * @param j2
	 */
	void fillTimes(int i1, int j1, int i2, int j2){
		int width = canvas.getCoordinateSpaceWidth();
		int height = canvas.getCoordinateSpaceHeight();
		int w = width / numDays;
		int h = height / numHours;
		canvas.getContext2d().setFillStyle("red");
		// Fill area given
		for( int i = i1; i < i2; i++){
			for( int j = j1; j < j2; j++){
				canvas.getContext2d().fillRect((j*w), (i*h), w, h);
			}
		}
	}

}
