package edu.wpi.scheduler.shared.model;

public class TimeCell 
{
	// Time Model Constants
	public final static DayOfWeek[] week = {DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY};
	public final static int START_DAY = 1; // Monday
	public final static int START_HOUR = 8; // 8am
	public final static int START_MIN = 0; // START_MIN minutes past START_HOUR
	public final static int CELLS_PER_HOUR = 2; // Times chosen every 60/CELLS_PER_HOUR minutes
	public final static int NUM_DAYS = 5; // NUM_DAYS columns are a part of the choosing
	public final static int NUM_HOURS = 10; // NUM_HOURS rows are a part of the choosing
	
	public DayOfWeek day;
	public Time time;
	
	public TimeCell(Time t, DayOfWeek d)
	{
		time = t;
		day = d;
	}
	public TimeCell(int i, int j)
	{
		time = gridToTime(i);
		day = gridToDay(j);
	}
	
	// Time to Cell methods
	public int dayToGrid()
	{
		int j = 0;
		while(!week[j++].equals(day));
		return j - START_DAY - 1;
	}
	public int timeToGrid()
	{
		int i = 0;
		final Time startTime = new Time(START_HOUR, START_MIN);
		i += (time.hour - startTime.hour) * CELLS_PER_HOUR;
		i += (time.minutes - startTime.minutes) / (60/CELLS_PER_HOUR);
		return i;
	}
	
	// Cell to Time methods
	private DayOfWeek gridToDay(int j)
	{
		int index = START_DAY + j;
		// j = 9
		while(index >= week.length)
		{
			index -= week.length;
			j -= week.length;
		}
		return week[START_DAY + j];
	}
	private Time gridToTime(int i)
	{
		Time calculatedTime = new Time(START_HOUR, START_MIN);
		calculatedTime.increment(0, (60/CELLS_PER_HOUR) * i);
		return calculatedTime;
	}
	
	@Override
	public String toString()
	{
		String out = "";
		out += day.getName() + ": " + time.toString();
		out += " || ";
		out += "(" + dayToGrid() + ", " + timeToGrid() + ")";
		return out;
	}
}
