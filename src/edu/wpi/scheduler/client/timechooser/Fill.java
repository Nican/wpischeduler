package edu.wpi.scheduler.client.timechooser;

import java.util.List;

import com.google.gwt.canvas.client.Canvas;

import edu.wpi.scheduler.client.controller.StudentChosenTimes;
import edu.wpi.scheduler.shared.model.Time;
import edu.wpi.scheduler.shared.model.TimeCell;

/**
 * Canvas representing selected times filled in
 * @author Ryan Danas
 *
 */
public class Fill {

	int width;
	int height;
	int numDays;
	int numHours;
	int col_w;
	int row_h;
	Canvas canvas = Canvas.createIfSupported();

	/**
	 * 
	 */
	public Fill(int w, int h, int dy, int hr){
		numDays = dy;
		numHours = hr;
		width = w;
		height = h;
		col_w = width / numDays;
		row_h = height / numHours;
		if(canvas == null){
			throw new RuntimeException("Canvas is unsupported");
		}
		canvas.setCoordinateSpaceWidth(width);
		canvas.setCoordinateSpaceHeight(height);
	}
	
	void fillTimes(StudentChosenTimes model)
	{
		// Clear current fill
		canvas.getContext2d().setFillStyle("#FFDEDE");
		canvas.getContext2d().fillRect(0, 0, width, height);
		// Fill currently selected times
		canvas.getContext2d().setFillStyle("#339900");
		// Fill area given
		List<Time> times; 
		for( int j = TimeCell.START_DAY; j <= TimeCell.NUM_DAYS; j++)
		{
			times = model.getTimes(TimeCell.week[j]);
			for( int i = 0; i < times.size(); i++)
			{
				TimeCell cell = new TimeCell(times.get(i), TimeCell.week[j]);
				int actual_j = cell.dayToGrid();
				int actual_i = cell.timeToGrid();
				canvas.getContext2d().fillRect((actual_j*col_w), (actual_i*row_h), col_w, row_h);
			}
		}
	}
}
