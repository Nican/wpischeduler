package edu.wpi.scheduler.client.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.wpi.scheduler.shared.model.DayOfWeek;
import edu.wpi.scheduler.shared.model.Time;
import edu.wpi.scheduler.shared.model.TimeCell;

public class StudentChosenTimes 
{
	// Terms of chosen times
	private final HashMap<DayOfWeek, List<Time>> termTimes = new HashMap<DayOfWeek, List<Time>>();

	/**
	 * Constructs beginning chosen times
	 */
	public StudentChosenTimes()
	{
		for(int i = TimeCell.START_DAY; i <= TimeCell.NUM_DAYS; i++)
		{
			termTimes.put(TimeCell.week[i], new ArrayList<Time>());
		}
	}

	
	public void selectTime(int i, int j)
	{
		TimeCell selectedCell = new TimeCell(i, j);
		List<Time> dayTimes = termTimes.get(selectedCell.day);
		if(!dayTimes.contains(selectedCell.time))
		{
			dayTimes.add(selectedCell.time);
		}
	}
	public void deselectTime(int i, int j)
	{
		TimeCell deselectedCell = new TimeCell(i, j);
		List<Time> dayTimes = termTimes.get(deselectedCell.day);
		if(dayTimes.contains(deselectedCell.time))
		{
			dayTimes.remove(deselectedCell.time);
		}
	}
	public boolean isTimeSelected(int i, int j)
	{
		TimeCell queriedCell = new TimeCell(i, j);
		List<Time> dayTimes = termTimes.get(queriedCell.day);
		return dayTimes.contains(queriedCell.time);
	}
	public void printTimes()
	{
		for(int i=TimeCell.START_DAY; i<=TimeCell.NUM_DAYS; i++){
			DayOfWeek day = TimeCell.week[i];
			System.out.print(day.toString());
			System.out.print(": ");
			List<Time> dayTimes = termTimes.get(day);
			for(int j=0; j<dayTimes.size(); j++){
				System.out.print(dayTimes.get(j).toString());
				System.out.print(", ");
			}
			System.out.print("\n");
		}
	}
}
