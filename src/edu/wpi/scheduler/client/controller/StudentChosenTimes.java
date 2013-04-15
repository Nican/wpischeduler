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
	private HashMap<DayOfWeek, List<Time>> termTimes = new HashMap<DayOfWeek, List<Time>>();
	public StudentSchedule studentSchedule;

	/**
	 * Constructs beginning chosen times
	 * @param studentSchedule 
	 */
	public StudentChosenTimes(StudentSchedule studentSchedule)
	{
		this.studentSchedule = studentSchedule;
		// Initialize hash map
		for(int i = TimeCell.START_DAY; i <= TimeCell.NUM_DAYS; i++)
		{
			termTimes.put(TimeCell.week[i], new ArrayList<Time>());
		}
		// Select every possible time
		for(int j = 0; j < TimeCell.NUM_DAYS; j++)
		{
			for(int i = 0; i < TimeCell.NUM_HOURS * TimeCell.CELLS_PER_HOUR; i++)
			{
				selectTime(i, j);
			}
		}
	}
	// Setters
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
	// Getters
	public boolean isTimeSelected(int i, int j)
	{
		TimeCell queriedCell = new TimeCell(i, j);
		List<Time> dayTimes = termTimes.get(queriedCell.day);
		return dayTimes.contains(queriedCell.time);
	}
	public List<Time> getTimes(DayOfWeek day)
	{
		List<Time> times = new ArrayList<Time>(termTimes.get(day));
		return times;
	}
	
	@Override
	public String toString()
	{
		String out = "";
		for(int i=TimeCell.START_DAY; i<=TimeCell.NUM_DAYS; i++)
		{
			DayOfWeek day = TimeCell.week[i];
			out += day.toString();
			out += ": ";
			List<Time> dayTimes = termTimes.get(day);
			for(int j=0; j<dayTimes.size(); j++){
				out += dayTimes.get(j).toString();
				out += ", ";
			}
			out += "\n";
		}
		return out;
	}
	
	public void set(HashMap<DayOfWeek, List<Time>> termTimes)
	{
		this.termTimes = termTimes;
	}
}
