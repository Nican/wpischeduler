package edu.wpi.scheduler.client.timechooser;

import com.google.gwt.user.client.ui.AbsolutePanel;

import edu.wpi.scheduler.client.controller.StudentChosenTimes;
import edu.wpi.scheduler.shared.model.TimeCell;

public class TimeChooser extends AbsolutePanel
{
	final int numColumns = TimeCell.NUM_DAYS;
	final int numRows = TimeCell.NUM_HOURS * TimeCell.CELLS_PER_HOUR;
	final int width = 700;
	final int height = 700;
	
	Control control;
	Grid grid;
	Fill fill;
	
	public TimeChooser(StudentChosenTimes model)
	{
		fill = new Fill(width, height, numColumns, numRows);
		grid = new Grid(width, height, numColumns, numRows);
		control = new Control(this, width, height, numColumns, numRows, model);
		this.add(control.canvas);
	}
}
