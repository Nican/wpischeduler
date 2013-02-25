package edu.wpi.scheduler.client.timechooser;

import com.google.gwt.user.client.ui.AbsolutePanel;

import edu.wpi.scheduler.client.controller.StudentChosenTimes;
import edu.wpi.scheduler.shared.model.TimeCell;

public class TimeChooser extends AbsolutePanel
{
	final int numColumns = TimeCell.NUM_DAYS;
	final int numRows = TimeCell.NUM_HOURS * TimeCell.CELLS_PER_HOUR;
	final int width;
	final int height;
	
	StudentChosenTimes model;
	Control control;
	Fill fill;
	Grid grid;
	
	public TimeChooser(StudentChosenTimes model, int gridWidth, int gridHeight)
	{
		this.model = model;
		this.width = gridWidth;
		this.height = gridHeight;
		control = new Control(this, width, height, numColumns, numRows, model);
		fill = new Fill(width, height, numColumns, numRows);
		grid = new Grid(width, height, numColumns, numRows);
		this.add(control.canvas);
		redraw();
	}
	
	void redraw()
	{
		control.canvas.getContext2d().clearRect(0, 0, width, height);
		fill.fillTimes(model);
		control.canvas.getContext2d().drawImage(fill.canvas.getCanvasElement(), 0, 0);
		control.canvas.getContext2d().drawImage(grid.canvas.getCanvasElement(), 0, 0);
	}
}
