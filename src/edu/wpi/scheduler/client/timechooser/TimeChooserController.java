package edu.wpi.scheduler.client.timechooser;

import edu.wpi.scheduler.client.controller.StudentChosenTimes;

public class TimeChooserController 
{
	TimeTable view;
	StudentChosenTimes model;

	public TimeChooserController(TimeTable v, StudentChosenTimes model) 
	{
		this.view = v;
		this.model = model;
	}

	void timeChosen(int dragX, int dragY, int dropX, int dropY)
	{
		if(dragX >= 0 && dragY >= 0 && dropX >= 0 && dropY >= 0)
		{
			// Calculate grid points
			int i1 = Math.min(dragY, dropY);
			int j1 = Math.min(dragX, dropX);
			int i2 = Math.max(dragY, dropY);
			int j2 = Math.max(dragX, dropX);
			// Update Model
			boolean isSelected = model.isTimeSelected(dragY, dragX);
			//System.out.println(isSelected);
			if(isSelected)
			{
				for( int i = i1; i <= i2; i++)
				{
					for( int j = j1; j <= j2; j++)
					{
						//System.out.println("DESELECT: " + i + ", " + j);
						model.deselectTime(i, j);
					}
				}
			}
			else
			{
				for( int i = i1; i <= i2; i++)
				{
					for( int j = j1; j <= j2; j++)
					{
						//System.out.println("SELECT: " + i + ", " + j);
						model.selectTime(i, j);
					}
				}
			}
		}
		model.studentSchedule.studentTermTimes.saveStudentTermTimes();
		model.studentSchedule.courseUpdated(null);
		//System.out.println(model.toString());
	}
}