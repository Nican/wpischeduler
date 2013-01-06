package edu.wpi.scheduler.client.timechooser;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.user.client.ui.SimplePanel;


/**
 * Panel containing time choosing canvas
 * @author Ryan Danas
 *
 */
public class Grid {

	int width;
	int height;
	int numDays;
	int numHours;
	
	Canvas canvas = Canvas.createIfSupported();
	int dragX = -1;
	int dragY = -1;
	int dropX = -1;
	int dropY = -1;

	/**
	 * 
	 */
	public Grid(int w, int h, int dy, int hr){
		numDays = dy;
		numHours = hr;
		width = w;
		height = h;
		if(canvas == null){
			throw new RuntimeException("Canvas is unsupported");
		}
		canvas.setCoordinateSpaceWidth(width);
		canvas.setCoordinateSpaceHeight(height);
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
}
