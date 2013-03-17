package edu.wpi.scheduler.client.timechooser;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.SimplePanel;

import edu.wpi.scheduler.client.controller.StudentChosenTimes;
import edu.wpi.scheduler.shared.model.Term;
import edu.wpi.scheduler.shared.model.Time;
import edu.wpi.scheduler.shared.model.TimeCell;

public class TimeTable extends SimplePanel
{
	private FlexTable table;
	Term term;
	StudentChosenTimes model;
	
	public TimeTable(Term term, StudentChosenTimes model)
	{
		table = new FlexTable();
		this.term = term;
		this.model = model;
		createCells();
		this.add(table);
	}
	
	private void createCells()
	{
		int i, j = 0;
		// Create hours on the side
		Time t = new Time(TimeCell.START_HOUR, TimeCell.START_MIN);
		for(i = 0; i <= TimeCell.NUM_HOURS; i++)
		{
			table.setText(i*2, 0, t.toString());
			t.increment(1, 0);
		}
		// Create days on the top
		for(j = 0; j < TimeCell.NUM_DAYS; j++)
		{
			table.setText(0, j+1, TimeCell.week[j+TimeCell.START_DAY].getShortName().toUpperCase());
		}
		// Create time cells
		for(i = 0; i < TimeCell.NUM_HOURS * TimeCell.CELLS_PER_HOUR; i++)
		{
			for(j = 0; j < TimeCell.NUM_DAYS; j++)
			{
				table.setText(i+1, j+1, String.valueOf((model.isTimeSelected(i, j))));
			}
		}
	}
}
