package edu.wpi.scheduler.client.timechooser;

import com.google.gwt.user.client.ui.AbsolutePanel;

import edu.wpi.scheduler.client.controller.StudentSchedule;

public class TimeChooser extends AbsolutePanel
{
	final int numColumns = StudentSchedule.NUM_DAYS;
	final int numRows = StudentSchedule.NUM_HOURS * StudentSchedule.GRIDS_PER_HOUR;
	final int width = 700;
	final int height = 700;
	
	Control control;
	Grid grid;
	Fill fill;
	
	public TimeChooser(StudentSchedule studentSchedule)
	{
		fill = new Fill(width, height, numColumns, numRows);
		grid = new Grid(width, height, numColumns, numRows);
		control = new Control(this, width, height, numColumns, numRows, studentSchedule);
		this.add(control.canvas);
	}
}
